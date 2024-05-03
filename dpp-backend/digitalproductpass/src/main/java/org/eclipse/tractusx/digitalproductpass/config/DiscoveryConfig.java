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

package org.eclipse.tractusx.digitalproductpass.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class consists exclusively to define the attributes and methods needed for the Discovery configuration.
 **/
@Configuration
@ConfigurationProperties(prefix="configuration.discovery")
public class DiscoveryConfig {

    /** ATTRIBUTES **/
    String endpoint;
    BPNConfig bpn;
    EDCConfig edc;

    /** GETTERS AND SETTERS **/
    public BPNConfig getBpn() {
        return bpn;
    }
    public void setBpn(BPNConfig bpn) {
        this.bpn = bpn;
    }
    public EDCConfig getEdc() {
        return edc;
    }
    public void setEdc(EDCConfig edc) {
        this.edc = edc;
    }
    public String getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }


    /** INNER CLASSES **/

    /**
     * This class consists exclusively to define the attributes and methods needed for the BPN configuration.
     **/
    public static class BPNConfig {

        /** ATTRIBUTES **/
        String key;
        String searchPath;

        Integer timeout;

        /** GETTERS AND SETTERS **/
        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }
        public String getSearchPath() {
            return searchPath;
        }
        @SuppressWarnings("Unused")
        public void setSearchPath(String searchPath) {
            this.searchPath = searchPath;
        }

        public Integer getTimeout() {
            return timeout;
        }

        public void setTimeout(Integer timeout) {
            this.timeout = timeout;
        }
    }

    /**
     * This class consists exclusively to define the attributes and methods needed for the EDC configuration.
     **/
    public static class EDCConfig {

        /** ATTRIBUTES **/
        String key;

        Integer timeout;
        /** GETTERS AND SETTERS **/
        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }
        public Integer getTimeout() {
            return timeout;
        }

        public void setTimeout(Integer timeout) {
            this.timeout = timeout;
        }
    }

}
