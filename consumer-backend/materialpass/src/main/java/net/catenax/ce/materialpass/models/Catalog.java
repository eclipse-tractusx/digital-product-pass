package net.catenax.ce.materialpass.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Catalog {
    @JsonProperty("id")
    String id;
    @JsonProperty("contractOffers")
    List<ContractOffer> contractOffers;
}
