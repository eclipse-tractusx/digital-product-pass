package net.catenax.ce.productpass.models.dtregistry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DigitalTwin {
    @JsonProperty("description")
    ArrayList<JsonNode> description;
    @JsonProperty("globalAssetId")
    JsonNode globalAssetId;
    @JsonProperty("idShort")
    String idShort;
    @JsonProperty("identification")
    String identification;
    @JsonProperty("specificAssetIds")
    ArrayList<JsonNode> specificAssetIds;

    @JsonProperty("submodelDescriptors")
    ArrayList<SubModel> submodelDescriptors;

    public DigitalTwin() {
    }

    public DigitalTwin(ArrayList<JsonNode> description, JsonNode globalAssetId, String idShort, String identification, ArrayList<JsonNode> specificAssetIds, ArrayList<SubModel> submodelDescriptors) {
        this.description = description;
        this.globalAssetId = globalAssetId;
        this.idShort = idShort;
        this.identification = identification;
        this.specificAssetIds = specificAssetIds;
        this.submodelDescriptors = submodelDescriptors;
    }


    public ArrayList<JsonNode> getDescription() {
        return description;
    }

    public void setDescription(ArrayList<JsonNode> description) {
        this.description = description;
    }

    public JsonNode getGlobalAssetId() {
        return globalAssetId;
    }

    public void setGlobalAssetId(JsonNode globalAssetId) {
        this.globalAssetId = globalAssetId;
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

    public ArrayList<JsonNode> getSpecificAssetIds() {
        return specificAssetIds;
    }

    public void setSpecificAssetIds(ArrayList<JsonNode> specificAssetIds) {
        this.specificAssetIds = specificAssetIds;
    }

    public ArrayList<SubModel> getSubmodelDescriptors() {
        return submodelDescriptors;
    }

    public void setSubmodelDescriptors(ArrayList<SubModel> submodelDescriptors) {
        this.submodelDescriptors = submodelDescriptors;
    }
}

