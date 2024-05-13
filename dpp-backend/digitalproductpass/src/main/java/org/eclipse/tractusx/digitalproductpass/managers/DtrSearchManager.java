/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.tractusx.digitalproductpass.managers;

import com.fasterxml.jackson.core.type.TypeReference;
import jdk.jfr.DataAmount;
import org.eclipse.tractusx.digitalproductpass.config.DtrConfig;
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.DataModelException;
import org.eclipse.tractusx.digitalproductpass.exceptions.ManagerException;
import org.eclipse.tractusx.digitalproductpass.models.catenax.Dtr;
import org.eclipse.tractusx.digitalproductpass.models.catenax.EdcDiscoveryEndpoint;
import org.eclipse.tractusx.digitalproductpass.models.general.Selection;
import org.eclipse.tractusx.digitalproductpass.models.http.responses.IdResponse;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog.Catalog;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog.Dataset;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog.Policy;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.policy.Set;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.response.Negotiation;
import org.eclipse.tractusx.digitalproductpass.services.DataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.*;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.time.Duration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class consists exclusively of methods to operate on executing the DTR search.
 *
 * <p> The methods defined here are intended to do every needed operations in order to be able to get and save the DTRs.
 */
@Component
public class DtrSearchManager {

    /**
     * ATTRIBUTES
     **/
    private DataTransferService dataTransferService;
    private FileUtil fileUtil;
    private JsonUtil jsonUtil;
    private EdcUtil edcUtil;
    private PolicyUtil policyUtil;
    private ProcessManager processManager;
    private DtrConfig dtrConfig;
    private ConcurrentHashMap<String, List<Dtr>> dtrDataModel;
    private ConcurrentHashMap<String, Catalog> catalogsCache;
    private final long searchTimeoutSeconds;
    private final long dtrRequestProcessTimeout;
    private final String fileName = "dtrDataModel.json";
    private String dtrDataModelFilePath;
    private State state;

    public enum State {
        Stopped,
        Running,
        Error,
        Finished
    }

    /**
     * CONSTRUCTOR(S)
     **/
    @Autowired
    public DtrSearchManager(FileUtil fileUtil, EdcUtil edcUtil, JsonUtil jsonUtil, PolicyUtil policyUtil, DataTransferService dataTransferService, DtrConfig dtrConfig, ProcessManager processManager) {
        this.catalogsCache = new ConcurrentHashMap<>();
        this.dataTransferService = dataTransferService;
        this.processManager = processManager;
        this.dtrConfig = dtrConfig;
        this.state = State.Stopped;
        this.edcUtil = edcUtil;
        this.fileUtil = fileUtil;
        this.policyUtil = policyUtil;
        this.jsonUtil = jsonUtil;
        this.dtrDataModelFilePath = this.createDataModelFile();
        this.dtrDataModel = this.loadDtrDataModel();
        this.searchTimeoutSeconds = this.dtrConfig.getTimeouts().getSearch();
        this.dtrRequestProcessTimeout = this.dtrConfig.getTimeouts().getDtrRequestProcess();
    }

    /**
     * GETTERS AND SETTERS
     **/
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    /** METHODS **/

