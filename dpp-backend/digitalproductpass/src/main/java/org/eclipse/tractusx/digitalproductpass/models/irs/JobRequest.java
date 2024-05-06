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
 * This class states how a Job request for the IRS Component need to be created.
 *
 * <p> It defines the structure to follow when requesting a Job Creation.
 * This is a model class and is used by the IRS service.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobRequest {
    /** ATTRIBUTES **/
    @JsonProperty("aspects")
    public ArrayList<String> aspects;

    @JsonProperty("bomLifecycle")
    public String bomLifecycle;
    @JsonProperty("lookupBPNs")
    public Boolean lookupBPNs;

    @JsonProperty("collectAspects")
    public Boolean collectAspects;

    @JsonProperty("direction")
    public String direction;

    @JsonProperty("callbackUrl")
    public String callbackUrl;

    @JsonProperty("depth")
    public Integer depth;

    @JsonProperty("integrityCheck")
    public Boolean integrityCheck;

    @JsonProperty("key")
    public Key key;

    /** CONSTRUCTOR(S) **/
    public JobRequest(ArrayList<String> aspects, String bomLifecycle, Boolean lookupBPNs, Boolean collectAspects, String direction, Integer depth, Boolean integrityCheck, Key key) {
        this.aspects = aspects;
        this.bomLifecycle = bomLifecycle;
        this.lookupBPNs = lookupBPNs;
        this.collectAspects = collectAspects;
        this.direction = direction;
        this.depth = depth;
        this.integrityCheck = integrityCheck;
        this.key = key;
    }

    public JobRequest() {
    }

    public JobRequest(ArrayList<String> aspects, String bomLifecycle, Boolean lookupBPNs, Boolean collectAspects, String direction, Integer depth, Boolean integrityCheck) {
        this.aspects = aspects;
        this.bomLifecycle = bomLifecycle;
        this.lookupBPNs = lookupBPNs;
        this.collectAspects = collectAspects;
        this.direction = direction;
        this.depth = depth;
        this.integrityCheck = integrityCheck;
    }
    public JobRequest(ArrayList<String> aspects, String bomLifecycle, Boolean lookupBPNs, Boolean collectAspects, String direction, Integer depth, Boolean integrityCheck, String callbackUrl) {
        this.aspects = aspects;
        this.bomLifecycle = bomLifecycle;
        this.lookupBPNs = lookupBPNs;
        this.collectAspects = collectAspects;
        this.direction = direction;
        this.callbackUrl = callbackUrl;
        this.depth = depth;
        this.integrityCheck = integrityCheck;
    }
    public JobRequest(ArrayList<String> aspects, String bomLifecycle, Boolean lookupBPNs, Boolean collectAspects, String direction, Integer depth, Boolean integrityCheck, Key key,  String callbackUrl) {
        this.aspects = aspects;
        this.bomLifecycle = bomLifecycle;
        this.lookupBPNs = lookupBPNs;
        this.collectAspects = collectAspects;
        this.direction = direction;
        this.callbackUrl = callbackUrl;
        this.depth = depth;
        this.integrityCheck = integrityCheck;
        this.key = key;
    }

    /** GETTERS AND SETTERS **/
    public ArrayList<String> getAspects() {
        return aspects;
    }

    public void setAspects(ArrayList<String> aspects) {
        this.aspects = aspects;
    }

    public String getBomLifecycle() {
        return bomLifecycle;
    }

    public void setBomLifecycle(String bomLifecycle) {
        this.bomLifecycle = bomLifecycle;
    }

    public Boolean getLookupBPNs() {
        return lookupBPNs;
    }

    public void setLookupBPNs(Boolean lookupBPNs) {
        this.lookupBPNs = lookupBPNs;
    }

    public Boolean getCollectAspects() {
        return collectAspects;
    }

    public void setCollectAspects(Boolean collectAspects) {
        this.collectAspects = collectAspects;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Boolean getIntegrityCheck() {
        return integrityCheck;
    }

    public void setIntegrityCheck(Boolean integrityCheck) {
        this.integrityCheck = integrityCheck;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public void setKey(String globalAssetId, String bpn) {
        this.key = new Key(globalAssetId, bpn);
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class Key {
        @JsonProperty("globalAssetId")
        String globalAssetId;
        @JsonProperty("bpn")
        String bpn;

        public Key(String globalAssetId, String bpn) {
            this.globalAssetId = globalAssetId;
            this.bpn = bpn;
        }

        public Key() {
        }

        public String getGlobalAssetId() {
            return globalAssetId;
        }

        public void setGlobalAssetId(String globalAssetId) {
            this.globalAssetId = globalAssetId;
        }

        public String getBpn() {
            return bpn;
        }

        public void setBpn(String bpn) {
            this.bpn = bpn;
        }
    }




}
