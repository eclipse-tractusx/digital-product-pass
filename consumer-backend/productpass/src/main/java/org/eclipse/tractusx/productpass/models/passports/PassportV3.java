/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the
 * License for the specific language govern in permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

package org.eclipse.tractusx.productpass.models.passports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;


/*
 *  =======[DESCRIPTION]=========================================================
 *   Passport Semantic BAMM Version @v3.0.1
 *   Aspect model URN: urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass
 *   https://portal.int.demo.catena-x.net/semantichub/urn%3Abamm%3Aio.catenax.battery.battery_pass%3A3.0.1%23BatteryPass
 *   Flexible Structure (Abstraction from main attributes using JsonNodes)
 *  =============================================================================
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PassportV3 extends Passport{

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
