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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class consists exclusively to define the attributes and methods needed for the Wallet configuration.
 **/
@Configuration
@ConfigurationProperties(prefix = "configuration.verification")
public class VerificationConfig {

    /**
     * ATTRIBUTES
     **/

    Boolean enabled;
    Boolean autoVerify;
    WalletConfig wallet;

    /**
     * CONSTRUCTOR(S)
     **/

    public VerificationConfig(Boolean enabled, Boolean autoVerify, WalletConfig wallet) {
        this.enabled = enabled;
        this.autoVerify = autoVerify;
        this.wallet = wallet;
    }

    public VerificationConfig() {
    }

    /**
     * GETTERS AND SETTERS
     **/
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getAutoVerify() {
        return autoVerify;
    }

    public void setAutoVerify(Boolean autoVerify) {
        this.autoVerify = autoVerify;
    }

    public WalletConfig getWallet() {
        return wallet;
    }

    public void setWallet(WalletConfig wallet) {
        this.wallet = wallet;
    }

}