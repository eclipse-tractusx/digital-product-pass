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

/**
 * This class consists exclusively to define attributes related to the designed model of the Endpoint's first version.
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EndPoint {

    /** ATTRIBUTES **/
    @JsonProperty("interface")
    String interfaceName;
    ProtocolInformation protocolInformation = new ProtocolInformation();

    /** GETTERS AND SETTERS **/
    public String getInterfaceName() {
        return interfaceName;
    }
    @SuppressWarnings("Unused")
    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }
    public ProtocolInformation getProtocolInformation() {
        return protocolInformation;
    }
    @SuppressWarnings("Unused")
    public void setProtocolInformation(String endpointAddress, String endpointProtocol, String endpointProtocolVersion) {
        this.protocolInformation = new ProtocolInformation(endpointAddress, endpointProtocol, endpointProtocolVersion);
    }

    /** INNER CLASSES **/
    /**
     * This class consists exclusively to define attributes related to the needed protocol information for the first version.
     **/
    public static class ProtocolInformation {

        /** ATTRIBUTES **/
        @JsonProperty("endpointAddress")
        String endpointAddress;
        @JsonProperty("endpointProtocol")
        String endpointProtocol;
        @JsonProperty("endpointProtocolVersion")
        String endpointProtocolVersion;

        /** CONSTRUCTOR(S) **/
        public ProtocolInformation(String endpointAddress, String endpointProtocol, String endpointProtocolVersion) {
            this.endpointAddress = endpointAddress;
            this.endpointProtocol = endpointProtocol;
            this.endpointProtocolVersion = endpointProtocolVersion;
        }
        public ProtocolInformation() {
        }

        /** GETTERS AND SETTERS **/
        public String getEndpointAddress() {
            return endpointAddress;
        }
        @SuppressWarnings("Unused")
        public void setEndpointAddress(String endpointAddress) {
            this.endpointAddress = endpointAddress;
        }
        @SuppressWarnings("Unused")
        public String getEndpointProtocol() {
            return endpointProtocol;
        }
        @SuppressWarnings("Unused")
        public void setEndpointProtocol(String endpointProtocol) {
            this.endpointProtocol = endpointProtocol;
        }
        @SuppressWarnings("Unused")
        public String getEndpointProtocolVersion() {
            return endpointProtocolVersion;
        }
        @SuppressWarnings("Unused")
        public void setEndpointProtocolVersion(String endpointProtocolVersion) {
            this.endpointProtocolVersion = endpointProtocolVersion;
        }
    }
}
