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

package org.eclipse.tractusx.productpass.managers;

import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.tractusx.productpass.config.IrsConfig;
import org.eclipse.tractusx.productpass.exceptions.DataModelException;
import org.eclipse.tractusx.productpass.exceptions.ManagerException;
import org.eclipse.tractusx.productpass.models.catenax.EdcDiscoveryEndpoint;
import org.eclipse.tractusx.productpass.models.dtregistry.DigitalTwin;
import org.eclipse.tractusx.productpass.models.irs.Job;
import org.eclipse.tractusx.productpass.models.irs.JobHistory;
import org.eclipse.tractusx.productpass.models.irs.JobResponse;
import org.eclipse.tractusx.productpass.models.irs.Relationship;
import org.eclipse.tractusx.productpass.models.manager.Node;
import org.eclipse.tractusx.productpass.models.manager.NodeComponent;
import org.eclipse.tractusx.productpass.models.manager.Status;
import org.eclipse.tractusx.productpass.models.negotiation.Negotiation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Component
public class TreeManager {
    private FileUtil fileUtil;
    private JsonUtil jsonUtil;

    private ProcessManager processManager;

    private IrsConfig irsConfig;

    private final String PATH_SEP = "/";
    @Autowired
    public TreeManager(FileUtil fileUtil, JsonUtil jsonUtil, ProcessManager processManager, IrsConfig irsConfig) {
        this.fileUtil = fileUtil;
        this.jsonUtil = jsonUtil;
        this.processManager = processManager;
        this.irsConfig = irsConfig;
    }

