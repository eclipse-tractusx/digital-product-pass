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

package org.eclipse.tractusx.digitalproductpass.models.negotiation.policy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ModelException;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DidDocument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * This class consists exclusively to define attributes related to the Policy's data.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Set extends DidDocument {

    /** ATTRIBUTES **/
    @JsonProperty("odrl:permission")
    Collection<Action> permissions;
    @JsonProperty("odrl:prohibition")
    Collection<Action> prohibitions;
    @JsonProperty("odrl:obligation")
    Collection<Action> obligations;

    /** CONSTRUCTOR(S) **/
    public Set(String id, String type, Collection<Action> permissions, Collection<Action> prohibitions, Collection<Action> obligations) {
        super(id, type);
        this.permissions = permissions;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
    }

    public Set(Collection<Action> permissions, Collection<Action> prohibitions, Collection<Action> obligations) {
        this.permissions = permissions;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
    }

    public Set(String id, String type, JsonNode context, Collection<Action> permissions, Collection<Action> prohibitions, Collection<Action> obligations) {
        super(id, type, context);
        this.permissions = permissions;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
    }

    public Set(String type, Collection<Action> permissions, Collection<Action> prohibitions, Collection<Action> obligations) {
        super(type);
        this.permissions = permissions;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
    }

    public Set(JsonNode context, Collection<Action> permissions, Collection<Action> prohibitions, Collection<Action> obligations) {
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

    public Collection<Action> buildActions(List<PolicyCheckConfig.ActionConfig> actionConfigs){
        try {
            // Set actions to the collection
            Collection<Action> actions = new ArrayList<>(actionConfigs.size());
            actionConfigs.forEach(actionConfig -> actions.add(new Action(actionConfig))); // Parse and create actions
            return actions;
        }catch (Exception e){
            throw new ModelException(this.getClass().getName(), "It was not possible to build actions");
        }
    }



    /** GETTERS AND SETTERS **/

    public Collection<Action> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<Action> permissions) {
        this.permissions = permissions;
    }

    public Collection<Action> getProhibitions() {
        return prohibitions;
    }

    public void setProhibitions(Collection<Action> prohibitions) {
        this.prohibitions = prohibitions;
    }

    public Collection<Action> getObligations() {
        return obligations;
    }

    public void setObligations(Collection<Action> obligations) {
        this.obligations = obligations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Set set = (Set) o;
        return Objects.equals(permissions, set.permissions) && Objects.equals(prohibitions, set.prohibitions) && Objects.equals(obligations, set.obligations);
    }

    /** Compare the two sets with the hashCodes **/
    public boolean compare(Set set){
        return this.hashCode() == set.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissions, prohibitions, obligations);
    }
}
