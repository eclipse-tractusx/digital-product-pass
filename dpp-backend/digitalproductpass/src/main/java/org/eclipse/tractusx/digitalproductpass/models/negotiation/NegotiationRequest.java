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
import com.fasterxml.jackson.databind.JsonNode;

/**
 * This class consists exclusively to define attributes related to the Negotiation requests.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NegotiationRequest extends DidDocument{

    /** ATTRIBUTES **/
    @JsonProperty("connectorAddress")
    String connectorAddress;
    @JsonProperty("protocol")
    String protocol;
    @JsonProperty("connectorId")
    String connectorId;
    @JsonProperty("providerId")
    String providerId;
    @JsonProperty("offer")
    Offer offer;

    /** CONSTRUCTOR(S) **/
    @SuppressWarnings("Unused")
    public NegotiationRequest(JsonNode context, String connectorAddress, String protocol, String connectorId, String providerId, Offer offer) {
        this.context = context;
        this.type = "NegotiationInitiateRequestDto";
        this.connectorAddress = connectorAddress;
        this.protocol = protocol;
        this.connectorId = connectorId;
        this.providerId = providerId;
        this.offer = offer;
    }
    public NegotiationRequest(JsonNode context, String connectorAddress, String connectorId, String providerId, Offer offer) {
        this.context = context;
        this.type = "NegotiationInitiateRequestDto";
        this.connectorAddress = connectorAddress;
        this.protocol = "dataspace-protocol-http";
        this.connectorId = connectorId;
        this.providerId = providerId;
        this.offer = offer;
    }
    @SuppressWarnings("Unused")
    public NegotiationRequest(JsonNode context, String type, String connectorAddress, String protocol, String connectorId, String providerId, Offer offer) {
        this.context = context;
        this.type = type;
        this.connectorAddress = connectorAddress;
        this.protocol = protocol;
        this.connectorId = connectorId;
        this.providerId = providerId;
        this.offer = offer;
    }
    @SuppressWarnings("Unused")
    public NegotiationRequest() {
    }

    /** GETTERS AND SETTERS **/
    public String getConnectorId() {
        return connectorId;
    }
    public void setConnectorId(String connectorId) {
        this.connectorId = connectorId;
    }
    public String getConnectorAddress() {
        return connectorAddress;
    }
    public void setConnectorAddress(String connectorAddress) {
        this.connectorAddress = connectorAddress;
    }
    public Offer getOffer() {
        return offer;
    }
    @SuppressWarnings("Unused")
    public void setOffer(Offer offer) {
        this.offer = offer;
    }
    public String getProtocol() {
        return protocol;
    }
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    @SuppressWarnings("Unused")
    public String getProviderId() {
        return providerId;
    }
    @SuppressWarnings("Unused")
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
