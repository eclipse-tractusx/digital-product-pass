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

package org.eclipse.tractusx.digitalproductpass.models.http.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DidDocument;

/**
 * This class consists exclusively to define attributes related to the response of the requests made by the Application.
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdResponse extends DidDocument {

    /** ATTRIBUTES **/
    @JsonProperty("edc:createdAt")
    public Long createdAt;
    @JsonProperty("@context")
    JsonNode context;

    /** CONSTRUCTOR(S) **/
    @SuppressWarnings("Unused")
    public IdResponse(String id, String type, Long createdAt, JsonNode context) {
        super(id, type);
        this.createdAt = createdAt;
        this.context = context;
    }
    @SuppressWarnings("Unused")
    public IdResponse(Long createdAt, JsonNode context) {
        this.createdAt = createdAt;
        this.context = context;
    }
    @SuppressWarnings("Unused")
    public IdResponse() {
    }

    /** GETTERS AND SETTERS **/
    public IdResponse(String id, String type) {
        super(id, type);
    }
    public Long getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
    public JsonNode getContext() {
        return context;
    }
    public void setContext(JsonNode context) {
        this.context = context;
    }
}
