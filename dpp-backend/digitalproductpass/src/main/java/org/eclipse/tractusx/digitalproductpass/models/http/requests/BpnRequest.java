/*********************************************************************************
 *
 * Tractus-X - Digital Product Pass Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2024 CGI Deutschland B.V. & Co. KG
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

import java.util.List;

/**
 * This class is responsible for parsing the request from the Bpn request
 *
 **/
public class BpnRequest {


    /** ATTRIBUTES **/
    @JsonProperty("legalEntity")
    List<String> legalEntities;

    @JsonProperty("site")
    List<String> sites;

    @JsonProperty("address")
    List<String> addresses;

    /** CONSTRUCTOR(S) **/

    public BpnRequest(List<String> legalEntities, List<String> sites, List<String> addresses) {
        this.legalEntities = legalEntities;
        this.sites = sites;
        this.addresses = addresses;
    }

    public BpnRequest() {
    }

    /** GETTERS AND SETTERS **/
    public List<String> getLegalEntities() {
        return legalEntities;
    }

    public void setLegalEntities(List<String> legalEntities) {
        this.legalEntities = legalEntities;
    }

    public List<String> getSites() {
        return sites;
    }

    public void setSites(List<String> sites) {
        this.sites = sites;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

}
