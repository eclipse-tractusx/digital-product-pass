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

package org.eclipse.tractusx.productpass.models.dtregistry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DigitalTwin3 {
    @JsonProperty("description")
    ArrayList<JsonNode> description;
    @JsonProperty("idShort")
    String idShort;

    @JsonProperty("globalAssetId")
    String globalAssetId;
    @JsonProperty("displayName")
    Object displayName;

    @JsonProperty("id")
    String identification;
    @JsonProperty("specificAssetIds")
    ArrayList<JsonNode> specificAssetIds;

    @JsonProperty("submodelDescriptors")
    ArrayList<SubModel3> submodelDescriptors;

    public DigitalTwin3() {
    }

    public DigitalTwin3(ArrayList<JsonNode> description, String idShort, String identification, ArrayList<JsonNode> specificAssetIds, ArrayList<SubModel3> submodelDescriptors) {
        this.description = description;
        this.idShort = idShort;
        this.identification = identification;
        this.specificAssetIds = specificAssetIds;
        this.submodelDescriptors = submodelDescriptors;
    }

    public DigitalTwin3(ArrayList<JsonNode> description, String idShort, Object displayName, String identification, ArrayList<JsonNode> specificAssetIds, ArrayList<SubModel3> submodelDescriptors) {
        this.description = description;
        this.idShort = idShort;
        this.displayName = displayName;
        this.identification = identification;
        this.specificAssetIds = specificAssetIds;
        this.submodelDescriptors = submodelDescriptors;
    }

    public DigitalTwin3(ArrayList<JsonNode> description, String idShort, String globalAssetId, Object displayName, String identification, ArrayList<JsonNode> specificAssetIds, ArrayList<SubModel3> submodelDescriptors) {
        this.description = description;
        this.idShort = idShort;
        this.globalAssetId = globalAssetId;
        this.displayName = displayName;
        this.identification = identification;
        this.specificAssetIds = specificAssetIds;
        this.submodelDescriptors = submodelDescriptors;
    }


    public ArrayList<JsonNode> getDescription() {
        return description;
    }

    public void setDescription(ArrayList<JsonNode> description) {
        this.description = description;
    }

    public String getIdShort() {
        return idShort;
    }

    public void setIdShort(String idShort) {
        this.idShort = idShort;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public ArrayList<JsonNode> getSpecificAssetIds() {
        return specificAssetIds;
    }

    public void setSpecificAssetIds(ArrayList<JsonNode> specificAssetIds) {
        this.specificAssetIds = specificAssetIds;
    }

    public ArrayList<SubModel3> getSubmodelDescriptors() {
        return submodelDescriptors;
    }

    public void setSubmodelDescriptors(ArrayList<SubModel3> submodelDescriptors) {
        this.submodelDescriptors = submodelDescriptors;
    }

    public Object getDisplayName() {
        return displayName;
    }

    public void setDisplayName(Object displayName) {
        this.displayName = displayName;
    }

    public String getGlobalAssetId() {
        return globalAssetId;
    }

    public void setGlobalAssetId(String globalAssetId) {
        this.globalAssetId = globalAssetId;
    }
}

