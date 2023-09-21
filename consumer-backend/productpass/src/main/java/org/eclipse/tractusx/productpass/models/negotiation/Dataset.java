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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dataset extends DidDocument{
    @JsonProperty("odrl:hasPolicy")
    Object policy;
    @JsonProperty("dcat:distribution")
    List<Distribution> distributions;

    @JsonProperty("edc:description")
    String assetDescription;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @JsonProperty("edc:contenttype")
    String contentType;
    @JsonProperty("edc:id")
    String assetId;
    @JsonProperty("edc:name")
    String assetName;
    @JsonProperty("edc:type")
    String assetType;

    public Dataset(String id, String type, Object policy, List<Distribution> distributions, String assetDescription, String contentType, String assetId) {
        super(id, type);
        this.policy = policy;
        this.distributions = distributions;
        this.assetDescription = assetDescription;
        this.contentType = contentType;
        this.assetId = assetId;
    }
    public Dataset(String id, String type, Object policy, List<Distribution> distributions, String assetDescription, String contentType, String assetId, String assetName) {
        super(id, type);
        this.policy = policy;
        this.distributions = distributions;
        this.assetDescription = assetDescription;
        this.contentType = contentType;
        this.assetId = assetId;
        this.assetName = assetName;
    }

    public Dataset(String id, String type, Object policy, List<Distribution> distributions, String assetDescription, String contentType, String assetId, String assetName, String assetType) {
        super(id, type);
        this.policy = policy;
        this.distributions = distributions;
        this.assetDescription = assetDescription;
        this.contentType = contentType;
        this.assetId = assetId;
        this.assetName = assetName;
        this.assetType = assetType;
    }

    public Dataset(String id, String type) {
        super(id, type);
    }

    public Dataset() {
    }


    public Object getPolicy() {
        return policy;
    }

    public void setPolicy(Object policy) {
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

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }
}
