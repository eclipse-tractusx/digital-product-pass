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

package org.eclipse.tractusx.productpass.models.edc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataPlaneEndpoint {
    @JsonProperty("id")
    String id;

    @JsonProperty("endpoint")
    String endpoint;

    @JsonProperty("authKey")
    String authKey;

    @JsonProperty("authCode")
    String authCode;

    @JsonProperty("properties")
    Properties properties;

    public DataPlaneEndpoint(String id, String endpoint, String authKey, String authCode, Properties properties) {
        this.id = id;
        this.endpoint = endpoint;
        this.authKey = authKey;
        this.authCode = authCode;
        this.properties = properties;
    }

    public DataPlaneEndpoint() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void setOfferId(String offerId) {
        this.properties.offerId = offerId;
    }

    public String getOfferId() {
        return this.properties.offerId;
    }

    public Boolean offerIdExists(){
        try {
            return this.properties != null && this.properties.offerId != null;
        }catch (Exception e){
            // Do nothing because is non-existent the offer id
        }
        return false;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class Properties {
        @JsonProperty("https://w3id.org/edc/v0.0.1/ns/cid")
        String offerId;

    }


}
