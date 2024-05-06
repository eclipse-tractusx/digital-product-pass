/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
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

package org.eclipse.tractusx.digitalproductpass.models.edc;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DidDocument;

/**
 * This class consists exclusively to define attributes related to Endpoint Data Reference (EDR) received from the EDC.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EndpointDataReference extends DidDocument {

    /** ATTRIBUTES **/
    @JsonProperty("id")
    @JsonAlias({"id", "edc:id"})
    String id;
    @JsonProperty("type")
    @JsonAlias({"type", "edc:type"})
    String edrType;
    @JsonProperty("endpoint")
    @JsonAlias({"endpoint", "edc:endpoint"})
    String endpoint;
    @JsonProperty("properties")
    @JsonAlias({"properties", "edc:properties"})
    Properties properties;
    @JsonProperty("contractId")
    @JsonAlias({"contractId", "edc:contractId"})
    String contractId;
    @JsonProperty("authKey")
    @JsonAlias({"authKey", "edc:authKey"})
    String authKey;
    @JsonProperty("authCode")
    @JsonAlias({"authCode", "edc:authCode"})
    String authCode;

    /** CONSTRUCTOR(S) **/
    @SuppressWarnings("Unused")
    public EndpointDataReference(String id, String endpoint, String authKey, String authCode) {
        this.id = id;
        this.endpoint = endpoint;
        this.authKey = authKey;
        this.authCode = authCode;
    }
    @SuppressWarnings("Unused")
    public EndpointDataReference() {
    }

    public EndpointDataReference(String id, String type, String id1, String edrType, String endpoint, String contractId, String authKey, String authCode) {
        super(id, type);
        this.id = id1;
        this.edrType = edrType;
        this.endpoint = endpoint;
        this.contractId = contractId;
        this.authKey = authKey;
        this.authCode = authCode;
    }

    public EndpointDataReference(JsonNode context, String id, String edrType, String endpoint, Properties properties, String contractId, String authKey, String authCode) {
        super(context);
        this.id = id;
        this.edrType = edrType;
        this.endpoint = endpoint;
        this.properties = properties;
        this.contractId = contractId;
        this.authKey = authKey;
        this.authCode = authCode;
    }

    public EndpointDataReference(String id, String edrType, String endpoint, String contractId, String authKey, String authCode) {
        this.id = id;
        this.edrType = edrType;
        this.endpoint = endpoint;
        this.contractId = contractId;
        this.authKey = authKey;
        this.authCode = authCode;
    }

    public EndpointDataReference(String id, String type, JsonNode context, String id1, String edrType, String endpoint, String contractId, String authKey, String authCode) {
        super(id, type, context);
        this.id = id1;
        this.edrType = edrType;
        this.endpoint = endpoint;
        this.contractId = contractId;
        this.authKey = authKey;
        this.authCode = authCode;
    }

    public EndpointDataReference(String type, String id, String edrType, String endpoint, String contractId, String authKey, String authCode) {
        super(type);
        this.id = id;
        this.edrType = edrType;
        this.endpoint = endpoint;
        this.contractId = contractId;
        this.authKey = authKey;
        this.authCode = authCode;
    }

    public EndpointDataReference(String id, String type, JsonNode context, String id1, String edrType, String endpoint, Properties properties, String contractId, String authKey, String authCode) {
        super(id, type, context);
        this.id = id1;
        this.edrType = edrType;
        this.endpoint = endpoint;
        this.properties = properties;
        this.contractId = contractId;
        this.authKey = authKey;
        this.authCode = authCode;
    }

    public EndpointDataReference(JsonNode context, String id, String edrType, String endpoint, String contractId, String authKey, String authCode) {
        super(context);
        this.id = id;
        this.edrType = edrType;
        this.endpoint = endpoint;
        this.contractId = contractId;
        this.authKey = authKey;
        this.authCode = authCode;
    }

    public EndpointDataReference(String id, String type, String id1, String edrType, String endpoint, Properties properties, String contractId, String authKey, String authCode) {
        super(id, type);
        this.id = id1;
        this.edrType = edrType;
        this.endpoint = endpoint;
        this.properties = properties;
        this.contractId = contractId;
        this.authKey = authKey;
        this.authCode = authCode;
    }


    /** GETTERS AND SETTERS **/
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
    @SuppressWarnings("Unused")
    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }
    public String getAuthCode() {
        return authCode;
    }
    @SuppressWarnings("Unused")
    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getEdrType() {
        return edrType;
    }

    public void setEdrType(String edrType) {
        this.edrType = edrType;
    }

    /** METHODS **/
    /**
     * Checks if offerId exists in the properties attribute.
     * <p>
     *
     * @return  true if the offerId exists, false otherwise.
     *
     */
    /** INNER CLASSES **/
    /**
     * This class consists exclusively to define attributes related to the Asset search's properties.
     **/
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class Properties {
        @JsonProperty("https://w3id.org/edc/v0.0.1/ns/cid")
        String offerId;
    }
    public Boolean offerIdExists(){
        try {
            return this.properties != null && this.properties.offerId != null;
        }catch (Exception e){
            // Do nothing because is non-existent the offer id
        }
        return false;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
