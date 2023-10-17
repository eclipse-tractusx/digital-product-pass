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

/**
 * This class consists exclusively to define attributes related to the designed model of the Digital Twin's improved version.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DigitalTwin {

    /** ATTRIBUTES **/
    @JsonProperty("description")
    ArrayList<JsonNode> description;
    @JsonProperty("idShort")
    String idShort;
    @JsonProperty("assetKind")
    String assetKind;
    @JsonProperty("assetType")
    String assetType;
    @JsonProperty("globalAssetId")
    String globalAssetId;
    @JsonProperty("displayName")
    Object displayName;
    @JsonProperty("id")
    String identification;
    @JsonProperty("specificAssetIds")
    ArrayList<JsonNode> specificAssetIds;
    @JsonProperty("submodelDescriptors")
    ArrayList<SubModel> submodelDescriptors;

    /** CONSTRUCTOR(S) **/
    @SuppressWarnings("Unused")
    public DigitalTwin() {
    }
    @SuppressWarnings("Unused")
    public DigitalTwin(ArrayList<JsonNode> description, String idShort, String identification, ArrayList<JsonNode> specificAssetIds, ArrayList<SubModel> submodelDescriptors) {
        this.description = description;
        this.idShort = idShort;
        this.identification = identification;
        this.specificAssetIds = specificAssetIds;
        this.submodelDescriptors = submodelDescriptors;
    }
    @SuppressWarnings("Unused")
    public DigitalTwin(ArrayList<JsonNode> description, String idShort, Object displayName, String identification, ArrayList<JsonNode> specificAssetIds, ArrayList<SubModel> submodelDescriptors) {
        this.description = description;
        this.idShort = idShort;
        this.displayName = displayName;
        this.identification = identification;
        this.specificAssetIds = specificAssetIds;
        this.submodelDescriptors = submodelDescriptors;
    }
    @SuppressWarnings("Unused")
    public DigitalTwin(ArrayList<JsonNode> description, String idShort, String globalAssetId, Object displayName, String identification, ArrayList<JsonNode> specificAssetIds, ArrayList<SubModel> submodelDescriptors) {
        this.description = description;
        this.idShort = idShort;
        this.globalAssetId = globalAssetId;
        this.displayName = displayName;
        this.identification = identification;
        this.specificAssetIds = specificAssetIds;
        this.submodelDescriptors = submodelDescriptors;
    }
    @SuppressWarnings("Unused")
    public DigitalTwin(ArrayList<JsonNode> description, String idShort, String assetKind, String assetType, String globalAssetId, Object displayName, String identification, ArrayList<JsonNode> specificAssetIds, ArrayList<SubModel> submodelDescriptors) {
        this.description = description;
        this.idShort = idShort;
        this.assetKind = assetKind;
        this.assetType = assetType;
        this.globalAssetId = globalAssetId;
        this.displayName = displayName;
        this.identification = identification;
        this.specificAssetIds = specificAssetIds;
        this.submodelDescriptors = submodelDescriptors;
    }

    /** GETTERS AND SETTERS **/
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
    @SuppressWarnings("Unused")
    public void setIdentification(String identification) {
        this.identification = identification;
    }
    @SuppressWarnings("Unused")
    public ArrayList<JsonNode> getSpecificAssetIds() {
        return specificAssetIds;
    }
    @SuppressWarnings("Unused")
    public void setSpecificAssetIds(ArrayList<JsonNode> specificAssetIds) {
        this.specificAssetIds = specificAssetIds;
    }
    public ArrayList<SubModel> getSubmodelDescriptors() {
        return submodelDescriptors;
    }
    @SuppressWarnings("Unused")
    public void setSubmodelDescriptors(ArrayList<SubModel> submodelDescriptors) {
        this.submodelDescriptors = submodelDescriptors;
    }
    @SuppressWarnings("Unused")
    public Object getDisplayName() {
        return displayName;
    }
    @SuppressWarnings("Unused")
    public void setDisplayName(Object displayName) {
        this.displayName = displayName;
    }
    @SuppressWarnings("Unused")
    public String getGlobalAssetId() {
        return globalAssetId;
    }
    @SuppressWarnings("Unused")
    public void setGlobalAssetId(String globalAssetId) {
        this.globalAssetId = globalAssetId;
    }
    @SuppressWarnings("Unused")
    public String getAssetKind() {
        return assetKind;
    }
    @SuppressWarnings("Unused")
    public void setAssetKind(String assetKind) {
        this.assetKind = assetKind;
    }
    @SuppressWarnings("Unused")
    public String getAssetType() {
        return assetType;
    }
    @SuppressWarnings("Unused")
    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }
}
