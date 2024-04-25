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


package org.eclipse.tractusx.digitalproductpass.models.bpn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.tractusx.digitalproductpass.config.BpdmConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ModelException;
import utils.JsonUtil;

import java.util.Map;

/**
 * This class consists exclusively to define attributes and methods related to the BDPM Service properties for company
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyInfo extends Info{

    /**
     * ATTRIBUTES
     **/
    @JsonProperty("name")
    private String name;

    private BpdmConfig.CompanyConfig config;

    /** CONSTRUCTOR(S) **/
    public CompanyInfo(Map<String,Object> data, BpdmConfig.CompanyConfig config) {
        // Build the address using the information from data in the main property
        super(data);
        this.config = config;
        this.name = unpackValue(config.getName(), new TypeReference<>(){});
    }


    public CompanyInfo(String name, BpdmConfig.CompanyConfig config) {
        this.name = name;
        this.config = config;
    }

    public CompanyInfo(Map<String, Object> data, String name, BpdmConfig.CompanyConfig config) {
        super(data);
        this.name = name;
        this.config = config;
    }

    public CompanyInfo() {
    }

    /** GETTERS AND SETTERS **/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BpdmConfig.CompanyConfig getConfig() {
        return config;
    }

    public void setConfig(BpdmConfig.CompanyConfig config) {
        this.config = config;
    }
    public Boolean isEmpty(){
        return this.name != null;
    }
}