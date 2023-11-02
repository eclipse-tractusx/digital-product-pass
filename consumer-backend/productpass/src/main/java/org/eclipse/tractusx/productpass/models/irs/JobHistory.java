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

package org.eclipse.tractusx.productpass.models.irs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JobHistory {

    /** ATTRIBUTES **/
    @JsonProperty("jobId")
    public String jobId;
    @JsonProperty("searchId")
    public String searchId;

    @JsonProperty("globalAssetId")
    public String globalAssetId;
    @JsonProperty("path")
    public String path;
    @JsonProperty("created")
    public Long created;
    @JsonProperty("updated")
    public Long updated;
    @JsonProperty("children")
    public Integer children;

    /** CONSTRUCTOR(S) **/
    public JobHistory() {
    }

    public JobHistory(String jobId, String searchId, String globalAssetId, String path, Long created, Long updated, Integer children) {
        this.jobId = jobId;
        this.searchId = searchId;
        this.globalAssetId = globalAssetId;
        this.path = path;
        this.created = created;
        this.updated = updated;
        this.children = children;
    }


    /** GETTERS AND SETTERS **/
    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }


    public String getGlobalAssetId() {
        return globalAssetId;
    }

    public void setGlobalAssetId(String globalAssetId) {
        this.globalAssetId = globalAssetId;
    }


    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }


    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public Integer getChildren() {
        return children;
    }

    public void setChildren(Integer children) {
        this.children = children;
    }
}
