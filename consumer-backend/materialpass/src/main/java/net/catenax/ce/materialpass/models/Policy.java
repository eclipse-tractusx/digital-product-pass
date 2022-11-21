package net.catenax.ce.materialpass.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class Policy {
    List<JsonNode> permissions;
    List<JsonNode> prohibitions;
    List<JsonNode> obligations;
    JsonNode extensibleProperties;
    String inheritsFrom;
    String assigner;
    String target;

    @JsonProperty("@type")
    JsonNode type;
}
