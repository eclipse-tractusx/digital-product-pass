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

import java.util.ArrayList;
/**
 * This class consists exclusively to define attributes related to the Job.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Job {

    /** ATTRIBUTES **/
    @JsonProperty("id")
    public String id;

    @JsonProperty("globalAssetId")
    public String globalAssetId;

    @JsonProperty("state")
    public String state;

    @JsonProperty("exception")
    public Object exception;

    @JsonProperty("createdOn")
    public String createdOn;

    @JsonProperty("startedOn")
    public String startedOn;

    @JsonProperty("lastModifiedOn")
    public String lastModifiedOn;

    @JsonProperty("completedOn")
    public String completedOn;

    @JsonProperty("owner")
    public String owner;

    @JsonProperty("summary")
    public Object summary;

    @JsonProperty("parameter")
    public JobRequest parameter;

    /** CONSTRUCTOR(S) **/
    public Job() {
    }

    public Job(String id, String globalAssetId, String state, Object exception, String createdOn, String startedOn, String lastModifiedOn, String completedOn, String owner, Object summary, JobRequest parameter) {
        this.id = id;
        this.globalAssetId = globalAssetId;
        this.state = state;
        this.exception = exception;
        this.createdOn = createdOn;
        this.startedOn = startedOn;
        this.lastModifiedOn = lastModifiedOn;
        this.completedOn = completedOn;
        this.owner = owner;
        this.summary = summary;
        this.parameter = parameter;
    }

    /** GETTERS AND SETTERS **/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGlobalAssetId() {
        return globalAssetId;
    }

    public void setGlobalAssetId(String globalAssetId) {
        this.globalAssetId = globalAssetId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Object getException() {
        return exception;
    }

    public void setException(Object exception) {
        this.exception = exception;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getStartedOn() {
        return startedOn;
    }

    public void setStartedOn(String startedOn) {
        this.startedOn = startedOn;
    }

    public String getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(String lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public String getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(String completedOn) {
        this.completedOn = completedOn;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Object getSummary() {
        return summary;
    }

    public void setSummary(Object summary) {
        this.summary = summary;
    }

    public JobRequest getParameter() {
        return parameter;
    }

    public void setParameter(JobRequest parameter) {
        this.parameter = parameter;
    }
}
