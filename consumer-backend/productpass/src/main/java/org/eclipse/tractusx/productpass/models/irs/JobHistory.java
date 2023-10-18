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
    @JsonProperty("globalAssetId")
    public String globalAssetId;
    @JsonProperty("path")
    public String path;
    @JsonProperty("created")
    public Long created;


    /** CONSTRUCTOR(S) **/
    public JobHistory() {
    }

    public JobHistory(String jobId, String globalAssetId, String path, Long created) {
        this.jobId = jobId;
        this.globalAssetId = globalAssetId;
        this.path = path;
        this.created = created;
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
}
