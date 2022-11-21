package net.catenax.ce.materialpass.models;

import java.util.Date;

public class ContractOffer {
    String id;

    Policy policy;
    Asset asset;

    String assetId;
    String provider;
    String consumer;

    Date offerStart;
    Date offerEnd;
    Date contractStart;
    Date contractEnd;
}
