/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

package org.eclipse.tractusx.digitalproductpass.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.config.BpdmConfig;
import org.eclipse.tractusx.digitalproductpass.config.IrsConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceException;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.managers.TreeManager;
import org.eclipse.tractusx.digitalproductpass.models.bpn.AddressInfo;
import org.eclipse.tractusx.digitalproductpass.models.bpn.CompanyInfo;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.BpnRequest;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobHistory;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobRequest;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobResponse;
import org.eclipse.tractusx.digitalproductpass.models.service.BaseService;
import org.sonarsource.scanner.api.internal.shaded.minimaljson.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import utils.DateTimeUtil;
import utils.HttpUtil;
import utils.JsonUtil;
import utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class is a service responsible for handling the communication with a external IRS component.
 *
 * <p> It contains all the methods and properties to communicate with the IRS Service.
 * Its is called by the IRS Controller and can be used by managers to update information.
 */
@Service
public class BpdmService extends BaseService {

    /**
     * ATTRIBUTES
     **/
    HttpUtil httpUtil;
    JsonUtil jsonUtil;
    String bpdmEndpoint;
    String companyInfoPath;
    String bpdmLegalEntityPath;
    String bpdmAddressPath;
    String bpdmSitePath;
    AuthenticationService authService;
    BpdmConfig bpdmConfig;
    ProcessManager processManager;
    TreeManager treeManager;
    VaultService vaultService;

    /**
     * CONSTRUCTOR(S)
     **/
    @Autowired
    public BpdmService(Environment env, ProcessManager processManager, BpdmConfig bpdmConfig, TreeManager treeManager, HttpUtil httpUtil, VaultService vaultService, JsonUtil jsonUtil, AuthenticationService authService) throws ServiceInitializationException {
        this.httpUtil = httpUtil;
        this.processManager = processManager;
        this.jsonUtil = jsonUtil;
        this.authService = authService;
        this.bpdmConfig = bpdmConfig;
        this.treeManager = treeManager;
        this.vaultService = vaultService;
        this.init(env);
    }

    public BpdmService() {
    }

    /** METHODS **/

    /**
     * Initiates the main needed variables for the service
     **/
    public void init(Environment env) {
        this.bpdmEndpoint = this.bpdmConfig.getEndpoint();
        this.bpdmLegalEntityPath = this.bpdmConfig.getLegalEntity().getApiPath();
        this.bpdmSitePath = this.bpdmConfig.getSite().getApiPath();
        this.bpdmAddressPath = this.bpdmConfig.getAddress().getApiPath();
        this.companyInfoPath = this.bpdmConfig.getCompanyInfo().getApiPath();
    }

    /**
     * Creates a List of missing variables needed to proceed with the request.
     * <p>
     *
     * @return an {@code Arraylist} with the environment variables missing in the configuration for the request.
     */
    @Override
    public List<String> getEmptyVariables() {
        List<String> missingVariables = new ArrayList<>();
        if (this.bpdmEndpoint.isEmpty()) {
            missingVariables.add("bpdm.endpoint");
        }
        if (this.bpdmLegalEntityPath.isEmpty()) {
            missingVariables.add("bpdm.legalEntity.apiPath");
        }
        if (this.bpdmSitePath.isEmpty()) {
            missingVariables.add("bpdm.site.apiPath");
        }
        if (this.bpdmAddressPath.isEmpty()) {
            missingVariables.add("bpdm.address.apiPath");
        }
        if (this.companyInfoPath.isEmpty()) {
            missingVariables.add("bpdm.companyInfoPath");
        }
        return missingVariables;
    }

