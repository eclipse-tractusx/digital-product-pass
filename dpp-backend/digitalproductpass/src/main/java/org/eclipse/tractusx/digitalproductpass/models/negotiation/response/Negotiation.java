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

package org.eclipse.tractusx.digitalproductpass.models.negotiation.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.response.NegotiationTransferResponse;

import java.util.List;

/**
 * This class consists exclusively to define attributes related to the Negotiation's data.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Negotiation extends NegotiationTransferResponse {

    /** ATTRIBUTES **/
    @JsonProperty("protocol")
    @JsonAlias({"protocol","edc:protocol"})
    String protocol;
    @JsonProperty("counterPartyId")
    @JsonAlias({"counterPartyId","edc:counterPartyId"})
    String counterPartyId;
    @JsonProperty("counterPartyAddress")
    @JsonAlias({"counterPartyAddress","edc:counterPartyAddress"})
    String counterPartyAddress;
    @JsonProperty("contractAgreementId")
    @JsonAlias({"contractAgreementId","edc:contractAgreementId"})
    String contractAgreementId;

    /** CONSTRUCTOR(S) **/

    public Negotiation() {
    }

    public Negotiation(String protocol, String counterPartyId, String counterPartyAddress, String contractAgreementId) {
        this.protocol = protocol;
        this.counterPartyId = counterPartyId;
        this.counterPartyAddress = counterPartyAddress;
        this.contractAgreementId = contractAgreementId;
    }

    public Negotiation(String id, String type, String state, String edcType, Long createdAt, Object callbackAddresses, String errorDetail, String protocol, String counterPartyId, String counterPartyAddress, String contractAgreementId) {
        super(id, type, state, edcType, createdAt, callbackAddresses, errorDetail);
        this.protocol = protocol;
        this.counterPartyId = counterPartyId;
        this.counterPartyAddress = counterPartyAddress;
        this.contractAgreementId = contractAgreementId;
    }

    public Negotiation(String state, String edcType, Long createdAt, Object callbackAddresses, String errorDetail, String protocol, String counterPartyId, String counterPartyAddress, String contractAgreementId) {
        super(state, edcType, createdAt, callbackAddresses, errorDetail);
        this.protocol = protocol;
        this.counterPartyId = counterPartyId;
        this.counterPartyAddress = counterPartyAddress;
        this.contractAgreementId = contractAgreementId;
    }

    public Negotiation(String id, String type, JsonNode context, String state, String edcType, Long createdAt, Object callbackAddresses, String errorDetail, String protocol, String counterPartyId, String counterPartyAddress, String contractAgreementId) {
        super(id, type, context, state, edcType, createdAt, callbackAddresses, errorDetail);
        this.protocol = protocol;
        this.counterPartyId = counterPartyId;
        this.counterPartyAddress = counterPartyAddress;
        this.contractAgreementId = contractAgreementId;
    }

    public Negotiation(String type, String state, String edcType, Long createdAt, Object callbackAddresses, String errorDetail, String protocol, String counterPartyId, String counterPartyAddress, String contractAgreementId) {
        super(type, state, edcType, createdAt, callbackAddresses, errorDetail);
        this.protocol = protocol;
        this.counterPartyId = counterPartyId;
        this.counterPartyAddress = counterPartyAddress;
        this.contractAgreementId = contractAgreementId;
    }

    public Negotiation(JsonNode context, String state, String edcType, Long createdAt, Object callbackAddresses, String errorDetail, String protocol, String counterPartyId, String counterPartyAddress, String contractAgreementId) {
        super(context, state, edcType, createdAt, callbackAddresses, errorDetail);
        this.protocol = protocol;
        this.counterPartyId = counterPartyId;
        this.counterPartyAddress = counterPartyAddress;
        this.contractAgreementId = contractAgreementId;
    }

    public Negotiation(JsonNode context, String type, String state, String edcType, Long createdAt, Object callbackAddresses, String errorDetail, String protocol, String counterPartyId, String counterPartyAddress, String contractAgreementId) {
        super(context, type, state, edcType, createdAt, callbackAddresses, errorDetail);
        this.protocol = protocol;
        this.counterPartyId = counterPartyId;
        this.counterPartyAddress = counterPartyAddress;
        this.contractAgreementId = contractAgreementId;
    }

    /** GETTERS AND SETTERS **/
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getCounterPartyId() {
        return counterPartyId;
    }

    public void setCounterPartyId(String counterPartyId) {
        this.counterPartyId = counterPartyId;
    }

    public String getCounterPartyAddress() {
        return counterPartyAddress;
    }

    public void setCounterPartyAddress(String counterPartyAddress) {
        this.counterPartyAddress = counterPartyAddress;
    }

    public String getContractAgreementId() {
        return contractAgreementId;
    }

    public void setContractAgreementId(String contractAgreementId) {
        this.contractAgreementId = contractAgreementId;
    }

}
