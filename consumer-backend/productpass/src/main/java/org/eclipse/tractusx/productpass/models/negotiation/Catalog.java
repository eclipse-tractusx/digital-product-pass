/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

package org.eclipse.tractusx.productpass.models.negotiation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Catalog extends DidDocument {

    @JsonProperty("dcat:dataset")
    List<Dataset> contractOffers;

    @JsonProperty("dcat:service")
    DataService service;

    @JsonProperty("edc:participantId")
    String participantId;

    @JsonProperty("@context")
    JsonNode context;

    @JsonIgnore
    protected Map<String, Integer> contractOffersMap = new HashMap<>();

    public Catalog(String id, String type, List<Dataset> contractOffers, DataService service, String participantId, JsonNode context) {
        super(id, type);
        this.contractOffers = contractOffers;
        this.service = service;
        this.participantId = participantId;
        this.context = context;
    }

    public Catalog(String id, String type) {
        super(id, type);
    }

    public Catalog() {
    }


    public List<Dataset> getContractOffers() {
        return contractOffers;
    }

    public void setContractOffers(List<Dataset> contractOffers) {
        this.contractOffers = contractOffers;
    }

    public Map<String, Integer> loadContractOffersMapByAssetId(){
        int i = 0;
        for(Dataset contractOffer: this.contractOffers){
            this.contractOffersMap.put(contractOffer.getAssetId(),i);
            i++;
        }
        return this.contractOffersMap;
    }
    public Map<String, Integer> getContractOffersMap() {
        return contractOffersMap;
    }

    public void setContractOffersMap(Map<String, Integer> contractOffersMap) {
        this.contractOffersMap = contractOffersMap;
    }

    public DataService getService() {
        return service;
    }

    public void setService(DataService service) {
        this.service = service;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public JsonNode getContext() {
        return context;
    }

    public void setContext(JsonNode context) {
        this.context = context;
    }
}
