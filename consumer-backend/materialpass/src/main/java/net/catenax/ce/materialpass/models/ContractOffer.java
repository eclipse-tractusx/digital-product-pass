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
}