    /**
     * Gets the information from different bpn types and also from companies
     * <p>
     *
     * @param legalBpns   the {@code List<String>} process id of the job
     * @param siteBpns    the {@code List<String>} global asset id from the digital twin
     * @param addressBpns the {@code List<String>} search id provided by the backend to identify the job
     * @return a {@code Map<String, String>} map object with the object prepared for return
     * @throws ServiceException if unable to start the IRS job
     */
    public Map<String, String> getBpnInformation(List<String> legalBpns, List<String> siteBpns, List<String> addressBpns) {
        try {
            // TODO: CHECK HOW THE RETURN WILL BE HERE. MANY TO MANY IS ENABLED..
            return null;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getAddressInformation",
                    e,
                    "It was not possible to get addresses information!");
        }
    }

    /**
     * Search for address information in the BDPM Service
     * <p>
     *
     * @param addressBpns the {@code List<String>} list of BPNA strings
     * @return a {@code List<AddressInfo>} object containing the information from address
     * @throws ServiceException if unable to get the information
     */
    public List<AddressInfo> getBpnaInformation(List<String> addressBpns) {
        try {
            this.checkEmptyVariables();
            String url = this.bpdmEndpoint + "/" + this.bpdmAddressPath;
            // Build the Job request for the IRS

            HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
            headers.add("Content-Type", "application/json");

            ResponseEntity<?> response = httpUtil.doPost(url, List.class, headers, httpUtil.getParams(), addressBpns, false, false);
            return this.buildAddressInfo((List<?>) Objects.requireNonNull(response.getBody()), this.bpdmConfig.getAddress());
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getAddressInformation",
                    e,
                    "It was not possible to get addresses information!");
        }
    }

    /**
     * Search for site address information in the BDPM Service
     * <p>
     *
     * @param siteBpns the {@code List<String>} list of BPNS strings
     * @return a {@code List<AddressInfo>} object containing the information from site
     * @throws ServiceException if unable to get the information
     */
    public List<AddressInfo> getBpnsInformation(List<String> siteBpns) {
        try {
            this.checkEmptyVariables();
            String url = this.bpdmEndpoint + "/" + this.bpdmSitePath;
            // Build the Job request for the IRS

            HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
            headers.add("Content-Type", "application/json");

            ResponseEntity<?> response = httpUtil.doPost(url, List.class, headers, httpUtil.getParams(), siteBpns, false, false);
            return this.buildAddressInfo((List<?>) Objects.requireNonNull(response.getBody()), this.bpdmConfig.getSite());
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getSiteInformation",
                    e,
                    "It was not possible to get site information!");
        }
    }

    /**
     * Search for legal entities address information in the BDPM Service
     * <p>
     *
     * @param legalEntitiesBpns the {@code List<String>} list of BPNL strings
     * @return a {@code List<AddressInfo>} object containing the address information from legal entities
     * @throws ServiceException if unable to get the information
     */
    public List<AddressInfo> getBpnlInformation(List<String> legalEntitiesBpns) {
        try {
            this.checkEmptyVariables();
            String url = this.bpdmEndpoint + "/" + this.bpdmLegalEntityPath;
            // Build the Job request for the IRS

            HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
            headers.add("Content-Type", "application/json");

            ResponseEntity<?> response = httpUtil.doPost(url, List.class, headers, httpUtil.getParams(), legalEntitiesBpns, false, false);
            return this.buildAddressInfo((List<?>) Objects.requireNonNull(response.getBody()), this.bpdmConfig.getLegalEntity());
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getLegalEntitiesInformation",
                    e,
                    "It was not possible to get legal entity information!");
        }
    }

    /**
     * Search for legal entities company information in the BDPM Service
     * <p>
     *
     * @param legalEntitiesBpns the {@code List<String>} list of BPNL strings
     * @return a {@code List<AddressInfo>} object containing the address information from legal entities
     * @throws ServiceException if unable to get the information
     */
    public List<CompanyInfo> getBpnlCompanyInformation(List<String> legalEntitiesBpns) {
        try {
            this.checkEmptyVariables();
            String url = this.bpdmEndpoint + "/" + this.bpdmLegalEntityPath;
            // Build the Job request for the IRS

            HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
            headers.add("Content-Type", "application/json");

            ResponseEntity<?> response = httpUtil.doPost(url, List.class, headers, httpUtil.getParams(), legalEntitiesBpns, false, false);
            return this.buildCompanyInfo((List<?>) Objects.requireNonNull(response.getBody()), this.bpdmConfig.getCompanyInfo());
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getLegalEntitiesInformation",
                    e,
                    "It was not possible to get legal entity information!");
        }
    }

    /**
     * Builds a company info object based on configuration and list of data
     * <p>
     *
     * @param objList the {@code List<?>} list of objects will information
     * @param config  the {@code BpdmConfig.CompanyConfig} the specific configuration for the company
     * @return a {@code List<CompanyInfo>} list of objects containing information about the company
     * @throws ServiceException if unable to get the information
     */
    public List<CompanyInfo> buildCompanyInfo(List<?> objList, BpdmConfig.CompanyConfig config) {
        List<CompanyInfo> companies = new ArrayList<>();
        objList.forEach(o -> {
            // Parse data
            Map<String, Object> data = JsonUtil.bind(o, new TypeReference<>() {
            });
            // If binding was not possible continue iterating
            if (data == null) {
                return;
            }
            CompanyInfo companyInfo = new CompanyInfo(data, config);
            if (!companies.isEmpty()) {
                companies.add(companyInfo);
            }
        });
        if (companies.size() == 0) {
            return null;
        }
        return companies;
    }

    /**
     * Builds an address info object based on configuration and list of data
     * <p>
     *
     * @param objList the {@code List<?>} list of objects will information
     * @param config  the {@code BpdmConfig.AddressConfig} the specific configuration for the Address Type (BPNL, BPNS, BPNA)
     * @return a {@code List<AddressInfo>} list of objects containing information about the company
     * @throws ServiceException if unable to get the information
     */
    public List<AddressInfo> buildAddressInfo(List<?> objList, BpdmConfig.AddressConfig config) {
        List<AddressInfo> addresses = new ArrayList<>();
        objList.forEach(o -> {
            // Parse data
            Map<String, Object> data = JsonUtil.bind(o, new TypeReference<>() {
            });
            // If binding was not possible continue iterating
            if (data == null) {
                return;
            }
            AddressInfo addressInfo = new AddressInfo(data, config);
            if (!addresses.isEmpty()) {
                addresses.add(addressInfo);
            }
        });
        if (addresses.size() == 0) {
            return null;
        }
        return addresses;
    }

}
