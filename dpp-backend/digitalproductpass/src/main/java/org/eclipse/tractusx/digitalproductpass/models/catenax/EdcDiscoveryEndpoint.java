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

package org.eclipse.tractusx.digitalproductpass.models.catenax;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * This class consists exclusively to define attributes and methods related to discovering the EDC endpoints for each BPN number.
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EdcDiscoveryEndpoint {

    /** ATTRIBUTES **/
    @JsonProperty("bpn")
    String bpn;
    @JsonProperty("connectorEndpoint")
    List<String> connectorEndpoint;

    /** CONSTRUCTOR(S) **/
    public EdcDiscoveryEndpoint(String bpn, List<String> connectorEndpoint) {
        this.bpn = bpn;
        this.connectorEndpoint = connectorEndpoint;
    }
    public EdcDiscoveryEndpoint() {
    }

    /** GETTERS AND SETTERS **/
    public String getBpn() {
        return bpn;
    }
    public void setBpn(String bpn) {
        this.bpn = bpn;
    }
    public List<String> getConnectorEndpoint() {
        return connectorEndpoint;
    }
    public void setConnectorEndpoint(List<String> connectorEndpoint) {
        this.connectorEndpoint = connectorEndpoint;
    }
}
