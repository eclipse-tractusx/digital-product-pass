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

package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig;
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig.ActionConfig;
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig.PolicyConfig;
import org.eclipse.tractusx.digitalproductpass.models.edc.EndpointDataReference;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog.Dataset;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.policy.Action;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.policy.Constraint;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.policy.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.exceptions.UtilException;

import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * This class is responsible for building and handling with policies
 *
 * <p> The methods defined here are intended to check or manipulate EDC's related data.
 */
@Component
public class PolicyUtil {

    @Autowired
    JsonUtil jsonUtil;
    public PolicyUtil() {
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
            if(strictMode){ return this.strictPolicyCheck(policy, validPolicies); }
            return this.defaultPolicyCheck(policy, validPolicies);
        }catch (Exception e) {
            throw new UtilException(EdcUtil.class, "It was not possible to check if policy is valid");
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
            policy = this.parsePolicy(rawPolicy);
        } else {
            List<Set> policyList = this.parsePolicies(rawPolicy);
            if (policyList == null) {
                return null;
            }
            policy = policyList.stream().filter(
                    (p) -> p.getId().equals(policyId)
            ).findFirst().orElse(null);
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
     * Gets a specific policy from a dataset by constraint
     * <p>
     *
     *  @param policies the {@code Object} object of with one or more policies
     *  @param policyCheckConfigs {@code List<PolicyConfig>} list of constraints for the permissions
     * @return List of valid policies for constraints or null if the policy or policies are not valid.
     */
    public List<Set> getValidPoliciesByConstraints(Object policies, PolicyCheckConfig policyCheckConfigs) {
        // Find if policy is array or object and call the evaluate functions
        try {
            // If the policy is not available
            if (policies == null || policyCheckConfigs == null) {
                return null;
            }
            List<PolicyConfig> policyConfigs = policyCheckConfigs.getPolicies();
            List<Set> validPolicies = this.buildPolicies(policyConfigs);

            LogUtil.printMessage("DEBUG: Valid Policies: " + jsonUtil.toJson(validPolicies, true));

            Boolean strictMode = policyCheckConfigs.getStrictMode();
            // There is no valid policy available
            if (validPolicies == null || validPolicies.size() == 0) {
                return null;
            }
            LogUtil.printMessage("DEBUG: Raw Policies: " + jsonUtil.toJson(policies, true));

            if (policies instanceof LinkedHashMap) {
                // Check if policy is valid or not
                LogUtil.printMessage("DEBUG: Single Policy!");
                Set policy = this.parsePolicy(policies);
                LogUtil.printMessage("DEBUG: Policy: " + jsonUtil.toJson(policy, true));

                // In case the policy is valid return the policy
                if(this.isPolicyValid(policy, validPolicies, strictMode)){
                    LogUtil.printMessage("DEBUG: Policy not valid!");

                    return new ArrayList<>(){{add(policy);}}; // Add policy to a list of valid policies
                }
                // If the policy is not valid return an empty list
                return new ArrayList<>();
            }
            LogUtil.printMessage("DEBUG: Multiple Policies!");
            List<Set> policyList = this.parsePolicies(policies);
            LogUtil.printMessage("DEBUG: Policies: " + jsonUtil.toJson(policyList, true));

            //Search for policies that are valid and get one of the valid ones
            return policyList.stream().parallel().filter(p -> this.isPolicyValid(p, validPolicies, strictMode)).toList();
        }catch (Exception e) {
            throw new UtilException(PolicyUtil.class, "It was not possible to get policy by constraints!");
        }
    }
    /**
     * Gets a specific policy from a dataset by constraint
     * <p>
     *
     *  @param policies the {@code Object} object of with one or more policies
     *  @param policyCheckConfigs {@code List<PolicyConfig>} list of constraints for the permissions
     * @return Correct policy for constraints or null if no policy is valid
     */
    public Set getPolicyByConstraints(Object policies, PolicyCheckConfig policyCheckConfigs) {
        // Find if policy is array or object and call the evaluate functions
        try {
            //Search for policies that are valid and get one of the valid ones or return null
            return this.getValidPoliciesByConstraints(policies, policyCheckConfigs).stream().findAny().orElse(null);
        }catch (Exception e) {
            throw new UtilException(PolicyUtil.class, "It was not possible to get policy by constraints!");
        }
    }

    /**
     * Build policies from configuration parameters
     * <p>
     *
     * @param policyConfigs {@code List<PolicyConfig>} the list of policy configurations
     * @return {@code List<Set>} the list of parsed policies built from the configuration parameters
     * @throws UtilException if error when parsing the contracts
     */
    public List<Set> buildPolicies(List<PolicyConfig> policyConfigs){
        try {
            // Initialize policy array
            List<Set> policies = new ArrayList<>(policyConfigs.size());
            policyConfigs.forEach(policyConfig -> policies.add(new Set(policyConfig))); //Build the policies based on the configuration
            return policies; // Return the clean policies
        } catch (Exception e) {
            throw new UtilException(PolicyUtil.class, e, "It was not possible to parse the policies from configuration!");
        }
    }

    /**
     * Checks a policy configuration strictly against the incoming policy
     * <p>
     *
     * @param policy {@code Set} the policy to be checked
     * @param validPolicies {@code List<Set>} the list of valid policies
     * @return {@code List<Set>} the list of parsed policies built from the configuration parameters
     * @throws UtilException if error when parsing the contracts
     */
    public Boolean strictPolicyCheck(Set policy, List<Set> validPolicies){
        try {
            // Get the hashCodes from the different policies
            List<String> hashes = validPolicies.stream().map(p -> CrypUtil.sha256(this.jsonUtil.toJson(p, false))).toList();
            String policyHash = CrypUtil.sha256(this.jsonUtil.toJson(policy, false));
            return hashes.contains(policyHash); // If hashcode is in the list the policy is valid
        }catch (Exception e) {
            throw new UtilException(PolicyUtil.class, "[STRICT MODE] It was not possible to check if policy is valid!");
        }
    }
    /**
     * Checks a policy configuration strictly against the incoming policy
     * <p>
     *
     * @param policy {@code Set} the policy to be checked
     * @param configPolicy {@code List<Set>} the config policy to be checked
     * @return {@code List<Set>} the list of parsed policies built from the configuration parameters
     * @throws UtilException if error when parsing the contracts
     */
    public Boolean isPolicyConstraintsValid(Set policy, Set configPolicy){
        try {
            return policy.compare(configPolicy);
        }catch (Exception e) {
            throw new UtilException(PolicyUtil.class, e, "[DEFAULT MODE] It was not possible to check if policy is valid!");
        }
    }
    /**
     * Builds a policy from a raw policy object
     * <p>
     *
     * @param rawPolicy {@code Object} the policy to be checked
     * @return {@code Set} the list of parsed policies built from the configuration parameters
     * @throws UtilException if error when parsing the contracts
     */
    public List<Set> parsePolicies(Object rawPolicy){
        try {
            JsonNode policy = jsonUtil.toJsonNode(rawPolicy);
            if(!policy.isArray()){
                Set parsedPolicy = this.parsePolicy(policy);
                return new ArrayList<>(){{add(parsedPolicy);}};
            }
            List<Set> policies = new ArrayList<>();
            for (JsonNode p : policy) {
                policies.add(this.parsePolicy(p));
            }
            return policies;
        }catch (Exception e) {
            throw new UtilException(PolicyUtil.class, e, "It was not possible to create a new policy!");
        }
    }
    /**
     * Builds a policy from a raw policy object
     * <p>
     *
     * @param rawPolicy {@code Object} the policy to be checked
     * @return {@code Set} the list of parsed policies built from the configuration parameters
     * @throws UtilException if error when parsing the contracts
     */
    public Set parsePolicy(Object rawPolicy){
        try {
            // Parse policy to json node
            JsonNode policy = jsonUtil.toJsonNode(rawPolicy);
            LogUtil.printMessage("DEBUG: Json Node Policy: " + jsonUtil.toJson(policy, true));
            // Get permission, prohibition and obligation
            JsonNode permission = policy.get("odrl:permission");
            LogUtil.printMessage("DEBUG: Permission Json Node: " + jsonUtil.toJson(permission, true));

            JsonNode prohibition = policy.get("odrl:prohibition");
            LogUtil.printMessage("DEBUG: prohibition Json Node: " + jsonUtil.toJson(prohibition, true));

            JsonNode obligation = policy.get("odrl:obligation");
            LogUtil.printMessage("DEBUG: prohibition Json Node: " + jsonUtil.toJson(obligation, true));

            // Check if its null
            if(permission == null || prohibition == null || obligation == null){
                throw new UtilException(PolicyUtil.class, "One of the policy action constraints is empty!");
            }
            // Check if all them are array then parse as default
            if(permission.isArray() && prohibition.isArray() && obligation.isArray()){
                LogUtil.printMessage("DEBUG: All is array ");

                return jsonUtil.bind(rawPolicy, new TypeReference<>(){});
            }
            LogUtil.printMessage("DEBUG: One is not array ");
            // If not parse the set by action type
            return new Set(this.parseActions(permission), this.parseActions(prohibition), this.parseActions(obligation));
        }catch (Exception e) {
            throw new UtilException(PolicyUtil.class, e, "It was not possible to create a new policy!");
        }
    }
    /**
     * Builds an action from a raw policy object
     * <p>
     *
     * @param node {@code JsonNode} action to be checked
     * @return {@code List<Action>} the list of actions parsed
     * @throws UtilException if error when parsing the contracts
     */
    public List<Action> parseActions(JsonNode node){
        // If node is not array parse a single action object
        if(!node.isArray()){
            LogUtil.printMessage("DEBUG: Node is not array " +jsonUtil.toJson(node, true));
            return new ArrayList<>(){{add(jsonUtil.bind(node, new TypeReference<>(){}));}};
        }
        LogUtil.printMessage("DEBUG: Node is array " +jsonUtil.toJson(node, true));
        // If node is array parse the action node as a list
        return jsonUtil.bind(node, new TypeReference<>(){});
    }

    /**
     * Checks a policy configuration strictly against the incoming policy
     * <p>
     *
     * @param policy {@code Set} the policy to be checked
     * @param validPolicies {@code List<Set>} the list of valid policies
     * @return {@code Boolean} return true if policy is valid with default mode
     * @throws UtilException if error when doing the policy check
     */
    public Boolean defaultPolicyCheck(Set policy, List<Set> validPolicies){
        try {
            // Filter the list of policies based on the policy configuration
            List<Set> policies = validPolicies.stream().filter(p -> this.isPolicyConstraintsValid(policy, p)).toList();
            System.out.println("[DEBUG] [VALID POLICIES] " + policies);
            return policies.size() > 0; //If any policy is valid then return true
        }catch (Exception e) {
            throw new UtilException(PolicyUtil.class, e, "[DEFAULT MODE] It was not possible to check if policy is valid!");
        }
    }


}