    /**
     * It's a Thread level method that implements the Runnable interface and starts the process of searching the DTRs with
     * the given EDC endpoint for a given processId.
     * <p>
     *
     * @param edcEndpoints the {@code List<String>} of EDC endpoints to search.
     * @param processId    the {@code String} id of the application's process.
     * @return a {@code Runnable} object to be used by a calling thread.
     * @throws DataModelException if unable to get and process the DTRs.
     * @throws RuntimeException   if there's an unexpected thread interruption.
     */
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
                                    searchEndpoint(processId, edcEndPoint.getBpn(), connectionUrl);
                                }
                        );
                    });
                    state = State.Finished;
                } catch (Exception e) {
                    state = State.Error;
                    throw new DataModelException(this.getClass().getName(), e, "It was not possible find any valid digital twin registry!");
                }
            }
        };
    }

    /**
     * It's a Thread level method that implements the Runnable interface and starts the process of searching known DTRs with
     * the given EDC endpoint for a given processId.
     * <p>
     *
     * @param dtrs      the {@code List<Dtr>} of digital twin registries known
     * @param processId the {@code String} id of the application's process.
     * @return a {@code Runnable} object to be used by a calling thread.
     * @throws DataModelException if unable to get and process the DTRs.
     * @throws RuntimeException   if there's an unexpected thread interruption.
     */
    public Runnable updateProcess(List<Dtr> dtrs, String processId) {
        return new Runnable() {
            @Override
            public void run() {
                state = State.Running;
                if (dtrs == null || processId == null) {
                    return;
                }
                List<Dtr> dtrEndpoints = null;
                try {
                    dtrEndpoints = jsonUtil.bind(dtrs, new TypeReference<>() {
                    });
                } catch (Exception e) {
                    throw new DataModelException(this.getClass().getName(), e, "Could not bind the reference type!");
                }
                try {
                    //Iterate the edcEndpoints
                    dtrEndpoints.parallelStream().forEach(dtr -> {
                        //Iterate the known DTRs
                        searchEndpoint(processId, dtr.getBpn(), dtr.getEndpoint());
                    });
                    state = State.Finished;
                } catch (Exception e) {
                    state = State.Error;
                    throw new DataModelException(this.getClass().getName(), e, "It was not possible to process the DTRs");
                }
            }
        };
    }

    public void searchEndpoint(String processId, String bpn, String endpoint) {
        //Search Digital Twin Catalog for each connectionURL with a timeout time
        SearchDtrCatalog searchDtrCatalog = new SearchDtrCatalog(endpoint, bpn);
        Thread asyncThread = ThreadUtil.runThread(searchDtrCatalog, "SearchEndpoint-" + processId + "-" + bpn + "-" + endpoint);
        Dtr dtr = new Dtr("", endpoint, "", bpn, DateTimeUtil.addHoursToCurrentTimestamp(dtrConfig.getTemporaryStorage().getLifetime()), true);
        try {
            if (!asyncThread.join(Duration.ofSeconds(searchTimeoutSeconds))) {
                asyncThread.interrupt();
                if (dtrConfig.getTemporaryStorage().getEnabled()) {
                    addConnectionToBpnEntry(bpn, dtr);
                    saveDtrDataModel();
                }

                LogUtil.printWarning("Failed to retrieve the Catalog due that the timeout was reached for the edc endpoint: " + endpoint);
                return;
            }
        } catch (InterruptedException e) {
            if (dtrConfig.getTemporaryStorage().getEnabled()) {
                addConnectionToBpnEntry(bpn, dtr);
                saveDtrDataModel();
            }
            throw new ManagerException("DtrSearchManager.searchEndpoint", "It was not possible to retrieve the Catalog for BPN " + bpn + " and ENDPOINT" + endpoint);
        }
        if (searchDtrCatalog.isError()) {
            if (dtrConfig.getTemporaryStorage().getEnabled()) {
                addConnectionToBpnEntry(bpn, dtr);
                saveDtrDataModel();
            }
            LogUtil.printError("The endpoint [" + endpoint + "] of the BPN [" + bpn + "] is invalid!");
            return;
        }
        //Get catalog for a specific connectionURL (if exists) in the catalogCache data structure
        Catalog catalog = catalogsCache.get(endpoint);
        if (catalog == null) {
            LogUtil.printWarning("Failed to retrieve the catalog from cache: " + endpoint);
            return;
        }
        String providerBpn = catalog.getParticipantId();
        Object contractOffers = catalog.getContractOffers();
        //Check if contractOffer is an Array or just an Object and if is not null or empty, adds it to the dtrDataModel data structure
        if (contractOffers == null) {
            LogUtil.printWarning("Failed to retrieve get contract offers from endpoint: " + endpoint);
            return;
        }
        if (contractOffers instanceof LinkedHashMap) {
            Dataset dataset = jsonUtil.bind(contractOffers, new TypeReference<>() {});
            if (dataset != null) {
                // Store the dataset in the digital twin logs
                Map<String, Dataset> datasets = new HashMap<>() {{
                    put(dataset.getId(), dataset);
                }};

                Selection<Dataset,Set> contractAndPolicy = getDtrDataset(datasets);
                if (contractAndPolicy == null) {
                    throw new ManagerException("DtrSearchManager.searchEndpoint", "There was no valid policy available for the digital twin registry found!");
                }

                LogUtil.printMessage("[DTR-AUTO-NEGOTIATION] [PROCESS "+processId + "] Selected [CONTRACT "+contractAndPolicy.d().getId()+"]:["+this.jsonUtil.toJson(contractAndPolicy.d(), false)+"]!");
                LogUtil.printMessage("[DTR-AUTO-NEGOTIATION] [PROCESS "+processId + "] Selected [POLICY "+contractAndPolicy.s().getId()+"]:["+this.jsonUtil.toJson(contractAndPolicy.s(), false)+"]!");

                Thread singleOfferThread = ThreadUtil.runThread(createAndSaveDtr(contractAndPolicy, datasets, bpn, providerBpn, endpoint, processId), "CreateAndSaveDtr-" + processId + "-" + bpn + "-" + endpoint);
                try {
                    if (!singleOfferThread.join(Duration.ofSeconds(this.dtrRequestProcessTimeout))) {
                        singleOfferThread.interrupt();
                        LogUtil.printWarning("Failed to retrieve do contract negotiations due a timeout for the URL: " + endpoint);
                        return;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return;
        }
        List<Dataset> contractOfferList = jsonUtil.bind(contractOffers, new TypeReference<>() {});
        if (contractOfferList == null || contractOfferList.isEmpty()) {
            return;
        }
        Map<String, Dataset> datasets = edcUtil.mapDatasetsById(contractOfferList);
        Selection<Dataset,Set> contractAndPolicy = getDtrDataset(datasets);
        if (contractAndPolicy == null) {
            throw new ManagerException("DtrSearchManager.searchEndpoint", "There was no valid policy available for the digital twin registry found!");
        }
        LogUtil.printMessage("[DTR-AUTO-NEGOTIATION] [PROCESS "+processId + "] Selected [CONTRACT "+contractAndPolicy.d().getId()+"]:["+this.jsonUtil.toJson(contractAndPolicy.d(), false)+"]!");
        LogUtil.printMessage("[DTR-AUTO-NEGOTIATION] [PROCESS "+processId + "] Selected [POLICY "+contractAndPolicy.s().getId()+"]:["+this.jsonUtil.toJson(contractAndPolicy.s(), false)+"]!");
        // Store datasets in the digital twin logs
        contractOfferList.parallelStream().forEach(dataset -> {
            Thread multipleOffersThread = ThreadUtil.runThread(createAndSaveDtr(contractAndPolicy, datasets, bpn, providerBpn, endpoint, processId), "CreateAndSaveDtr-" + processId + "-" + bpn + "-" + endpoint);
            try {
                if (!multipleOffersThread.join(Duration.ofSeconds(this.dtrRequestProcessTimeout))) {
                    multipleOffersThread.interrupt();
                    LogUtil.printWarning("Failed to retrieve the contract negotiations due a timeout for the URL: " + endpoint);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Creates the DTR Data Model file where the DTRs are stored.
     * <p>
     *
     * @return a {@code String} path to the file created.
     */
    public String createDataModelFile() {
        Map<String, Object> dataModel = Map.of();
        // If path exists try to
        if (this.dtrConfig.getTemporaryStorage().getEnabled() && this.fileUtil.pathExists(this.getDataModelPath())) {
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
        LogUtil.printMessage("Created DTR DataModel file at [" + this.getDataModelPath() + "]");
        return jsonUtil.toJsonFile(this.getDataModelPath(), dataModel, true);
    }

    /**
     * Gets the path to the DTR Data Model file.
     * <p>
     *
     * @return a {@code String} path to the DTR Data Model file.
     */
    public String getDataModelPath() {
        return Path.of(this.getDataModelDir(), this.fileName).toAbsolutePath().toString();
    }

    /**
     * Gets the path to the DTR Data Model directory.
     * <p>
     *
     * @return a {@code String} path to the DTR Data Model directory.
     */
    public String getDataModelDir() {
        return fileUtil.getTmpDir();
    }

    /**
     * It's a Thread level method that implements the Runnable interface and searches for the Digital Twin Catalog for
     * a given URL connection. It doesn't return the Catalogs found, it fills the catalogs cache of this class object.
     */
    public class SearchDtrCatalog implements Runnable {
        Boolean error = true;
        String connectionUrl;
        String bpn;

        public SearchDtrCatalog(String connectionUrl, String bpn) {
            this.connectionUrl = connectionUrl;
            this.bpn = bpn;
        }

        public Boolean isError() {
            return this.error;
        }

        @Override
        public void run() {
            try {
                Catalog catalog = dataTransferService.searchDigitalTwinCatalog(connectionUrl, bpn);
                if (catalog == null) {
                    return;
                }
                catalogsCache.put(connectionUrl, catalog);
                this.error = false;
            } catch (Exception e) {
                // Suppress exception
            }
        }
    }

    /**
     * Adds the found DTR to the DTR Data Model map object at the given BPN number entry.
     * <p>
     *
     * @param bpn the {@code String} bpn number.
     * @param dtr the {@code DTR} object to add.
     * @return this {@code DtrSearchManager} object.
     */
    public DtrSearchManager addConnectionToBpnEntry(String bpn, Dtr dtr) {
        if (bpn == null || bpn.isEmpty() || bpn.isBlank()) {
            return this;
        }
        if (!(dtr == null || dtr.getEndpoint().isEmpty() || dtr.getEndpoint().isBlank())) {
            if (this.dtrDataModel.containsKey(bpn)) {
                if (!hasDtrDuplicates(this.dtrDataModel.get(bpn), dtr)) {
                    LogUtil.printMessage("[DTR DataModel] 1 " + (!dtr.getInvalid() ? "valid" : "invalid") + " DTR [" + dtr.getEndpoint() + "] was found and stored! [" + this.dtrDataModel.get(bpn).size() + "] endpoints found for BPN [" + bpn + "]");
                    this.dtrDataModel.get(bpn).add(dtr);
                }
            } else {
                this.dtrDataModel.put(bpn, new ArrayList<>() {{
                    add(dtr);
                }});
            }
        } else {
            this.dtrDataModel.put(bpn, new ArrayList<>() {
            });
        }
        return this;
    }

    /**
     * Check if elements are present in array list already as duplicates
     * <p>
     *
     * @param dtrList the {@code List<Dtr>} list of dtrs
     * @param dtr     the {@code DTR} object to check if exists
     * @return this {@code DtrSearchManager} object.
     */
    public Boolean hasDtrDuplicates(List<Dtr> dtrList, Dtr dtr) {
        if (dtrList.contains(dtr)) {
            return true;
        }
        List<Dtr> dtrListParsed = null;
        try {
            dtrListParsed = (List<Dtr>) jsonUtil.bindReferenceType(dtrList, new TypeReference<List<Dtr>>() {
            });
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "Could not bind the reference type for the DTR list!");
        }
        if (dtrListParsed == null) {
            throw new ManagerException(this.getClass().getName(), "Could not bind the reference type for the DTR list because it is null!");
        }
        return dtrListParsed.stream().anyMatch(e -> e.getAssetId().equals(dtr.getAssetId()) && e.getEndpoint().equals(dtr.getEndpoint()) && e.getBpn().equals(dtr.getBpn()));
    }

    /**
     * Loads the thread safe DTR Data Model from the storage file.
     * <p>
     *
     * @return the {@code CocurrentHashMap<String, List<Dtr>>} object with the DTR Data Model values.
     */
    public ConcurrentHashMap<String, List<Dtr>> loadDataModel() {
        try {
            String path = this.getDataModelPath();
            if (fileUtil.pathExists(path)) {
                this.createDataModelFile();
            }
            return (ConcurrentHashMap<String, List<Dtr>>) jsonUtil.fromJsonFileToObject(path, ConcurrentHashMap.class);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName() + ".loadDataModel", e, "It was not possible to load the DTR data model");
        }
    }

    /**
     * Gets the thread safe DTR Data Model cached in this class object.
     * <p>
     *
     * @return the {@code CocurrentHashMap<String, List<Dtr>>} object with the DTR Data Model values.
     */
    public ConcurrentHashMap<String, List<Dtr>> getDtrDataModel() {
        return dtrDataModel;
    }


    @SuppressWarnings("Unused")
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

    /**
     * Gets the correct dtr dataset for the digital twin registry.
     * <p>
     *
     * @param datasets the {@code Map<String,Dataset>} data for the contract offer.
     * @return the {@code Dataset} selected for the digital registry
     */
    public Selection<Dataset, Set> getDtrDataset(Map<String, Dataset> datasets) {
        try {
            // Get a contract and a policy from the data set offers
            return edcUtil.selectValidContractAndPolicy(datasets, this.dtrConfig.getPolicyCheck());
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName() + ".getDtrDataset", e, "No valid dtr dataset found for the policy configuration provided!");
        }

    }

    /**
     * It's a Thread level method that implements the Runnable interface. It creates the DTR and saves it in the DTR Data Model
     * for a given BPN number and an URL connection into a process with the given process id.
     * <p>
     *
     * @param contractAndPolicy the {@code Selection<Dataset,Set>} the selected contract and policy
     * @param datasets the {@code Map<String, Dataset>} map of contracts available
     * @param bpn           the {@code String} bpn number.
     * @param connectionUrl the {@code String} URL connection of the Digital Twin.
     * @param processId     the {@code String} id of the application's process.
     * @return a {@code Runnable} object to be used by a calling thread.
     * @throws ManagerException if unable to do the contract negotiation for the DTR.
     */
    private Runnable createAndSaveDtr(Selection<Dataset,Set> contractAndPolicy, Map<String, Dataset> datasets, String bpn, String providerBpn, String connectionUrl, String processId) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Dataset dataset = contractAndPolicy.d(); // Get the contract element
                    if (dataset == null) {
                        LogUtil.printError("It was not possible to get the contract!");
                        return;
                    }
                    Set set = contractAndPolicy.s(); // Get the policy element
                    if (set == null) {
                        LogUtil.printError("It was not possible to get the policy!");
                        return;
                    }

                    Policy policy = dataTransferService.buildOffer(dataset, set, providerBpn);
                    String builtDataEndpoint = CatenaXUtil.buildDataEndpoint(connectionUrl);
                    IdResponse negotiationResponse = dataTransferService.doContractNegotiation(policy, builtDataEndpoint);
                    if (negotiationResponse == null) {
                        return;
                    }
                    Integer millis = dtrConfig.getTimeouts().getNegotiation() * 1000; // Set max timeout from seconds to milliseconds
                    // If negotiation takes way too much time give timeout
                    Negotiation negotiation = ThreadUtil.timeout(millis, () -> dataTransferService.seeNegotiation(negotiationResponse.getId()), null);
                    if (negotiation == null) {
                        LogUtil.printWarning("It was not possible to do ContractNegotiation for URL: " + connectionUrl);
                        return;
                    }
                    LogUtil.printDebug(negotiation.getContractAgreementId());
                    if (negotiation.getContractAgreementId() == null || negotiation.getContractAgreementId().isEmpty()) {
                        LogUtil.printError("It was not possible to get an Contract Agreement Id for the URL: " + connectionUrl);
                        return;
                    }
                    Dtr dtr = new Dtr(datasets, dataset.getId(), set.getId(), negotiation.getContractAgreementId(), connectionUrl, dataset.getAssetId(), bpn, providerBpn, DateTimeUtil.addHoursToCurrentTimestamp(dtrConfig.getTemporaryStorage().getLifetime()));
                    if (dtrConfig.getTemporaryStorage().getEnabled()) {
                        addConnectionToBpnEntry(bpn, dtr);
                        saveDtrDataModel();
                    }

                    processManager.addSearchStatusDtr(processId, dtr);
                } catch (Exception e) {
                    throw new ManagerException(this.getClass().getName() + ".createAndSaveDtr", e, "Failed to create the digital twin registry for url: " + connectionUrl);
                }
            }
        };

    }

    /**
     * Saves the cached DTR Data Model of this class object to the storage file.
     * <p>
     *
     * @return true if the cached DTR Data Model was successfully saved, false otherwise.
     */
    public boolean saveDtrDataModel() {
        if (fileUtil.pathExists(this.dtrDataModelFilePath)) {
            this.createDataModelFile();
        }
        String filePath = jsonUtil.toJsonFile(this.dtrDataModelFilePath, this.dtrDataModel, true);
        return filePath != null;
    }

    /**
     * Saves the cached DTR Data Model of this class object to the storage file.
     * <p>
     *
     * @return true if the cached DTR Data Model was successfully saved, false otherwise.
     */
    public boolean saveDtrDataModel(ConcurrentHashMap<String, List<Dtr>> dataModel) {
        if (fileUtil.pathExists(this.dtrDataModelFilePath)) {
            this.createDataModelFile();
        }
        String filePath = jsonUtil.toJsonFile(this.dtrDataModelFilePath, dataModel, true);
        return filePath != null;
    }

    /**
     * Saves the cached DTR Data Model of this class object to the storage file.
     * <p>
     */
    public void deleteBpns(ConcurrentHashMap<String, List<Dtr>> dataModel, List<String> bpnList) {
        try {
            boolean deleted = dataModel.keySet().removeAll(bpnList); // Remove keys from the local storage data model
            this.dtrDataModel.keySet().removeAll(bpnList); // Remove keys from the memory data model
            if (deleted) {
                LogUtil.printMessage("[DTR DataModel Cache Cleaning] Deleted [" + bpnList.size() + "] bpn numbers from the DTR.");
                saveDtrDataModel(dataModel);
            } else {
                LogUtil.printError("[DTR DataModel Cache Cleaning] Failed to do the dtrDataModel cleanup!");
            }
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName() + ".deleteBpns", e, "It was not possible to delete bpns from the DTR data model");
        }
    }

    /**
     * Loads the thread safe DTR Data Model from the storage file.
     * <p>
     *
     * @return the {@code CocurrentHashMap<String, List<Dtr>>} object with the DTR Data Model values.
     */
    private ConcurrentHashMap<String, List<Dtr>> loadDtrDataModel() {
        try {
            if (fileUtil.pathExists(this.dtrDataModelFilePath)) {
                this.createDataModelFile();
            }
            ConcurrentHashMap<String, List<Dtr>> result = (ConcurrentHashMap<String, List<Dtr>>) jsonUtil.fromJsonFileToObject(this.dtrDataModelFilePath, ConcurrentHashMap.class);
            if (result == null) {
                return new ConcurrentHashMap<String, List<Dtr>>();
            }
            LogUtil.printMessage("Loaded [" + result.size() + "] entries from DTR Data Model Json.");
            return result;
        } catch (Exception e) {
            LogUtil.printException(e, "It was not possible to load Dtr Data Model!");
            return new ConcurrentHashMap<String, List<Dtr>>();
        }

    }

}
