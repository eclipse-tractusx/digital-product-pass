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

@Configuration
@ConfigurationProperties(prefix="configuration.dtr")
public class DtrConfig {
    Boolean central;
    String centralUrl;
    String internalDtr;

    Timeouts timeouts;

    Boolean temporaryStorage;
    DecentralApis decentralApis;
    String assetId;

    String endpointInterface;
    String dspEndpointKey;
    public DecentralApis getDecentralApis() {
        return decentralApis;
    }
    public void setDecentralApis(DecentralApis decentralApis) {
        this.decentralApis = decentralApis;
    }
    public DtrConfig(Boolean central) {
        this.central = central;
    }

    public String getInternalDtr() {
        return internalDtr;
    }

    public void setInternalDtr(String internalDtr) {
        this.internalDtr = internalDtr;
    }


    public Boolean getTemporaryStorage() {
        return temporaryStorage;
    }

    public void setTemporaryStorage(Boolean temporaryStorage) {
        this.temporaryStorage = temporaryStorage;
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

    public static class Timeouts{
        Integer search;
        Integer negotiation;
        Integer transfer;
        Integer digitalTwin;

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

        public void setDigitalTwin(Integer digitalTwin) {
            this.digitalTwin = digitalTwin;
        }
    }

    public static class DecentralApis{
        String prefix;
        String search;
        String digitalTwin;
        String subModel;

        public DecentralApis(String prefix, String search, String digitalTwin, String subModel) {
            this.prefix = prefix;
            this.search = search;
            this.digitalTwin = digitalTwin;
            this.subModel = subModel;
        }

        public DecentralApis() {
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getSearch() {
            return search;
        }

        public void setSearch(String search) {
            this.search = search;
        }

        public String getDigitalTwin() {
            return digitalTwin;
        }

        public void setDigitalTwin(String digitalTwin) {
            this.digitalTwin = digitalTwin;
        }

        public String getSubModel() {
            return subModel;
        }

        public void setSubModel(String subModel) {
            this.subModel = subModel;
        }
    }
    public DtrConfig() {
    }


    public Boolean getCentral() {
        return central;
    }

    public void setCentral(Boolean central) {
        this.central = central;
    }

    public String getCentralUrl() {
        return centralUrl;
    }

    public void setCentralUrl(String centralUrl) {
        this.centralUrl = centralUrl;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }
}
