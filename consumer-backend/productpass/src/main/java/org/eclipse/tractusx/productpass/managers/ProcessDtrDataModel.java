package org.eclipse.tractusx.productpass.managers;

import org.eclipse.tractusx.productpass.models.catenax.EdcDiscoveryEndpoint;
import org.eclipse.tractusx.productpass.models.negotiation.Catalog;
import org.eclipse.tractusx.productpass.models.negotiation.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.FileUtil;
import org.eclipse.tractusx.productpass.services.DataTransferService;
import utils.JsonUtil;
import utils.ThreadUtil;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class ProcessDtrDataModel {
    private DataTransferService dataTransferService;
    private FileUtil fileUtil;
    private JsonUtil jsonUtil;
    private ConcurrentHashMap<String, List<String>> dtrDataModel;
    private ConcurrentHashMap<String, Catalog> catalogsCache;

    private final long searchTimeoutMillis = 1000;
    private final String fileName = "dtrDataModel.json";
    private final String tmpFolderName = "tmp";
    private String dtrDataModelFilePath;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    private State state;
    public enum State {
        Stopped,
        Running,
        Finished
    }
    @Autowired
    public ProcessDtrDataModel(FileUtil fileUtil, JsonUtil jsonUtil, DataTransferService dataTransferService)  {
        this.dtrDataModel = new ConcurrentHashMap<>();
        this.catalogsCache = new ConcurrentHashMap<>();
        this.dataTransferService = dataTransferService;
        this.state = State.Stopped;
        this.fileUtil = fileUtil;
        this.jsonUtil = jsonUtil;
        this.dtrDataModelFilePath = fileUtil.createFile(Path.of(fileUtil.createTempDir(tmpFolderName), fileName).toAbsolutePath().toString());
    }

    public ProcessDtrDataModel startProcess (List<EdcDiscoveryEndpoint> edcEndpoints) {
        this.state = State.Running;
        //Iterate the edcEndpoints
        edcEndpoints.parallelStream().forEach(edcEndPoint -> {
            //Iterate the connectionsURLs for each BPN
            edcEndPoint.getConnectorEndpoint().parallelStream().forEach(connectionUrl -> {
                //Search Digital Twin Catalog for each connectionURL with a timeout time
                Thread asyncThread = ThreadUtil.runThread(searchDigitalTwinCatalogExecutor(connectionUrl, dataTransferService));
                try {
                    asyncThread.join(searchTimeoutMillis);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //Get catalog for a specific connectionURL (if exists) in the catalogCache data structure
                Catalog catalog = catalogsCache.get(connectionUrl);
                if (catalog == null) {
                    return;
                }
                Object contractOffers = catalog.getContractOffers();
                //Check if contractOffer is an Array or just an Object and if is not null or empty, adds it to the dtrDataModel data structure
                if (contractOffers == null) {
                   return;
                }
                if (contractOffers instanceof LinkedHashMap) {
                    Dataset dataset = (Dataset) jsonUtil.bindObject(contractOffers, Dataset.class);
                    if (dataset != null) {
                        this.addConnectionToBpnEntry(edcEndPoint.getBpn(), connectionUrl);
                    }
                    return;
                }
                List<Dataset> contractOfferList = (List<Dataset>) jsonUtil.bindObject(contractOffers, List.class);
                if (contractOfferList.isEmpty()) {
                    return;
                }
                this.addConnectionToBpnEntry(edcEndPoint.getBpn(), connectionUrl);
            });
        });
        this.state = State.Finished;
        return this;
    }

    private Runnable searchDigitalTwinCatalogExecutor (String connectionUrl, DataTransferService dataTransferService) {
        Catalog catalog = dataTransferService.searchDigitalTwinCatalog(connectionUrl);
        if (catalog != null) {
            catalogsCache.put(connectionUrl, catalog);
        }
        return null;
    }

    public ProcessDtrDataModel addConnectionToBpnEntry (String bpn, String connectionEdcEndpoint) {
        if (!(bpn.isEmpty() || bpn.isBlank() || connectionEdcEndpoint.isEmpty() || connectionEdcEndpoint.isBlank()) ) {
            if (this.dtrDataModel.contains(bpn)) {
                if (!this.dtrDataModel.get(bpn).contains(connectionEdcEndpoint))
                    this.dtrDataModel.get(bpn).add(connectionEdcEndpoint);
            } else {
                this.dtrDataModel.put(bpn, List.of(connectionEdcEndpoint));
            }
        }
        return this;
    }

    public ProcessDtrDataModel addConnectionsToBpnEntry (String bpn, List<String> connectionEdcEndpoints) {
        if (!(bpn.isEmpty() || bpn.isBlank()) || checkConnectionEdcEndpoints(connectionEdcEndpoints) ) {
            if (this.dtrDataModel.contains(bpn)) {
                this.dtrDataModel.get(bpn).addAll(connectionEdcEndpoints);
                this.dtrDataModel.get(bpn).stream().distinct().collect(Collectors.toList());
            } else {
                this.dtrDataModel.put(bpn, connectionEdcEndpoints);
            }
        }
        return this;
    }

    public ConcurrentHashMap<String, List<String>> getDtrDataModel() {
        return dtrDataModel;
    }

    private boolean checkConnectionEdcEndpoints (List<String> connectionEdcEndpoints) {
        AtomicInteger count = new AtomicInteger(0);
        int connectionsSize = connectionEdcEndpoints.size();
        connectionEdcEndpoints.parallelStream().forEach(connection -> {
            if (connection.isBlank() || connection.isEmpty()) {
                count.getAndIncrement();
                synchronized (connectionEdcEndpoints) {
                    connectionEdcEndpoints.remove(connection);
                }
            }
        });
        return count.get() == connectionsSize;
    }

    //TODO: create the method to save the DTR data model as json.
    public boolean saveDtrDataModel() {
        String filePath = jsonUtil.toJsonFile(this.dtrDataModelFilePath, this.dtrDataModel, true);
        return filePath != null;
    }
}
