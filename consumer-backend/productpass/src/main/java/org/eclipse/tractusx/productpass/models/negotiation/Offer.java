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

import com.fasterxml.jackson.annotation.JsonProperty;

public class Offer extends ContractOffer {

    @JsonProperty("offerId")
    String offerId;

    public void open(){
        this.offerId = this.id;
        this.assetId = this.getAssetId();
    }
    public void close(){
        this.offerId = null;
        this.assetId = null;
    }

    public String getOfferId() {
        return offerId;
    }

    public String getConnectorId() {
        return this.id.split(":")[1];
    }
    public String getAssetId() {
        return this.id.split(":")[0];
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }
}
