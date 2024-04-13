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
import org.eclipse.tractusx.digitalproductpass.config.DtrConfig;
import org.eclipse.tractusx.digitalproductpass.config.PolicyConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ControllerException;
import org.eclipse.tractusx.digitalproductpass.models.catenax.Dtr;
import org.eclipse.tractusx.digitalproductpass.models.edc.EndpointDataReference;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.Dataset;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.Set;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.Constraint;
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

    private final List<String> logicConstraints = List.of("odrl:and", "odrl:or");
    private final String odrlConstraintKey = "odrl:constraint";
    private final String odrlLeftOperandKey = "odrl:leftOperand";
    private final String odrlRightOperandKey = "odrl:rightOperand";
    private final String odrlOperatorIdPath = "odrl:operator.@id";

    private final JsonUtil jsonUtil;
    private Object cp;

    @Autowired
    public EdcUtil(JsonUtil jsonUtil) {
        this.jsonUtil = jsonUtil;
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
     * Evaluates a policy for specific constraints to check if policy is valid and can be parsed
     * <p>
     *
     *  @param constraint the {@code Object} to be checked if constraint
     *  @return mapped constraint or null if constraint is not valid
     *
     **/
    public Constraint parseConstraint(Object constraint){
        try {
            // Checks if a constraint can be parsed or is empty
            Constraint parsedConstraint = jsonUtil.convertObject(constraint, Constraint.class);
            return !parsedConstraint.isEmpty()?parsedConstraint:null;
        }catch (Exception e) {
            throw new UtilException(EdcUtil.class, "It is not possible to parse constraint, something went wrong!");
        }
    }


    public Boolean isConstraintValid(Constraint parsedConstraint, List<PolicyConfig.ConstraintConfig> constraintsConfig, String prefix){
        return true;
    }
    /**
     * Evaluates a policy for specific constraints to check if policy is valid
     * <p>
     *
     *  @param policy the {@code Set} object of with one or more policies
     *  @param constraintsConfig {@code List<PolicyConfig.ConstraintConfig>} list of constraints for permissions
     *  @param operand the {@code String} operand from the policy, can be null, "odrl:and" or "odrl:or"
     *  @param prefix the {@code String} prefix before the leftConstraint key ex: "cx-policy"
     *  @return {@code Boolean} condition true if the policy is valid.
     */
    public Boolean isPolicyPermissionsValid(Set policy, List<PolicyConfig.ConstraintConfig> constraintsConfig, String operand, String prefix){
        if(policy == null || constraintsConfig == null || constraintsConfig.size() == 0){
            return null;
        }

        Object permissions = policy.getPermissions();
        // In case permissions is not a list
        if (permissions instanceof LinkedHashMap) {
             Object constraint = jsonUtil.getValue(permissions, odrlConstraintKey, ".", null);
             Constraint parsedConstraint = parseConstraint(constraint);
             if(parsedConstraint != null){
                 // It was possible to parse the constraint
                 return isConstraintValid(parsedConstraint, constraintsConfig, prefix);
             }else{
                 return false;
                 // This is a collection of constraints
             }

        }else{

            // Many permissions
            return false;

        }
    }

   /**
    * Gets a specific policy from a dataset by constraint
    * <p>
    *
    *  @param policies the {@code Object} object of with one or more policies
    *  @param permissionConstraints {@code List<PolicyConfig.ConstraintConfig>} list of constraints for permissions
    *  @param operand the {@code String} operand from the policy, can be null, "odrl:and" or "odrl:or"
    *  @param prefix the {@code String} prefix before the leftConstraint key ex: "cx-policy"
    * @return Set if the policy is found
     */
    public Set getPolicyByConstraints(Object policies, List<PolicyConfig.ConstraintConfig> permissionConstraints, String operand, String prefix) {
        // If the policy is not available
        if (policies == null) {
            return null;
        }
        Set policy = null;
        // Check policy from contract offer catalog
        // If the catalog policy is an object
        if (policies instanceof LinkedHashMap) {
            policy = (Set) jsonUtil.bindObject(policies, Set.class);
            this.isPolicyPermissionsValid(policy, permissionConstraints, operand, prefix);
        } else {
            List<Set> policyList = null;
            try {
                policyList = (List<Set>) jsonUtil.bindReferenceType(policies, new TypeReference<List<Set>>() {});
            } catch (Exception e) {
                throw new UtilException(EdcUtil.class, e, "It was not possible to parse the policy");
            }
        }
        return policy;
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
