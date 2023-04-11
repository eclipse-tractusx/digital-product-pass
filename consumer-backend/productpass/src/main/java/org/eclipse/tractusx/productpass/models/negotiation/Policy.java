/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

package org.eclipse.tractusx.productpass.models.negotiation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Policy {
    @JsonProperty("permissions")
    List<JsonNode> permissions;
    @JsonProperty("prohibitions")
    List<JsonNode> prohibitions;
    @JsonProperty("obligations")
    List<JsonNode> obligations;
    @JsonProperty("extensibleProperties")
    JsonNode extensibleProperties;
    @JsonProperty("inheritsFrom")
    String inheritsFrom;
    @JsonProperty("assigner")
    String assigner;
    @JsonProperty("assignee")
    String assignee;
    @JsonProperty("target")
    String target;

    @JsonProperty("@type")
    JsonNode type;

    public List<JsonNode> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<JsonNode> permissions) {
        this.permissions = permissions;
    }

    public List<JsonNode> getProhibitions() {
        return prohibitions;
    }

    public void setProhibitions(List<JsonNode> prohibitions) {
        this.prohibitions = prohibitions;
    }

    public List<JsonNode> getObligations() {
        return obligations;
    }

    public void setObligations(List<JsonNode> obligations) {
        this.obligations = obligations;
    }

    public JsonNode getExtensibleProperties() {
        return extensibleProperties;
    }

    public void setExtensibleProperties(JsonNode extensibleProperties) {
        this.extensibleProperties = extensibleProperties;
    }

    public String getInheritsFrom() {
        return inheritsFrom;
    }

    public void setInheritsFrom(String inheritsFrom) {
        this.inheritsFrom = inheritsFrom;
    }

    public String getAssigner() {
        return assigner;
    }

    public void setAssigner(String assigner) {
        this.assigner = assigner;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public JsonNode getType() {
        return type;
    }

    public void setType(JsonNode type) {
        this.type = type;
    }
}
