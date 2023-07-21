package org.eclipse.tractusx.productpass.models.catenax;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dtr {
    @JsonProperty("contractId")
    private String contractId;
    @JsonProperty("endpoint")
    private String endpoint;
    @JsonProperty("assetId")
    private String assetId;

    public Dtr(String contractId, String endpoint, String assetId) {
        this.contractId = contractId;
        this.endpoint = endpoint;
        this.assetId = assetId;
    }

    public Dtr() {
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }
}
