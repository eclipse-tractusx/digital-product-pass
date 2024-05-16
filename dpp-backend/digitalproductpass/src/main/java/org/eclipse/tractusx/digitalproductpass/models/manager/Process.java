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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import utils.DateTimeUtil;


/**
 * This class consists exclusively to define attributes related to the Processes.
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Process {

    /** ATTRIBUTES **/
    @JsonProperty("id")
    public String id;
    @JsonProperty("state")
    public String state;
    @JsonProperty("created")
    public Long created;
    @JsonProperty("updated")
    public Long updated;
    @JsonProperty("thread")
    public Thread thread;

    /** CONSTRUCTOR(S) **/
    public Process(String id, String state, Thread thread) {
        this.id = id;
        this.state = state;
        this.created = DateTimeUtil.getTimestamp();
        this.updated = DateTimeUtil.getTimestamp();
        this.thread = thread;
    }
    public Process() {
    }
    public Process(String id, String state, Long created, Long updated) {
        this.id = id;
        this.state = state;
        this.created = created;
        this.updated = updated;
    }
    public Process(String id, String state, Long created) {
        this.id = id;
        this.state = state;
        this.created = created;
        this.updated = DateTimeUtil.getTimestamp();
    }
    public Process(String id, String state) {
        this.id = id;
        this.state = state;
        this.created = DateTimeUtil.getTimestamp();
        this.updated = DateTimeUtil.getTimestamp();
    }
    public Process(String id, String state, Long created, Long updated, Thread thread) {
        this.id = id;
        this.state = state;
        this.created = created;
        this.updated = updated;
        this.thread = thread;
    }

    /** GETTERS AND SETTERS **/
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
        this.updated = DateTimeUtil.getTimestamp();
    }
    public Thread getThread() {
        return thread;
    }
    public void setThread(Thread thread) {
        this.thread = thread;
    }
    public Long getUpdated() {
        return updated;
    }
    public void setUpdated(Long updated) {
        this.updated = updated;
    }
    public Long getCreated() {
        return created;
    }
    public void setCreated(Long created) {
        this.created = created;
    }

}
