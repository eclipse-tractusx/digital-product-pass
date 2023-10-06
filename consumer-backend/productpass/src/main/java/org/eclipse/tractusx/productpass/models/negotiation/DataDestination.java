package org.eclipse.tractusx.productpass.models.negotiation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class consists exclusively to define attributes related to the Transfer's and Transfer request's data destination property.
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataDestination {

    /** ATTRIBUTES **/
    @JsonProperty("edc:type")
    String type;

    /** GETTERS AND SETTERS **/
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
