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

package org.eclipse.tractusx.digitalproductpass.models.negotiation.policy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ModelException;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DidDocument;
import org.springframework.beans.factory.annotation.Autowired;
import utils.LogUtil;
import utils.PolicyUtil;
import utils.exceptions.UtilException;

import java.util.ArrayList;
import java.util.List;
import java.util.List;
import java.util.Objects;

/**
 * This class consists exclusively to define attributes related to the Policy's data.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Set extends DidDocument {

    /** ATTRIBUTES **/
    @JsonIgnore
    static final String PERMISSION = "odrl:permission";
    @JsonIgnore
    static final String PROHIBITION = "odrl:prohibition";
    @JsonIgnore
    static final String OBLIGATION = "odrl:obligation";

    @JsonProperty(PERMISSION)
    List<Action> permissions;
    @JsonProperty(PROHIBITION)
    List<Action> prohibitions;

    @JsonProperty(OBLIGATION)
    List<Action> obligations;

    /** CONSTRUCTOR(S) **/
    public Set(String id, String type, List<Action> permissions, List<Action> prohibitions, List<Action> obligations) {
        super(id, type);
        this.permissions = permissions;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
    }

    public Set(List<Action> permissions, List<Action> prohibitions, List<Action> obligations) {
        this.permissions = permissions;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
    }

    public Set(String id, String type, JsonNode context, List<Action> permissions, List<Action> prohibitions, List<Action> obligations) {
        super(id, type, context);
        this.permissions = permissions;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
    }

    public Set(String type, List<Action> permissions, List<Action> prohibitions, List<Action> obligations) {
        super(type);
        this.permissions = permissions;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
    }

    public Set(JsonNode context, List<Action> permissions, List<Action> prohibitions, List<Action> obligations) {
        super(context);
        this.permissions = permissions;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
    }

    public Set(String id, String type) {
        super(id, type);
    }
    public Set() {
    }
    public Set(PolicyCheckConfig.PolicyConfig policyConfig) {
        buildSet(policyConfig);
    }
    /* METHODS */

    /**
     * Method responsible for parsing the policy based on the configuration
     * <p>
     * @param  policyConfig {@code PolicyCheckConfig.PolicyConfig} instance representing the policy configuration from the policy
     *
     */
    public void buildSet(PolicyCheckConfig.PolicyConfig policyConfig){
        // Set all the values for the different actions
        this.permissions = this.buildActions(policyConfig.getPermission());
        this.obligations = this.buildActions(policyConfig.getObligation());
        this.prohibitions = this.buildActions(policyConfig.getProhibition());
    }

    public List<Action> buildActions(List<PolicyCheckConfig.ActionConfig> actionConfigs){
        try {
            // Set actions to the collection
            List<Action> actions = new ArrayList<>(actionConfigs.size());
            actionConfigs.forEach(actionConfig -> actions.add(new Action(actionConfig))); // Parse and create actions
            return actions;
        }catch (Exception e){
            throw new ModelException(this.getClass().getName(), "It was not possible to build actions");
        }
    }

    /**
     * Builds a policy from a raw policy object
     * <p>
     *
     * @param rawPolicy {@code Object} the policy to be checked
     * @throws ModelException if error when parsing the contracts
     */
    static public Set build(Object rawPolicy){
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode policy = mapper.convertValue(rawPolicy, new TypeReference<>(){});
            String policyId = null;
            String policyType = null;
            // Parse policy to json node
            if(policy.has(ID)) {
                policyId = mapper.convertValue(policy.get(ID), new TypeReference<>() {});
            }
            if(policy.has(TYPE)) {
                policyType = mapper.convertValue(policy.get(TYPE), new TypeReference<>() {});
            }

            // Get permission, prohibition and obligation
            JsonNode permission = policy.get(PERMISSION);
            JsonNode prohibition = policy.get(PROHIBITION);
            JsonNode obligation = policy.get(OBLIGATION);

            // Check if its null
            if(permission == null || prohibition == null || obligation == null){
                throw new ModelException(Set.class.getName(), "One of the mandatory fields where not found! This is not a valid policy set!");
            }
            // Check if all them are array then parse as default
            if(permission.isArray() && prohibition.isArray() && obligation.isArray()){
                return mapper.convertValue(rawPolicy, new TypeReference<>(){});
            }
            // If not parse the set by action type
            return new Set(policyId, policyType, Action.build(permission), Action.build(prohibition), Action.build(obligation));
        }catch (Exception e) {
            throw new ModelException(Set.class.getName(), "It was not possible to create a new policy!");
        }
    }




    /** GETTERS AND SETTERS **/

    public List<Action> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Action> permissions) {
        this.permissions = permissions;
    }

    public List<Action> getProhibitions() {
        return prohibitions;
    }

    public void setProhibitions(List<Action> prohibitions) {
        this.prohibitions = prohibitions;
    }

    public List<Action> getObligations() {
        return obligations;
    }

    public void setObligations(List<Action> obligations) {
        this.obligations = obligations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Set set = (Set) o;
        return Objects.equals(permissions, set.permissions) && Objects.equals(prohibitions, set.prohibitions) && Objects.equals(obligations, set.obligations);
    }



    /**
     * Method responsible for parsing the policy based on the configuration
     * <p>
     * @param  set {@code Set} policy to compare with the current policy
     *
     */
    public Boolean compare(Set set){
        // Compare policies actions
        if(!this.compareActions(set.getProhibitions(), this.getProhibitions())){return false;}
        if(!this.compareActions(set.getObligations(), this.getObligations())){return false;}
        return this.compareActions(set.getPermissions(), this.getPermissions());
    }
    /**
     * Method responsible for comparing two actions constraints
     * <p>
     * @param  currentActions {@code List<Action>} is the object to be compared
     * @param  incomingActions {@code List<Action>} is the object to be compared to
     * @return true if the actions are the same
     */
    public Boolean compareActions(List<Action> currentActions, List<Action> incomingActions){
        try {
            // Optimizations to avoid searching in children
            if(currentActions == null && incomingActions == null){return true;} // If both actions are null they are equal
            if(currentActions == null || incomingActions == null){return false;}// If one of be options are null and they both are not than they are not the same
            if(currentActions.size() != incomingActions.size()){return false;} // If the actions are with different sizes
            if(currentActions.isEmpty()){return true;} // If the actions are empty they are the same and have the same size if one is empty both are empty
            // Set actions to the collection
            List<Boolean> validActions = new ArrayList<>();
            incomingActions.stream().parallel().forEach(a -> currentActions.stream().parallel().forEach(ac -> {if(ac.compare(a)){validActions.add(true);}}));
            return validActions.size() == currentActions.size(); //If all the actions are the same there must be the same number of actions
        }catch (Exception e){
            throw new ModelException(this.getClass().getName(), e,"It was not possible to build actions");
        }
    }


    @Override
    public int hashCode() {
        return Objects.hash(permissions, prohibitions, obligations);
    }
}
