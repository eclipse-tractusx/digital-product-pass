package net.catenax.ce.productpass.models.dtregistry;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;

public class SubModel {
    @JsonProperty("description")
    ArrayList<JsonNode> description;
    @JsonProperty("idShort")
    String idShort;
    @JsonProperty("identification")
    String identification;
    @JsonProperty("semanticId")
    JsonNode semanticId;

    @JsonProperty("endpoints")
    ArrayList<EndPoint> endpoints;

    public SubModel(ArrayList<JsonNode> description, String idShort, String identification, JsonNode semanticId, ArrayList<EndPoint> endpoints) {
        this.description = description;
        this.idShort = idShort;
        this.identification = identification;
        this.semanticId = semanticId;
        this.endpoints = endpoints;
    }

    public SubModel() {
    }

    public ArrayList<JsonNode> getDescription() {
        return description;
    }

    public void setDescription(ArrayList<JsonNode> description) {
        this.description = description;
    }

    public String getIdShort() {
        return idShort;
    }

    public void setIdShort(String idShort) {
        this.idShort = idShort;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public JsonNode getSemanticId() {
        return semanticId;
    }

    public void setSemanticId(JsonNode semanticId) {
        this.semanticId = semanticId;
    }

    public ArrayList<EndPoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(ArrayList<EndPoint> endpoints) {
        this.endpoints = endpoints;
    }
}
