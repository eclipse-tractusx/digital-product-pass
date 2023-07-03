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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dataset extends DidDocument{
    @JsonProperty("odrl:hasPolicy")
    Set policy;
    @JsonProperty("dcat:distribution")
    List<Distribution> distributions;

    @JsonProperty("edc:description")
    String assetDescription;
    @JsonProperty("edc:id")
    String assetId;

    public Dataset(String id, String type, Set policy, List<Distribution> distributions, String assetDescription, String assetId) {
        super(id, type);
        this.policy = policy;
        this.distributions = distributions;
        this.assetDescription = assetDescription;
        this.assetId = assetId;
    }

    public Dataset(String id, String type) {
        super(id, type);
    }

    public Dataset() {
    }


    public Set getPolicy() {
        return policy;
    }

    public void setPolicy(Set policy) {
        this.policy = policy;
    }

    public List<Distribution> getDistributions() {
        return distributions;
    }

    public void setDistributions(List<Distribution> distributions) {
        this.distributions = distributions;
    }

    public String getAssetDescription() {
        return assetDescription;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }
}
