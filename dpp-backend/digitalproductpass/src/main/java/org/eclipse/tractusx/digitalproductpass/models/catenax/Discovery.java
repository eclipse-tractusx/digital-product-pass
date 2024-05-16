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

package org.eclipse.tractusx.digitalproductpass.models.catenax;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * This class consists exclusively to define attributes and methods related to discovering the needed Endpoints.
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Discovery {

    /** ATTRIBUTES **/
    @JsonProperty("endpoints")
    List<Endpoint> endpoints;

    /** CONSTRUCTOR(S) **/
    public Discovery() {
    }
    public Discovery(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    /** GETTERS AND SETTERS **/
    public List<Endpoint> getEndpoints() {
        return endpoints;
    }
    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    /** INNER CLASSES **/

    /**
     * This class consists exclusively to define attributes and methods related to the Endpoints information.
     **/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Endpoint {

        /** ATTRIBUTES **/
        @JsonProperty("type")
        String type;
        @JsonProperty("description")
        String description;
        @JsonProperty("endpointAddress")
        String endpointAddress;
        @JsonProperty("documentation")
        String documentation;
        @JsonProperty("resourceId")
        String resourceId;
        @JsonProperty("timeToLive")
        String timeToLive;

        /** CONSTRUCTOR(S) **/
        public Endpoint(String type, String description, String endpointAddress, String documentation, String resourceId, String timeToLive) {
            this.type = type;
            this.description = description;
            this.endpointAddress = endpointAddress;
            this.documentation = documentation;
            this.resourceId = resourceId;
            this.timeToLive = timeToLive;
        }
        public Endpoint() {
        }

        /** GETTERS AND SETTERS **/
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public String getEndpointAddress() {
            return endpointAddress;
        }
        @SuppressWarnings("Unused")
        public void setEndpointAddress(String endpointAddress) {
            this.endpointAddress = endpointAddress;
        }
        @SuppressWarnings("Unused")
        public String getDocumentation() {
            return documentation;
        }
        @SuppressWarnings("Unused")
        public void setDocumentation(String documentation) {
            this.documentation = documentation;
        }
        @SuppressWarnings("Unused")
        public String getResourceId() {
            return resourceId;
        }
        @SuppressWarnings("Unused")
        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }
        @SuppressWarnings("Unused")
        public String getTimeToLive() {
            return timeToLive;
        }
        @SuppressWarnings("Unused")
        public void setTimeToLive(String timeToLive) {
            this.timeToLive = timeToLive;
        }
    }
}
