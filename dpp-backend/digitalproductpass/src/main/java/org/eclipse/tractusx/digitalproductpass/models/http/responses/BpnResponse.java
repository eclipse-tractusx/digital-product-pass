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

package org.eclipse.tractusx.digitalproductpass.models.http.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.tractusx.digitalproductpass.models.bpn.AddressInfo;
import org.eclipse.tractusx.digitalproductpass.models.bpn.BpnCompany;
import org.eclipse.tractusx.digitalproductpass.models.bpn.CompanyInfo;

import java.util.List;
import java.util.Map;

/**
 * This class is responsible for parsing the request from the Bpn response
 **/
public class BpnResponse {


    /** ATTRIBUTES **/
    @JsonProperty("legalEntity")
    Map<String, BpnCompany> legalEntities;

    @JsonProperty("site")
    Map<String, AddressInfo> sites;

    @JsonProperty("address")
    Map<String, AddressInfo> addresses;

    /** CONSTRUCTOR(S) **/
    public BpnResponse(Map<String, BpnCompany> legalEntities, Map<String, AddressInfo> sites, Map<String, AddressInfo> addresses) {
        this.legalEntities = legalEntities;
        this.sites = sites;
        this.addresses = addresses;
    }
    public BpnResponse() {
    }


    /** GETTERS AND SETTERS **/
    public Map<String, BpnCompany> getLegalEntities() {
        return legalEntities;
    }

    public void setLegalEntities(Map<String, BpnCompany> legalEntities) {
        this.legalEntities = legalEntities;
    }

    public Map<String, AddressInfo> getSites() {
        return sites;
    }

    public void setSites(Map<String, AddressInfo> sites) {
        this.sites = sites;
    }

    public Map<String, AddressInfo> getAddresses() {
        return addresses;
    }

    public void setAddresses(Map<String, AddressInfo> addresses) {
        this.addresses = addresses;
    }

    public Boolean isEmpty(){
        return (this.legalEntities == null || this.legalEntities.isEmpty()) && (this.addresses == null || addresses.isEmpty()) && (this.sites == null || this.sites.isEmpty());
    }

}
