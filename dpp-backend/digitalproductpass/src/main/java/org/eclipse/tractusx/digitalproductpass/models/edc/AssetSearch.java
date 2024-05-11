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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.tractusx.digitalproductpass.models.catenax.Dtr;

/**
 * This class consists exclusively to define attributes related to the Asset search.
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetSearch {

    /** ATTRIBUTES **/
    @JsonProperty("assetId")
    String assetId;

    @JsonProperty("bpn")
    String bpn;
    @JsonProperty("connectorAddress")
    String connectorAddress;

    /** CONSTRUCTOR(S) **/
    @SuppressWarnings("Unused")
    public AssetSearch() {
    }

    public AssetSearch(String assetId, String connectorAddress) {
        this.assetId = assetId;
        this.connectorAddress = connectorAddress;
    }

    public AssetSearch(String assetId, String bpn, String connectorAddress) {
        this.assetId = assetId;
        this.bpn = bpn;
        this.connectorAddress = connectorAddress;
    }


    /** GETTERS AND SETTERS **/
    public String getAssetId() {
        return assetId;
    }
    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }
    public String getConnectorAddress() {
        return connectorAddress;
    }
    public void setConnectorAddress(String connectorAddress) {
        this.connectorAddress = connectorAddress;
    }

    public String getBpn() {
        return bpn;
    }

    public void setBpn(String bpn) {
        this.bpn = bpn;
    }
}
