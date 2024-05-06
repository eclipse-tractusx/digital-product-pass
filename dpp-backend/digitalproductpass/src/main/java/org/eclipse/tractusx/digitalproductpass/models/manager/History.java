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
 * This class consists exclusively to define attributes related to the historization of the processes' status.
 * To keep track of each process status history.
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class History {

    /** ATTRIBUTES **/
    @JsonProperty("id")
    public String id;
    @JsonProperty("status")
    public String status;
    @JsonProperty("started")
    public Long started;
    @JsonProperty("updated")
    public Long updated;
    @JsonProperty("attempts")
    public Integer attempts;

    /** CONSTRUCTOR(S) **/
    @SuppressWarnings("Unused")
    public History(String id, String status, Integer attempts) {
        this.id = id;
        this.status = status;
        this.started = DateTimeUtil.getTimestamp();
        this.updated = DateTimeUtil.getTimestamp();
        this.attempts = attempts;
    }
    public History(String id, String status, Long started) {
        this.id = id;
        this.status = status;
        this.started = started;
        this.updated = DateTimeUtil.getTimestamp();
    }
    @SuppressWarnings("Unused")
    public History(String id, String status, Long started, Integer attempts) {
        this.id = id;
        this.status = status;
        this.started = started;
        this.updated = DateTimeUtil.getTimestamp();
        this.attempts = attempts;
    }
    public History(String id, String status) {
        this.id = id;
        this.status = status;
        this.started = DateTimeUtil.getTimestamp();
        this.updated = DateTimeUtil.getTimestamp();
    }
    @SuppressWarnings("Unused")
    public History() {
    }
    @SuppressWarnings("Unused")
    public History(String id,String status, Long started, Long updated) {
        this.id = id;
        this.status = status;
        this.started = started;
        this.updated = updated;
    }
    @SuppressWarnings("Unused")
    public History(String id,String status, Long started, Long updated, Integer attempts) {
        this.id = id;
        this.status = status;
        this.started = started;
        this.updated = updated;
        this.attempts = attempts;
    }

    /** GETTERS AND SETTERS **/
    public Long getStarted() {
        return started;
    }
    public void setStarted(Long started) {
        this.started = started;
    }
    public Long getUpdated() {
        return updated;
    }
    public void setUpdated(Long updated) {
        this.updated = updated;
    }
    @SuppressWarnings("Unused")
    public Integer getAttempts() {
        return attempts;
    }
    @SuppressWarnings("Unused")
    public void addAttempt(){
        this.updated = DateTimeUtil.getTimestamp();
        if(this.attempts==null){
            this.attempts=0;
        }
        this.attempts++;
    }
    @SuppressWarnings("Unused")
    public void setAttempts(Integer attempts) {
        this.updated = DateTimeUtil.getTimestamp();
        this.attempts = attempts;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
        this.updated = DateTimeUtil.getTimestamp();
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
