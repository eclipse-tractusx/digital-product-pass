package org.eclipse.tractusx.productpass.models.passports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DigitalProductPassport extends Passport {
    @JsonProperty("serialization")
    JsonNode serialization;
    @JsonProperty("typology")
    JsonNode typology;
    @JsonProperty("metadata")
    JsonNode metadata;
    @JsonProperty("characteristics")
    JsonNode characteristics;
    @JsonProperty("commercial")
    JsonNode commercial;
    @JsonProperty("identification")
    JsonNode identification;
    @JsonProperty("sources")
    JsonNode sources;
    @JsonProperty("handling")
    JsonNode handling;
    @JsonProperty("additionalData")
    JsonNode additionalData;
    @JsonProperty("sustainability")
    JsonNode sustainability;
    @JsonProperty("operation")
    JsonNode operation;

    public DigitalProductPassport(JsonNode serialization, JsonNode typology, JsonNode metadata, JsonNode characteristics, JsonNode commercial, JsonNode identification, JsonNode sources, JsonNode handling, JsonNode additionalData, JsonNode sustainability, JsonNode operation) {
        this.serialization = serialization;
        this.typology = typology;
        this.metadata = metadata;
        this.characteristics = characteristics;
        this.commercial = commercial;
        this.identification = identification;
        this.sources = sources;
        this.handling = handling;
        this.additionalData = additionalData;
        this.sustainability = sustainability;
        this.operation = operation;
    }

    public DigitalProductPassport() {
    }

    public JsonNode getSerialization() {
        return serialization;
    }

    public void setSerialization(JsonNode serialization) {
        this.serialization = serialization;
    }

    public JsonNode getTypology() {
        return typology;
    }

    public void setTypology(JsonNode typology) {
        this.typology = typology;
    }

    public JsonNode getMetadata() {
        return metadata;
    }

    public void setMetadata(JsonNode metadata) {
        this.metadata = metadata;
    }

    public JsonNode getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(JsonNode characteristics) {
        this.characteristics = characteristics;
    }

    public JsonNode getCommercial() {
        return commercial;
    }

    public void setCommercial(JsonNode commercial) {
        this.commercial = commercial;
    }

    public JsonNode getIdentification() {
        return identification;
    }

    public void setIdentification(JsonNode identification) {
        this.identification = identification;
    }

    public JsonNode getSources() {
        return sources;
    }

    public void setSources(JsonNode sources) {
        this.sources = sources;
    }

    public JsonNode getHandling() {
        return handling;
    }

    public void setHandling(JsonNode handling) {
        this.handling = handling;
    }

    public JsonNode getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(JsonNode additionalData) {
        this.additionalData = additionalData;
    }

    public JsonNode getSustainability() {
        return sustainability;
    }

    public void setSustainability(JsonNode sustainability) {
        this.sustainability = sustainability;
    }

    public JsonNode getOperation() {
        return operation;
    }

    public void setOperation(JsonNode operation) {
        this.operation = operation;
    }
}
