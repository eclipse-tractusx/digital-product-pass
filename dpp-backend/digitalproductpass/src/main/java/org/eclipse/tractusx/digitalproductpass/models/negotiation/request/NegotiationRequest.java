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

package org.eclipse.tractusx.digitalproductpass.models.negotiation.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DidDocument;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog.Policy;

/**
 * This class consists exclusively to define attributes related to the Negotiation requests.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NegotiationRequest extends DidDocument {

    /** ATTRIBUTES **/
    @JsonProperty("counterPartyAddress")
    String counterPartyAddress;
    @JsonProperty("protocol")
    String protocol;
    @JsonProperty("policy")
    Policy policy;

    public NegotiationRequest(String id, String type, String counterPartyAddress, String protocol, Policy policy) {
        super(id, type);
        this.counterPartyAddress = counterPartyAddress;
        this.protocol = protocol;
        this.policy = policy;
    }

    public NegotiationRequest(String counterPartyAddress, String protocol, Policy policy) {
        this.counterPartyAddress = counterPartyAddress;
        this.protocol = protocol;
        this.policy = policy;
    }

    public NegotiationRequest(String id, String type, JsonNode context, String counterPartyAddress, String protocol, Policy policy) {
        super(id, type, context);
        this.counterPartyAddress = counterPartyAddress;
        this.protocol = protocol;
        this.policy = policy;
    }

    public NegotiationRequest(String type, String counterPartyAddress, String protocol, Policy policy) {
        super(type);
        this.counterPartyAddress = counterPartyAddress;
        this.protocol = protocol;
        this.policy = policy;
    }

    public NegotiationRequest(JsonNode context, String counterPartyAddress, String protocol, Policy policy) {
        super(context);
        this.counterPartyAddress = counterPartyAddress;
        this.protocol = protocol;
        this.policy = policy;
    }

    public NegotiationRequest(JsonNode context, String type, String counterPartyAddress, String protocol, Policy policy) {
        super(context, type);
        this.counterPartyAddress = counterPartyAddress;
        this.protocol = protocol;
        this.policy = policy;
    }

    public NegotiationRequest(String id, String type) {
        super(id, type);
    }

    public NegotiationRequest() {
    }

    public NegotiationRequest(String id, String type, JsonNode context) {
        super(id, type, context);
    }

    public NegotiationRequest(String type) {
        super(type);
    }

    public NegotiationRequest(JsonNode context) {
        super(context);
    }

    public NegotiationRequest(JsonNode context, String type) {
        super(context, type);
    }


    public String getCounterPartyAddress() {
        return counterPartyAddress;
    }

    public void setCounterPartyAddress(String counterPartyAddress) {
        this.counterPartyAddress = counterPartyAddress;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    /** CONSTRUCTOR(S) **/


    /** GETTERS AND SETTERS **/

}
