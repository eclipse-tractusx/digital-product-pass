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
 * This class consists exclusively to define the attributes and methods needed for the IRS configuration.
 **/
@Configuration
@ConfigurationProperties(prefix="configuration.bpdm")
public class BpdmConfig {

    /** ATTRIBUTES **/
    Boolean enabled;
    String endpoint;
    CompanyConfig companyInfo;
    AddressConfig site;
    AddressConfig legalEntity;
    AddressConfig address;

    /** CONSTRUCTOR(S) **/
    public BpdmConfig() {
    }


    public BpdmConfig(Boolean enabled, String endpoint, CompanyConfig companyInfo, AddressConfig site, AddressConfig legalEntity, AddressConfig address) {
        this.enabled = enabled;
        this.endpoint = endpoint;
        this.companyInfo = companyInfo;
        this.site = site;
        this.legalEntity = legalEntity;
        this.address = address;
    }


    /** GETTERS AND SETTERS **/

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }


    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public AddressConfig getSite() {
        return site;
    }

    public void setSite(AddressConfig site) {
        this.site = site;
    }

    public AddressConfig getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(AddressConfig legalEntity) {
        this.legalEntity = legalEntity;
    }

    public AddressConfig getAddress() {
        return address;
    }

    public void setAddress(AddressConfig address) {
        this.address = address;
    }

    public CompanyConfig getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyConfig companyInfo) {
        this.companyInfo = companyInfo;
    }

    /** INNER CLASSES **/


    /**
     * This class consists in a generic structure which is required to identify an entity (company or address)
     **/
    public static class EntityConfig {

        String apiPath;
        String bpnKey;


        public EntityConfig(String apiPath, String bpnKey) {
            this.apiPath = apiPath;
            this.bpnKey = bpnKey;
        }

        public EntityConfig() {
        }

        public String getApiPath() {
            return apiPath;
        }

        public void setApiPath(String apiPath) {
            this.apiPath = apiPath;
        }

        public String getBpnKey() {
            return bpnKey;
        }

        public void setBpnKey(String bpnKey) {
            this.bpnKey = bpnKey;
        }
    }


    /**
     * This class consists exclusively to define the attributes and methods needed for the bpdm company configuration.
     **/
    public static class CompanyConfig extends EntityConfig{

        AttributeConfig name;

        public CompanyConfig(String apiPath, String bpnKey, AttributeConfig name) {
            super(apiPath, bpnKey);
            this.name = name;
        }

        public CompanyConfig(AttributeConfig name) {
            this.name = name;
        }

        public CompanyConfig() {
        }


        public AttributeConfig getName() {
            return name;
        }

        public void setName(AttributeConfig name) {
            this.name = name;
        }
    }

    /**
     * This class consists exclusively to define the attributes and methods needed for the bpdm address configuration.
     **/
    public static class AddressConfig extends EntityConfig{

        String countryPath;
        AttributeConfig postalCode;
        AttributeConfig city;
        AttributeConfig street;

        public AddressConfig() {
        }

        public AddressConfig(String apiPath, String bpnKey, String countryPath, AttributeConfig postalCode, AttributeConfig city, AttributeConfig street) {
            super(apiPath, bpnKey);
            this.countryPath = countryPath;
            this.postalCode = postalCode;
            this.city = city;
            this.street = street;
        }

        public AddressConfig(String countryPath, AttributeConfig postalCode, AttributeConfig city, AttributeConfig street) {
            this.countryPath = countryPath;
            this.postalCode = postalCode;
            this.city = city;
            this.street = street;
        }

        public String getCountryPath() {
            return countryPath;
        }

        public void setCountryPath(String countryPath) {
            this.countryPath = countryPath;
        }

        public AttributeConfig getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(AttributeConfig postalCode) {
            this.postalCode = postalCode;
        }

        public AttributeConfig getCity() {
            return city;
        }

        public void setCity(AttributeConfig city) {
            this.city = city;
        }

        public AttributeConfig getStreet() {
            return street;
        }

        public void setStreet(AttributeConfig street) {
            this.street = street;
        }

    }

    /**
     * This class consists exclusively to define the attributes and methods needed for the bpdm address attribute configuration.
     **/
    public static class AttributeConfig{
        String path;
        List<String> technicalKeys;

        public AttributeConfig(String path, List<String> technicalKeys) {
            this.path = path;
            this.technicalKeys = technicalKeys;
        }

        public AttributeConfig() {
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public List<String> getTechnicalKeys() {
            return technicalKeys;
        }

        public void setTechnicalKeys(List<String> technicalKeys) {
            this.technicalKeys = technicalKeys;
        }
    }

}
