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

package org.eclipse.tractusx.productpass.models.negotiation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Catalog {
    @JsonProperty("id")
    String id;
    @JsonProperty("contractOffers")
    List<Offer> contractOffers;

    @JsonIgnore
    protected Map<String, Integer> contractOffersMap = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Offer> getContractOffers() {
        return contractOffers;
    }

    public void setContractOffers(List<Offer> contractOffers) {
        this.contractOffers = contractOffers;
    }

    public Map<String, Integer> loadContractOffersMapByAssetId(){
        int i = 0;
        for(Offer contractOffer: this.contractOffers){
            this.contractOffersMap.put(contractOffer.getAsset().getId(),i);
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
}
