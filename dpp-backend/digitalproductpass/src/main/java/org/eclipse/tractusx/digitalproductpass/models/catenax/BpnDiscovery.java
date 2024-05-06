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
import java.util.stream.Collectors;

/**
 * This class consists exclusively to define attributes and methods related to discovering the BPN numbers.
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BpnDiscovery {

    /** ATTRIBUTES **/
    @JsonProperty("bpns")
    List<BpnEndpoint> bpns;

    /** CONSTRUCTOR(S) **/
    @SuppressWarnings("Unused")
    public BpnDiscovery(List<BpnEndpoint> bpns) {
        this.bpns = bpns;
    }
    @SuppressWarnings("Unused")
    public BpnDiscovery() {
    }

    /** GETTERS AND SETTERS **/
    @SuppressWarnings("Unused")
    public List<BpnEndpoint> getBpns() {
        return bpns;
    }
    @SuppressWarnings("Unused")
    public void setBpns(List<BpnEndpoint> bpns) {
        this.bpns = bpns;
    }

    /** METHODS **/
    public List<String> mapBpnNumbers(){
        return bpns.stream().map(
                BpnEndpoint::getValue
        ).collect(Collectors.toList());
    }

    /** INNER CLASSES **/
    /**
     * This class consists exclusively to define attributes and methods related to BPN endpoints.
     **/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BpnEndpoint {

        /** ATTRIBUTES **/
        @JsonProperty("type")
        String type;
        @JsonProperty("key")
        String key;
        @JsonProperty("value")
        String value;
        @JsonProperty("resourceId")
        String resourceId;

        /** CONSTRUCTOR(S) **/
        @SuppressWarnings("Unused")
        public BpnEndpoint(String type, String key, String value, String resourceId) {
            this.type = type;
            this.key = key;
            this.value = value;
            this.resourceId = resourceId;
        }
        @SuppressWarnings("Unused")
        public BpnEndpoint() {
        }

        /** GETTERS AND SETTERS **/
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
        @SuppressWarnings("Unused")
        public String getResourceId() {
            return resourceId;
        }
        @SuppressWarnings("Unused")
        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }
    }
}
