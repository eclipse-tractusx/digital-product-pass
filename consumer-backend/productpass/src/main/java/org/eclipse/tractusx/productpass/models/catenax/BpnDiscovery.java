/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2023 Contributors to the CatenaX (ng) GitHub Organisation.
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

package org.eclipse.tractusx.productpass.models.catenax;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BpnDiscovery {
    @JsonProperty("bpns")
    List<BpnEndpoint> bpns;

    public BpnDiscovery(List<BpnEndpoint> bpns) {
        this.bpns = bpns;
    }

    public BpnDiscovery() {
    }

    public List<BpnEndpoint> getBpns() {
        return bpns;
    }

    public List<String> getBpnNumbers(){
        return bpns.stream().map(
                BpnEndpoint::getValue
        ).collect(Collectors.toList());
    }


    public void setBpns(List<BpnEndpoint> bpns) {
        this.bpns = bpns;
    }

    public static class BpnEndpoint {
        @JsonProperty("type")
        String type;
        @JsonProperty("key")
        String key;
        @JsonProperty("value")
        String value;
        @JsonProperty("resourceId")
        String resourceId;

        public BpnEndpoint(String type, String key, String value, String resourceId) {
            this.type = type;
            this.key = key;
            this.value = value;
            this.resourceId = resourceId;
        }

        public BpnEndpoint() {
        }

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

        public String getResourceId() {
            return resourceId;
        }

        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }
    }
}
