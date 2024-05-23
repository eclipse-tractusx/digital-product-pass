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
import org.eclipse.tractusx.digitalproductpass.models.negotiation.CallbackAddress;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DidDocument;

import java.util.List;

/**
 * This class consists exclusively to define attributes that are common between the needed data objects for the Negotiation and Tranfer.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NegotiationTransferResponse extends DidDocument {

    /** ATTRIBUTES **/
    @JsonProperty("state")
    @JsonAlias({"state","edc:state"})
    String state;

    @JsonProperty("type")
    @JsonAlias({"type","edc:type"})
    String edcType;

    @JsonProperty("createdAt")
    @JsonAlias({"createdAt","edc:createdAt"})
    Long createdAt;
    @JsonProperty("callbackAddresses")
    @JsonAlias({"callbackAddresses","edc:callbackAddresses"})
    Object callbackAddresses;

    @JsonProperty("errorDetail")
    @JsonAlias({"errorDetail","edc:errorDetail"})
    String errorDetail;

    /** CONSTRUCTOR(S) **/
    public NegotiationTransferResponse() {
    }

    public NegotiationTransferResponse(String id, String type, String state, String edcType, Long createdAt, Object callbackAddresses, String errorDetail) {
        super(id, type);
        this.state = state;
        this.edcType = edcType;
        this.createdAt = createdAt;
        this.callbackAddresses = callbackAddresses;
        this.errorDetail = errorDetail;
    }

    public NegotiationTransferResponse(String state, String edcType, Long createdAt, Object callbackAddresses, String errorDetail) {
        this.state = state;
        this.edcType = edcType;
        this.createdAt = createdAt;
        this.callbackAddresses = callbackAddresses;
        this.errorDetail = errorDetail;
    }

    public NegotiationTransferResponse(String id, String type, JsonNode context, String state, String edcType, Long createdAt, Object callbackAddresses, String errorDetail) {
        super(id, type, context);
        this.state = state;
        this.edcType = edcType;
        this.createdAt = createdAt;
        this.callbackAddresses = callbackAddresses;
        this.errorDetail = errorDetail;
    }

    public NegotiationTransferResponse(String type, String state, String edcType, Long createdAt, Object callbackAddresses, String errorDetail) {
        super(type);
        this.state = state;
        this.edcType = edcType;
        this.createdAt = createdAt;
        this.callbackAddresses = callbackAddresses;
        this.errorDetail = errorDetail;
    }

    public NegotiationTransferResponse(JsonNode context, String state, String edcType, Long createdAt, Object callbackAddresses, String errorDetail) {
        super(context);
        this.state = state;
        this.edcType = edcType;
        this.createdAt = createdAt;
        this.callbackAddresses = callbackAddresses;
        this.errorDetail = errorDetail;
    }

    public NegotiationTransferResponse(JsonNode context, String type, String state, String edcType, Long createdAt, Object callbackAddresses, String errorDetail) {
        super(context, type);
        this.state = state;
        this.edcType = edcType;
        this.createdAt = createdAt;
        this.callbackAddresses = callbackAddresses;
        this.errorDetail = errorDetail;
    }

    /** GETTERS AND SETTERS **/
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEdcType() {
        return edcType;
    }

    public void setEdcType(String edcType) {
        this.edcType = edcType;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }


    public Object getCallbackAddresses() {
        return callbackAddresses;
    }

    public void setCallbackAddresses(Object callbackAddresses) {
        this.callbackAddresses = callbackAddresses;
    }
}
