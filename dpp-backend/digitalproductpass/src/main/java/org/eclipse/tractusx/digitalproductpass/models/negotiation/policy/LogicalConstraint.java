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

import com.fasterxml.jackson.annotation.*;
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
    enum LogicType{
        AND,
        OR,
        NONE
    }

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


    // Find which logic operation has the policy constraints
    public LogicType findLogicalConstraint(){
        boolean condition1 = (this.orOperator == null || this.orOperator.isEmpty());
        boolean condition2 = (this.andOperator == null || this.andOperator.isEmpty());
        if (condition1&&condition2)
            return LogicType.NONE;
        if (condition1)
            return LogicType.AND;
        return LogicType.OR;
    }

    /** GETTERS AND SETTERS **/

    /**
     * Method responsible for comparing two logical constraints
     * <p>
     * @param  logicalConstraint {@code LogicalConstraint} is the object to be compared
     * @return true if the logicalConstraint is the same
     */
    public Boolean compare(LogicalConstraint logicalConstraint){
        try{
            // Get new logicalConstraint logic
            LogicType logic = logicalConstraint.findLogicalConstraint();
            if(logic != this.findLogicalConstraint()){return false;} // In case the logic is not the same as the currenct logic
            // Compare logic constraints
            switch (logic){
                case AND -> {return this.compareLogicalConstraint(this.getAndOperator(), logicalConstraint.getAndOperator());}
                case OR -> {return this.compareLogicalConstraint(this.getOrOperator(), logicalConstraint.getOrOperator());}
                default -> {return this.compareConstraint(logicalConstraint);}
            }
        }catch (Exception e){
            throw new ModelException(this.getClass().getName(), e, "It was not possible to compare the logical constraint!");
        }
    }
    /**
     * Method responsible for comparing two logical constraints
     * <p>
     * @param  logicalConstraint {@code List<Constraint>} is the list of constraints to be compared
     * @param  incomingConstraints {@code List<Constraint>} is the list of constraints to be compared to
     * @return true if the list of constraints is the same
     */
    public Boolean compareLogicalConstraint(List<Constraint> logicalConstraint, List<Constraint> incomingConstraints) {
        try{
            // Optimizations to avoid searching in children
            if(logicalConstraint == null && incomingConstraints == null){return true; } // If both constraints are null they are equal
            if(logicalConstraint == null || incomingConstraints == null){return false; }// If one of be options are null and they both are not than they are not the same
            if(logicalConstraint.size() != incomingConstraints.size()){return false;} // If the constraints are with different sizes
            if(logicalConstraint.isEmpty()){return true; }// If the constraints are empty they are the same and have the same size if one is empty both are empty

            List<Boolean> validConstraints = new ArrayList<>();
            // Compare constraints against each other
            incomingConstraints.stream().parallel().forEach(c -> {logicalConstraint.stream().parallel().forEach(co -> {if(c.compareConstraint(co)) {validConstraints.add(true);}});});
            return validConstraints.size() == logicalConstraint.size(); // If the number of constraints is the same as the size of the current constraint they are the same.
        }catch (Exception e){
            throw new ModelException(this.getClass().getName(), e, "It was not possible to compare the list of logical constraints!");
        }
    }

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
