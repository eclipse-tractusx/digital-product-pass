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

package org.eclipse.tractusx.digitalproductpass.models.negotiation.policy;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ModelException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class responsible for mapping the logic constraints from a policy set
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Action {

    /**
     * ATTRIBUTES
     **/
    @JsonProperty("odrl:action")
    @JsonAlias({"action", "odrl:action"})
    ActionType action;
    @JsonProperty("odrl:constraint")
    @JsonAlias({"constraint", "odrl:constraint"})
    LogicalConstraint constraint;


    /**
     * CONSTRUCTOR(S)
     **/

    public Action() {
    }

    public Action(ActionType action, LogicalConstraint constraint) {
        this.action = action;
        this.constraint = constraint;
    }
    public Action(PolicyCheckConfig.ActionConfig actionConfig) {
        this.buildAction(actionConfig);
    }
    /* METHODS */

     /**
     * Method responsible for parsing the action based on the configuration
     * <p>
     * @param  actionConfig {@code PolicyCheckConfig.ActionConfig} instance representing the action configuration
     *
     */
    public void buildAction(PolicyCheckConfig.ActionConfig actionConfig){
        // Create clean list of constraints
        addAction(actionConfig.getAction());
        this.constraint = new LogicalConstraint(actionConfig);
    }
    /**
     * GETTERS AND SETTERS
     **/
    public String retrieveAction() {
        if(this.action == null){
            return null;
        }
        return this.action.getType();
    }
    public void addAction(String action) {
        this.action = new ActionType();
        this.action.setType(action);
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public LogicalConstraint getConstraint() {
        return constraint;
    }

    public void setConstraint(LogicalConstraint constraint) {
        this.constraint = constraint;
    }


    /**
     * Method responsible for comparing two actions
     * <p>
     * @param  action {@code Action} is the object to be compared
     * @return true if the actions is the same
     */
    public Boolean compare(Action action){
        try{
            if(action == null){return false;} // If action is null not continue
            if(!action.retrieveAction().equalsIgnoreCase(this.retrieveAction())){return false;} // If actions strings are not the same
            return action.getConstraint().compare(this.getConstraint()); //If constraints are the same
        }catch (Exception e){
            throw new ModelException(this.getClass().getName(), e, "It was not possible to compare the actions!");
        }
    }
    /**
     * Builds an action from a raw policy object
     * <p>
     *
     * @param node {@code JsonNode} action to be checked
     * @return {@code List<Action>} the list of actions parsed
     * @throws ModelException if error when parsing the contracts
     */
    static public List<Action> build(JsonNode node){
        ObjectMapper mapper = new ObjectMapper();
        // If node is not array parse a single action object
        if(!node.isArray()){
            return new ArrayList<>(){{add(mapper.convertValue(node, new TypeReference<>(){}));}};
        }
        // If node is array parse the action node as a list
        return mapper.convertValue(node, new TypeReference<>(){});
    }

    static class ActionType{
        @JsonProperty("odrl:type")
        @JsonAlias({"type", "odrl:type", "@type"})
        String type;

        public ActionType(String type) {
            this.type = type;
        }

        public ActionType() {
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}