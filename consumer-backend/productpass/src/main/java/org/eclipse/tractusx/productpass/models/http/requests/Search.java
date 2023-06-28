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

package org.eclipse.tractusx.productpass.models.http.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class Search {

    @NotNull(message = "Id needs to be defined!")
    @JsonProperty("id")
    String id;

    @NotNull(message = "Passport Version needs to be defined!")
    @JsonProperty("version")
    String version;

    @JsonProperty(value = "idType", defaultValue = "partInstanceId")
    String idType = "partInstanceId";
    @JsonProperty(value = "dtIndex", defaultValue = "0")
    Integer dtIndex = 0;
    @JsonProperty(value = "idShort", defaultValue = "batteryPass")
    String idShort = "batteryPass";

    public Search(String id, String version, String idType, Integer dtIndex, String idShort) {
        this.id = id;
        this.version = version;
        this.idType = idType;
        this.dtIndex = dtIndex;
        this.idShort = idShort;
    }
    public Search() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public Integer getDtIndex() {
        return dtIndex;
    }

    public void setDtIndex(Integer dtIndex) {
        this.dtIndex = dtIndex;
    }

    public String getIdShort() {
        return idShort;
    }

    public void setIdShort(String idShort) {
        this.idShort = idShort;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
