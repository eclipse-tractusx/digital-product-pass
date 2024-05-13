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

package org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DataService;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DidDocument;

/**
 * This class consists exclusively to define attributes related to the Dataset's distributions property.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Distribution extends DidDocument {

    /** ATTRIBUTES **/
    @JsonProperty("dct:format")
    DidDocument format;
    @JsonProperty("dcat:accessService")
    DataService accessService;

    /** CONSTRUCTOR(S) **/

    public Distribution() {
    }

    public Distribution(String id, String type, DidDocument format, DataService accessService) {
        super(id, type);
        this.format = format;
        this.accessService = accessService;
    }

    public Distribution(DidDocument format, DataService accessService) {
        this.format = format;
        this.accessService = accessService;
    }

    public Distribution(String id, String type, JsonNode context, DidDocument format, DataService accessService) {
        super(id, type, context);
        this.format = format;
        this.accessService = accessService;
    }

    public Distribution(String type, DidDocument format, DataService accessService) {
        super(type);
        this.format = format;
        this.accessService = accessService;
    }

    public Distribution(JsonNode context, DidDocument format, DataService accessService) {
        super(context);
        this.format = format;
        this.accessService = accessService;
    }

    public Distribution(JsonNode context, String type, DidDocument format, DataService accessService) {
        super(context, type);
        this.format = format;
        this.accessService = accessService;
    }
    /** GETTERS AND SETTERS **/

    public DidDocument getFormat() {
        return format;
    }

    public void setFormat(DidDocument format) {
        this.format = format;
    }

    public DataService getAccessService() {
        return accessService;
    }

    public void setAccessService(DataService accessService) {
        this.accessService = accessService;
    }


}
