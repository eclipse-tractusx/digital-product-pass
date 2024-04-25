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


package org.eclipse.tractusx.digitalproductpass.models.bpn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class consists exclusively to define attributes and methods related to the BDPM Service properties for company
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BpnCompany {

    /**
     * ATTRIBUTES
     **/
    @JsonProperty("company")
    private CompanyInfo company;
    @JsonProperty("address")
    private AddressInfo address;


    /** CONSTRUCTOR(S) **/
    public BpnCompany() {
    }

    public BpnCompany(CompanyInfo company, AddressInfo address) {
        this.company = company;
        this.address = address;
    }
    /** GETTERS AND SETTERS **/
    public AddressInfo getAddress() {
        return address;
    }

    public void setAddress(AddressInfo address) {
        this.address = address;
    }

    public CompanyInfo getCompany() {
        return company;
    }

    public void setCompany(CompanyInfo company) {
        this.company = company;
    }



}