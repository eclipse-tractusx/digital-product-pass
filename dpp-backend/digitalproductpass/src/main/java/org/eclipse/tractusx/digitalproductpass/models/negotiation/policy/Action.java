/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BASF SE, BMW AG, Henkel AG & Co. KGaA
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
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ModelException;

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
    String action;
    @JsonProperty("odrl:constraint")
    @JsonAlias({"constraint", "odrl:constraint"})
    LogicalConstraint constraint;


    /**
     * CONSTRUCTOR(S)
     **/

    public Action() {
    }

    public Action(String action, LogicalConstraint constraint) {
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
        this.action = actionConfig.getAction();
        this.constraint = new LogicalConstraint(actionConfig);
    }
    /**
     * GETTERS AND SETTERS
     **/
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
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
            if(!action.getAction().equalsIgnoreCase(this.getAction())){return false;} // If actions strings are not the same
            return action.getConstraint().compare(this.getConstraint()); //If constraints are the same
        }catch (Exception e){
            throw new ModelException(this.getClass().getName(), e, "It was not possible to compare the actions!");
        }
    }
}