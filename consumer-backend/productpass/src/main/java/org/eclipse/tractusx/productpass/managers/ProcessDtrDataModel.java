package org.eclipse.tractusx.productpass.managers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.tractusx.productpass.exceptions.DataModelException;
import org.eclipse.tractusx.productpass.exceptions.ServiceException;
import org.eclipse.tractusx.productpass.models.negotiation.Negotiation;
import org.eclipse.tractusx.productpass.models.catenax.Dtr;
import org.eclipse.tractusx.productpass.models.catenax.EdcDiscoveryEndpoint;
import org.eclipse.tractusx.productpass.models.http.responses.IdResponse;
import org.eclipse.tractusx.productpass.models.negotiation.Catalog;
import org.eclipse.tractusx.productpass.models.negotiation.Dataset;
import org.eclipse.tractusx.productpass.models.negotiation.Offer;
import org.eclipse.tractusx.productpass.services.CatenaXService;
import org.eclipse.tractusx.productpass.services.DataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import utils.*;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ProcessDtrDataModel {
    private DataTransferService dataTransferService;
    private FileUtil fileUtil;
    private JsonUtil jsonUtil;
    private ConcurrentHashMap<String, List<Dtr>> dtrDataModel;
    private ConcurrentHashMap<String, Catalog> catalogsCache;
    private final long searchTimeoutSeconds = 10;
    private final long negotiationTimeoutSeconds = 20;
    private final String fileName = "dtrDataModel.json";
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
        Error,
        Finished
    }
    @Autowired
    public ProcessDtrDataModel(FileUtil fileUtil, JsonUtil jsonUtil, DataTransferService dataTransferService) {
        this.catalogsCache = new ConcurrentHashMap<>();
        this.dataTransferService = dataTransferService;
        this.state = State.Stopped;
        this.fileUtil = fileUtil;
        this.jsonUtil = jsonUtil;
        this.dtrDataModelFilePath = fileUtil.createFile(Path.of(fileUtil.createTmpDir("DtrDataModel"), this.fileName).toAbsolutePath().toString());
        this.dtrDataModel =  this.loadDtrDataModel();
    }

    public Runnable startProcess (List<EdcDiscoveryEndpoint> edcEndpoints) {
        return new Runnable() {
            @Override
            public void run() {
                state = State.Running;
                if (edcEndpoints == null) {
                    return;
                }
                List<EdcDiscoveryEndpoint> edcEndpointsToSearch = null;
                try {
                    edcEndpointsToSearch = (List<EdcDiscoveryEndpoint>) jsonUtil.bindReferenceType(edcEndpoints, new TypeReference<List<EdcDiscoveryEndpoint>>() {});
                } catch (Exception e) {
                    throw new DataModelException(this.getClass().getName(), e, "Could not bind the reference type!");
                }
                try {
                    //Iterate the edcEndpoints
                    edcEndpointsToSearch.parallelStream().forEach(edcEndPoint -> {
                        //Iterate the connectionsURLs for each BPN
                        edcEndPoint.getConnectorEndpoint().parallelStream().forEach(connectionUrl -> {
                            //Search Digital Twin Catalog for each connectionURL with a timeout time
                            Thread asyncThread = ThreadUtil.runThread(searchDigitalTwinCatalogExecutor(connectionUrl), "ProcessDtrDataModel");
                            try {
                                if (!asyncThread.join(Duration.ofSeconds(searchTimeoutSeconds))) {
                                    asyncThread.interrupt();
                                    LogUtil.printWarning("Could not retrieve the Catalog due a timeout for the URL: " + connectionUrl);
                                    return;
                                }
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
                                    Thread singleOfferThread = ThreadUtil.runThread(createAndSaveDtr(dataset, edcEndPoint.getBpn(), connectionUrl), "CreateAndSaveDtr");
                                    try {
                                        if (!singleOfferThread.join(Duration.ofSeconds(negotiationTimeoutSeconds))) {
                                            singleOfferThread.interrupt();
                                            LogUtil.printWarning("Could not retrieve the Catalog due a timeout for the URL: " + connectionUrl);
                                            return;
                                        }
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                return;
                            }
                            List<Dataset> contractOfferList = (List<Dataset>) jsonUtil.bindObject(contractOffers, List.class);
                            if (contractOfferList.isEmpty()) {
                                return;
                            }
                            contractOfferList.parallelStream().forEach(dataset -> {
                                Thread multipleOffersThread = ThreadUtil.runThread(createAndSaveDtr(dataset, edcEndPoint.getBpn(), connectionUrl), "CreateAndSaveDtr");
                                try {
                                    if (!multipleOffersThread.join(Duration.ofSeconds(negotiationTimeoutSeconds))) {
                                        multipleOffersThread.interrupt();
                                        LogUtil.printWarning("Could not retrieve the Catalog due a timeout for the URL: " + connectionUrl);
                                    }
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        });
                    });
                    state = State.Finished;
                } catch (Exception e) {
                    state = State.Error;
                    throw new DataModelException(this.getClass().getName(), e, "Was not possible to process the DTRs");
                }
            }
        };

    }

    private Runnable searchDigitalTwinCatalogExecutor (String connectionUrl) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Catalog catalog = dataTransferService.searchDigitalTwinCatalog(connectionUrl);
                    if (catalog == null) {
                        LogUtil.printWarning("No catalog was found for the URL: " + connectionUrl);
                        return;
                    }
                    catalogsCache.put(connectionUrl, catalog);
                } catch (Exception e) {
                    LogUtil.printWarning("Could not find the catalog for the URL: " + connectionUrl);
                }
            }
        };
    }

    public ProcessDtrDataModel addConnectionToBpnEntry (String bpn, Dtr dtr) {
        if (!(bpn.isEmpty() || bpn.isBlank() || dtr.getEndpoint().isEmpty() || dtr.getEndpoint().isBlank()) ) {
            if (this.dtrDataModel.contains(bpn)) {
                if (!this.dtrDataModel.get(bpn).contains(dtr))
                    this.dtrDataModel.get(bpn).add(dtr);
            } else {
                this.dtrDataModel.put(bpn, List.of(dtr));
            }
        }
        return this;
    }

    /*public ProcessDtrDataModel addConnectionsToBpnEntry (String bpn, List<String> connectionEdcEndpoints) {
        if (!(bpn.isEmpty() || bpn.isBlank()) || checkConnectionEdcEndpoints(connectionEdcEndpoints) ) {
            if (this.dtrDataModel.contains(bpn)) {
                this.dtrDataModel.get(bpn).addAll(connectionEdcEndpoints);
                this.dtrDataModel.get(bpn).stream().distinct().collect(Collectors.toList());
            } else {
                this.dtrDataModel.put(bpn, connectionEdcEndpoints);
            }
        }
        return this;
    }*/

    public ConcurrentHashMap<String, List<Dtr>> getDtrDataModel() {
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

    private Runnable createAndSaveDtr (Dataset dataset, String bpn, String connectionUrl) {
        return new Runnable() {
            @Override
            public void run() {
                Offer offer = dataTransferService.buildOffer(dataset);
                try {
                    IdResponse negotiationResponse = dataTransferService.doContractNegotiations(offer, CatenaXUtil.buildDataEndpoint(connectionUrl));
                    if (negotiationResponse == null) {
                        return;
                    }
                    Negotiation negotiation = dataTransferService.seeNegotiation(negotiationResponse.getId());
                    if (negotiation == null) {
                        LogUtil.printWarning("Was not possible to do ContractNegotiation for URL: " + connectionUrl);
                        return;
                    }
                    addConnectionToBpnEntry(bpn, new Dtr(negotiation.getContractAgreementId(), connectionUrl, offer.getAssetId()));
                    saveDtrDataModel();
                } catch (Exception e) {
                    LogUtil.printWarning("Was not possible to do ContractNegotiation for URL: " + connectionUrl);
                }
            }
        };

    }

    public boolean saveDtrDataModel() {
       String filePath = jsonUtil.toJsonFile(this.dtrDataModelFilePath, this.dtrDataModel, true);
       LogUtil.printMessage("[DTR DataModel] Saved [" + this.dtrDataModel.size() + "] assets in DTR data model." );
       return filePath != null;
    }

    private ConcurrentHashMap<String, List<Dtr>> loadDtrDataModel() {
        try {
            ConcurrentHashMap<String, List<Dtr>> result = (ConcurrentHashMap<String, List<Dtr>>) jsonUtil.fromJsonFileToObject(this.dtrDataModelFilePath, ConcurrentHashMap.class);
            if (result == null) {
                return new ConcurrentHashMap<String, List<Dtr>>();
            }
            LogUtil.printMessage("Loaded [" + result.size() + "] entries from DTR Data Model Json.");
            return result;
        } catch (Exception e) {
            LogUtil.printException(e, "Was not possible to load Dtr Data Model!");
            return new ConcurrentHashMap<String, List<Dtr>>();
        }

    }

}
