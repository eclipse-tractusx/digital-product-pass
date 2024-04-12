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

import org.eclipse.tractusx.digitalproductpass.config.DtrConfig;
import org.eclipse.tractusx.digitalproductpass.models.edc.EndpointDataReference;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.Dataset;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.exceptions.UtilException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class consists exclusively of methods to operate on Eclipse Dataspace Connector (EDC) data.
 *
 * <p> The methods defined here are intended to check or manipulate EDC's related data.
 */
@Component
public class EdcUtil {

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
    // This method is responsible for finding if the EDC is version v0.5.0 basing itself in the contractId format.


//    /**
//     * Gets a specific policy from a dataset by constraint
//     * <p>
//     *
//     * @param dataset the {@code Dataset} object of data set contained in the catalog
//     * @param constraints {@code List<Constraints>} the constraint of the policy to get
//     * @return Set of policy if found or null otherwise.
//     */
    public List<DtrConfig.Policy> getPolicyByConstraint(Dataset dataset, Object configPolicy) {
        Object rawPolicy = dataset.getPolicy();
        // If the policy is not available
        if (rawPolicy == null) {
            return null;
        }
        Set policy = null;
        Set definedPolicy = null;
        Set catalogPolicy = null;
        List<DtrConfig.Constraint> result = null;
        List<DtrConfig.Policy> validatedPolicyList = null;
        List<DtrConfig.Policy> list = null;
        List<LinkedHashMap>  definedPolicyList = null;
        List<DtrConfig.Constraint>  definedConstraints = null;
        List<DtrConfig.Constraint> catalogConstraints = null;
        List<LinkedHashMap> catalogPolicyList = null;

        // Check policy from contract offer catalog
        // If the catalog policy is an object
        if (rawPolicy instanceof LinkedHashMap)
            catalogPolicy = (Set) jsonUtil.bindObject(rawPolicy, Set.class);
        else
            catalogPolicyList = (List<LinkedHashMap>) jsonUtil.bindObject(rawPolicy, List.class);

        // Check policy defined in configuration
        // If the defined policy is an object
        if (configPolicy instanceof LinkedHashMap)
                definedPolicy = (Set) jsonUtil.bindObject(configPolicy, Set.class);
            else {
            definedPolicyList = (List<LinkedHashMap>) jsonUtil.bindObject(configPolicy, List.class);

            // If policy list is null or empty
            if (definedPolicyList == null || definedPolicyList.size() == 0)
                return null;

            for (Object dp : definedPolicyList) {
                for (Object cp : catalogPolicyList) {

                    Set objDefinedPolicy = (Set) jsonUtil.bindObject(dp, Set.class);
                    Set objCatalogPolicy = (Set) jsonUtil.bindObject(cp, Set.class);

                    DtrConfig.Policy obj1 =  (DtrConfig.Policy) jsonUtil.bindObject(objCatalogPolicy.getPermissions(), DtrConfig.Policy.class);
                    DtrConfig.Policy obj2 =  (DtrConfig.Policy) jsonUtil.bindObject(objDefinedPolicy.getPermissions(), DtrConfig.Policy.class);
                    definedConstraints = obj1.getPermissions().getConstraint();
                    catalogConstraints = obj2.getPermissions().getConstraint();

                    List<DtrConfig.Constraint> finalCatalogConstraints = catalogConstraints;
                    result = definedConstraints.stream().filter(definedConstraint ->
                                    finalCatalogConstraints.stream().anyMatch(catalogConstraint ->
                                            catalogConstraint.getLeftOperand().equals(definedConstraint.getLeftOperand())
                                                    && catalogConstraint.getOperator().equals(definedConstraint.getOperator())
                                                    && catalogConstraint.getRightOperand().equals(definedConstraint.getRightOperand())))
                            .collect(Collectors.toList());
                    if (validatedPolicyList != null)
                        validatedPolicyList.add(new DtrConfig.Policy(new DtrConfig.Permission(result)));
                }
            }
        }

            // If the policy does not exist
            if (policy == null) {
                return null;
            }
            // If the policy selected is not the one available!
//        if (!policy.getId().equals(constraints)) {
//            return null;
//        }



        System.out.println(result);
        return null;
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
