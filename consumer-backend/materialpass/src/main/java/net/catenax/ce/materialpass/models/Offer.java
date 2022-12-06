package net.catenax.ce.materialpass.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Offer extends ContractOffer{

    @JsonProperty("offerId")
    String offerId;

    public void open(){
        this.offerId = this.id;
        this.assetId = this.getAssetId();
    }
    public void close(){
        this.offerId = null;
        this.assetId = null;
    }

    public String getOfferId() {
        return offerId;
    }

    public String getConnectorId() {
        return this.offerId.split(":")[1];
    }
    public String getAssetId() {
        return this.id.split(":")[0];
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }
}
