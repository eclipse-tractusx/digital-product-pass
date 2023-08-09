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

package org.eclipse.tractusx.productpass.models.manager;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import utils.DateTimeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Status {
    @JsonProperty("id")
    public String id;

    @JsonProperty("status")
    public String status;

    @JsonProperty("created")
    public Long created;

    @JsonProperty("modified")
    public Long modified;


    @JsonProperty("endpoint")
    public String endpoint;

    @JsonProperty("bpn")
    public String bpn;

    @JsonProperty("history")
    public Map<String, History> history;

    public Status(String id, String status, Long created, Long modified, String endpoint, Map<String, History> history) {
        this.id = id;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.endpoint = endpoint;
        this.history = history;
    }

    public Status(String id, String status, Long modified, String endpoint, Map<String, History> history) {
        this.id = id;
        this.status = status;
        this.created = DateTimeUtil.getTimestamp();
        this.modified = modified;
        this.endpoint = endpoint;
        this.history = history;
    }

    public Status(String id, String status, Long created, Long modified,  String endpoint, String historyId, History history) {
        this.id = id;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.endpoint = endpoint;
        this.history = Map.of(historyId, history);
    }
    public Status(String id, String status, Long modified,  String endpoint, String historyId, History history) {
        this.id = id;
        this.status = status;
        this.created = DateTimeUtil.getTimestamp();
        this.modified = modified;
        this.endpoint = endpoint;
        this.history = Map.of(historyId, history);
    }

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
    public Status() {
    }

    public Status(String id, String status, Long created, Long modified) {
        this.id = id;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.history = new HashMap<String, History>();
    }
    public Status(String id, String status, Long created, Long modified, String historyId, History history) {
        this.id = id;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.history = Map.of(historyId, history);
    }

    public Status(String id, String status, Long created, Long modified, String endpoint, String bpn, Map<String, History> history) {
        this.id = id;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.endpoint = endpoint;
        this.bpn = bpn;
        this.history = history;
    }

    public Status(String id, String status, Long created, Long modified, String endpoint, String bpn) {
        this.id = id;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.endpoint = endpoint;
        this.bpn = bpn;
        this.history = new HashMap<String, History>();
    }
    public Status(String id, String status, Long created, Long modified, String endpoint, String bpn, String historyId, History history) {
        this.id = id;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.endpoint = endpoint;
        this.bpn = bpn;
        this.history = Map.of(historyId, history);
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
    public void deleteHistory() {
        this.history = new HashMap<>();
    }
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
}










