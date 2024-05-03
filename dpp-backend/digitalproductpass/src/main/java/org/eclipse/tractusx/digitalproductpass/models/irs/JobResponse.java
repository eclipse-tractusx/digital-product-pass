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

package org.eclipse.tractusx.digitalproductpass.models.irs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.models.dtregistry.DigitalTwin;

import java.util.ArrayList;

/**
 * This class consists exclusively to define attributes related to the Job Response coming from the IRS.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobResponse {

    /** ATTRIBUTES **/
    @JsonProperty("job")
    public Job job;
    @JsonProperty("relationships")
    public ArrayList<Relationship> relationships;
    @JsonProperty("shells")
    public ArrayList<DigitalTwin> shells;
    @JsonProperty("tombstones")
    public Object tombstones;
    @JsonProperty("submodels")
    public ArrayList<JsonNode> submodels;
    @JsonProperty("bpns")
    public ArrayList<String> bpns;

    /** CONSTRUCTOR(S) **/
    public JobResponse(Job job, ArrayList<Relationship> relationships, ArrayList<DigitalTwin> shells, Object tombstones, ArrayList<JsonNode> submodels, ArrayList<String> bpns) {
        this.job = job;
        this.relationships = relationships;
        this.shells = shells;
        this.tombstones = tombstones;
        this.submodels = submodels;
        this.bpns = bpns;
    }

    public JobResponse() {
    }

    /** GETTERS AND SETTERS **/
    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public ArrayList<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(ArrayList<Relationship> relationships) {
        this.relationships = relationships;
    }

    public ArrayList<DigitalTwin> getShells() {
        return shells;
    }

    public void setShells(ArrayList<DigitalTwin> shells) {
        this.shells = shells;
    }

    public Object getTombstones() {
        return tombstones;
    }

    public void setTombstones(Object tombstones) {
        this.tombstones = tombstones;
    }

    public ArrayList<JsonNode> getSubmodels() {
        return submodels;
    }

    public void setSubmodels(ArrayList<JsonNode> submodels) {
        this.submodels = submodels;
    }

    public ArrayList<String> getBpns() {
        return bpns;
    }

    public void setBpns(ArrayList<String> bpns) {
        this.bpns = bpns;
    }
}
