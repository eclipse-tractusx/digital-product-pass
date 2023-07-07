/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

package org.eclipse.tractusx.productpass.models.negotiation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Negotiation extends DidDocument {
    @JsonProperty("edc:type")
    String edcType;

    @JsonProperty("edc:protocol")
    String protocol;

    @JsonProperty("edc:state")
    String state;
    @JsonProperty("edc:errorDetail")
    String errorDetail;

    @JsonProperty("edc:counterPartyAddress")
    String counterPartyAddress;

    @JsonProperty("edc:callbackAddresses")
    List<String> callbackAddresses;

    @JsonProperty("edc:contractAgreementId")
    String contractAgreementId;

    @JsonProperty("@context")
    JsonNode context;

    public Negotiation(String id, String type, String edcType, String protocol, String state, String counterPartyAddress, List<String> callbackAddresses, String contractAgreementId, JsonNode context) {
        super(id, type);
        this.edcType = edcType;
        this.protocol = protocol;
        this.state = state;
        this.counterPartyAddress = counterPartyAddress;
        this.callbackAddresses = callbackAddresses;
        this.contractAgreementId = contractAgreementId;
        this.context = context;
    }

    public Negotiation(String edcType, String protocol, String state, String counterPartyAddress, List<String> callbackAddresses, String contractAgreementId, JsonNode context) {
        this.edcType = edcType;
        this.protocol = protocol;
        this.state = state;
        this.counterPartyAddress = counterPartyAddress;
        this.callbackAddresses = callbackAddresses;
        this.contractAgreementId = contractAgreementId;
        this.context = context;
    }



    public Negotiation() {
    }

    public Negotiation(String id, String type) {
        super(id, type);
    }

    public Negotiation(String id, String type, String edcType, String protocol, String state, String errorDetail, String counterPartyAddress, List<String> callbackAddresses, String contractAgreementId, JsonNode context) {
        super(id, type);
        this.edcType = edcType;
        this.protocol = protocol;
        this.state = state;
        this.errorDetail = errorDetail;
        this.counterPartyAddress = counterPartyAddress;
        this.callbackAddresses = callbackAddresses;
        this.contractAgreementId = contractAgreementId;
        this.context = context;
    }

    public String getEdcType() {
        return edcType;
    }

    public void setEdcType(String edcType) {
        this.edcType = edcType;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounterPartyAddress() {
        return counterPartyAddress;
    }

    public void setCounterPartyAddress(String counterPartyAddress) {
        this.counterPartyAddress = counterPartyAddress;
    }

    public List<String> getCallbackAddresses() {
        return callbackAddresses;
    }

    public void setCallbackAddresses(List<String> callbackAddresses) {
        this.callbackAddresses = callbackAddresses;
    }

    public String getContractAgreementId() {
        return contractAgreementId;
    }

    public void setContractAgreementId(String contractAgreementId) {
        this.contractAgreementId = contractAgreementId;
    }

    public JsonNode getContext() {
        return context;
    }

    public void setContext(JsonNode context) {
        this.context = context;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }
}
