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

package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig;
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig.ActionConfig;
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig.PolicyConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ManagerException;
import org.eclipse.tractusx.digitalproductpass.models.edc.EndpointDataReference;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog.Dataset;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.policy.Set;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.policy.Constraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.exceptions.UtilException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class consists exclusively of methods to operate on Eclipse Dataspace Connector (EDC) data.
 *
 * <p> The methods defined here are intended to check or manipulate EDC's related data.
 */
@Component
public class EdcUtil {

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private PolicyUtil policyUtil;

    public EdcUtil() {
    }
    public EdcUtil(JsonUtil jsonUtil,PolicyUtil policyUtil) {
        this.jsonUtil = jsonUtil;
        this.policyUtil = policyUtil;
    }
    /**
     * Parses the data from HTTP request {@code Object} body to a {@code DataPLaneEndpoint} object data.
     * <p>
     *
     * @param body the {@code Object} object representing the body for the request.
     * @return a {@code DataPlaneEndpoint} object with the parsed date retrieved from the body.
     * @throws UtilException if unable to parse to the data.
     */
    public EndpointDataReference parseDataPlaneEndpoint(Object body) {
        try {
            return (EndpointDataReference) this.jsonUtil.bindObject(body, EndpointDataReference.class);
        } catch (Exception e) {
            throw new UtilException(EdcUtil.class, e, "It was not possible to parse the data plain endpoint");
        }
    }

    /**
     * Checks if the EDC is version v0.5.0 basing on the contractId format.
     * <p>
     *
     * @param contractId the contractId of the Digital Twin Registry (DTR) as a String.
     * @return true if the EDC's version is v0.5.0, false otherwise.
     * @throws UtilException if unable to check the EDC's version.
     */
    @SuppressWarnings("Unused")
    public Boolean isEdc5(String contractId) {
        try {
            String[] parts = contractId.split(String.format("\\%s", ":"));
            if (parts.length != 3) {
                throw new UtilException(EdcUtil.class, "It was not possible to check if EDC is v0.5.0, Invalid Contract Id");
            }
            return CrypUtil.isBase64(parts[1]); // If the contractId is base64 encoded
        } catch (Exception e) {
            throw new UtilException(EdcUtil.class, e, "It was not possible check if is the EDC v0.5.0");
        }
    }

    /**
     * Maps lists of datasets from a catalog into a map of contract ids and contracts
     * <p>
     *
     * @param datasets {@code List<Dataset>} the list of contracts to map.
     * @return {@code Map<String, Dataset>} the map of contract ids with the contracts
     * @throws UtilException if error when parsing the contracts
     */
    public Map<String, Dataset> mapDatasetsById(List<Dataset> datasets){
        try {
            return datasets.stream()
                    .collect(Collectors.toMap(Dataset::getId, Function.identity()));
        } catch (Exception e) {
            throw new UtilException(EdcUtil.class, e, "It was not possible to map the datasets");
        }
    }

    /**
     * Filters a map of contracts by policy configuration constraints
     * <p>
     *
     * @param contracts {@code Map<String, Dataset>} the map of contracts by id
     * @param policyConfig {@code PolicyCheckConfig} the policy configuration to filter the valid contracts
     * @return {@code Map<String, Dataset>} the map of contract ids with the valid contracts
     * @throws UtilException if error when parsing the contracts or filtering
     */
    public Map<String, Dataset> filterValidContracts(Map<String, Dataset> contracts, PolicyCheckConfig policyConfig){
        try {
            // If policy config is disabled then all the contracts are valid
            if(!policyConfig.getEnabled()){
                return contracts;
            }
            // Filter contracts by policy config
            return contracts.entrySet().stream()
                    .filter(contract -> this.isContractValid(contract.getValue(), policyConfig))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (Exception e) {
            throw new UtilException(EdcUtil.class, e, "It was not possible to filter the valid contracts");
        }
    }

    /**
     * Checks if a contract is valid against the policy configuration
     * <p>
     *
     * @param contract {@code Dataset} the contract to be checked if valid
     * @param policyConfig {@code PolicyCheckConfig} the policy configuration to be checked
     * @return {@code Boolean} true if contract is valid
     * @throws UtilException if error when checking if contract is valid
     */
    public Boolean isContractValid(Dataset contract, PolicyCheckConfig policyConfig){
        try {
            // If policy config is disabled then the contract is valid
            if(!policyConfig.getEnabled()){
                return true;
            }
            Object policies = contract.getPolicy();
            if (policies == null){
                throw new UtilException(EdcUtil.class,"The contract has no parsable policies");
            }
            // Check all the contract policies against the configuration
            List<Set> validPolicies = policyUtil.getValidPoliciesByConstraints(policies, policyConfig);
            return validPolicies.size() > 0; // If any policy is found the contract is valid
        } catch (Exception e) {
            throw new UtilException(EdcUtil.class, e, "It was not possible to check if contract is valid");
        }
    }

}
