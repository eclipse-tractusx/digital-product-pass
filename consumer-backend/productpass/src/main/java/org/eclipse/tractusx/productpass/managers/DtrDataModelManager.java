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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DtrDataModelManager {
    private DataTransferService dataTransferService;
    private FileUtil fileUtil;
    private JsonUtil jsonUtil;
    private ConcurrentHashMap<String, List<Dtr>> dtrDataModel;
    private ConcurrentHashMap<String, Catalog> catalogsCache;
    private final long searchTimeoutMillis = 1000;
    private final long negotiationTimeoutMillis = 100000;
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
    public DtrDataModelManager(FileUtil fileUtil, JsonUtil jsonUtil, DataTransferService dataTransferService)  {
        this.dtrDataModel = new ConcurrentHashMap<>();
        this.catalogsCache = new ConcurrentHashMap<>();
        this.dataTransferService = dataTransferService;
        this.state = State.Stopped;
        this.fileUtil = fileUtil;
        this.jsonUtil = jsonUtil;
        this.dtrDataModelFilePath = this.createDataModelFile();
    }

    public String createDataModelFile(){
        return fileUtil.createFile(this.getDataModelPath());
    }

    public String getDataModelPath(){
        return Path.of(this.getDataModelDir(), this.fileName).toAbsolutePath().toString();
    }
    public String getDataModelDir() {
        return fileUtil.createTmpDir("DtrDataModel");
    }

    public Runnable startProcess (List<EdcDiscoveryEndpoint> edcEndpoints) {
        this.state = State.Running;
        if (edcEndpoints == null) {
            return null;
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
                    Thread asyncThread = ThreadUtil.runThread(this.searchDigitalTwinCatalogExecutor(connectionUrl), "ProcessDtrDataModel");
                    try {
                        asyncThread.join(searchTimeoutMillis);
                        if (asyncThread.isAlive()) {
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
                            Thread singleOfferThread = ThreadUtil.runThread(this.createAndSaveDtrExecutor(dataset, edcEndPoint.getBpn(), connectionUrl), "CreateAndSaveDtr");
                            try {
                                singleOfferThread.join(negotiationTimeoutMillis);
                                if (singleOfferThread.isAlive()) {
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
                        Thread multipleOffersThread = ThreadUtil.runThread(this.createAndSaveDtrExecutor(dataset, edcEndPoint.getBpn(), connectionUrl), "CreateAndSaveDtr");
                        try {
                            multipleOffersThread.join(negotiationTimeoutMillis);
                            if (multipleOffersThread.isAlive()) {
                                multipleOffersThread.interrupt();
                                LogUtil.printWarning("Could not retrieve the Catalog due a timeout for the URL: " + connectionUrl);
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                });
            });
        this.state = State.Finished;
        } catch (Exception e) {
            this.state = State.Error;
            throw new DataModelException(this.getClass().getName(), e, "Was not possible to process the DTRs");
        }
        return null;
    }

    private Runnable searchDigitalTwinCatalogExecutor (String connectionUrl) {
        try {
            Catalog catalog = dataTransferService.searchDigitalTwinCatalog(connectionUrl);
            if (catalog == null) {
                LogUtil.printWarning("No catalog was found for the URL: " + connectionUrl);
                return null;
            }
            catalogsCache.put(connectionUrl, catalog);
        } catch (Exception e) {
            LogUtil.printWarning("Could not find the catalog for the URL: " + connectionUrl);
            return null;
        }
        return null;
    }

    private Runnable createAndSaveDtrExecutor (Dataset dataset, String bpn, String connectionUrl) {
        this.createAndSaveDtr(dataset, bpn, connectionUrl);
        return null;
    }

    public DtrDataModelManager addConnectionToBpnEntry (String bpn, Dtr dtr) {
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

    public ConcurrentHashMap<String, List<Dtr>> loadDataModel() {
        try {
            String path = this.getDataModelPath();
            return (ConcurrentHashMap<String, List<Dtr>>) jsonUtil.fromJsonFileToObject(path, ConcurrentHashMap.class);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to load the DTR data model");
        }
    }
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

    private void createAndSaveDtr (Dataset dataset, String bpn, String connectionUrl) {
        Offer offer = dataTransferService.buildOffer(dataset);
        try {
            IdResponse negotiationResponse = dataTransferService.doContractNegotiations(offer, CatenaXUtil.buildDataEndpoint(connectionUrl));
            if (negotiationResponse == null) {
                return;
            }
            Negotiation negotiation = dataTransferService.seeNegotiation(negotiationResponse.getId());
            LogUtil.printMessage(jsonUtil.toJson(negotiation, true));
            if (negotiation == null) {
                LogUtil.printWarning("Was not possible to do ContractNegotiation for URL: " + connectionUrl);
                return;
            }
            this.addConnectionToBpnEntry(bpn, new Dtr(negotiation.getContractAgreementId(), connectionUrl, offer.getAssetId()));
            this.saveDtrDataModel();
        } catch (Exception e) {
            LogUtil.printWarning("Was not possible to do ContractNegotiation for URL: " + connectionUrl);
        }
    }

    public boolean saveDtrDataModel() {
       String filePath = jsonUtil.toJsonFile(this.dtrDataModelFilePath, this.dtrDataModel, true);
       LogUtil.printMessage("[DTR DataModel] Saved [" + this.dtrDataModel.size() + "] assets in DTR data model." );
       return filePath != null;
    }

}
