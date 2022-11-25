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

    public List<JsonNode> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<JsonNode> permissions) {
        this.permissions = permissions;
    }

    public List<JsonNode> getProhibitions() {
        return prohibitions;
    }

    public void setProhibitions(List<JsonNode> prohibitions) {
        this.prohibitions = prohibitions;
    }

    public List<JsonNode> getObligations() {
        return obligations;
    }

    public void setObligations(List<JsonNode> obligations) {
        this.obligations = obligations;
    }

    public JsonNode getExtensibleProperties() {
        return extensibleProperties;
    }

    public void setExtensibleProperties(JsonNode extensibleProperties) {
        this.extensibleProperties = extensibleProperties;
    }

    public String getInheritsFrom() {
        return inheritsFrom;
    }

    public void setInheritsFrom(String inheritsFrom) {
        this.inheritsFrom = inheritsFrom;
    }

    public String getAssigner() {
        return assigner;
    }

    public void setAssigner(String assigner) {
        this.assigner = assigner;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public JsonNode getType() {
        return type;
    }

    public void setType(JsonNode type) {
        this.type = type;
    }
}
