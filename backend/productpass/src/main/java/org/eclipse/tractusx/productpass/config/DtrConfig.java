/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2023 Contributors to the CatenaX (ng) GitHub Organisation.
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

package org.eclipse.tractusx.productpass.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.sql.Time;
import java.util.Map;

/**
 * This class consists exclusively to define the attributes and methods needed for the Digital Twin Registry (DTR) configuration.
 **/
@Configuration
@ConfigurationProperties(prefix="configuration.dtr")
public class DtrConfig {

    /** ATTRIBUTES **/
    Timeouts timeouts;
    TemporaryStorage temporaryStorage;
    DecentralApis decentralApis;
    String assetType;
    String endpointInterface;
    String dspEndpointKey;

    String semanticIdTypeKey;
    /** CONSTRUCTOR(S) **/
    public DtrConfig() {
    }

    public DtrConfig(Timeouts timeouts, TemporaryStorage temporaryStorage, DecentralApis decentralApis, String assetType, String endpointInterface, String dspEndpointKey, String semanticIdTypeKey) {
        this.timeouts = timeouts;
        this.temporaryStorage = temporaryStorage;
        this.decentralApis = decentralApis;
        this.assetType = assetType;
        this.endpointInterface = endpointInterface;
        this.dspEndpointKey = dspEndpointKey;
        this.semanticIdTypeKey = semanticIdTypeKey;
    }


    /** GETTERS AND SETTERS **/
    public DecentralApis getDecentralApis() {
        return decentralApis;
    }
    public void setDecentralApis(DecentralApis decentralApis) {
        this.decentralApis = decentralApis;
    }
    public Timeouts getTimeouts() {
        return timeouts;
    }
    public void setTimeouts(Timeouts timeouts) {
        this.timeouts = timeouts;
    }
    public String getEndpointInterface() {
        return endpointInterface;
    }
    public void setEndpointInterface(String endpointInterface) {
        this.endpointInterface = endpointInterface;
    }
    public String getDspEndpointKey() {
        return dspEndpointKey;
    }
    public void setDspEndpointKey(String dspEndpointKey) {
        this.dspEndpointKey = dspEndpointKey;
    }
    public String getAssetType() {
        return assetType;
    }
    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getSemanticIdTypeKey() {
        return semanticIdTypeKey;
    }

    public void setSemanticIdTypeKey(String semanticIdTypeKey) {
        this.semanticIdTypeKey = semanticIdTypeKey;
    }

    public TemporaryStorage getTemporaryStorage() {
        return temporaryStorage;
    }

    public void setTemporaryStorage(TemporaryStorage temporaryStorage) {
        this.temporaryStorage = temporaryStorage;
    }

    /** INNER CLASSES **/

    /**
     * This class consists exclusively to define the attributes and methods needed for the DTR's timeouts configuration.
     **/

    public static class TemporaryStorage{

        /** ATTRIBUTES **/
        Boolean enabled;

        Integer lifetime;

        public TemporaryStorage(Boolean enabled, Integer lifetime) {
            this.enabled = enabled;
            this.lifetime = lifetime;
        }

        /** GETTERS AND SETTERS **/
        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public Integer getLifetime() {
            return lifetime;
        }

        public void setLifetime(Integer lifetime) {
            this.lifetime = lifetime;
        }
    }
    public static class Timeouts{

        /** ATTRIBUTES **/
        Integer search;
        Integer negotiation;
        Integer transfer;
        Integer digitalTwin;
        Integer dtrRequestProcess;

        /** GETTERS AND SETTERS **/
        public Integer getSearch() {
            return search;
        }
        public void setSearch(Integer search) {
            this.search = search;
        }
        public Integer getNegotiation() {
            return negotiation;
        }
        public void setNegotiation(Integer negotiation) {
            this.negotiation = negotiation;
        }
        public Integer getTransfer() {
            return transfer;
        }
        public void setTransfer(Integer transfer) {
            this.transfer = transfer;
        }
        public Integer getDigitalTwin() {
            return digitalTwin;
        }
        @SuppressWarnings("Unused")
        public void setDigitalTwin(Integer digitalTwin) {
            this.digitalTwin = digitalTwin;
        }

        public Integer getDtrRequestProcess() {
            return dtrRequestProcess;
        }

        public void setDtrRequestProcess(Integer dtrRequestProcess) {
            this.dtrRequestProcess = dtrRequestProcess;
        }
    }

    /**
     * This class consists exclusively to define the attributes and methods needed for the decentralize APIs configuration.
     **/
    public static class DecentralApis{

        /** ATTRIBUTES **/
        String prefix;
        String search;
        String digitalTwin;
        String subModel;

        /** CONSTRUCTOR(S) **/
        @SuppressWarnings("Unused")
        public DecentralApis(String prefix, String search, String digitalTwin, String subModel) {
            this.prefix = prefix;
            this.search = search;
            this.digitalTwin = digitalTwin;
            this.subModel = subModel;
        }

        @SuppressWarnings("Unused")
        public DecentralApis() {
        }

        /** GETTERS AND SETTERS **/
        public String getPrefix() {
            return prefix;
        }
        @SuppressWarnings("Unused")
        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }
        public String getSearch() {
            return search;
        }
        public void setSearch(String search) {
            this.search = search;
        }
        @SuppressWarnings("Unused")
        public String getDigitalTwin() {
            return digitalTwin;
        }
        @SuppressWarnings("Unused")
        public void setDigitalTwin(String digitalTwin) {
            this.digitalTwin = digitalTwin;
        }
        @SuppressWarnings("Unused")
        public String getSubModel() {
            return subModel;
        }
        @SuppressWarnings("Unused")
        public void setSubModel(String subModel) {
            this.subModel = subModel;
        }
    }
}
