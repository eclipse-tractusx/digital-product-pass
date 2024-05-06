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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
/**
 * This class consists exclusively to define attributes related to the Node Component.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeComponent {

    /** ATTRIBUTES **/
    @JsonProperty("id")
    public String id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("searchId")
    public String searchId;
    @JsonProperty("path")
    public String path;
    @JsonProperty("children")
    public List<NodeComponent> children;

    /** CONSTRUCTOR(S) **/
    public NodeComponent() {
    }
    public NodeComponent(Node node, List<NodeComponent> children) {
        this.id = node.globalAssetId;
        this.path = node.path;
        this.searchId = node.searchId;
        this.name = node.idShort;
        this.children = children;
    }
    public NodeComponent(Node node) {
        this.id = node.globalAssetId;
        this.path = node.path;
        this.searchId = node.searchId;
        this.name = node.idShort;
        this.children = List.of();
    }

    public NodeComponent(String id, String idShort, String path, List<NodeComponent> children) {
        this.id = id;
        this.name = idShort;
        this.path = path;
        this.children = children;
    }

    /** GETTERS AND SETTERS **/
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public List<NodeComponent> getChildren() {
        return children;
    }
    public void setChildren(List<NodeComponent> children) {
        this.children = children;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }
}
