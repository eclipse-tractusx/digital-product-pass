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

package org.eclipse.tractusx.digitalproductpass.models.manager;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.tractusx.digitalproductpass.models.catenax.Dtr;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobHistory;
import utils.DateTimeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class consists exclusively to define attributes and methods related to the Process' status.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Status {

    /** ATTRIBUTES **/
    @JsonProperty("id")
    public String id;

    @JsonProperty("status")
    public String status;

    @JsonProperty("created")
    public Long created;

    @JsonProperty("modified")
    public Long modified;

    @JsonProperty("job")
    public JobHistory job;

    @JsonProperty("endpoint")
    public String endpoint;

    @JsonProperty("dataPlaneUrl")
    public String dataPlaneUrl;

    @JsonProperty("bpn")
    public String bpn;
    @JsonProperty("providerBpn")
    public String providerBpn;

    @JsonProperty("children")
    public Boolean children;

    @JsonProperty("treeState")
    public String treeState;

    @JsonProperty("history")
    public Map<String, History> history;

    /** CONSTRUCTOR(S) **/
    @JsonProperty("semanticId")
    public String semanticId;

    @JsonProperty("dtr")
    public Dtr dtr;


    @SuppressWarnings("Unused")
    public Status(String id, String status, Long created, Long modified, String endpoint, Map<String, History> history) {
        this.id = id;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.endpoint = endpoint;
        this.history = history;
    }
    @SuppressWarnings("Unused")
    public Status(String id, String status, Long modified, String endpoint, Map<String, History> history) {
        this.id = id;
        this.status = status;
        this.created = DateTimeUtil.getTimestamp();
        this.modified = modified;
        this.endpoint = endpoint;
        this.history = history;
    }
    @SuppressWarnings("Unused")
    public Status(String id, String status, Long created, Long modified,  String endpoint, String historyId, History history) {
        this.id = id;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.endpoint = endpoint;
        this.history = Map.of(historyId, history);
    }
    @SuppressWarnings("Unused")
    public Status(String id, String status, Long modified,  String endpoint, String historyId, History history) {
        this.id = id;
        this.status = status;
        this.created = DateTimeUtil.getTimestamp();
        this.modified = modified;
        this.endpoint = endpoint;
        this.history = Map.of(historyId, history);
    }
    @SuppressWarnings("Unused")
    public Status(String id, String status,  String endpoint, Long modified) {
        this.id = id;
        this.status = status;
        this.created = DateTimeUtil.getTimestamp();
        this.modified = modified;
        this.endpoint = endpoint;
        this.history = new HashMap<String, History>();
    }

    public Status(String id, String status, String endpoint, Long created, Long modified) {
        this.id = id;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.endpoint = endpoint;
        this.history = new HashMap<String, History>();
    }


    public Status(String id, String status, String endpoint, String dataPlaneUrl, Long created, Long modified) {
        this.id = id;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.endpoint = endpoint;
        this.dataPlaneUrl = dataPlaneUrl;
        this.history = new HashMap<String, History>();
    }


    public Status(Map<String, History> history) {
        this.history = history;
    }

    @SuppressWarnings("Unused")
    public Status(String id, String status, Long created, Long modified) {
        this.id = id;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.history = new HashMap<String, History>();
    }
    @SuppressWarnings("Unused")
    public Status(String id, String status, Long created, Long modified, String historyId, History history) {
        this.id = id;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.history = Map.of(historyId, history);
    }
    @SuppressWarnings("Unused")
    public Status(String id, String status, Long created, Long modified, String endpoint, String bpn, Map<String, History> history) {
        this.id = id;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.endpoint = endpoint;
        this.bpn = bpn;
        this.history = history;
    }
    @SuppressWarnings("Unused")
    public Status(String id, String status, Long created, Long modified, String endpoint, String bpn) {
        this.id = id;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.endpoint = endpoint;
        this.bpn = bpn;
        this.history = new HashMap<String, History>();
    }



    public Status() {
    }

    public Status(String id, String status, Long created, Long modified, JobHistory job, String endpoint, String dataPlaneUrl, String bpn, Boolean children, String treeState, Map<String, History> history, String semanticId) {
        this.id = id;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.job = job;
        this.endpoint = endpoint;
        this.dataPlaneUrl = dataPlaneUrl;
        this.bpn = bpn;
        this.children = children;
        this.treeState = treeState;
        this.history = history;
        this.semanticId = semanticId;
    }

    public Status(String id, String status, Long created, Long modified, JobHistory job, String endpoint, String dataPlaneUrl, String bpn, Boolean children, String treeState, Map<String, History> history, String semanticId, Dtr dtr) {
        this.id = id;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.job = job;
        this.endpoint = endpoint;
        this.dataPlaneUrl = dataPlaneUrl;
        this.bpn = bpn;
        this.children = children;
        this.treeState = treeState;
        this.history = history;
        this.semanticId = semanticId;
        this.dtr = dtr;
    }

    public Status(String id, String status, Long created, Long modified, JobHistory job, String endpoint, String dataPlaneUrl, String bpn, String providerBpn, Boolean children, String treeState, Map<String, History> history, String semanticId, Dtr dtr) {
        this.id = id;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.job = job;
        this.endpoint = endpoint;
        this.dataPlaneUrl = dataPlaneUrl;
        this.bpn = bpn;
        this.providerBpn = providerBpn;
        this.children = children;
        this.treeState = treeState;
        this.history = history;
        this.semanticId = semanticId;
        this.dtr = dtr;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getModified() {
        return modified;
    }

    public void setModified(Long modified) {
        this.modified = modified;
    }

    public Map<String,History> getHistory() {
        return history;
    }
    public Boolean historyExists(String name) {
        return this.history.containsKey(name);
    }

    public void setHistory(Map<String,History> history) {
        this.history = history;
    }

    public void setHistory(String name, History history) {
        this.history.put(name, history);
    }
    @SuppressWarnings("Unused")
    public void deleteHistory() {
        this.history = new HashMap<>();
    }
    @SuppressWarnings("Unused")
    public Boolean removeHistory(String name) {
        if(!this.history.containsKey(name)){
            return false;
        }
        this.history.remove(name);
        return true;
    }
    public History getHistory(String name) {
        return this.history.getOrDefault(name, null);
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBpn() {
        return this.bpn;
    }

    public void setBpn(String bpn) {
        this.bpn = bpn;
    }


    public String getTreeState() {
        return treeState;
    }

    public void setTreeState(String treeState) {
        this.treeState = treeState;
    }

    public Boolean getChildren() {
        return children;
    }

    public void setChildren(Boolean children) {
        this.children = children;
    }

    public String getSemanticId() {
        return semanticId;
    }

    public void setSemanticId(String semanticId) {
        this.semanticId = semanticId;
    }

    public String getDataPlaneUrl() {
        return dataPlaneUrl;
    }

    public void setDataPlaneUrl(String dataPlaneUrl) {
        this.dataPlaneUrl = dataPlaneUrl;
    }

    public JobHistory getJob() {
        return job;
    }

    public void setJob(JobHistory job) {
        this.job = job;
    }

    public Dtr getDtr() {
        return dtr;
    }

    public void setDtr(Dtr dtr) {
        this.dtr = dtr;
    }

    public String getProviderBpn() {
        return providerBpn;
    }

    public void setProviderBpn(String providerBpn) {
        this.providerBpn = providerBpn;
    }
}










