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
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DidDocument;

import java.util.List;

/**
 * This class responsible for mapping the logic constraints from a policy set
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogicConstraint extends Constraint {

    /** ATTRIBUTES **/

    enum LogicType {
        AND, // Represents the "and" constraint
        OR, // Represents the "or" constraint
        NONE // No logic constraint available
    }
    @JsonProperty("odrl:and")
    @JsonAlias({"and","odrl:and"})
    List<Constraint> andOperator;
    @JsonProperty("odrl:or")
    @JsonAlias({"or","odrl:or"})
    List<Constraint> orOperator;


    /** CONSTRUCTOR(S) **/
    public LogicConstraint(String leftOperand, DidDocument operator, String rightOperand, List<Constraint> andOperator, List<Constraint> orOperator) {
        super(leftOperand, operator, rightOperand);
        this.andOperator = andOperator;
        this.orOperator = orOperator;
    }

    public LogicConstraint(List<Constraint> andOperator, List<Constraint> orOperator) {
        this.andOperator = andOperator;
        this.orOperator = orOperator;
    }

    public LogicConstraint() {
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

    // Find which logic operation has the policy constraints
    public LogicType getLogicType(){
        boolean condition1 = (this.orOperator == null || this.orOperator.isEmpty());
        boolean condition2 = (this.andOperator == null || this.andOperator.isEmpty());
        if (condition1&&condition2)
            return LogicType.NONE;
        if (condition1)
            return LogicType.AND;
        return LogicType.OR;
    }

}
