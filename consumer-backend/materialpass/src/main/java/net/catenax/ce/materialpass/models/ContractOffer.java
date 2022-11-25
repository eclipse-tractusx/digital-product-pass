package net.catenax.ce.materialpass.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ContractOffer {
    @JsonProperty("id")
    String id;
    @JsonProperty("policy")
    Policy policy;
    @JsonProperty("asset")
    Asset asset;


    @JsonProperty("assetId")
    String assetId;
    @JsonProperty("provider")
    String provider;
    @JsonProperty("consumer")
    String consumer;

    @JsonProperty("offerStart")
    Date offerStart;
    @JsonProperty("offerEnd")
    Date offerEnd;
    @JsonProperty("contractStart")
    Date contractStart;
    @JsonProperty("contractEnd")
    Date contractEnd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public Date getOfferStart() {
        return offerStart;
    }

    public void setOfferStart(Date offerStart) {
        this.offerStart = offerStart;
    }

    public Date getOfferEnd() {
        return offerEnd;
    }

    public void setOfferEnd(Date offerEnd) {
        this.offerEnd = offerEnd;
    }

    public Date getContractStart() {
        return contractStart;
    }

    public void setContractStart(Date contractStart) {
        this.contractStart = contractStart;
    }

    public Date getContractEnd() {
        return contractEnd;
    }

    public void setContractEnd(Date contractEnd) {
        this.contractEnd = contractEnd;
    }
}
