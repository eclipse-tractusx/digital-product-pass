/*********************************************************************************
 *
 * Tractus-X - Digital Product Pass Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2024 CGI Deutschland B.V. & Co. KG
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

package org.eclipse.tractusx.digitalproductpass.models.http.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.tractusx.digitalproductpass.models.bpn.BpnAddress;

import java.util.Map;

/**
 * This class is responsible for parsing the request from the Bpn response
 **/
public class BpnResponse {


    /** ATTRIBUTES **/
    @JsonProperty("bpns")
    Map<String, BpnAddress> bpns;

    /** CONSTRUCTOR(S) **/
    public BpnResponse(Map<String, BpnAddress> bpns) {
        this.bpns = bpns;
    }

    public BpnResponse() {
    }

    /** GETTERS AND SETTERS **/

    public Map<String, BpnAddress> getBpns() {
        return bpns;
    }

    public void setBpns(Map<String, BpnAddress> bpns) {
        this.bpns = bpns;
    }
}
