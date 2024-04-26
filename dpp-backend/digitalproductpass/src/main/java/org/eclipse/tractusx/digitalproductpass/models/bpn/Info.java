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

import java.util.List;
import java.util.Map;

/**
 * This class consists exclusively to define common attributes and methods related to the BDPM Service properties for company + address
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Info {

    /**
     * ATTRIBUTES
     **/
    public Map<String,Object> data;
    public String bpn;

    /** CONSTRUCTOR(S) **/
    public Info() {
    }

    public Info(Map<String, Object> data) {
        this.data = data;
    }

    public Info(Map<String, Object> data, String bpn) {
        this.data = data;
        this.bpn = bpn;
    }


    /** METHODS **/
    public <T> T unpackValue(BpdmConfig.AttributeConfig attributeConfig, TypeReference<T> typeReference){
        // Parse the street from the thoroughfares property
        Object rawData = JsonUtil.getValue(this.data, attributeConfig.getPath(), ".", null);
        if(rawData == null){
            throw new ModelException(this.getClass().getName(), "It was not possible to parse attribute!");
        }
        List<Map<String, Object>> thoroughFaresList = JsonUtil.bind(rawData, new TypeReference<>() {});
        if(thoroughFaresList == null){
            throw new ModelException(this.getClass().getName(), "It was not possible to parse the attribute list!");
        }
        // If the technical key is street get the first object
        Map<String, Object> streetObj = JsonUtil.unpackPropertyByTag(thoroughFaresList,"type.technicalKey", ".", attributeConfig.getTechnicalKeys());
        T value = JsonUtil.bind(streetObj.get("value"), typeReference);
        if(value == null){
            throw new ModelException(this.getClass().getName(), "It was not possible to parse the selected value");
        }
        return value;
    }
    /** GETTERS AND SETTERS **/

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getBpn() {
        return bpn;
    }

    public void setBpn(String bpn) {
        this.bpn = bpn;
    }
}