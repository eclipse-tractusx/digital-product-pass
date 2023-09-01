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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Splitter;
import utils.LogUtil;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EndPoint3 {
    @JsonProperty("interface")
    String interfaceName;

    @JsonProperty("protocolInformation")
    ProtocolInformation3 protocolInformation = new ProtocolInformation3();

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public ProtocolInformation3 getProtocolInformation() {
        return protocolInformation;
    }

    public void setProtocolInformation(String endpointAddress, String endpointProtocol, List<String> endpointProtocolVersion) {
        this.protocolInformation = new ProtocolInformation3(endpointAddress, endpointProtocol, endpointProtocolVersion);
    }

    public static class ProtocolInformation3{
        @JsonProperty("href")
        String endpointAddress;

        @JsonProperty("endpointProtocol")
        String endpointProtocol;
        @JsonProperty("subprotocol")
        String subprotocol;
        @JsonProperty("subprotocolBody")
        String subprotocolBody;

        @JsonProperty("subprotocolBodyEncoding")
        String subprotocolBodyEncoding;

        @JsonProperty("securityAttributes")
        Object securityAttributes;

        @JsonProperty("endpointProtocolVersion")
        List<String> endpointProtocolVersion;

        public ProtocolInformation3(String endpointAddress, String endpointProtocol, List<String> endpointProtocolVersion) {
            this.endpointAddress = endpointAddress;
            this.endpointProtocol = endpointProtocol;
            this.endpointProtocolVersion = endpointProtocolVersion;
        }

        public ProtocolInformation3() {
        }

        public ProtocolInformation3(String endpointAddress, String endpointProtocol, String subprotocol, String subprotocolBody, List<String> endpointProtocolVersion) {
            this.endpointAddress = endpointAddress;
            this.endpointProtocol = endpointProtocol;
            this.subprotocol = subprotocol;
            this.subprotocolBody = subprotocolBody;
            this.endpointProtocolVersion = endpointProtocolVersion;
        }

        public ProtocolInformation3(String endpointAddress, String endpointProtocol, String subprotocol, String subprotocolBody, String subprotocolBodyEncoding, List<String> endpointProtocolVersion) {
            this.endpointAddress = endpointAddress;
            this.endpointProtocol = endpointProtocol;
            this.subprotocol = subprotocol;
            this.subprotocolBody = subprotocolBody;
            this.subprotocolBodyEncoding = subprotocolBodyEncoding;
            this.endpointProtocolVersion = endpointProtocolVersion;
        }

        public ProtocolInformation3(String endpointAddress, String endpointProtocol, String subprotocol, String subprotocolBody, String subprotocolBodyEncoding, Object securityAttributes, List<String> endpointProtocolVersion) {
            this.endpointAddress = endpointAddress;
            this.endpointProtocol = endpointProtocol;
            this.subprotocol = subprotocol;
            this.subprotocolBody = subprotocolBody;
            this.subprotocolBodyEncoding = subprotocolBodyEncoding;
            this.securityAttributes = securityAttributes;
            this.endpointProtocolVersion = endpointProtocolVersion;
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

        public List<String> getEndpointProtocolVersion() {
            return endpointProtocolVersion;
        }

        public void setEndpointProtocolVersion(List<String> endpointProtocolVersion) {
            this.endpointProtocolVersion = endpointProtocolVersion;
        }

        public String getSubprotocol() {
            return subprotocol;
        }

        public void setSubprotocol(String subprotocol) {
            this.subprotocol = subprotocol;
        }

        public String getSubprotocolBody() {
            return subprotocolBody;
        }

        public Map<String, String> getParsedSubprotocolBody() {
            try {
                return Splitter.on(';').withKeyValueSeparator('=').split(this.subprotocolBody);
            }catch (Exception e){
                LogUtil.printException(e, "Error when parsing the subprotocol body params!");
                return null;
            }
        }

        public void setSubprotocolBody(String subprotocolBody) {
            this.subprotocolBody = subprotocolBody;
        }

        public String getSubprotocolBodyEncoding() {
            return subprotocolBodyEncoding;
        }

        public void setSubprotocolBodyEncoding(String subprotocolBodyEncoding) {
            this.subprotocolBodyEncoding = subprotocolBodyEncoding;
        }

        public Object getSecurityAttributes() {
            return securityAttributes;
        }

        public void setSecurityAttributes(Object securityAttributes) {
            this.securityAttributes = securityAttributes;
        }
    }
}
