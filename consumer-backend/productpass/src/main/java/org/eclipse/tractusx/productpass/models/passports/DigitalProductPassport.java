package org.eclipse.tractusx.productpass.models.passports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * This class consists exclusively to define attributes related to the designed model of the Digital Product Passport.
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DigitalProductPassport extends Passport {
    @JsonProperty("serialization")
    JsonNode serialization;
    @JsonProperty("typology")
    JsonNode typology;
    @JsonProperty("metadata")
    String metadata;
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
    String additionalData;
    @JsonProperty("sustainability")
    JsonNode sustainability;
    @JsonProperty("operation")
    JsonNode operation;
}
