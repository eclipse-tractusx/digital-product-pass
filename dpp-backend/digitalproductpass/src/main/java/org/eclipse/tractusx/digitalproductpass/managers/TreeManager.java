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

package org.eclipse.tractusx.digitalproductpass.managers;

import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.tractusx.digitalproductpass.config.IrsConfig;
import org.eclipse.tractusx.digitalproductpass.config.PassportConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ManagerException;
import org.eclipse.tractusx.digitalproductpass.models.dtregistry.DigitalTwin;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobHistory;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobResponse;
import org.eclipse.tractusx.digitalproductpass.models.irs.Relationship;
import org.eclipse.tractusx.digitalproductpass.models.manager.Node;
import org.eclipse.tractusx.digitalproductpass.models.manager.NodeComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * This class consists exclusively of methods to operate on managing the Tree Structure for the IRS component.
 *
 * <p> The methods defined here are intended to do every needed operations to run the processes.
 *
 */
@Component
public class TreeManager {

    /** ATTRIBUTES **/
    private FileUtil fileUtil;
    private JsonUtil jsonUtil;
    private ProcessManager processManager;
    private PassportConfig passportConfig;
    private IrsConfig irsConfig;
    private final String PATH_SEP = "/";

    /** CONSTRUCTOR(S) **/
    @Autowired
    public TreeManager(FileUtil fileUtil, JsonUtil jsonUtil, ProcessManager processManager, IrsConfig irsConfig, PassportConfig passportConfig) {
        this.fileUtil = fileUtil;
        this.jsonUtil = jsonUtil;
        this.processManager = processManager;
        this.irsConfig = irsConfig;
        this.passportConfig = passportConfig;
    }

    /** METHODS **/

