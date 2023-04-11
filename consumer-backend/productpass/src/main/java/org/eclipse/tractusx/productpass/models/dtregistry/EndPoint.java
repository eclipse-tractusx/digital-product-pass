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

package org.eclipse.tractusx.productpass.models.dtregistry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EndPoint {
    @JsonProperty("interface")
    String interfaceName;

    ProtocolInformation protocolInformation = new ProtocolInformation();

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public ProtocolInformation getProtocolInformation() {
        return protocolInformation;
    }

    public void setProtocolInformation(String endpointAddress, String endpointProtocol, String endpointProtocolVersion) {
        this.protocolInformation = new ProtocolInformation(endpointAddress, endpointProtocol, endpointProtocolVersion);
    }

    public static class ProtocolInformation{
        @JsonProperty("endpointAddress")
        String endpointAddress;

        @JsonProperty("endpointProtocol")
        String endpointProtocol;

        @JsonProperty("endpointProtocolVersion")
        String endpointProtocolVersion;

        public ProtocolInformation(String endpointAddress, String endpointProtocol, String endpointProtocolVersion) {
            this.endpointAddress = endpointAddress;
            this.endpointProtocol = endpointProtocol;
            this.endpointProtocolVersion = endpointProtocolVersion;
        }

        public ProtocolInformation() {
        }

        public String getEndpointAddress() {
            return endpointAddress;
        }

        public void setEndpointAddress(String endpointAddress) {
            this.endpointAddress = endpointAddress;
        }

        public String getEndpointProtocol() {
            return endpointProtocol;
        }

        public void setEndpointProtocol(String endpointProtocol) {
            this.endpointProtocol = endpointProtocol;
        }

        public String getEndpointProtocolVersion() {
            return endpointProtocolVersion;
        }

        public void setEndpointProtocolVersion(String endpointProtocolVersion) {
            this.endpointProtocolVersion = endpointProtocolVersion;
        }
    }
}
