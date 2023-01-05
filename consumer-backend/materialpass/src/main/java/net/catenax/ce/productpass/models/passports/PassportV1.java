package net.catenax.ce.productpass.models.passports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PassportV1{

    @JsonProperty("electrochemicalProperties")
    JsonNode electrochemicalProperties;
    @JsonProperty("document")
    JsonNode document;

    @JsonProperty("datePlacedOnMarket")
    String datePlacedOnMarket;

    @JsonProperty("cellChemistry")
    JsonNode cellChemistry;
    @JsonProperty("physicalDimensions")
    JsonNode physicalDimensions;

    @JsonProperty("temperatureRangeIdleState")
    JsonNode temperatureRangeIdleState;
    @JsonProperty("batteryCycleLife")
    JsonNode batteryCycleLife;
    @JsonProperty("manufacturer")
    JsonNode manufacturer;

    @JsonProperty("warrantyPeriod")
    String warrantyPeriod;

    @JsonProperty("composition")
    JsonNode composition;

    @JsonProperty("manufacturing")
    JsonNode manufacturing;

    @JsonProperty("batteryIdentification")
    JsonNode batteryIdentification;

    @JsonProperty("stateOfBattery")
    JsonNode stateOfBattery;

    @JsonProperty("cO2FootprintTotal")
    JsonNode cO2FootprintTotal;

}
