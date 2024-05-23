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

package org.eclipse.tractusx.digitalproductpass.models.dtregistry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    ArrayList<SpecificAssetId> specificAssetIds;
    @JsonProperty("submodelDescriptors")
    ArrayList<SubModel> submodelDescriptors;

    public DigitalTwin(ArrayList<JsonNode> description, String idShort, String assetKind, String assetType, String globalAssetId, Object displayName, String identification, ArrayList<SpecificAssetId> specificAssetIds, ArrayList<SubModel> submodelDescriptors) {
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

    /** INNER CLASSES **/

    /**
     * This class consists exclusively to define attributes and methods related to the digital twin specific asset ids
     **/
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class SpecificAssetId{
        /** ATTRIBUTES **/

        @JsonProperty("supplementalSemanticIds")
        List<Object> supplementalSemanticIds;

        @JsonProperty("name")
        String name;

        @JsonProperty("value")
        String value;

        @JsonProperty("externalSubjectId")
        Object externalSubjectId;
        /** CONSTRUCTOR(S) **/
        public SpecificAssetId(List<Object> supplementalSemanticIds, String name, String value, Object externalSubjectId) {
            this.supplementalSemanticIds = supplementalSemanticIds;
            this.name = name;
            this.value = value;
            this.externalSubjectId = externalSubjectId;
        }
      
        public SpecificAssetId() {
        }

        /** GETTERS AND SETTERS **/
        public List<Object> getSupplementalSemanticIds() {
            return supplementalSemanticIds;
        }

        public void setSupplementalSemanticIds(List<Object> supplementalSemanticIds) {
            this.supplementalSemanticIds = supplementalSemanticIds;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Object getExternalSubjectId() {
            return externalSubjectId;
        }

        public void setExternalSubjectId(Object externalSubjectId) {
            this.externalSubjectId = externalSubjectId;
        }
    }

    /** CONSTRUCTOR(S) **/
    @SuppressWarnings("Unused")
    public DigitalTwin() {
    }


    /** GETTERS AND SETTERS **/
    public ArrayList<SpecificAssetId> getSpecificAssetIds() {
        return specificAssetIds;
    }

    public void setSpecificAssetIds(ArrayList<SpecificAssetId> specificAssetIds) {
        this.specificAssetIds = specificAssetIds;
    }

    public Map<String, String> mapSpecificAssetIds() {
        return this.getSpecificAssetIds().stream().collect(
                Collectors.toMap(SpecificAssetId::getName, SpecificAssetId::getValue)
        );
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
    @SuppressWarnings("Unused")
    public void setIdentification(String identification) {
        this.identification = identification;
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
