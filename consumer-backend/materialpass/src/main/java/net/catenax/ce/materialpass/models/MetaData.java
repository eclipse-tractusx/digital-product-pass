package net.catenax.ce.materialpass.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class MetaData {

    @JsonProperty("createdAt")
    Long createdAt;

    @JsonProperty("updatedAt")
    Long updatedAt;

    @JsonProperty("state")
    String state;

    @JsonProperty("type")
    String type;

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
