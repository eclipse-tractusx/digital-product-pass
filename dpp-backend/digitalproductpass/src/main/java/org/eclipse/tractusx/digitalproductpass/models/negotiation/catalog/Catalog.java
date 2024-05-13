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

package org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DataService;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DidDocument;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class consists exclusively to define attributes related to the Catalog's data.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Catalog extends DidDocument {

    /** ATTRIBUTES **/
    @JsonProperty("dcat:dataset")
    Object contractOffers;
    @JsonProperty("dcat:service")
    DataService service;
    @JsonProperty("participantId")
    @JsonAlias({"participantId", "edc:participantId", "dspace:participantId"})
    String participantId;
    @JsonIgnore
    protected Map<String, Integer> contractOffersMap = new HashMap<>();

    /** CONSTRUCTOR(S) **/
    public Catalog(String id, String type, Object contractOffers, DataService service, String participantId, JsonNode context) {
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

    /** GETTERS AND SETTERS **/
    public Object getContractOffers() {
        return contractOffers;
    }
    @SuppressWarnings("Unused")
    public void setContractOffer(Object contractOffers) {
        this.contractOffers = contractOffers;
    }
    @SuppressWarnings("Unused")
    public Map<String, Integer> getContractOffersMap() {
        return contractOffersMap;
    }
    @SuppressWarnings("Unused")
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
}
