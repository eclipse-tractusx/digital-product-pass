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

package org.eclipse.tractusx.digitalproductpass.models.negotiation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * This class consists exclusively to define attributes that are common between the needed data objects for the Application.
 * For example for Catalog, Dataset, Negotiation, etc. that extends this class.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DidDocument {
    @JsonIgnore
    public static final String ID = "@id";
    @JsonIgnore
    public static final String TYPE = "@type";
    @JsonIgnore
    public static final String CONTEXT = "@context";

    /** ATTRIBUTES **/
    @JsonProperty(ID)
    public String id;

    @JsonProperty(TYPE)
    public String type;

    @JsonProperty("@context")
    public JsonNode context;

    /** CONSTRUCTOR(S) **/
    public DidDocument(String id, String type) {
        this.id = id;
        this.type = type;
    }
    public DidDocument() {
    }

    public DidDocument(String id, String type, JsonNode context) {
        this.id = id;
        this.type = type;
        this.context = context;
    }

    public DidDocument(String type) {
        this.type = type;
    }

    public DidDocument(JsonNode context) {
        this.context = context;
    }
    public DidDocument(JsonNode context,  String type) {
        this.context = context;
        this.type = type;
    }

    /** GETTERS AND SETTERS **/
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public JsonNode getContext() {
        return context;
    }

    public void setContext(JsonNode context) {
        this.context = context;
    }
}
