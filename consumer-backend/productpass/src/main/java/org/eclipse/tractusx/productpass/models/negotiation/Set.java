/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

package org.eclipse.tractusx.productpass.models.negotiation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Set extends DidDocument{
    @JsonProperty("odrl:permission")
    Object permissions;
    @JsonProperty("odrl:prohibition")
    Object prohibitions;
    @JsonProperty("odrl:obligation")
    Object obligations;
    @JsonProperty("odrl:target")
    String target;

    public Set(String id, String type, Object permissions, Object prohibitions, Object obligations, String target) {
        super(id, type);
        this.permissions = permissions;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
        this.target = target;
    }

    public Set(Object permissions, Object prohibitions, Object obligations, String target) {
        this.permissions = permissions;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
        this.target = target;
    }

    public Set(String id, String type) {
        super(id, type);
    }

    public Set() {
    }
    

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Object getPermissions() {
        return permissions;
    }

    public void setPermissions(Object permissions) {
        this.permissions = permissions;
    }

    public Object getProhibitions() {
        return prohibitions;
    }

    public void setProhibitions(Object prohibitions) {
        this.prohibitions = prohibitions;
    }

    public Object getObligations() {
        return obligations;
    }

    public void setObligations(Object obligations) {
        this.obligations = obligations;
    }

}