    public String getTreeFilePath(String processId){
        return processManager.getProcessFilePath(processId, this.irsConfig.getTree().getFileName());
    }
    public Boolean treeExists(String processId){
        String path = this.getTreeFilePath(processId);
        return fileUtil.pathExists(path);
    }
    public String createTreeFile(String processId){
        try {
            return this.saveTree(processId, Map.of()); // Save the tree
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName()+".newTree()", e, "It was not possible to create the tree data model file");
        }
    }
    public static String generateSearchId(String processId, String globalAssetId) {
        return CrypUtil.md5(DateTimeUtil.getDateTimeFormatted("yyyyMMddHHmmssSSS") + processId + globalAssetId);
    }
    public Map<String, Node> getTree(String processId){
        try {
            String path = this.getTreeFilePath(processId); // Get filepath from tree
            if(!fileUtil.pathExists(path)){
                this.createTreeFile(processId); // Create empty tree
                return Map.of();
            }
            return (Map<String, Node>) jsonUtil.fromJsonFileToObject(path, Map.class); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName()+".getTree()", e, "It was not possible to load the tree");
        }
    }


    public List<NodeComponent> parseChildren(Map<String, Node> children){
        List<NodeComponent> components = new ArrayList<>(); // Create a component list
        if(children == null || children.size() == 0){
            return components; // Stop condition. Empty list when no children are available
        }
        List<Node> rawChildren = null;
        try {
            rawChildren = (List<Node>) jsonUtil.bindReferenceType(jsonUtil.mapToList(children), new TypeReference<List<Node>>() {});
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "Could not bind the reference type for the Node!");
        }
        if(rawChildren == null){
            LogUtil.printWarning(this.getClass().getName() + ".parseChildren() It was not possible to parse children as a list of Nodes");
            return components;
        }
        rawChildren.forEach(
                k -> {
                    List<NodeComponent> parsedChildren = this.parseChildren(k.getChildren()); // Parse the children below
                    NodeComponent childComponent = new NodeComponent(k,parsedChildren); // Add the existing children to a node component
                    components.add(childComponent); // Add to the list of components
                }
        );
        return components; // Return the components
    }


    public List<NodeComponent> recursiveParseChildren(Map<String, Node> currentNodes){
        try {
            return this.parseChildren(currentNodes);
        }catch (Exception e) {
                throw new ManagerException(this.getClass().getName()+".recursiveParseChildren()", e, "It was not possible to parse tree of nodes");
            }

    }

    public List<NodeComponent> getTreeComponents(String processId){
        try {
            Map<String, Node> treeDataModel = this.getTree(processId); // Get filepath from tree
            return this.recursiveParseChildren(treeDataModel);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName()+".getTreeComponents()", e, "It was not possible to get the tree components!");
        }
    }

    public DigitalTwin searchDigitalTwin(List<DigitalTwin> digitalTwinList, String digitalTwinId){
        // Use a parallel search to make the search faster
        return digitalTwinList.parallelStream().filter(digitalTwin -> digitalTwin.getGlobalAssetId().equals(digitalTwinId)).findFirst().orElse(new DigitalTwin());
    }

    public String populateTree(Map<String, Node> treeDataModel, String processId, JobHistory jobHistory, JobResponse job){
        try {
            List<Relationship> relationships = job.getRelationships();
            String parentPath = jobHistory.getPath();
            Node parent = this.getNodeByPath(treeDataModel, parentPath);
            // All the relationships will be of depth one, so we just need to add them in the parent
            List<DigitalTwin> digitalTwinList = null;
            try {
                digitalTwinList = (List<DigitalTwin>) jsonUtil.bindReferenceType(job.getShells(), new TypeReference<List<DigitalTwin>>() {});
            } catch (Exception e) {
                throw new ManagerException(this.getClass().getName(), e, "Could not bind the reference type for the Digital Twin!");
            }

            for(Relationship relationship : relationships){
                String childId = relationship.getLinkedItem().getChildCatenaXId();
                // Search for the Digital Twin from the child or a new instance
                DigitalTwin childDigitalTwin = this.searchDigitalTwin(digitalTwinList, childId);
                if(childDigitalTwin.getGlobalAssetId().isEmpty()){
                    childDigitalTwin.setGlobalAssetId(childId);
                }
                // Create child with the digital twin
                Node child = new Node(childDigitalTwin);
                // Add child to the parent
                parent.setChild(child);
            }
            // Set node and save the tree
            treeDataModel = this.setNodeByPath(treeDataModel, parentPath, parent); // Save the parent node in the tree
            return this.saveTree(processId, treeDataModel);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName() + ".setChild()", e, "It was not possible to get the node from the tree");
        }
    }
    public String populateTree(String processId, JobHistory jobHistory, JobResponse job){
        try {
            Map<String, Node> treeDataModel = this.getTree(processId);
            return this.populateTree(treeDataModel,processId, jobHistory, job);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName() + ".setChild()", e, "It was not possible to get the node from the tree");
        }
    }

    public String saveTree(String processId, Map<String, Node> treeDataModel){
        try {
            String path = this.getTreeFilePath(processId); // Get filepath from tree
            return jsonUtil.toJsonFile(
                    path,
                    treeDataModel,
                    this.irsConfig.getTree().getIndent()
            ); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName()+".saveTree()", e, "It was not possible to save the tree data model file");
        }
    }
    public Node getNodeByPath(Map<String, Node> treeDataModel, String path){
        try {
            String translatedPath = jsonUtil.translatePathSep(path, PATH_SEP, ".children."); // Join the path with the children
            return (Node) this.jsonUtil.getValue(treeDataModel, translatedPath, ".", null); // Get the node
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName()+".getNodeByPath()", e, "It was not possible to get the node from the tree");
        }
    }
    public Node getNodeByPath(String processId, String path){
        try {
            Map<String, Node> treeDataModel = this.getTree(processId);
            String translatedPath = jsonUtil.translatePathSep(path, PATH_SEP, ".children."); // Join the path with the children
            return (Node) this.jsonUtil.getValue(treeDataModel, translatedPath, ".", null); // Get the node
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName()+".getNodeByPath()", e, "It was not possible to get the node from the tree");
        }
    }
    public Map<String, Node> setNodeByPath(Map<String, Node> treeDataModel, String path, Node node){
        try {
            String translatedPath = jsonUtil.translatePathSep(path, PATH_SEP, ".children."); // Join the path with the children
            treeDataModel = (Map<String, Node>) this.jsonUtil.setValue(treeDataModel, translatedPath, node, ".", null); // Set the node
            if(treeDataModel == null){ // Check if the response was successful
                throw new ManagerException(this.getClass().getName()+".setNodeByPath()", "It was not possible to set the node in path because the return was null");
            }
            return treeDataModel;
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName()+".setNodeByPath()", e, "It was not possible to get the node from the tree");
        }
    }
    public Object setNodeByPath(String processId, String path, Node node){
        try {
            Map<String, Node> treeDataModel = this.getTree(processId);
            String translatedPath = jsonUtil.translatePathSep(path, PATH_SEP, ".children."); // Join the path with the children
            treeDataModel = (Map<String, Node>) this.jsonUtil.setValue(treeDataModel, translatedPath, node, ".", null); // Set the node
            if(treeDataModel == null){ // Check if the response was successful
                throw new ManagerException(this.getClass().getName()+".setNodeByPath()", "It was not possible to set the node in path because the return was null");
            }
            return this.saveTree(processId, treeDataModel);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName()+".setNodeByPath()", e, "It was not possible to get the node from the tree");
        }
    }

    public Object setChild(String processId, String parentPath, Node childNode){
        try {
            Map<String, Node> treeDataModel = this.getTree(processId);
            Node parentNode = this.getNodeByPath(treeDataModel, parentPath); // Get parent node
            parentNode.setChild(childNode); // Add the child to the parent node
            treeDataModel = this.setNodeByPath(treeDataModel, parentPath, parentNode); // Save the parent node in the tree
            return this.saveTree(processId, treeDataModel);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName()+".setChild()", e, "It was not possible to get the node from the tree");
        }
    }





}
