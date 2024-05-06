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
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ModelException;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DidDocument;

import java.util.Objects;

/**
 * This class defined the specification from the EDC constraints
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Constraint {

    /** ATTRIBUTES **/
    @JsonProperty("odrl:leftOperand")
    @JsonAlias({"leftOperand","odrl:leftOperand"})
    String leftOperand;
    @JsonProperty("odrl:operator")
    @JsonAlias({"operator","odrl:operator"})
    DidDocument operator;
    @JsonProperty("odrl:rightOperand")
    @JsonAlias({"rightOperand","odrl:rightOperand"})
    String rightOperand;

    /** CONSTRUCTOR(S) **/
    public Constraint(String leftOperand, DidDocument operator, String rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
    }

    public Constraint() {
    }
    public Constraint(String leftOperand, String operator, String rightOperand){
        this.leftOperand = leftOperand;
        this.operator = new DidDocument();
        this.operator.setId(operator);
        this.rightOperand = rightOperand;
    }
    public Constraint(PolicyCheckConfig.ConstraintConfig constraintConfig) {
        this.buildConstraint(constraintConfig);
    }

    public void buildConstraint(PolicyCheckConfig.ConstraintConfig constraintConfig){
        this.leftOperand = constraintConfig.getLeftOperand();
        this.operator = new DidDocument();
        this.operator.setId(constraintConfig.getOperator());
        this.rightOperand = constraintConfig.getRightOperand();
    }

    /** GETTERS AND SETTERS **/
    public String getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(String leftOperand) {
        this.leftOperand = leftOperand;
    }

    public DidDocument getOperator() {
        return operator;
    }

    public void setOperator(DidDocument operator) {
        this.operator = operator;
    }

    public String getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(String rightOperand) {
        this.rightOperand = rightOperand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constraint that = (Constraint) o;
        return Objects.equals(leftOperand, that.leftOperand) && Objects.equals(operator, that.operator) && Objects.equals(rightOperand, that.rightOperand);
    }

    /**
     * Method responsible for comparing two constraints
     * <p>
     * @param  constraint {@code Constraint} is the object to be compared
     * @return true if the constraint is the same
     */
    public Boolean compareConstraint(Constraint constraint){
        try{
            if(!this.leftOperand.equalsIgnoreCase(constraint.getLeftOperand())){return false;} //If left operand is not the same as the left operand
            if(!this.operator.getId().equalsIgnoreCase(constraint.getOperator().getId())){return false;}//If operator id is not the same as the operator id
            return this.rightOperand.equalsIgnoreCase(constraint.getRightOperand());//If right operand is not the same as the right operand
        }catch (Exception e){
            throw new ModelException(this.getClass().getName(), e, "It was not possible to compare the constraints!");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftOperand, operator, rightOperand);
    }
    public String retrieveOperatorId(){
        return this.operator.getId();
    }
}
