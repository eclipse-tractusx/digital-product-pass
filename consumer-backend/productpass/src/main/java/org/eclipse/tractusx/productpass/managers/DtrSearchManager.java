/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2023 Contributors to the CatenaX (ng) GitHub Organisation.
 *
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the
 * License for the specific language govern in permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

package org.eclipse.tractusx.productpass.managers;

import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.tractusx.productpass.config.DtrConfig;
import org.eclipse.tractusx.productpass.exceptions.DataModelException;
import org.eclipse.tractusx.productpass.exceptions.ManagerException;
import org.eclipse.tractusx.productpass.models.negotiation.Negotiation;
import org.eclipse.tractusx.productpass.models.catenax.Dtr;
import org.eclipse.tractusx.productpass.models.catenax.EdcDiscoveryEndpoint;
import org.eclipse.tractusx.productpass.models.http.responses.IdResponse;
import org.eclipse.tractusx.productpass.models.negotiation.Catalog;
import org.eclipse.tractusx.productpass.models.negotiation.Dataset;
import org.eclipse.tractusx.productpass.models.negotiation.Offer;
import org.eclipse.tractusx.productpass.services.DataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.*;

import java.nio.file.Path;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DtrSearchManager {
    private DataTransferService dataTransferService;
    private FileUtil fileUtil;
    private JsonUtil jsonUtil;

    private ProcessManager processManager;
    private DtrConfig dtrConfig;
    private ConcurrentHashMap<String, List<Dtr>> dtrDataModel;
    private ConcurrentHashMap<String, Catalog> catalogsCache;
    private final long searchTimeoutSeconds;
    private final long negotiationTimeoutSeconds;
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
    public DtrSearchManager(FileUtil fileUtil, JsonUtil jsonUtil, DataTransferService dataTransferService, DtrConfig dtrConfig, ProcessManager processManager) {
        this.catalogsCache = new ConcurrentHashMap<>();
        this.dataTransferService = dataTransferService;
        this.processManager = processManager;
        this.dtrConfig = dtrConfig;
        this.state = State.Stopped;
        this.fileUtil = fileUtil;
        this.jsonUtil = jsonUtil;
        this.dtrDataModelFilePath = this.createDataModelFile();
        this.dtrDataModel = this.loadDtrDataModel();
        this.searchTimeoutSeconds = this.dtrConfig.getTimeouts().getSearch();
        this.negotiationTimeoutSeconds = this.dtrConfig.getTimeouts().getNegotiation();

    }

    public Runnable startProcess(List<EdcDiscoveryEndpoint> edcEndpoints, String processId) {
        return new Runnable() {
            @Override
            public void run() {
                state = State.Running;
                if (edcEndpoints == null || processId == null) {
                    return;
                }
                List<EdcDiscoveryEndpoint> edcEndpointsToSearch = null;
                try {
                    edcEndpointsToSearch = (List<EdcDiscoveryEndpoint>) jsonUtil.bindReferenceType(edcEndpoints, new TypeReference<List<EdcDiscoveryEndpoint>>() {
                    });
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
                                    Thread singleOfferThread = ThreadUtil.runThread(createAndSaveDtr(dataset, edcEndPoint.getBpn(), connectionUrl, processId), "CreateAndSaveDtr");
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
                                Thread multipleOffersThread = ThreadUtil.runThread(createAndSaveDtr(dataset, edcEndPoint.getBpn(), connectionUrl, processId), "CreateAndSaveDtr");
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

    public String createDataModelFile() {
        Map<String, Object> dataModel = Map.of();
        // If path exists try to
        if(this.dtrConfig.getTemporaryStorage() && this.fileUtil.pathExists(this.getDataModelPath())) {
            try {
                // Try to load the data model if it exists
                dataModel = (Map<String, Object>) jsonUtil.fromJsonFileToObject(this.getDataModelPath(), Map.class);
            } catch (Exception e) {
                LogUtil.printWarning("It was not possible to load data model content.");
                dataModel = Map.of();
            }
            if (dataModel != null) {
                return this.getDataModelPath(); // There is no need to create the dataModelFile
            }
        }
        LogUtil.printMessage("Created DTR DataModel file at [" + this.getDataModelPath()+"]");
        return jsonUtil.toJsonFile(this.getDataModelPath(), dataModel, true);
    }

    public String getDataModelPath() {
        return Path.of(this.getDataModelDir(), this.fileName).toAbsolutePath().toString();
    }

    public String getDataModelDir() {
        return fileUtil.getTmpDir();
    }

    private Runnable searchDigitalTwinCatalogExecutor(String connectionUrl) {
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

    public DtrSearchManager addConnectionToBpnEntry(String bpn, Dtr dtr) {
        if (!(bpn.isEmpty() || bpn.isBlank() || dtr.getEndpoint().isEmpty() || dtr.getEndpoint().isBlank())) {
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

    public ConcurrentHashMap<String, List<Dtr>> loadDataModel() {
        try {
            String path = this.getDataModelPath();
            if(fileUtil.pathExists(path)) {
                this.createDataModelFile();
            }
            return (ConcurrentHashMap<String, List<Dtr>>) jsonUtil.fromJsonFileToObject(path, ConcurrentHashMap.class);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName() + ".loadDataModel", e, "It was not possible to load the DTR data model");
        }
    }

    public ConcurrentHashMap<String, List<Dtr>> getDtrDataModel() {
        return dtrDataModel;
    }

    private boolean checkConnectionEdcEndpoints(List<String> connectionEdcEndpoints) {
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

    private Runnable createAndSaveDtr(Dataset dataset, String bpn, String connectionUrl, String processId) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Offer offer = dataTransferService.buildOffer(dataset, 0);
                    IdResponse negotiationResponse = dataTransferService.doContractNegotiations(offer, bpn,  CatenaXUtil.buildDataEndpoint(connectionUrl));
                    if (negotiationResponse == null) {
                        return;
                    }
                    Negotiation negotiation = dataTransferService.seeNegotiation(negotiationResponse.getId());
                    if (negotiation == null) {
                        LogUtil.printWarning("Was not possible to do ContractNegotiation for URL: " + connectionUrl);
                        return;
                    }
                    Dtr dtr = new Dtr(negotiation.getContractAgreementId(), connectionUrl, offer.getAssetId(), bpn);
                    if (dtrConfig.getTemporaryStorage()) {
                        addConnectionToBpnEntry(bpn, dtr);
                        saveDtrDataModel();
                    }

                    processManager.addSearchStatusDtr(processId, dtr);

                } catch (Exception e) {
                    throw new ManagerException(this.getClass().getName() + ".createAndSaveDtr",e,"Was not possible to do ContractNegotiation for URL: " + connectionUrl);
                }
            }
        };

    }

    public boolean saveDtrDataModel() {
        if(fileUtil.pathExists(this.dtrDataModelFilePath)) {
            this.createDataModelFile();
        }
        String filePath = jsonUtil.toJsonFile(this.dtrDataModelFilePath, this.dtrDataModel, true);
        LogUtil.printMessage("[DTR DataModel] Saved [" + this.dtrDataModel.size() + "] assets in DTR data model.");
        return filePath != null;
    }

    private ConcurrentHashMap<String, List<Dtr>> loadDtrDataModel() {
        try {
            if(fileUtil.pathExists(this.dtrDataModelFilePath)) {
                this.createDataModelFile();
            }
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
