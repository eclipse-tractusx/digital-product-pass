/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

import java.util.List;

/**
 * This class consists exclusively to define the attributes and methods needed for the Passport configuration.
 **/
@Configuration
@ConfigurationProperties(prefix="configuration.passport")
public class PassportConfig {
    private String searchIdSchema;

    private List<String> aspects;

    PolicyConfig policyCheck;

    public PassportConfig() {
    }

    public PassportConfig(List<String> aspects) {
        this.aspects = aspects;
    }

    public PassportConfig(String searchIdSchema, List<String> aspects) {
        this.searchIdSchema = searchIdSchema;
        this.aspects = aspects;
    }

    public PassportConfig(String searchIdSchema, List<String> aspects, PolicyConfig policyCheck) {
        this.searchIdSchema = searchIdSchema;
        this.aspects = aspects;
        this.policyCheck = policyCheck;
    }

    public List<String> getAspects() {
        return aspects;
    }
    public void setAspects(List<String> aspects) {
        this.aspects = aspects;
    }


    public String getSearchIdSchema() {
        return searchIdSchema;
    }

    public void setSearchIdSchema(String searchIdSchema) {
        this.searchIdSchema = searchIdSchema;
    }

    public PolicyConfig getPolicyCheck() {
        return policyCheck;
    }

    public void setPolicyCheck(PolicyConfig policyCheck) {
        this.policyCheck = policyCheck;
    }
}
