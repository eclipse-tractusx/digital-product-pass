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

package org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DidDocument;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.policy.Action;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.policy.Set;

import java.util.List;

/**
 * Policy representing the offer from the catalog
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Policy extends Set {

    /**
     * ATTRIBUTES
     **/
    @JsonProperty("odrl:target")
    DidDocument target;
    @JsonProperty("odrl:assigner")
    DidDocument assigner;

    /** CONSTRUCTOR(S) **/

    public Policy(String id, String type, List<Action> permissions, List<Action> prohibitions, List<Action> obligations, DidDocument target, DidDocument assigner) {
        super(id, type, permissions, prohibitions, obligations);
        this.target = target;
        this.assigner = assigner;
    }

    public Policy(List<Action> permissions, List<Action> prohibitions, List<Action> obligations, DidDocument target, DidDocument assigner) {
        super(permissions, prohibitions, obligations);
        this.target = target;
        this.assigner = assigner;
    }

    public Policy(String id, String type, JsonNode context, List<Action> permissions, List<Action> prohibitions, List<Action> obligations, DidDocument target, DidDocument assigner) {
        super(id, type, context, permissions, prohibitions, obligations);
        this.target = target;
        this.assigner = assigner;
    }

    public Policy(String type, List<Action> permissions, List<Action> prohibitions, List<Action> obligations, DidDocument target, DidDocument assigner) {
        super(type, permissions, prohibitions, obligations);
        this.target = target;
        this.assigner = assigner;
    }

    public Policy(JsonNode context, List<Action> permissions, List<Action> prohibitions, List<Action> obligations, DidDocument target, DidDocument assigner) {
        super(context, permissions, prohibitions, obligations);
        this.target = target;
        this.assigner = assigner;
    }

    public Policy(String id, String type, DidDocument target, DidDocument assigner) {
        super(id, type);
        this.target = target;
        this.assigner = assigner;
    }

    public Policy(DidDocument target, DidDocument assigner) {
        this.target = target;
        this.assigner = assigner;
    }

    public Policy(PolicyCheckConfig.PolicyConfig policyConfig, DidDocument target, DidDocument assigner) {
        super(policyConfig);
        this.target = target;
        this.assigner = assigner;
    }

    public Policy(String id, String type, List<Action> permissions, List<Action> prohibitions, List<Action> obligations) {
        super(id, type, permissions, prohibitions, obligations);
    }

    public Policy(List<Action> permissions, List<Action> prohibitions, List<Action> obligations) {
        super(permissions, prohibitions, obligations);
    }

    public Policy(String id, String type, JsonNode context, List<Action> permissions, List<Action> prohibitions, List<Action> obligations) {
        super(id, type, context, permissions, prohibitions, obligations);
    }

    public Policy(String type, List<Action> permissions, List<Action> prohibitions, List<Action> obligations) {
        super(type, permissions, prohibitions, obligations);
    }

    public Policy(JsonNode context, List<Action> permissions, List<Action> prohibitions, List<Action> obligations) {
        super(context, permissions, prohibitions, obligations);
    }

    public Policy(String id, String type) {
        super(id, type);
    }

    public Policy(String id, String targetId, String assignerId) {
        super(id, "odrl:Offer");
        this.target = new DidDocument(); // Set Target
        this.target.setId(targetId);
        this.assigner = new DidDocument(); // Set BPN from assigner
        this.assigner.setId(assignerId);
    }


    public Policy() {
    }

    public Policy(PolicyCheckConfig.PolicyConfig policyConfig) {
        super(policyConfig);
    }

    public DidDocument getTarget() {
        return target;
    }

    public void setTarget(DidDocument target) {
        this.target = target;
    }

    public DidDocument getAssigner() {
        return assigner;
    }

    public void setAssigner(DidDocument assigner) {
        this.assigner = assigner;
    }

    public Policy setup(String targetId, String assignerId, String type) {
        this.setType(type);
        this.target = new DidDocument(); // Set Target
        this.target.setId(targetId);
        this.assigner = new DidDocument(); // Set BPN from assigner
        this.assigner.setId(assignerId);
        return this;
    }
}
