package net.catenax.ce.materialpass.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Catalog {
    @JsonProperty("id")
    String id;
    @JsonProperty("contractOffers")
    List<ContractOffer> contractOffers;

    @JsonIgnore
    protected Map<String, Integer> contractOffersMap = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ContractOffer> getContractOffers() {
        return contractOffers;
    }

    public void setContractOffers(List<ContractOffer> contractOffers) {
        this.contractOffers = contractOffers;
    }

    public Map<String, Integer> loadContractOffersMapByAssetId(){
        int i = 0;
        for(ContractOffer contractOffer: this.contractOffers){
            this.contractOffersMap.put(contractOffer.getAsset().getId(),i);
            i++;
        }
        return this.contractOffersMap;
    }
    public Map<String, Integer> getContractOffersMap() {
        return contractOffersMap;
    }

    public void setContractOffersMap(Map<String, Integer> contractOffersMap) {
        this.contractOffersMap = contractOffersMap;
    }
}
