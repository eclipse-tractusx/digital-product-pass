package net.catenax.ce.materialpass.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transfer {
    @JsonProperty("id")
    String id;
    @JsonProperty("createdAt")
    String createdAt;

    @JsonProperty("updatedAt")
    String updatedAt;
    @JsonProperty("type")
    String type;
    @JsonProperty("state")
    String state;

    @JsonProperty("stateTimestamp")
    Long stateTimestamp;

    @JsonProperty("errorDetail")
    String errorDetail;

    @JsonProperty("dataRequest")
    DataRequest dataRequest;

    @JsonProperty("dataDestination")
    DataDestination dataDestination;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getStateTimestamp() {
        return stateTimestamp;
    }

    public void setStateTimestamp(Long stateTimestamp) {
        this.stateTimestamp = stateTimestamp;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    public DataRequest getDataRequest() {
        return dataRequest;
    }

    public void setDataRequest(DataRequest dataRequest) {
        this.dataRequest = dataRequest;
    }

    static class DataRequest {
        @JsonProperty("assetId")
        String assetId;
        @JsonProperty("contractId")
        String contractId;
        @JsonProperty("connectorId")
        String connectorId;

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

    static class DataDestination {
        @JsonProperty("properties")
        Properties properties;

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public DataDestination getDataDestination() {
        return dataDestination;
    }

    public void setDataDestination(DataDestination properties) {
        this.dataDestination = properties;
    }
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

