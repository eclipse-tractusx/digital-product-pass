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
import org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog.Dataset;

import java.util.Map;

/**
 * This class consists exclusively to define attributes and methods related to the Digital Twin Registry (DTR).
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dtr {

    /** ATTRIBUTES **/

    @JsonProperty("contracts")
    private Map<String, Dataset> contracts;
    @JsonProperty("contractId")
    private String contractId;
    @JsonProperty("policyId")
    private String policyId;
    @JsonProperty("id")
    private String id;
    @JsonProperty("endpoint")
    private String endpoint;
    @JsonProperty("assetId")
    private String assetId;
    @JsonProperty("bpn")
    private String bpn;
    @JsonProperty("providerBpn")
    private String providerBpn;
    @JsonProperty("validUntil")
    private Long validUntil;
    @JsonProperty("invalid")
    private Boolean invalid;
    /** CONSTRUCTOR(S) **/

    public Dtr() {
    }

    public Dtr(String contractId, String endpoint, String assetId, String bpn, Long validUntil) {
        this.contractId = contractId;
        this.endpoint = endpoint;
        this.assetId = assetId;
        this.bpn = bpn;
        this.validUntil = validUntil;
        this.invalid = false;
    }

    public Dtr(String contractId, String endpoint, String assetId, String bpn, Long validUntil, Boolean invalid) {
        this.contractId = contractId;
        this.endpoint = endpoint;
        this.assetId = assetId;
        this.bpn = bpn;
        this.validUntil = validUntil;
        this.invalid = invalid;
    }
    public Dtr(Map<String, Dataset> contracts, String id, String contractId, String endpoint, String assetId, String bpn, Long validUntil) {
        this.contracts = contracts;
        this.contractId = contractId;
        this.id = id;
        this.endpoint = endpoint;
        this.assetId = assetId;
        this.bpn = bpn;
        this.validUntil = validUntil;
        this.invalid = false;
    }
    public Dtr(Map<String, Dataset> contracts, String contractId, String endpoint, String assetId, String bpn, Long validUntil, Boolean invalid) {
        this.contracts = contracts;
        this.contractId = contractId;
        this.endpoint = endpoint;
        this.assetId = assetId;
        this.bpn = bpn;
        this.validUntil = validUntil;
        this.invalid = invalid;
    }

    public Dtr(Map<String, Dataset> contracts, String id, String contractId, String endpoint, String assetId, String bpn, Long validUntil, Boolean invalid) {
        this.contracts = contracts;
        this.contractId = contractId;
        this.id = id;
        this.endpoint = endpoint;
        this.assetId = assetId;
        this.bpn = bpn;
        this.validUntil = validUntil;
        this.invalid = invalid;
    }

    public Dtr(Map<String, Dataset> contracts, String id, String policyId, String contractId, String endpoint, String assetId, String bpn, String providerBpn, Long validUntil, Boolean invalid) {
        this.contracts = contracts;
        this.contractId = contractId;
        this.policyId = policyId;
        this.id = id;
        this.providerBpn = providerBpn;
        this.endpoint = endpoint;
        this.assetId = assetId;
        this.bpn = bpn;
        this.validUntil = validUntil;
        this.invalid = invalid;
    }
    public Dtr(Map<String, Dataset> contracts,String id, String policyId, String contractId,  String endpoint, String assetId, String bpn, String providerBpn, Long validUntil) {
        this.contracts = contracts;
        this.contractId = contractId;
        this.policyId = policyId;
        this.id = id;
        this.endpoint = endpoint;
        this.assetId = assetId;
        this.bpn = bpn;
        this.providerBpn = providerBpn;
        this.validUntil = validUntil;
        this.invalid = false;
    }
    /** GETTERS AND SETTERS **/
    public String getContractId() {
        return contractId;
    }
    public void setContractId(String contractId) { this.contractId = contractId; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public String getAssetId() { return assetId; }
    public void setAssetId(String assetId) { this.assetId = assetId; }
    public String getBpn() { return bpn; }
    public void setBpn(String bpn) { this.bpn = bpn; }

    public Long getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Long validUntil) {
        this.validUntil = validUntil;
    }

    public Boolean getInvalid() {
        return invalid;
    }

    public void setInvalid(Boolean invalid) {
        this.invalid = invalid;
    }

    public Map<String, Dataset> getContracts() {
        return contracts;
    }

    public void setContracts(Map<String, Dataset> contracts) {
        this.contracts = contracts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getProviderBpn() {
        return providerBpn;
    }

    public void setProviderBpn(String providerBpn) {
        this.providerBpn = providerBpn;
    }
}
