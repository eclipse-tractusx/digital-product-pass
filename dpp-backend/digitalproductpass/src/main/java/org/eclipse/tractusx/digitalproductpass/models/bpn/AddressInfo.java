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
import org.sonarsource.scanner.api.internal.shaded.minimaljson.Json;
import org.springframework.beans.factory.annotation.Autowired;
import utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class consists exclusively to define attributes and methods related to the BDPM Service properties for company
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressInfo extends Info {

    /**
     * ATTRIBUTES
     **/
    @JsonProperty("country")
    private String country;
    @JsonProperty("city")
    private String city;
    @JsonProperty("street")
    private String street;
    @JsonProperty("postalCode")
    private String postalCode;

    private BpdmConfig.AddressConfig config;


    /** CONSTRUCTOR(S) **/
    public AddressInfo(String country, String city, String street, String postalCode) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.postalCode = postalCode;
    }
    public AddressInfo(Map<String,Object> addressInfo, BpdmConfig.AddressConfig config) {
        // Build the address using the information from data in the main property
        super(addressInfo);
        this.config = config;
        this.street = unpackValue(config.getStreet(), new TypeReference<>(){});
        this.postalCode = unpackValue(config.getPostalCode(), new TypeReference<>(){});
        this.city = unpackValue(config.getCity(), new TypeReference<>(){});
        this.country = unpackCountry();
    }

    public AddressInfo() {
    }

    /** METHODS **/
    private String unpackCountry(){
        // Parse the country name information
        Object rawData = JsonUtil.getValue(this.data, this.config.getCountryPath(), ".", null);
        if(rawData == null){
            throw new ModelException(this.getClass().getName(), "It was not possible to parse the country name!");
        }
        return JsonUtil.bind(rawData, new TypeReference<>() {});
    }
    /** GETTERS AND SETTERS **/

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Boolean isEmpty(){
        return this.country != null && this.street != null && this.postalCode != null && this.city != null;
    }

}