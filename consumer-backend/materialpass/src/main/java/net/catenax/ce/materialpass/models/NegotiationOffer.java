package net.catenax.ce.materialpass.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NegotiationOffer {
    @JsonProperty("connectorId")
    String connectorId;

    @JsonProperty("connectorAddress")
    String connectorAddress;

    @JsonProperty("offer")
    Offer offer;
    public NegotiationOffer(){

    }

    public NegotiationOffer(String connectorId, String connectorAddress, Offer offer) {
        this.connectorId = connectorId;
        this.connectorAddress = connectorAddress;
        this.offer = offer;
    }

    public String getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(String connectorId) {
        this.connectorId = connectorId;
    }

    public String getConnectorAddress() {
        return connectorAddress;
    }

    public void setConnectorAddress(String connectorAddress) {
        this.connectorAddress = connectorAddress;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}
