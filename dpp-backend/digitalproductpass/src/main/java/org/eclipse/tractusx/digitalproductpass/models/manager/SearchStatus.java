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
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceException;
import org.eclipse.tractusx.digitalproductpass.models.catenax.Dtr;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.Search;
import utils.CrypUtil;
import utils.DateTimeUtil;

import java.util.Map;

/**
 * This class consists exclusively to define attributes and methods related to the DTR's search status.
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchStatus {

    @JsonProperty("search")
    public Search search;
    @JsonProperty("dtrs")
    public Map<String, Dtr> dtrs;
    @JsonProperty("created")
    public Long created;
    @JsonProperty("updated")
    public Long updated;

    /** CONSTRUCTOR(S) **/
    public SearchStatus(Search search, Map<String, Dtr> dtrs, Long created, Long updated) {
        this.search = search;
        this.dtrs = dtrs;
        this.created = created;
        this.updated = updated;
    }
    public SearchStatus(Search search, Map<String, Dtr> dtrs){
        this.search = search;
        this.dtrs = dtrs;
        Long timestamp = DateTimeUtil.getTimestamp();
        this.updated = this.created = timestamp;
    }
    public SearchStatus() {
        this.dtrs = Map.of();
        Long timestamp = DateTimeUtil.getTimestamp();
        this.updated = this.created = timestamp;
    }

    /** GETTERS AND SETTERS **/
    public Search getSearch() {
        return search;
    }
    public void setSearch(Search search) {
        this.search = search;
    }
    public Map<String, Dtr> getDtrs() {
        return dtrs;
    }
    public void setDtrs(Map<String, Dtr> dtrs) {
        this.dtrs = dtrs;
    }
    public Long getCreated() {
        return created;
    }
    public void setCreated(Long created) {
        this.created = created;
    }
    public Long getUpdated() {
        return updated;
    }
    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    /** METHODS **/
    /**
     * Gets the DTR with the given id from the DTR map object.
     * <p>
     * @param   id
     *          the {@code String} identification of the DTR.
     *
     * @return a {@code Dtr} object with the given id, if exists.
     *
     */
    public Dtr getDtr(String id) {
        return this.dtrs.get(id);
    }

    /**
     * Gets a new DTR into the DTR map object.
     * <p>
     * @param   dtr
     *          the {@code Dtr} object to add.
     *
     */
    public void addDtr(Dtr dtr) {
        this.updated = DateTimeUtil.getTimestamp();
        this.dtrs.put(
                CrypUtil.md5(dtr.getEndpoint()), // Use MD5 to identify the endpoint
                dtr
        );
    }
}
