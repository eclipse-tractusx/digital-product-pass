package org.eclipse.tractusx.productpass.models.catenax;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dtr {
    @JsonProperty("contractId")
    private String contractId;
    @JsonProperty("connectorEndpoint")
    private String connectorEndpoint;
    @JsonProperty("assetType")
    private String assetType;

    public Dtr(String contractId, String connectorEndpoint, String assetType) {
        this.contractId = contractId;
        this.connectorEndpoint = connectorEndpoint;
        this.assetType = assetType;
    }

    public Dtr() {
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getConnectorEndpoint() {
        return connectorEndpoint;
    }

    public void setConnectorEndpoint(String connectorEndpoint) {
        this.connectorEndpoint = connectorEndpoint;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }
}
