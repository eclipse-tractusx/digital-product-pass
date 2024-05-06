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

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DidDocument;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog.Distribution;

import java.util.List;

/**
 * This class consists exclusively to define attributes related to the Contract Offer's information data set.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dataset extends DidDocument {

    /** ATTRIBUTES **/
    @JsonProperty("odrl:hasPolicy")
    Object policy;
    @JsonProperty("dcat:distribution")
    List<Distribution> distributions;
    @JsonProperty("id")
    @JsonAlias({"id", "edc:id"})
    String assetId;

    /** CONSTRUCTOR(S) **/

    public Dataset(String id, String type) {
        super(id, type);
    }
    public Dataset() {
    }

    public Dataset(String id, String type, Object policy, List<Distribution> distributions, String assetId) {
        super(id, type);
        this.policy = policy;
        this.distributions = distributions;
        this.assetId = assetId;
    }

    public Dataset(Object policy, List<Distribution> distributions, String assetId) {
        this.policy = policy;
        this.distributions = distributions;
        this.assetId = assetId;
    }

    /** GETTERS AND SETTERS **/
    public Object getPolicy() {
        return policy;
    }
    public void setPolicy(Object policy) {
        this.policy = policy;
    }
    @SuppressWarnings("Unused")
    public List<Distribution> getDistributions() {
        return distributions;
    }
    @SuppressWarnings("Unused")
    public void setDistributions(List<Distribution> distributions) {
        this.distributions = distributions;
    }
    public String getAssetId() {
        return assetId;
    }
    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }
}
