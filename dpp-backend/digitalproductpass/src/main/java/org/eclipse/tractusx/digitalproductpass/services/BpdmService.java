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
import io.swagger.v3.core.util.ReflectionUtils;
import org.eclipse.tractusx.digitalproductpass.config.BpdmConfig;
import org.eclipse.tractusx.digitalproductpass.config.IrsConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceException;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.managers.TreeManager;
import org.eclipse.tractusx.digitalproductpass.models.bpn.AddressInfo;
import org.eclipse.tractusx.digitalproductpass.models.bpn.BpnCompany;
import org.eclipse.tractusx.digitalproductpass.models.bpn.CompanyInfo;
import org.eclipse.tractusx.digitalproductpass.models.bpn.Info;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.BpnRequest;
import org.eclipse.tractusx.digitalproductpass.models.http.responses.BpnResponse;
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
import utils.*;

import java.util.*;
import java.util.stream.Collectors;

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
     * Gets the information in the BPDM Service from different bpn types (BPNL, BPNS, BPNA)
     * <p>
     *
     * @param legalBpns   the {@code List<String>} list of BPNL for legal entities
     * @param siteBpns    the {@code List<String>} list of BPNS for sites
     * @param addressBpns the {@code List<String>} list of BPNA for addresses
     * @return {@code BpnResponse} the response from the bpn information request
     * @throws ServiceException if unable to get bpn information
     */
    public BpnResponse getBpnInformation(List<String> legalBpns, List<String> siteBpns, List<String> addressBpns) {
        try {
            Map<String, BpnCompany> companyInfo = this.requestLegalEntityInformation(legalBpns);
            Map<String, AddressInfo> siteLocations = this.getBpnsInformation(siteBpns);
            Map<String, AddressInfo> addressLocations = this.getBpnaInformation(addressBpns);
            return new BpnResponse(companyInfo, siteLocations, addressLocations);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getBpnInformation", e, "It was not possible to get bpn information!");
        }
    }

    /**
     * Gets the information in the BPDM Service from a single bpn type ( BPNA)
     * <p>
     *
     * @param addressBpns the {@code List<String>} list of BPNA for addresses
     * @return {@code BpnResponse} the response from the bpn information request
     * @throws ServiceException if unable to get bpn information
     */
    @SuppressWarnings("Unused")
    public BpnResponse getAddressInformation(List<String> addressBpns) {
        try {
            BpnResponse response = new BpnResponse();
            response.setAddresses(this.getBpnaInformation(addressBpns));
            return response;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getAddressInformation", e, "It was not possible to get bpn address information!");
        }
    }

    /**
     * Gets the information in the BPDM Service from single bpn type (BPNS)
     * <p>
     *
     * @param siteBpns the {@code List<String>} list of BPNS for sites
     * @return {@code BpnResponse} the response from the bpn information request
     * @throws ServiceException if unable to get bpn information
     */
    @SuppressWarnings("Unused")
    public BpnResponse getSiteInformation(List<String> siteBpns) {
        try {
            BpnResponse response = new BpnResponse();
            response.setSites(this.getBpnsInformation(siteBpns));
            return response;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getSiteInformation", e, "It was not possible to get bpn site information!");
        }
    }

    /**
     * Gets the information in the BPDM Service from single bpn type (BPNL)
     * <p>
     *
     * @param legalBpns the {@code List<String>} list of BPNL for legal entities
     * @return {@code BpnResponse} the response from the bpn information request
     * @throws ServiceException if unable to get bpn information
     */
    @SuppressWarnings("Unused")
    public BpnResponse getLegalEntityInformation(List<String> legalBpns) {
        try {
            BpnResponse response = new BpnResponse();
            response.setLegalEntities(this.requestLegalEntityInformation(legalBpns));
            return response;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getLegalEntityInformation", e, "It was not possible to get bpn the legal entity information!");
        }
    }

    /**
     * Gets the information in the BPDM Service from different bpn types (BPNL)
     * <p>
     *
     * @param legalBpns the {@code List<String>} list of BPNL for legal entities
     * @return {@code BpnResponse} the response from the bpn information request
     * @throws ServiceException if unable to get bpn information
     */
    public Map<String, BpnCompany> requestLegalEntityInformation(List<String> legalBpns) {
        try {
            Map<String, CompanyInfo> legalInfo = this.getBpnlCompanyInformation(legalBpns);
            Map<String, AddressInfo> legalLocations = this.getBpnlInformation(legalBpns);
            return this.mergeCompanyInformation(legalInfo, legalLocations);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getLegalEntityInformation", e, "It was not possible to get the legal entity information!");
        }
    }

    /**
     * Merges company information with company address information into one map
     * <p>
     *
     * @param companyInfo      the {@code Map<String, CompanyInfo>} the company information mapped by BPNL
     * @param companyLocations the {@code Map<String, AddressInfo>} the company address informatino mapped by BPNL
     * @return a {@code Map<String, BpnCompany>} map object with the company information merged
     * @throws ServiceException if it was not possible to merge the information
     */
    public Map<String, BpnCompany> mergeCompanyInformation(Map<String, CompanyInfo> companyInfo, Map<String, AddressInfo> companyLocations) {
        try {
            Map<String, BpnCompany> companyMap = new HashMap<>();
            // Map companies information with address locations
            companyInfo.keySet().forEach(o -> companyMap.put(o, new BpnCompany(companyInfo.get(o), companyLocations.get(o))));
            return companyMap;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "mergeInformation", e, "It was not possible to get the company information merged!");
        }
    }

    /**
     * Search for single address information in the BDPM Service
     * <p>
     *
     * @param addressBpn the {@code String} single BPNA element
     * @return a {@code List<AddressInfo>} object containing the information from address
     * @throws ServiceException if unable to get the information
     */
    public AddressInfo getAddressInformation(String addressBpn) {
        try {
            this.checkEmptyVariables();
            String url = this.bpdmEndpoint + "/" + this.bpdmAddressPath + "/" + addressBpn;
            // Build the Job request for the IRS
            HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
            ResponseEntity<?> response = httpUtil.doGet(url, List.class, headers, httpUtil.getParams(), false, false);
            Map<String, Object> body = JsonUtil.bind(response.getBody(), new TypeReference<>() {
            });
            return buildSingleAddressInfo(body, this.bpdmConfig.getAddress());
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getAddressInformation", e, "It was not possible to get address information!");
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
    public Map<String, AddressInfo> getBpnaInformation(List<String> addressBpns) {
        try {
            this.checkEmptyVariables();
            Map<String, AddressInfo> addresses = new HashMap<>();
            addressBpns.forEach(a -> {
                AddressInfo address = this.getAddressInformation(a);
                if (address.isEmpty()) {
                    LogUtil.printError("It was not possible to find information for the following address BPNA [" + a + "]");
                    return;
                }
                addresses.put(address.getBpn(), address);
            });
            if (addresses.isEmpty()) {
                throw new ServiceException(this.getClass().getName() + ".getBpnaInformation", "It was not possible to find any information for the requested BPNA Addresses!");
            }
            return addresses;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getSiteInformation", e, "It was not possible to get site information!");
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
    public Map<String, AddressInfo> getBpnsInformation(List<String> siteBpns) {
        try {
            this.checkEmptyVariables();
            String url = this.bpdmEndpoint + "/" + this.bpdmSitePath;
            // Build the Job request for the IRS

            HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
            headers.add("Content-Type", "application/json");

            ResponseEntity<?> response = httpUtil.doPost(url, List.class, headers, httpUtil.getParams(), siteBpns, false, false);
            return this.buildAddressInfo((List<?>) Objects.requireNonNull(response.getBody()), this.bpdmConfig.getSite());
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getSiteInformation", e, "It was not possible to get site information!");
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
    public Map<String, AddressInfo> getBpnlInformation(List<String> legalEntitiesBpns) {
        try {
            this.checkEmptyVariables();
            String url = this.bpdmEndpoint + "/" + this.bpdmLegalEntityPath;
            // Build the Job request for the IRS

            HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
            headers.add("Content-Type", "application/json");

            ResponseEntity<?> response = httpUtil.doPost(url, List.class, headers, httpUtil.getParams(), legalEntitiesBpns, false, false);
            return this.buildAddressInfo((List<?>) Objects.requireNonNull(response.getBody()), this.bpdmConfig.getLegalEntity());
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getLegalEntitiesInformation", e, "It was not possible to get legal entity information!");
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
    public Map<String, CompanyInfo> getBpnlCompanyInformation(List<String> legalEntitiesBpns) {
        try {
            this.checkEmptyVariables();
            String url = this.bpdmEndpoint + "/" + this.companyInfoPath;
            // Build the Job request for the IRS

            HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
            headers.add("Content-Type", "application/json");

            ResponseEntity<?> response = httpUtil.doPost(url, List.class, headers, httpUtil.getParams(), legalEntitiesBpns, false, false);
            return this.buildCompanyInfo((List<?>) Objects.requireNonNull(response.getBody()), this.bpdmConfig.getCompanyInfo());
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getLegalEntitiesInformation", e, "It was not possible to get legal entity information!");
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
    public Map<String, CompanyInfo> buildCompanyInfo(List<?> objList, BpdmConfig.CompanyConfig config) {
        try {
            Map<String, CompanyInfo> companies = new HashMap<>();
            objList.forEach(o -> {
                // Get the company data and parse it into the company information
                CompanyInfo companyInfo = this.getData(o, CompanyInfo.class, config);
                if (companyInfo.isEmpty()) {
                    LogUtil.printError("It was not possible to find information for the following company object [" + jsonUtil.toJson(o, false) + "]");
                    return;
                }
                companies.put(companyInfo.getBpn(), companyInfo);
            });
            if (companies.isEmpty()) {
                throw new ServiceException(this.getClass().getName() + ".buildCompanyInfo", "It was not possible to build any company information!");
            }
            return companies;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "buildCompanyInfo", e, "Failed to build company information!");
        }
    }

    /**
     * Builds a single address info object based on configuration and list of data
     * <p>
     *
     * @param obj    the {@code Map<String, Object>} object with the address information
     * @param config the {@code BpdmConfig.AddressConfig} the specific configuration for the Address Type (BPNA)
     * @return a {@code AddressInfo} object containing information about the address
     * @throws ServiceException if unable to get the information
     */
    public AddressInfo buildSingleAddressInfo(Map<String, Object> obj, BpdmConfig.AddressConfig config) {
        try {
            // Get a single address information and parse it into the address information
            return this.getData(obj, AddressInfo.class, config);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "buildSingleAddressInfo", e, "Failed to build single address information");
        }
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
    public Map<String, AddressInfo> buildAddressInfo(List<?> objList, BpdmConfig.AddressConfig config) {
        try {
            Map<String, AddressInfo> addresses = new HashMap<>();
            objList.forEach(o -> {
                // Get address information and parse it into the address information
                AddressInfo addressInfo = this.getData(o, AddressInfo.class, config);
                if (addressInfo.isEmpty()) {
                    LogUtil.printError("It was not possible to find information for the following address object [" + jsonUtil.toJson(o, false) + "]");
                    return;
                }
                addresses.put(addressInfo.getBpn(), addressInfo);
            });
            if (addresses.isEmpty()) {
                throw new ServiceException(this.getClass().getName() + ".buildCompanyInfo", "It was not possible to build any address information!");
            }
            return addresses;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "buildAddressInfo", e, "Failed to build address information!");
        }
    }

    /**
     * Get the entity data from a raw data into a specific class instance using the configuration
     * <p>
     *
     * @param rawData  the {@code Object} the object which contains the raw data
     * @param instance the {@code Class<T>} class type which will be returned a object
     * @param config   the {@code BpdmConfig.EntityConfig} the generic configuration for the Company and Address Type (BPNL, BPNS, BPNA)
     * @return a {@code T} class type from the parameter instance
     * @throws ServiceException if unable to get the data
     */

    public <T> T getData(Object rawData, Class<T> instance, BpdmConfig.EntityConfig config) {
        try {
            // Parse data
            Map<String, Object> data = JsonUtil.bind(rawData, new TypeReference<>() {
            });
            // If binding was not possible continue iterating
            if (data == null) {
                return null;
            }
            // Get bpn by configured key for the entity
            String bpn = JsonUtil.bind(data.get(config.getBpnKey()), new TypeReference<>() {
            });
            if (bpn == null) {
                throw new ServiceException(this.getClass().getName() + "." + "getData", "No bpn key available for the information!");
            }
            // Instance a class dynamically with its parameters
            return ReflectionUtil.instanceClass(instance, data, config);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getData", e, "Failed to parse and get data!");
        }
    }

}
