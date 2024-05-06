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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * This class contains the structure of an EDC callback address.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CallbackAddress {
    /**
     * ATTRIBUTES
     **/
    @JsonProperty("transactional")
    Boolean transactional;
    @JsonProperty("uri")
    String uri;
    @JsonProperty("events")
    List<String> events;
    @JsonProperty("authKey")
    String authKey;
    @JsonProperty("authCodeId")
    String authCodeId;
    /**
     * CONSTRUCTORS
     **/

    public CallbackAddress(Boolean transactional, String uri, String authKey, String authCodeId) {
        this.transactional = transactional;
        this.uri = uri;
        this.authKey = authKey;
        this.authCodeId = authCodeId;
    }

    public CallbackAddress(Boolean transactional, String uri, List<String> events) {
        this.transactional = transactional;
        this.uri = uri;
        this.events = events;
    }

    /**
     * GETTERS AND SETTERS
     **/

    public Boolean getTransactional() {
        return transactional;
    }

    public void setTransactional(Boolean transactional) {
        this.transactional = transactional;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getAuthCodeId() {
        return authCodeId;
    }

    public void setAuthCodeId(String authCodeId) {
        this.authCodeId = authCodeId;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }
}

