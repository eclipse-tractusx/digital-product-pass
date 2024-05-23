/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
 *
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

package org.eclipse.tractusx.digitalproductpass.models.edc;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.CallbackAddress;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DidDocument;

import java.util.List;
import java.util.Properties;

/**
 * This class consists exclusively to define attributes related to Endpoint Data Reference (EDR) received from the EDC.
 * */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
@Data
@Getter
public class EndpointDataReference {

    /** CONSTANTS **/
    @JsonIgnore
    private static final String W3C_EDC = "https://w3id.org/edc/v0.0.1/ns/";
    @JsonIgnore
    private static final String W3C_TRACTUSX = "https://w3id.org/tractusx/auth/";

    /** ATTRIBUTES **/
    @JsonProperty("id")
    @JsonAlias({"id", "edc:id"})
    String id;
    @JsonProperty("at")
    @JsonAlias({"at", "edc:at"})
    Long at;

    @JsonProperty("type")
    @JsonAlias({"type", "edc:type"})
    String type;

    @JsonProperty("payload")
    @JsonAlias({"payload", "edc:payload"})
    Payload payload;

    /** INNER CLASSES **/

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Jacksonized
    public static class Payload {
        @JsonProperty("transferProcessId")
        @JsonAlias({"transferProcessId", "edc:transferProcessId"})
        String transferProcessId;
        @JsonProperty("callbackAddresses")
        @JsonAlias({"callbackAddresses", "edc:callbackAddresses"})
        List<CallbackAddress> callbackAddresses;

        @JsonProperty("assetId")
        @JsonAlias({"assetId", "edc:assetId"})
        String assetId;

        @JsonProperty("type")
        @JsonAlias({"type", "edc:type"})
        String type;

        @JsonProperty("contractId")
        @JsonAlias({"contractId", "edc:contractId"})
        String contractId;

        @JsonProperty("dataAddress")
        @JsonAlias({"dataAddress", "edc:dataAddress"})
        DataAddress dataAddress;
    }
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Jacksonized
    public static class DataAddress{
        @JsonProperty("properties")
        @JsonAlias({"properties", "edc:properties"})
        Properties properties;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Jacksonized
    public static class Properties{
        @JsonProperty("processId")
        @JsonAlias({"process_id", "edc:process_id"})
        String processId;
        @JsonProperty("participantId")
        @JsonAlias({"participant_id", "edc:participant_id"})
        String participantId;

        @JsonProperty("assetId")
        @JsonAlias({"asset_id", "edc:asset_id"})
        String assetId;

        @JsonProperty("endpointType")
        @JsonAlias({W3C_EDC+"endpointType", "endpointType", "edc:endpointType"})
        String endpointType;

        @JsonProperty("refreshEndpoint")
        @JsonAlias({W3C_TRACTUSX+"refreshEndpoint", "refreshEndpoint", "edc:refreshEndpoint"})
        String refreshEndpoint;

        @JsonProperty("audience")
        @JsonAlias({W3C_TRACTUSX+"audience", "audience", "edc:audience"})
        String audience;

        @JsonProperty("agreementId")
        @JsonAlias({"agreement_id", "edc:agreement_id"})
        String agreement_id;

        @JsonProperty("flowType")
        @JsonAlias({"flow_type", "edc:flow_type"})
        String flow_type;

        @JsonProperty("type")
        @JsonAlias({W3C_EDC+"type", "type", "edc:type"})
        String type;

        @JsonProperty("endpoint")
        @JsonAlias({W3C_EDC+"endpoint", "endpoint", "edc:endpoint"})
        String endpoint;
        @JsonProperty("refreshToken")
        @JsonAlias({W3C_TRACTUSX+"refreshToken", "refreshToken", "edc:refreshToken"})
        String refreshToken;

        @JsonProperty("expiresIn")
        @JsonAlias({W3C_TRACTUSX+"expiresIn", "expiresIn", "edc:expiresIn"})
        String expiresIn;

        @JsonProperty("authorization")
        @JsonAlias({W3C_EDC+"authorization", "authorization", "edc:authorization"})
        String authorization;

        @JsonProperty("refreshAudience")
        @JsonAlias({W3C_TRACTUSX+"refreshAudience", "refreshAudience", "edc:refreshAudience"})
        String refreshAudience;
    }
}
