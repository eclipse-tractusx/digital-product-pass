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
import org.eclipse.tractusx.digitalproductpass.models.negotiation.response.NegotiationTransferResponse;

import java.util.List;

/**
 * This class consists exclusively to define attributes related to the Transfer's data.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transfer extends NegotiationTransferResponse {

    /** ATTRIBUTES **/
    @JsonProperty("stateTimestamp")
    @JsonAlias({"stateTimestamp","edc:stateTimestamp"})
    Long stateTimestamp;
    @JsonProperty("correlationId")
    @JsonAlias({"correlationId","edc:correlationId"})
    String correlationId;
    @JsonProperty("assetId")
    @JsonAlias({"assetId","edc:assetId"})
    String assetId;
    @JsonProperty("contractId")
    @JsonAlias({"contractId","edc:contractId"})
    String contractId;
    @JsonProperty("dataDestination")
    @JsonAlias({"dataDestination","edc:dataDestination"})
    DataDestination dataDestination;
    @JsonProperty("dataRequest")
    @JsonAlias({"dataRequest","edc:dataRequest"})
    DataRequest dataRequest;
    @JsonProperty("receiverHttpEndpoint")
    @JsonAlias({"receiverHttpEndpoint","edc:receiverHttpEndpoint"})
    String receiverHttpEndpoint;
    @JsonProperty("callbackAddresses")
    @JsonAlias({"callbackAddresses","edc:callbackAddresses"})
    Object callbackAddresses;

    public Transfer(Long stateTimestamp, String correlationId, String assetId, String contractId, DataDestination dataDestination, DataRequest dataRequest, String receiverHttpEndpoint, Object callbackAddresses) {
        this.stateTimestamp = stateTimestamp;
        this.correlationId = correlationId;
        this.assetId = assetId;
        this.contractId = contractId;
        this.dataDestination = dataDestination;
        this.dataRequest = dataRequest;
        this.receiverHttpEndpoint = receiverHttpEndpoint;
        this.callbackAddresses = callbackAddresses;
    }

    public Transfer(String id, String type, String state, String edcType, Long createdAt, Object callbackAddresses, String errorDetail, Long stateTimestamp, String correlationId, String assetId, String contractId, DataDestination dataDestination, DataRequest dataRequest, String receiverHttpEndpoint, Object callbackAddresses1) {
        super(id, type, state, edcType, createdAt, callbackAddresses, errorDetail);
        this.stateTimestamp = stateTimestamp;
        this.correlationId = correlationId;
        this.assetId = assetId;
        this.contractId = contractId;
        this.dataDestination = dataDestination;
        this.dataRequest = dataRequest;
        this.receiverHttpEndpoint = receiverHttpEndpoint;
        this.callbackAddresses = callbackAddresses1;
    }

    public Transfer(String state, String edcType, Long createdAt, Object callbackAddresses, String errorDetail, Long stateTimestamp, String correlationId, String assetId, String contractId, DataDestination dataDestination, DataRequest dataRequest, String receiverHttpEndpoint, Object callbackAddresses1) {
        super(state, edcType, createdAt, callbackAddresses, errorDetail);
        this.stateTimestamp = stateTimestamp;
        this.correlationId = correlationId;
        this.assetId = assetId;
        this.contractId = contractId;
        this.dataDestination = dataDestination;
        this.dataRequest = dataRequest;
        this.receiverHttpEndpoint = receiverHttpEndpoint;
        this.callbackAddresses = callbackAddresses1;
    }

    public Transfer(String id, String type, JsonNode context, String state, String edcType, Long createdAt, Object callbackAddresses, String errorDetail, Long stateTimestamp, String correlationId, String assetId, String contractId, DataDestination dataDestination, DataRequest dataRequest, String receiverHttpEndpoint, Object callbackAddresses1) {
        super(id, type, context, state, edcType, createdAt, callbackAddresses, errorDetail);
        this.stateTimestamp = stateTimestamp;
        this.correlationId = correlationId;
        this.assetId = assetId;
        this.contractId = contractId;
        this.dataDestination = dataDestination;
        this.dataRequest = dataRequest;
        this.receiverHttpEndpoint = receiverHttpEndpoint;
        this.callbackAddresses = callbackAddresses1;
    }

    public Transfer(String type, String state, String edcType, Long createdAt, Object callbackAddresses, String errorDetail, Long stateTimestamp, String correlationId, String assetId, String contractId, DataDestination dataDestination, DataRequest dataRequest, String receiverHttpEndpoint, Object callbackAddresses1) {
        super(type, state, edcType, createdAt, callbackAddresses, errorDetail);
        this.stateTimestamp = stateTimestamp;
        this.correlationId = correlationId;
        this.assetId = assetId;
        this.contractId = contractId;
        this.dataDestination = dataDestination;
        this.dataRequest = dataRequest;
        this.receiverHttpEndpoint = receiverHttpEndpoint;
        this.callbackAddresses = callbackAddresses1;
    }

    public Transfer(JsonNode context, String state, String edcType, Long createdAt, Object callbackAddresses, String errorDetail, Long stateTimestamp, String correlationId, String assetId, String contractId, DataDestination dataDestination, DataRequest dataRequest, String receiverHttpEndpoint, Object callbackAddresses1) {
        super(context, state, edcType, createdAt, callbackAddresses, errorDetail);
        this.stateTimestamp = stateTimestamp;
        this.correlationId = correlationId;
        this.assetId = assetId;
        this.contractId = contractId;
        this.dataDestination = dataDestination;
        this.dataRequest = dataRequest;
        this.receiverHttpEndpoint = receiverHttpEndpoint;
        this.callbackAddresses = callbackAddresses1;
    }

    public Transfer(JsonNode context, String type, String state, String edcType, Long createdAt, Object callbackAddresses, String errorDetail, Long stateTimestamp, String correlationId, String assetId, String contractId, DataDestination dataDestination, DataRequest dataRequest, String receiverHttpEndpoint, Object callbackAddresses1) {
        super(context, type, state, edcType, createdAt, callbackAddresses, errorDetail);
        this.stateTimestamp = stateTimestamp;
        this.correlationId = correlationId;
        this.assetId = assetId;
        this.contractId = contractId;
        this.dataDestination = dataDestination;
        this.dataRequest = dataRequest;
        this.receiverHttpEndpoint = receiverHttpEndpoint;
        this.callbackAddresses = callbackAddresses1;
    }

    public Transfer() {
    }

    /** CONSTRUCTOR(S) **/


    public Long getStateTimestamp() {
        return stateTimestamp;
    }

    public void setStateTimestamp(Long stateTimestamp) {
        this.stateTimestamp = stateTimestamp;
    }

    public DataDestination getDataDestination() {
        return dataDestination;
    }

    public void setDataDestination(DataDestination dataDestination) {
        this.dataDestination = dataDestination;
    }

    public DataRequest getDataRequest() {
        return dataRequest;
    }

    public void setDataRequest(DataRequest dataRequest) {
        this.dataRequest = dataRequest;
    }

    public String getReceiverHttpEndpoint() {
        return receiverHttpEndpoint;
    }

    public void setReceiverHttpEndpoint(String receiverHttpEndpoint) {
        this.receiverHttpEndpoint = receiverHttpEndpoint;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }


    /** INNER CLASSES **/
    /**
     * This class consists exclusively to define attributes related to the Transfer's data request property.
     **/
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class DataRequest extends DidDocument {

        /** ATTRIBUTES **/
        @JsonProperty("assetId")
        @JsonAlias({"assetId","edc:assetId"})
        String assetId;
        @JsonProperty("connectorId")
        @JsonAlias({"connectorId","edc:connectorId"})
        String connectorId;
        @JsonProperty("contractId")
        @JsonAlias({"contractId","edc:contractId"})
        String contractId;

        /** GETTERS AND SETTERS **/
        public String getAssetId() {
            return assetId;
        }
        public void setAssetId(String assetId) {
            this.assetId = assetId;
        }
        public String getContractId() {
            return contractId;
        }
        public void setContractId(String contractId) {
            this.contractId = contractId;
        }
        public String getConnectorId() {
            return connectorId;
        }
        public void setConnectorId(String connectorId) {
            this.connectorId = connectorId;
        }
    }

    /**
     * This class consists exclusively to define attributes related to the Transfer's data destination attribute.
     **/
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DataDestination extends DidDocument{

        /** ATTRIBUTES **/
        @JsonProperty("type")
        @JsonAlias({"type","edc:type"})
        String edcType;

        /** GETTERS AND SETTERS **/
        public String getEdcType() {
            return edcType;
        }
        public void setEdcType(String edcType) {
            this.edcType = edcType;
        }
    }
}

