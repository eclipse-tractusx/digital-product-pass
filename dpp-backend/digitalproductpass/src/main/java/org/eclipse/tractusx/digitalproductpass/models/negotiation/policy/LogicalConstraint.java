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
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DidDocument;
import utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class responsible for mapping the logic constraints from a policy set
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogicalConstraint extends Constraint {

    /** ATTRIBUTES **/

    @JsonProperty("odrl:and")
    @JsonAlias({"and","odrl:and"})
    List<Constraint> andOperator;
    @JsonProperty("odrl:or")
    @JsonAlias({"or","odrl:or"})
    List<Constraint> orOperator;


    /** CONSTRUCTOR(S) **/
    public LogicalConstraint(String leftOperand, DidDocument operator, String rightOperand, List<Constraint> andOperator, List<Constraint> orOperator) {
        super(leftOperand, operator, rightOperand);
        this.andOperator = andOperator;
        this.orOperator = orOperator;
    }

    public LogicalConstraint(List<Constraint> andOperator, List<Constraint> orOperator) {
        this.andOperator = andOperator;
        this.orOperator = orOperator;
    }

    public LogicalConstraint() {
    }
    public LogicalConstraint(PolicyCheckConfig.ActionConfig actionConfig){
        this.buildLogicalConstraint(actionConfig);
    }
    /** METHODS **/

    /**
     * Method responsible for parsing the logical constraint based on the configuration
     * <p>
     * @param  actionConfig {@code PolicyCheckConfig.ActionConfig} instance representing the configuration for an action constraint
     *
     */
    public void buildLogicalConstraint(PolicyCheckConfig.ActionConfig actionConfig) {
        List<PolicyCheckConfig.ConstraintConfig> constraints = actionConfig.getConstraints();
        // Check if there is any constraints available for this action
        if(constraints == null || constraints.size() == 0){
            throw new ModelException(this.getClass().getName(), "There is no constraint available for this logic constraint, add only one!");
        }
        String logicalConstraint = actionConfig.getLogicalConstraint();
        if(logicalConstraint == null){
            // If more than one constraint is available print a warning message
            if(constraints.size() > 1){
                LogUtil.printWarning("For policy actions without logic constraints just the first defined constraint will be used!");
            }
            // Get first constraint
            PolicyCheckConfig.ConstraintConfig constraintConfig = constraints.get(0);

            // Map the first constraint defined to the operands
            this.buildConstraint(constraintConfig);

            return; // Finish the processing
        }
        List<Constraint> parsedConstraints = this.parseConstraints(constraints);

        // Apply the parsed constraints to the different policies
        switch (logicalConstraint) {
            case "odrl:and" -> this.andOperator = parsedConstraints;
            case "odrl:or" -> this.orOperator = parsedConstraints;
            default -> throw new ModelException(this.getClass().getName(),"The logic constraint [" + logicalConstraint + "] is not supported!");
        }

    }
    /**
     * Method responsible for parsing the logical constraint constraints based on the configuration
     * <p>
     * @param  constraints {@code PolicyCheckConfig.ConstraintConfig} instance representing the configuration for different constraints
     *
     */
    public List<Constraint> parseConstraints(List<PolicyCheckConfig.ConstraintConfig> constraints){
        // Create clean list of constraints
        List<Constraint> newConstraints = new ArrayList<>();
        constraints.forEach(c -> newConstraints.add(new Constraint(c))); //Build the different constraints
        return newConstraints;
    }

    /** GETTERS AND SETTERS **/



    public List<Constraint> getAndOperator() {
        return andOperator;
    }

    public void setAndOperator(List<Constraint> andOperator) {
        this.andOperator = andOperator;
    }

    public List<Constraint> getOrOperator() {
        return orOperator;
    }

    public void setOrOperator(List<Constraint> orOperator) {
        this.orOperator = orOperator;
    }

}