    /**
     * Gets the Tree File Path by processId
     * <p>
     * @param   processId
     *           the {@code String} id of the application's process.
     *
     * @return  a {@code String} Absolute file path for the tree file
     *
     */
    public String getTreeFilePath(String processId){
        return processManager.getProcessFilePath(processId, this.irsConfig.getTree().getFileName());
    }
    /**
     * Checks if the tree data model exists
     * <p>
     * @param   processId
     *          id from the process
     *
     * @return  a {@code Boolean} Confirms if the tree data model exists
     *
     */
    public Boolean treeExists(String processId){
        String path = this.getTreeFilePath(processId);
        return fileUtil.pathExists(path);
    }
    /**
     * Creates a new tree file
     * <p>
     * @param   processId
     *           the {@code String} id of the application's process.
     *
     * @return  a {@code String} Path to the tree file created
     *
     * @throws ManagerException
     *           if unable to create the file
     */
    public String createTreeFile(String processId){
        try {
            return this.saveTree(processId, Map.of()); // Save the tree
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName()+".newTree()", e, "It was not possible to create the tree data model file");
        }
    }
    /**
     * Generates a new unique md5 hash (searchId) which is bounded by the globalAssetId and the ProcessId
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   globalAssetId
     *          global asset id {@code String} found in the digital twin asset
     *
     * @return  a {@code String} Search id hash generated
     *
     */
    public static String generateSearchId(String processId, String globalAssetId) {
        return CrypUtil.md5(DateTimeUtil.getDateTimeFormatted("yyyyMMddHHmmssSSS") + processId + globalAssetId);
    }
    /**
     * Returns the tree component from the file system
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  a {@code Map<String, Node>} Map with the tree content
     *
     * @throws ManagerException
     *           if unable to get the tree
     */
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

    /**
     * Recursive function to parse children from a node data model structure
     * <p>
     * @param   children
     *          the {@code Map<String, Node>} children objects to translate to list
     *
     * @return  a {@code List<NodeComponent>} List of objects simplified from the tree
     *
     */
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

    /**
     * Recursive parsing of the current nodes, main parent function.
     * <p>
     * @param   currentNodes
     *          the {@code Map<String, Node>}  objects to be translated to a list of children
     *
     * @return  a {@code List<NodeComponent>} List of objects simplified from the tree
     *
     * @throws ManagerException
     *           if unable to parse the children recursively
     */
    public List<NodeComponent> recursiveParseChildren(Map<String, Node> currentNodes){
        try {
            return this.parseChildren(currentNodes);
        }catch (Exception e) {
                throw new ManagerException(this.getClass().getName()+".recursiveParseChildren()", e, "It was not possible to parse tree of nodes");
            }

    }
    /**
     * Gets the children components from the tree dataModel simplified
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  a {@code List<NodeComponent>} List of objects simplified from the tree
     *
     * @throws ManagerException
     *           if unable to get the tree components
     */
    public List<NodeComponent> getTreeComponents(String processId){
        try {
            Map<String, Node> treeDataModel = this.getTree(processId); // Get filepath from tree
            return this.recursiveParseChildren(treeDataModel);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName()+".getTreeComponents()", e, "It was not possible to get the tree components!");
        }
    }
    /**
     * This method can search a digital twin inside of a digital twin list by id
     * <p>
     * @param   digitalTwinList
     *          the {@code List<DigitalTwin>} id of the application's process.
     * @param   digitalTwinId
     *          the {@code String} id from the digital twin to be searched
     *
     * @return  a {@code DigitalTwin} from the search result OR {@code null} if not found
     *
     */
    public DigitalTwin searchDigitalTwin(List<DigitalTwin> digitalTwinList, String digitalTwinId){
        // Use a parallel search to make the search faster
        return digitalTwinList.parallelStream().filter(digitalTwin -> digitalTwin.getGlobalAssetId().equals(digitalTwinId)).findFirst().orElse(new DigitalTwin());
    }
    /**
     * Populates a tree with the Job content and the relationships from an specific data model
     * <p>
     *
     * @param   treeDataModel
     *          the {@code Map<String, Node>} tree data model to populate with children
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   jobHistory
     *          the {@code JobHistory} job history which contains the identification of the job
     * @param   job
     *          the {@code JobResponse} job response containing the IRS content
     *
     * @return  a {@code String} data model file path
     *
     * @throws ManagerException
     *           if unable to populate the tree
     */
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
            int childrenFound = relationships.size();
            this.processManager.setJobChildrenFound(processId, childrenFound);
            if(childrenFound == 0){
                parent.setChildren(null); // If there is no children return null;
                treeDataModel = this.setNodeByPath(treeDataModel, parentPath, parent); // Save the parent node in the tree
                return this.saveTree(processId, treeDataModel);
            }
            for(Relationship relationship : relationships){
                String childId = relationship.getLinkedItem().getChildCatenaXId();
                // Search for the Digital Twin from the child or a new instance
                DigitalTwin childDigitalTwin = this.searchDigitalTwin(digitalTwinList, childId);
                if(childDigitalTwin.getGlobalAssetId().isEmpty()){
                    childDigitalTwin.setGlobalAssetId(childId);
                }
                // Create child with the digital twin
                Node child = new Node(parentPath, childDigitalTwin, this.passportConfig.getSearchIdSchema());
                // Add child to the parent
                parent.setChild(child, childId);
            }
            // Set node and save the tree
            treeDataModel = this.setNodeByPath(treeDataModel, parentPath, parent); // Save the parent node in the tree
            return this.saveTree(processId, treeDataModel);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName() + ".setChild()", e, "It was not possible to get the node from the tree");
        }
    }
    /**
     * Populates a tree with the Job content and the relationships from the data model in the process file
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   jobHistory
     *          the {@code JobHistory} job history which contains the identification of the job
     * @param   job
     *          the {@code JobResponse} job response containing the IRS content
     *
     * @return  a {@code String} data model file path
     *
     * @throws ManagerException
     *           if unable to populate the tree
     */
    public String populateTree(String processId, JobHistory jobHistory, JobResponse job){
        try {
            Map<String, Node> treeDataModel = this.getTree(processId);
            return this.populateTree(treeDataModel,processId, jobHistory, job);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName() + ".setChild()", e, "It was not possible to get the node from the tree");
        }
    }
    /**
     * Saves the tree data model in the processId file
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   treeDataModel
     *          the {@code Map<String, Node>} tree data model to be saved
     *
     * @return  a {@code String} data model file path
     *
     * @throws ManagerException
     *           if unable to save the tree data model
     */
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
    /**
     * Gets a node in the specified tree by path
     * <p>
     * @param   treeDataModel
     *          the {@code Map<String, Node>} tree data model in which the node will be searched
     * @param   path
     *          the {@code String} unique path separated by "/" of the node in the tree "example: /node1/node2"
     *
     * @return  a {@code Node} Node if found or {@code null} otherwise
     *
     * @throws ManagerException
     *           if unable to get the node by path
     */
    public Node getNodeByPath(Map<String, Node> treeDataModel, String path){
        try {
            String translatedPath = jsonUtil.translatePathSep(path, PATH_SEP, ".children."); // Join the path with the children
            Object rawNode = this.jsonUtil.getValue(treeDataModel, translatedPath, ".", null); // Get the node;
            Node node = null;
            try {
                node = (Node) jsonUtil.bindReferenceType(rawNode, new TypeReference<Node>() {});
            } catch (Exception e) {
                throw new ManagerException(this.getClass().getName(), e, "Could not bind the reference type for the Node!");
            }
            if(node == null){ // Check if the response was successful
                throw new ManagerException(this.getClass().getName()+".getNodeByPath()", "It was not possible to set the node in path because the return was null");
            }
            return node;
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName()+".getNodeByPath()", e, "It was not possible to get the node from the tree");
        }
    }
    /**
     * Gets a node in the specified tree by path
     * <p>
     * @param   processId
     *          the {@code String} process id in which the data model will be retrieved and then searched
     * @param   path
     *          the {@code String} unique path separated by "/" of the node in the tree "example: /node1/node2"
     *
     * @return  a {@code Node} Node if found or {@code null} otherwise
     *
     * @throws ManagerException
     *           if unable to get the node by path
     */
    public Node getNodeByPath(String processId, String path){
        try {
            Map<String, Node> treeDataModel = this.getTree(processId);
            String translatedPath = jsonUtil.translatePathSep(path, PATH_SEP, ".children."); // Join the path with the children
            Object rawNode = this.jsonUtil.getValue(treeDataModel, translatedPath, ".", null); // Get the node;
            Node node = null;
            try {
                node = (Node) jsonUtil.bindReferenceType(rawNode, new TypeReference<Node>() {});
            } catch (Exception e) {
                throw new ManagerException(this.getClass().getName(), e, "Could not bind the reference type for the Node!");
            }
            if(node == null){ // Check if the response was successful
                throw new ManagerException(this.getClass().getName()+".getNodeByPath()", "It was not possible to set the node in path because the return was null");
            }
            return node;
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName()+".getNodeByPath()", e, "It was not possible to get the node from the tree");
        }
    }
    /**
     * Gets a node in the specified tree by path
     * <p>
     * @param   treeDataModel
     *          the {@code Map<String, Node>} tree data model in which the node will be added
     * @param   path
     *          the {@code String} unique path separated by "/" of the node in the tree "example: /node1/node2"
     * @param   node
     *          the {@code Node} Node object which needs to be added in the data model
     *
     * @return  a {@code (Map<String, Node>)} Data Model in which the node was stored
     *
     * @throws ManagerException
     *           if unable to save in the data model the node
     */
    public Map<String, Node> setNodeByPath(Map<String, Node> treeDataModel, String path, Node node){
        try {
            String translatedPath = jsonUtil.translatePathSep(path, PATH_SEP, ".children."); // Join the path with the children
            Object rawDataModel =  this.jsonUtil.setValue(treeDataModel, translatedPath, node, ".", null); // Set the node
            try {
                treeDataModel = (Map<String, Node>) jsonUtil.bindReferenceType(rawDataModel, new TypeReference<Map<String, Node>>() {});
            } catch (Exception e) {
                throw new ManagerException(this.getClass().getName(), e, "Could not bind the reference type for the Node!");
            }
            if(treeDataModel == null){ // Check if the response was successful
                throw new ManagerException(this.getClass().getName()+".setNodeByPath()", "It was not possible to set the node in path because the return was null");
            }
            return treeDataModel;
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName()+".setNodeByPath()", e, "It was not possible to get the node from the tree");
        }
    }
    /**
     * Gets a node in the specified tree by path
     * <p>
     * @param   processId
     *          the {@code String} process id in which the data model will be retrieved and then substituted
     * @param   path
     *          the {@code String} unique path separated by "/" of the node in the tree "example: /node1/node2"
     * @param   node
     *          the {@code Node} Node object which needs to be added in the data model
     *
     * @return  a {@code String} Data Model File Path where the node was stored
     *
     * @throws ManagerException
     *           if unable to save in the data model the node
     */
    public String setNodeByPath(String processId, String path, Node node){
        try {
            Map<String, Node> treeDataModel = this.getTree(processId);
            String translatedPath = jsonUtil.translatePathSep(path, PATH_SEP, ".children."); // Join the path with the children
            Object rawDataModel =  this.jsonUtil.setValue(treeDataModel, translatedPath, node, ".", null); // Set the node
            try {
                treeDataModel = (Map<String, Node>) jsonUtil.bindReferenceType(rawDataModel, new TypeReference<Map<String, Node>>() {});
            } catch (Exception e) {
                throw new ManagerException(this.getClass().getName(), e, "Could not bind the reference type for the Tree Data Model!");
            }
            return this.saveTree(processId, treeDataModel);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName()+".setNodeByPath()", e, "It was not possible to get the node from the tree");
        }
    }
    /**
     * Gets a node in the specified tree by path
     * <p>
     * @param   processId
     *          the {@code String} process id in which the data model will be retrieved and then substituted
     * @param   parentPath
     *          the {@code String} parent node unique path separated by "/" of the node in the tree "example: /node1/node2"
     * @param   childNode
     *          the {@code Node} children node which will be added to the father
     *
     * @return  a {@code String} Data Model File Path where the node was stored
     *
     * @throws ManagerException
     *           if unable to save in the data model and in the parent the child node
     */
    public String setChild(String processId, String parentPath, Node childNode, String globalAssetId){
        try {
            Map<String, Node> treeDataModel = this.getTree(processId);
            Node parentNode = this.getNodeByPath(treeDataModel, parentPath); // Get parent node
            parentNode.setChild(childNode, globalAssetId); // Add the child to the parent node
            treeDataModel = this.setNodeByPath(treeDataModel, parentPath, parentNode); // Save the parent node in the tree
            return this.saveTree(processId, treeDataModel);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName()+".setChild()", e, "It was not possible to get the node from the tree");
        }
    }





}
