package net.catenax.ce.materialpass.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class Asset {
    @JsonProperty("id")
    String id;
    @JsonProperty("createdAt")
    String createdAt;
    @JsonProperty("properties")
    JsonNode properties;;
}
