/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
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
 * This class consists exclusively to define attributes related to the Transfer requests.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferRequest {

    /** ATTRIBUTES **/
    @JsonProperty("@context")
    JsonNode context;
    @JsonProperty("assetId")
    String assetId;
    @JsonProperty("connectorAddress")
    String connectorAddress;
    @JsonProperty("connectorId")
    String connectorId;
    @JsonProperty("contractId")
    String contractId;
    @JsonProperty("dataDestination")
    DataDestination dataDestination;
    @JsonProperty("managedResources")
    Boolean managedResources;
    @JsonProperty("privateProperties")
    PrivateProperties privateProperties;
    @JsonProperty("protocol")
    String protocol;
    @JsonProperty("transferType")
    TransferType transferType;

    /** CONSTRUCTOR(S) **/

    public TransferRequest() {
    }
    public TransferRequest(JsonNode context, String assetId, String connectorAddress, String connectorId, String contractId, DataDestination dataDestination, Boolean managedResources, PrivateProperties privateProperties, String protocol, TransferType transferType) {
        this.context = context;
        this.assetId = assetId;
        this.connectorAddress = connectorAddress;
        this.connectorId = connectorId;
        this.contractId = contractId;
        this.dataDestination = dataDestination;
        this.managedResources = managedResources;
        this.privateProperties = privateProperties;
        this.protocol = protocol;
        this.transferType = transferType;
    }

    /** GETTERS AND SETTERS **/
    public JsonNode getContext() {
        return context;
    }
    public void setContext(JsonNode context) {
        this.context = context;
    }
    public String getAssetId() {
        return assetId;
    }
    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }
    public String getConnectorAddress() {
        return connectorAddress;
    }
    public void setConnectorAddress(String connectorAddress) {
        this.connectorAddress = connectorAddress;
    }
    public String getContractId() { return contractId; }
    public void setContractId(String contractId) { this.contractId = contractId; }
    public DataDestination getDataDestination() { return dataDestination; }
    public void setDataDestination(DataDestination dataDestination) { this.dataDestination = dataDestination; }
    public Boolean getManagedResources() { return managedResources; }
    public void setManagedResources(Boolean managedResources) { this.managedResources = managedResources; }
    public PrivateProperties getPrivateProperties() { return privateProperties; }
    public void setPrivateProperties(PrivateProperties privateProperties) { this.privateProperties = privateProperties; }
    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public TransferType getTransferType() { return transferType; }
    public void setTransferType(TransferType transferType) { this.transferType = transferType; }
    public String getConnectorId() { return connectorId; }
    public void setConnectorId(String connectorId) { this.connectorId = connectorId; }

    /** INNER CLASSES **/
    /**
     * This class consists exclusively to define attributes related to the Transfer request's transfer type property.
     **/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TransferType {

        /** ATTRIBUTES **/
        @JsonProperty("contentType")
        String contentType;
        @JsonProperty("isFinite")
        Boolean isFinite;

        /** GETTERS AND SETTERS **/
        public String getContentType() {
            return contentType;
        }
        public void setContentType(String contentType) {
            this.contentType = contentType;
        }
        public Boolean getIsFinite() {
            return isFinite;
        }
        public void setIsFinite(Boolean isFinite) {
            this.isFinite = isFinite;
        }
    }

    /**
     * This class consists exclusively to define attributes related to the Transfer request's private properties' attribute.
     **/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PrivateProperties {

        /** ATTRIBUTES **/
        @JsonProperty("receiverHttpEndpoint")
        String receiverHttpEndpoint;

        /** GETTERS AND SETTERS **/
        public String getReceiverHttpEndpoint() {
            return receiverHttpEndpoint;
        }
        public void setReceiverHttpEndpoint(String receiverHttpEndpoint) {
            this.receiverHttpEndpoint = receiverHttpEndpoint;
        }
    }

    /**
     * This class consists exclusively to define attributes related to the Transfer request's data destination attribute.
     **/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DataDestination {

        /** ATTRIBUTES **/
        @JsonProperty("type")
        String type;

        /** GETTERS AND SETTERS **/
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
    }
}
