/*********************************************************************************
 *
 * Tractus-X - Digital Product Pass Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2024 CGI Deutschland B.V. & Co. KG
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

package org.eclipse.tractusx.digitalproductpass.verification.config;

import org.springframework.context.annotation.Configuration;

/**
 * This class consists exclusively to define the attributes and methods needed for the Wallet configuration.
 **/
@Configuration
public class WalletConfig {

    /**
     * ATTRIBUTES
     **/
    String url;
    Endpoints endpoints;

    /**
     * CONSTRUCTOR(S)
     **/
    public WalletConfig(String url, Endpoints endpoints) {
        this.url = url;
        this.endpoints = endpoints;
    }

    public WalletConfig() {
    }

    /**
     * GETTERS AND SETTERS
     **/
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Endpoints getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(Endpoints endpoints) {
        this.endpoints = endpoints;
    }
    /** INNER CLASSES **/

    /**
     * This class consists exclusively to define the attributes and methods needed for the wallet endpoints configuration.
     **/
    public static class Endpoints {
        String health;
        String verify;

        public Endpoints(String health, String verify) {
            this.health = health;
            this.verify = verify;
        }

        public Endpoints() {
        }

        public String getHealth() {
            return health;
        }

        public void setHealth(String health) {
            this.health = health;
        }

        public String getVerify() {
            return verify;
        }

        public void setVerify(String verify) {
            this.verify = verify;
        }
    }

}