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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;

public class SubModel {
    @JsonProperty("description")
    ArrayList<JsonNode> description;
    @JsonProperty("idShort")
    String idShort;
    @JsonProperty("identification")
    String identification;
    @JsonProperty("semanticId")
    JsonNode semanticId;

    @JsonProperty("endpoints")
    ArrayList<EndPoint> endpoints;

    public SubModel(ArrayList<JsonNode> description, String idShort, String identification, JsonNode semanticId, ArrayList<EndPoint> endpoints) {
        this.description = description;
        this.idShort = idShort;
        this.identification = identification;
        this.semanticId = semanticId;
        this.endpoints = endpoints;
    }

    public SubModel() {
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

    public JsonNode getSemanticId() {
        return semanticId;
    }

    public void setSemanticId(JsonNode semanticId) {
        this.semanticId = semanticId;
    }

    public ArrayList<EndPoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(ArrayList<EndPoint> endpoints) {
        this.endpoints = endpoints;
    }
}
