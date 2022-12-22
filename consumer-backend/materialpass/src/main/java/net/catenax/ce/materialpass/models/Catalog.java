package net.catenax.ce.materialpass.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Catalog {
    @JsonProperty("id")
    String id;
    @JsonProperty("contractOffers")
    List<Offer> contractOffers;

    @JsonIgnore
    protected Map<String, Integer> contractOffersMap = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Offer> getContractOffers() {
        return contractOffers;
    }

    public void setContractOffers(List<Offer> contractOffers) {
        this.contractOffers = contractOffers;
    }

    public Map<String, Integer> loadContractOffersMapByAssetId(){
        int i = 0;
        for(Offer contractOffer: this.contractOffers){
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
