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
import org.eclipse.tractusx.digitalproductpass.models.negotiation.CallbackAddress;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DidDocument;

import java.util.List;

/**
 * This class consists exclusively to define attributes related to the Transfer requests.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferRequest extends DidDocument {

    /** ATTRIBUTES **/
    @JsonProperty("assetId")
    String assetId;
    @JsonProperty("connectorAddress")
    String connectorAddress;
    @JsonProperty("contractId")
    String contractId;
    @JsonProperty("dataDestination")
    DataDestination dataDestination;
    @JsonProperty("managedResources")
    Boolean managedResources;
    @JsonProperty("protocol")
    String protocol;
    @JsonProperty("transferType")
    String transferType;
    @JsonProperty("callbackAddresses")
    List<CallbackAddress> callbackAddresses;

    /** CONSTRUCTOR(S) **/

    public TransferRequest() {
    }

    public TransferRequest(String id, String type, String assetId, String connectorAddress, String contractId, DataDestination dataDestination, Boolean managedResources, String protocol, String transferType, List<CallbackAddress> callbackAddresses) {
        super(id, type);
        this.assetId = assetId;
        this.connectorAddress = connectorAddress;
        this.contractId = contractId;
        this.dataDestination = dataDestination;
        this.managedResources = managedResources;
        this.protocol = protocol;
        this.transferType = transferType;
        this.callbackAddresses = callbackAddresses;
    }

    public TransferRequest(String assetId, String connectorAddress, String contractId, DataDestination dataDestination, Boolean managedResources, String protocol, String transferType, List<CallbackAddress> callbackAddresses) {
        this.assetId = assetId;
        this.connectorAddress = connectorAddress;
        this.contractId = contractId;
        this.dataDestination = dataDestination;
        this.managedResources = managedResources;
        this.protocol = protocol;
        this.transferType = transferType;
        this.callbackAddresses = callbackAddresses;
    }

    public TransferRequest(String id, String type, JsonNode context, String assetId, String connectorAddress, String contractId, DataDestination dataDestination, Boolean managedResources, String protocol, String transferType, List<CallbackAddress> callbackAddresses) {
        super(id, type, context);
        this.assetId = assetId;
        this.connectorAddress = connectorAddress;
        this.contractId = contractId;
        this.dataDestination = dataDestination;
        this.managedResources = managedResources;
        this.protocol = protocol;
        this.transferType = transferType;
        this.callbackAddresses = callbackAddresses;
    }

    public TransferRequest(String type, String assetId, String connectorAddress, String contractId, DataDestination dataDestination, Boolean managedResources, String protocol, String transferType, List<CallbackAddress> callbackAddresses) {
        super(type);
        this.assetId = assetId;
        this.connectorAddress = connectorAddress;
        this.contractId = contractId;
        this.dataDestination = dataDestination;
        this.managedResources = managedResources;
        this.protocol = protocol;
        this.transferType = transferType;
        this.callbackAddresses = callbackAddresses;
    }

    public TransferRequest(JsonNode context, String assetId, String connectorAddress, String contractId, DataDestination dataDestination, Boolean managedResources, String protocol, String transferType, List<CallbackAddress> callbackAddresses) {
        super(context);
        this.assetId = assetId;
        this.connectorAddress = connectorAddress;
        this.contractId = contractId;
        this.dataDestination = dataDestination;
        this.managedResources = managedResources;
        this.protocol = protocol;
        this.transferType = transferType;
        this.callbackAddresses = callbackAddresses;
    }

    public TransferRequest(JsonNode context, String type, String assetId, String connectorAddress, String contractId, DataDestination dataDestination, Boolean managedResources, String protocol, String transferType, List<CallbackAddress> callbackAddresses) {
        super(context, type);
        this.assetId = assetId;
        this.connectorAddress = connectorAddress;
        this.contractId = contractId;
        this.dataDestination = dataDestination;
        this.managedResources = managedResources;
        this.protocol = protocol;
        this.transferType = transferType;
        this.callbackAddresses = callbackAddresses;
    }


    /** GETTERS AND SETTERS **/
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

    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public String getString() { return transferType; }
    public void setString(String transferType) { this.transferType = transferType; }

    public List<CallbackAddress> getCallbackAddresses() {
        return callbackAddresses;
    }

    public void setCallbackAddresses(List<CallbackAddress> callbackAddresses) {
        this.callbackAddresses = callbackAddresses;
    }

    /** INNER CLASSES **/


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
