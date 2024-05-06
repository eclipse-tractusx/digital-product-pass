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

package org.eclipse.tractusx.digitalproductpass.models.passports;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

/**
 * This class consists exclusively to define attributes and methods needed for the Passport response given to the consumer
 * with metadata from the transaction and the passport.
 **/
public class PassportResponse {
    @JsonProperty("metadata")
    Map<String, Object> metadata;
    @JsonProperty("passport")
    JsonNode passport;

    @SuppressWarnings("Unused")
    public PassportResponse(Map<String, Object> metadata, JsonNode passport) {
        this.metadata = metadata;
        this.passport = passport;
    }
    @SuppressWarnings("Unused")
    public PassportResponse() {
    }
}
