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

package org.eclipse.tractusx.digitalproductpass.models.negotiation.policy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DidDocument;

import java.util.List;

/**
 * This class consists exclusively to define attributes related to the Policy's data.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Set extends DidDocument {

    /** ATTRIBUTES **/
    @JsonProperty("odrl:permission")
    Object permissions;
    @JsonProperty("odrl:prohibition")
    Object prohibitions;
    @JsonProperty("odrl:obligation")
    Object obligations;

    /** CONSTRUCTOR(S) **/
    public Set(String id, String type, Object permissions, Object prohibitions, Object obligations) {
        super(id, type);
        this.permissions = permissions;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
    }
    public Set(Object permissions, Object prohibitions, Object obligations) {
        this.permissions = permissions;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
    }
    public Set(String id, String type) {
        super(id, type);
    }
    public Set() {
    }

    /** GETTERS AND SETTERS **/
    public Object getPermissions() {
        return permissions;
    }
    public void setPermissions(Object permissions) {
        this.permissions = permissions;
    }
    @SuppressWarnings("Unused")
    public Object getProhibitions() {
        return prohibitions;
    }
    @SuppressWarnings("Unused")
    public void setProhibitions(Object prohibitions) {
        this.prohibitions = prohibitions;
    }
    @SuppressWarnings("Unused")
    public Object getObligations() {
        return obligations;
    }
    @SuppressWarnings("Unused")
    public void setObligations(Object obligations) {
        this.obligations = obligations;
    }
}
