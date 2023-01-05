package net.catenax.ce.productpass.models.dtregistry;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Properties {
    @JsonProperty("type")
    String type;

    public Properties(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
