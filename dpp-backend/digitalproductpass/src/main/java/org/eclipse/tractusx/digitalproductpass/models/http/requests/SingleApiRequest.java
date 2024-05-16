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
 * This class consists exclusively to define attributes related to the many endpoint request like "/sign", "/passport", etc.
 * It's the mandatory body parameter for the HTTP requests.
 **/
public class SingleApiRequest {

    /** ATTRIBUTES **/
    @NotNull(message = "serialized id needs to be defined!")
    @JsonProperty(value="id")
    String id;

    @JsonProperty(value="idType", defaultValue = "partInstanceId")
    String idType;

    @NotNull(message = "Dtr search id needs to be defined!")
    @JsonProperty("discoveryId")
    String discoveryId;

    @JsonProperty(value="discoveryIdType", defaultValue = "manufacturerPartId")
    String discoveryIdType;

    @JsonProperty(value="children", defaultValue = "false")
    Boolean children;

    @JsonProperty("semanticId")
    String semanticId;

    /** CONSTRUCTOR(S) **/
    public SingleApiRequest(String id, String idType, String discoveryId, String discoveryIdType, Boolean children, String semanticId) {
        this.id = id;
        this.idType = idType;
        this.discoveryId = discoveryId;
        this.discoveryIdType = discoveryIdType;
        this.children = children;
        this.semanticId = semanticId;
    }

    public SingleApiRequest() {
    }

    public SingleApiRequest(String id, String discoveryId) {
        this.id = id;
        this.discoveryId = discoveryId;
    }


    /** GETTERS AND SETTERS **/
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

    public String getDiscoveryId() {
        return discoveryId;
    }

    public String getDiscoveryIdType() {
        return discoveryIdType;
    }


    public Boolean getChildren() {
        return children;
    }

    public void setChildren(Boolean children) {
        this.children = children;
    }

    public String getSemanticId() {
        return semanticId;
    }

    public void setSemanticId(String semanticId) {
        this.semanticId = semanticId;
    }

    public void setDiscoveryId(String discoveryId) {
        this.discoveryId = discoveryId;
    }

    public void setDiscoveryIdType(String discoveryIdType) {
        this.discoveryIdType = discoveryIdType;
    }
}
