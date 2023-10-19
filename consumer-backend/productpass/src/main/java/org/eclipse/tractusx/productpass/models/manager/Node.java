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

package org.eclipse.tractusx.productpass.models.manager;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.tractusx.productpass.models.dtregistry.DigitalTwin;
import org.eclipse.tractusx.productpass.models.irs.JobResponse;

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
    @JsonProperty("path")
    public String path;
    @JsonProperty("digitalTwin")
    public DigitalTwin digitalTwin;
    @JsonProperty("job")
    public JobResponse job;
    @JsonProperty("children")
    public Map<String, Node> children;

    /** CONSTRUCTOR(S) **/
    public Node(Node parent, DigitalTwin digitalTwin, Map<String, Node> children){
        this.setup(digitalTwin);
        this.setPath(parent, digitalTwin.getIdentification());
        this.children = children;
    }

    public Node(String parentPath, DigitalTwin digitalTwin, Map<String, Node> children){
        this.setup(digitalTwin);
        this.setPath(parentPath, digitalTwin.getIdentification());
        this.children = children;
    }

    public Node(DigitalTwin digitalTwin, Map<String, Node> children){
        this.setup(digitalTwin);
        this.setPath("", digitalTwin.getIdentification());
        this.children = children;
    }
    public Node(Node parent, DigitalTwin digitalTwin){
        this.setup(digitalTwin);
        this.setPath(parent, digitalTwin.getIdentification());
        this.children = Map.of();
    }

    public Node(String parentPath, DigitalTwin digitalTwin){
        this.setup(digitalTwin);
        this.setPath(parentPath, digitalTwin.getIdentification());
        this.children = Map.of();
    }

    public Node(DigitalTwin digitalTwin){
        this.setup(digitalTwin);
        this.setPath("", digitalTwin.getIdentification());
        this.children = Map.of();
    }

    public Node(String id, String globalAssetId, String idShort, String path, DigitalTwin digitalTwin, JobResponse job, Map<String, Node> children) {
        this.id = id;
        this.globalAssetId = globalAssetId;
        this.idShort = idShort;
        this.path = path;
        this.digitalTwin = digitalTwin;
        this.job = job;
        this.children = children;
    }

    public Node(String id, String globalAssetId, String idShort, String path, DigitalTwin digitalTwin, Map<String, Node> children) {
        this.id = id;
        this.globalAssetId = globalAssetId;
        this.idShort = idShort;
        this.path = path;
        this.digitalTwin = digitalTwin;
        this.children = children;
    }

    public Node() {
    }

    /** GETTERS AND SETTERS **/

    public void setup(DigitalTwin digitalTwin){
        this.id = digitalTwin.getIdentification();
        this.globalAssetId = digitalTwin.getGlobalAssetId();
        this.idShort = digitalTwin.getIdShort();
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
}
