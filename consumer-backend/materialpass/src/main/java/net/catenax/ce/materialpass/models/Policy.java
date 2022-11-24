package net.catenax.ce.materialpass.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class Policy {
    @JsonProperty("permissions")
    List<JsonNode> permissions;
    @JsonProperty("prohibitions")
    List<JsonNode> prohibitions;
    @JsonProperty("obligations")
    List<JsonNode> obligations;
    @JsonProperty("extensibleProperties")
    JsonNode extensibleProperties;
    @JsonProperty("inheritsFrom")
    String inheritsFrom;
    @JsonProperty("assigner")
    String assigner;
    @JsonProperty("assignee")
    String assignee;
    @JsonProperty("target")
    String target;

    @JsonProperty("@type")
    JsonNode type;
}
