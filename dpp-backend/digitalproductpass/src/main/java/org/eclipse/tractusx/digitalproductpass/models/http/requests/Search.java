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

package org.eclipse.tractusx.digitalproductpass.models.http.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

/**
 * This class consists exclusively to define attributes related to the "/search" endpoint request.
 * It's the mandatory body parameter for the HTTP request.
 **/
public class Search {

    /** ATTRIBUTES **/
    @NotNull(message = "Process Id")
    @JsonProperty("processId")
    String processId;
    @NotNull(message = "Id needs to be defined!")
    @JsonProperty("id")
    String id;
    @JsonProperty(value = "idType", defaultValue = "partInstanceId")
    String idType = "partInstanceId";
    @JsonProperty(value = "dtIndex", defaultValue = "0")
    Integer dtIndex = 0;

    @JsonProperty(value = "children", defaultValue = "true")
    Boolean children = true;

    @JsonProperty(value = "idShort", defaultValue = "batteryPass")
    String idShort = "batteryPass";
    @JsonProperty(value = "semanticId")
    String semanticId;

    /** CONSTRUCTOR(S) **/
    @SuppressWarnings("Unused")
    public Search() {
    }
    @SuppressWarnings("Unused")
    public Search(String processId, String id, String idType, Integer dtIndex, String semanticId) {
        this.processId = processId;
        this.id = id;
        this.idType = idType;
        this.dtIndex = dtIndex;
        this.semanticId = semanticId;
    }

    public Search(String processId, String id, String idType, Integer dtIndex, Boolean children, String idShort, String semanticId) {
        this.processId = processId;
        this.id = id;
        this.idType = idType;
        this.dtIndex = dtIndex;
        this.children = children;
        this.idShort = idShort;
        this.semanticId = semanticId;
    }
    /** GETTERS AND SETTERS **/
    public String getSemanticId() {
        return semanticId;
    }
    public void setSemanticId(String semanticId) {
        this.semanticId = semanticId;
    }
    public String getProcessId() {
        return processId;
    }
    public void setProcessId(String processId) {
        this.processId = processId;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIdType() {
        return idType;
    }
    public void setIdType(String idType) {
        this.idType = idType;
    }
    public Integer getDtIndex() {
        return dtIndex;
    }
    public void setDtIndex(Integer dtIndex) {
        this.dtIndex = dtIndex;
    }

    public Boolean getChildren() {
        return children;
    }

    public void setChildren(Boolean children) {
        this.children = children;
    }
}
