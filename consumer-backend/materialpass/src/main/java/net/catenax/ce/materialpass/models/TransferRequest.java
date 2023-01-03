package net.catenax.ce.materialpass.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferRequest {
    @JsonProperty("id")
    String id;
    @JsonProperty("connectorId")
    String connectorId;
    @JsonProperty("connectorAddress")
    String connectorAddress;
    @JsonProperty("contractId")
    String contractId;
    @JsonProperty("assetId")
    String assetId;
    @JsonProperty("managedResources")
    Boolean managedResources;
    @JsonProperty("dataDestination")
    Properties dataDestination;

    public TransferRequest(String id, String connectorId, String connectorAddress, String contractId, String assetId, Boolean managedResources, String destinationType) {
        this.id = id;
        this.connectorId = connectorId;
        this.connectorAddress = connectorAddress;
        this.contractId = contractId;
        this.assetId = assetId;
        this.managedResources = managedResources;
        this.dataDestination = new Properties(destinationType);
    }

    public TransferRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public Boolean getManagedResources() {
        return managedResources;
    }

    public void setManagedResources(Boolean managedResources) {
        this.managedResources = managedResources;
    }

    public Properties getDataDestination() {
        return dataDestination;
    }

    public void setDataDestination(Properties dataDestination) {
        this.dataDestination = dataDestination;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

}
