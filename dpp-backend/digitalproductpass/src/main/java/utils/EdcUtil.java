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

    private final List<String> logicalConstraints = List.of("odrl:and", "odrl:or");
    private final String odrlConstraintKey = "odrl:constraint";
    private final String odrlLeftOperandKey = "odrl:leftOperand";
    private final String odrlRightOperandKey = "odrl:rightOperand";
    private final String odrlOperatorIdPath = "odrl:operator.@id";

    private Object cp;

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private PolicyUtil policyUtil;

    public EdcUtil() {
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
     * Gets a specific policy from a dataset by id
     * <p>
     *
     * @param dataset the {@code Dataset} object of data set contained in the catalog
     * @param policyId {@code String} the id of the policy to get
     * @return Set of policy if found or null otherwise.
     */
    public Set getPolicyById(Dataset dataset, String policyId) {
        Object rawPolicy = dataset.getPolicy();
        // If the policy is not available
        if (rawPolicy == null) {
            return null;
        }
        Set policy = null;
        // If the policy is an object
        if (rawPolicy instanceof LinkedHashMap) {
            policy = (Set) this.jsonUtil.bindObject(rawPolicy, Set.class);
        } else {
            List<LinkedHashMap> policyList = (List<LinkedHashMap>) this.jsonUtil.bindObject(rawPolicy, List.class);
            if (policyList == null) {
                return null;
            }
            policy = (Set) this.jsonUtil.bindObject(policyList.stream().filter(
                    (p) -> p.get("@id").equals(policyId)
            ).findFirst(), Set.class); // Get policy with the specific policy id
        }
        // If the policy does not exist
        if (policy == null) {
            return null;
        }
        // If the policy selected is not the one available!
        if (!policy.getId().equals(policyId)) {
            return null;
        }

        return policy;
    }


    /**
     * Evaluate if the policy give in included in the list of policies
     * <p>
     *
     *  @param policy the {@code Set} of the policy
     *  @param validPolicies the {@code validPolicies} list of valid policies to be compared to
     *  @return true if the policy is valid
     *
     **/
    public Boolean isPolicyValid(Set policy, List<Set> validPolicies, Boolean strictMode){
        try {
            // Check is strict mode is selected
            if(strictMode){ return policyUtil.strictPolicyCheck(policy, validPolicies); }
            return policyUtil.defaultPolicyCheck(policy, validPolicies);
        }catch (Exception e) {
            throw new UtilException(EdcUtil.class, "It was not possible to check if policy is valid");
        }
    }

   /**
    * Gets a specific policy from a dataset by constraint
    * <p>
    *
    *  @param policies the {@code Object} object of with one or more policies
    *  @param policyCheckConfigs {@code List<PolicyConfig>} list of constraints for the permissions
    * @return Correct policy for constraints or null if the policy or policies are not valid.
     */
    public Set getPolicyByConstraints(Object policies, PolicyCheckConfig policyCheckConfigs) {
        // Find if policy is array or object and call the evaluate functions
        try {
            List<PolicyConfig> policyConfigs = policyCheckConfigs.getPolicies();
            // If the policy is not available
            if (policies == null || policyCheckConfigs == null) {
                return null;
            }

            List<Set> validPolicies = policyUtil.buildPolicies(policyConfigs);
            Boolean strictMode = policyCheckConfigs.getStrictMode();
            // There is no valid policy available
            if (validPolicies == null || validPolicies.size() == 0) {
                return null;
            }
            if (policies instanceof LinkedHashMap) {
                System.out.println(policies.getClass().getName());
                System.out.println(jsonUtil.toJson(policies,true));
                // Check if policy is valid or not
                Set policy = jsonUtil.bind(policies, new TypeReference<>(){});
                System.out.println(policy.getClass().getName());
                System.out.println(jsonUtil.toJson(policy, true));
                // In case the policy is valid return the policy
                return this.isPolicyValid(policy, validPolicies, strictMode)?policy:null;
            }
            List<Set> policyList = null;
            try {
                policyList = jsonUtil.bind(policies, new TypeReference<>(){});
            } catch (Exception e) {
                throw new UtilException(EdcUtil.class, e, "It was not possible to parse the policy");
            }
            return policyList.stream().findFirst().orElse(null);
        }catch (Exception e) {
            throw new UtilException(EdcUtil.class, "It was not possible to get policy by constraints!");
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

}
