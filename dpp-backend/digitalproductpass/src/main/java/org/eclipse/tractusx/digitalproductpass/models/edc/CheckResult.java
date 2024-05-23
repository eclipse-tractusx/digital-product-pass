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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * This class consists exclusively to define attributes related to the health check result from the EDC
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckResult {

    /** ATTRIBUTES **/
    @JsonProperty("componentResults")
    List<ComponentResult> componentResults;
    @JsonProperty("isSystemHealthy")
    Boolean isSystemHealthy;

    /** CONSTRUCTOR(S) **/
    public CheckResult(List<ComponentResult> componentResults, Boolean isSystemHealthy) {
        this.componentResults = componentResults;
        this.isSystemHealthy = isSystemHealthy;
    }

    public CheckResult() {
    }

    /** GETTERS AND SETTERS **/
    public List<ComponentResult> getComponentResults() {
        return componentResults;
    }

    public void setComponentResults(List<ComponentResult> componentResults) {
        this.componentResults = componentResults;
    }

    public Boolean getSystemHealthy() {
        return isSystemHealthy;
    }

    public void setSystemHealthy(Boolean systemHealthy) {
        isSystemHealthy = systemHealthy;
    }

}
