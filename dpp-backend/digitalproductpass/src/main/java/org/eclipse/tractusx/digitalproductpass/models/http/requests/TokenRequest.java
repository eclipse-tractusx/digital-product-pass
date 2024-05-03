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
public class TokenRequest {

    /** ATTRIBUTES **/
    @NotNull(message = "Process id needs to be defined!")
    @JsonProperty("processId")
    String processId;
    @NotNull(message = "ContractId needs to be defined!")
    @JsonProperty("contractId")
    String contractId;
    @NotNull(message = "Token needs to be defined!")
    @JsonProperty("token")
    String token;
    @JsonProperty("policyId")
    String policyId;


    /** CONSTRUCTOR(S) **/
    @SuppressWarnings("Unused")
    public TokenRequest(String processId, String contractId, String token) {
        this.processId = processId;
        this.contractId = contractId;
        this.token = token;
    }

    public TokenRequest(String processId, String contractId, String token, String policyId) {
        this.processId = processId;
        this.contractId = contractId;
        this.token = token;
        this.policyId = policyId;
    }


    /** GETTERS AND SETTERS **/
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    @SuppressWarnings("Unused")
    public TokenRequest() {
    }
    public String getProcessId() {
        return processId;
    }
    public void setProcessId(String processId) {
        this.processId = processId;
    }
    public String getContractId() {
        return contractId;
    }
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }
}
