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
import org.eclipse.tractusx.digitalproductpass.models.dtregistry.DigitalTwin;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobResponse;
import utils.CatenaXUtil;

import java.util.List;
import java.util.Map;
/**
 * This class consists exclusively to define attributes related to the Node.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Node {

    /** ATTRIBUTES **/
    @JsonProperty("id")
    public String id;
    @JsonProperty("globalAssetId")
    public String globalAssetId;
    @JsonProperty("idShort")
    public String idShort;
    @JsonProperty("searchId")
    public String searchId;
    @JsonProperty("path")
    public String path;
    @JsonProperty("digitalTwin")
    public DigitalTwin digitalTwin;
    @JsonProperty("job")
    public JobResponse job;
    @JsonProperty("children")
    public Map<String, Node> children;

    /** CONSTRUCTOR(S) **/
    public Node(Node parent, DigitalTwin digitalTwin, Map<String, Node> children, String searchIdSchema){
        this.setup(digitalTwin, searchIdSchema);
        this.setPath(parent, digitalTwin.getGlobalAssetId());
        this.children = children;
    }

    public Node(String parentPath, DigitalTwin digitalTwin, Map<String, Node> children, String searchIdSchema){
        this.setup(digitalTwin, searchIdSchema);
        this.setPath(parentPath, digitalTwin.getGlobalAssetId());
        this.children = children;
    }

    public Node(Node parent, DigitalTwin digitalTwin, String searchIdSchema){
        this.setup(digitalTwin, searchIdSchema);
        this.setPath(parent, digitalTwin.getGlobalAssetId());
        this.children = Map.of();
    }

    public Node(String parentPath, DigitalTwin digitalTwin, String searchIdSchema){
        this.setup(digitalTwin,searchIdSchema);
        this.setPath(parentPath, digitalTwin.getGlobalAssetId());
        this.children = Map.of();
    }

    public Node(DigitalTwin digitalTwin, String searchIdSchema){
        this.setup(digitalTwin, searchIdSchema);
        this.setPath("", digitalTwin.getGlobalAssetId());
        this.children = Map.of();
    }

    public Node(DigitalTwin digitalTwin, Map<String, Node> children, String searchIdSchema) {
        this.id= digitalTwin.getIdentification();
        this.globalAssetId = digitalTwin.getGlobalAssetId();
        this.idShort = digitalTwin.getIdShort();
        this.setSearchId(digitalTwin, searchIdSchema);
        this.setDigitalTwin(digitalTwin);
        this.children = children;
    }

    public Node() {
    }

    public Node(String id, String globalAssetId, String idShort, String searchId, String path, DigitalTwin digitalTwin, JobResponse job, Map<String, Node> children) {
        this.id = id;
        this.globalAssetId = globalAssetId;
        this.idShort = idShort;
        this.searchId = searchId;
        this.path = path;
        this.digitalTwin = digitalTwin;
        this.job = job;
        this.children = children;
    }


    /** GETTERS AND SETTERS **/

    public void setup(DigitalTwin digitalTwin,String searchIdSchema){
        this.id = digitalTwin.getIdentification();
        this.globalAssetId = digitalTwin.getGlobalAssetId();
        this.idShort = digitalTwin.getIdShort();
        this.setSearchId(digitalTwin, searchIdSchema);
        this.digitalTwin = digitalTwin;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGlobalAssetId() {
        return globalAssetId;
    }

    public void setGlobalAssetId(String globalAssetId) {
        this.globalAssetId = globalAssetId;
    }

    public String getIdShort() {
        return idShort;
    }

    public void setIdShort(String idShort) {
        this.idShort = idShort;
    }

    public String getPath() {
        return path;
    }
    public void setPath(Node parentNode, String id) {
        this.path = parentNode.getPath()+"/"+id;
    }
    public void setPath(String parentPath, String id) {
        this.path = parentPath+"/"+id;
    }

    public DigitalTwin getDigitalTwin() {
        return digitalTwin;
    }

    public void setDigitalTwin(DigitalTwin digitalTwin) {
        this.digitalTwin = digitalTwin;
    }

    public Map<String, Node> getChildren() {
        return children;
    }

    public void setChildren(Map<String, Node> children) {
        this.children = children;
    }
    public void setChild(Node childNode, String globalAssetId){
        this.children.put(globalAssetId, childNode);
    }

    public Node getChild(String childId){
        return this.children.get(childId);
    }


    public void setPath(String path) {
        this.path = path;
    }

    public JobResponse getJob() {
        return job;
    }

    public void setJob(JobResponse job) {
        this.job = job;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public void setSearchId(DigitalTwin digitalTwin, String idSchema) {
        this.searchId = CatenaXUtil.buildDppSearchId(digitalTwin, idSchema);
    }
}
