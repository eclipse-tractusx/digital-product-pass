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

package org.eclipse.tractusx.digitalproductpass.models.dtregistry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Splitter;
import utils.LogUtil;

import java.util.List;
import java.util.Map;

/**
 * This class consists exclusively to define attributes related to the designed model of the Endpoint's improved version.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EndPoint {

    /** ATTRIBUTES **/
    @JsonProperty("interface")
    String interfaceName;
    @JsonProperty("protocolInformation")
    ProtocolInformation3 protocolInformation = new ProtocolInformation3();

    /** GETTERS AND SETTERS **/
    public String getInterfaceName() {
        return interfaceName;
    }
    @SuppressWarnings("Unused")
    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }
    public ProtocolInformation3 getProtocolInformation() {
        return protocolInformation;
    }
    @SuppressWarnings("Unused")
    public void setProtocolInformation(String endpointAddress, String endpointProtocol, List<String> endpointProtocolVersion) {
        this.protocolInformation = new ProtocolInformation3(endpointAddress, endpointProtocol, endpointProtocolVersion);
    }

    /** INNER CLASSES **/
    /**
     * This class consists exclusively to define attributes related to the needed protocol information for the improved version.
     **/
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProtocolInformation3 {

        /** ATTRIBUTES **/
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

        /** CONSTRUCTOR(S) **/
        public ProtocolInformation3(String endpointAddress, String endpointProtocol, List<String> endpointProtocolVersion) {
            this.endpointAddress = endpointAddress;
            this.endpointProtocol = endpointProtocol;
            this.endpointProtocolVersion = endpointProtocolVersion;
        }
        public ProtocolInformation3() {
        }
        @SuppressWarnings("Unused")
        public ProtocolInformation3(String endpointAddress, String endpointProtocol, String subprotocol, String subprotocolBody, List<String> endpointProtocolVersion) {
            this.endpointAddress = endpointAddress;
            this.endpointProtocol = endpointProtocol;
            this.subprotocol = subprotocol;
            this.subprotocolBody = subprotocolBody;
            this.endpointProtocolVersion = endpointProtocolVersion;
        }
        @SuppressWarnings("Unused")
        public ProtocolInformation3(String endpointAddress, String endpointProtocol, String subprotocol, String subprotocolBody, String subprotocolBodyEncoding, List<String> endpointProtocolVersion) {
            this.endpointAddress = endpointAddress;
            this.endpointProtocol = endpointProtocol;
            this.subprotocol = subprotocol;
            this.subprotocolBody = subprotocolBody;
            this.subprotocolBodyEncoding = subprotocolBodyEncoding;
            this.endpointProtocolVersion = endpointProtocolVersion;
        }
        @SuppressWarnings("Unused")
        public ProtocolInformation3(String endpointAddress, String endpointProtocol, String subprotocol, String subprotocolBody, String subprotocolBodyEncoding, Object securityAttributes, List<String> endpointProtocolVersion) {
            this.endpointAddress = endpointAddress;
            this.endpointProtocol = endpointProtocol;
            this.subprotocol = subprotocol;
            this.subprotocolBody = subprotocolBody;
            this.subprotocolBodyEncoding = subprotocolBodyEncoding;
            this.securityAttributes = securityAttributes;
            this.endpointProtocolVersion = endpointProtocolVersion;
        }

        /** GETTERS AND SETTERS **/
        @SuppressWarnings("Unused")
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
        public List<String> getEndpointProtocolVersion() {
            return endpointProtocolVersion;
        }
        @SuppressWarnings("Unused")
        public void setEndpointProtocolVersion(List<String> endpointProtocolVersion) {
            this.endpointProtocolVersion = endpointProtocolVersion;
        }
        @SuppressWarnings("Unused")
        public String getSubprotocol() {
            return subprotocol;
        }
        @SuppressWarnings("Unused")
        public void setSubprotocol(String subprotocol) {
            this.subprotocol = subprotocol;
        }
        @SuppressWarnings("Unused")
        public String getSubprotocolBody() {
            return subprotocolBody;
        }
        public Map<String, String> parseSubProtocolBody() {
            try {
                return Splitter.on(';').withKeyValueSeparator('=').split(this.subprotocolBody);
            }catch (Exception e){
                LogUtil.printException(e, "Error when parsing the subprotocol body params!");
                return null;
            }
        }
        @SuppressWarnings("Unused")
        public void setSubprotocolBody(String subprotocolBody) {
            this.subprotocolBody = subprotocolBody;
        }
        @SuppressWarnings("Unused")
        public String getSubprotocolBodyEncoding() {
            return subprotocolBodyEncoding;
        }
        @SuppressWarnings("Unused")
        public void setSubprotocolBodyEncoding(String subprotocolBodyEncoding) {
            this.subprotocolBodyEncoding = subprotocolBodyEncoding;
        }
        @SuppressWarnings("Unused")
        public Object getSecurityAttributes() {
            return securityAttributes;
        }
        @SuppressWarnings("Unused")
        public void setSecurityAttributes(Object securityAttributes) {
            this.securityAttributes = securityAttributes;
        }
    }
}
