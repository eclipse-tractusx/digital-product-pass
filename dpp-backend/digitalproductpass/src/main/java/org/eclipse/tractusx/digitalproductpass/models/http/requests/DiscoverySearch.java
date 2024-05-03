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
 * This class consists exclusively to define attributes related to the "/create" endpoint request and therefor to the Discovery search request.
 *  It's the mandatory body parameter for the HTTP request.
 *
 **/
public class DiscoverySearch {

    /** ATTRIBUTES **/
    @NotNull(message = "Id needs to be defined!")
    @JsonProperty("id")
    String id;
    @JsonProperty("type")
    String type;

    /** CONSTRUCTOR(S) **/
    @SuppressWarnings("Unused")
    public DiscoverySearch(String id, String type) {
        this.id = id;
        this.type = type;
    }
    @SuppressWarnings("Unused")
    public DiscoverySearch(String id) {
        this.id = id;
    }
    @SuppressWarnings("Unused")
    public DiscoverySearch() {
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
}
