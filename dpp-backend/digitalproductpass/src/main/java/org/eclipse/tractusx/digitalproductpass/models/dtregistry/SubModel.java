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

/**
 * This class consists exclusively to define attributes related to the designed model of the Submodel's improved version.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubModel {

    /** ATTRIBUTES **/
    @JsonProperty("description")
    ArrayList<JsonNode> description;
    @JsonProperty("idShort")
    String idShort;

    @JsonProperty("supplementalSemanticId")
    Object supplementalSemanticId;

    @JsonProperty("id")
    String identification;
    @JsonProperty("semanticId")
    SemanticId semanticId;

    @JsonProperty("endpoints")
    ArrayList<EndPoint> endpoints;

    /** CONSTRUCTOR(S) **/
    @SuppressWarnings("Unused")
    public SubModel(ArrayList<JsonNode> description, String idShort, String identification, SemanticId semanticId, ArrayList<EndPoint> endpoints) {
        this.description = description;
        this.idShort = idShort;
        this.identification = identification;
        this.semanticId = semanticId;
        this.endpoints = endpoints;
    }
    @SuppressWarnings("Unused")
    public SubModel() {
    }
    @SuppressWarnings("Unused")
    public SubModel(ArrayList<JsonNode> description, String idShort, Object supplementalSemanticId, String identification, SemanticId semanticId, ArrayList<EndPoint> endpoints) {
        this.description = description;
        this.idShort = idShort;
        this.supplementalSemanticId = supplementalSemanticId;
        this.identification = identification;
        this.semanticId = semanticId;
        this.endpoints = endpoints;
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

    public SemanticId getSemanticId() {
        return semanticId;
    }

    public void setSemanticId(SemanticId semanticId) {
        this.semanticId = semanticId;
    }

    public ArrayList<EndPoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(ArrayList<EndPoint> endpoints) {
        this.endpoints = endpoints;
    }
    @SuppressWarnings("Unused")
    public Object getSupplementalSemanticId() {
        return supplementalSemanticId;
    }
    @SuppressWarnings("Unused")
    public void setSupplementalSemanticId(Object supplementalSemanticId) {
        this.supplementalSemanticId = supplementalSemanticId;
    }

    /** INNER CLASSES **/
    /**
     * This class consists exclusively to define attributes related to the Submodel's SemanticId.
     **/
    public static class SemanticId {

        /** ATTRIBUTES **/
        @JsonProperty("type")
        String type;
        @JsonProperty("keys")
        ArrayList<Key> keys;

        /** CONSTRUCTOR(S) **/
        @SuppressWarnings("Unused")
        public SemanticId(String type, ArrayList<Key> keys) {
            this.type = type;
            this.keys = keys;
        }
        @SuppressWarnings("Unused")
        public SemanticId() {
        }

        /** GETTERS AND SETTERS **/
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public ArrayList<Key> getKeys() {
            return keys;
        }
        @SuppressWarnings("Unused")
        public void setKeys(ArrayList<Key> keys) {
            this.keys = keys;
        }

        /** INNER CLASSES **/
        /**
         * This class consists exclusively to define attributes related to the SemanticId's keys property.
         **/
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        static public class Key {

            /** ATTRIBUTES **/
            @JsonProperty("type")
            String type;
            @JsonProperty("value")
            String value;

            /** CONSTRUCTOR(S) **/
            public Key() {}

            public Key(String type, String value) {
                this.type = type;
                this.value = value;
            }

            /** GETTERS AND SETTERS **/
            public String getType() { return type; }
            public void setType(String type) { this.type = type; }
            public String getValue() { return value; }
            public void setValue(String value) { this.value = value; }
        }
    }
}
