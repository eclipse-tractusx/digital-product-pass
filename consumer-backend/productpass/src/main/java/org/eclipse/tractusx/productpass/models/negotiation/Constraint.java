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

package org.eclipse.tractusx.productpass.models.negotiation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Constraint {
    @JsonProperty("odrl:target")
    String target;

    @JsonProperty("odrl:action")
    Action action;

    public Constraint(String target, Action action, List<Operator> constraints) {
        this.target = target;
        this.action = action;
        this.constraints = constraints;
    }

    public Constraint() {
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
    static class Action{
        @JsonProperty("odrl:type")
        String type;

        public Action(String type) {
            this.type = type;
        }

        public Action() {
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public List<Operator> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<Operator> constraints) {
        this.constraints = constraints;
    }

    @JsonProperty("odrl:constraint")
    List<Operator> constraints;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class Operator{

        @JsonProperty("odrl:or")
        List<OrOperator> orOperator;

        public Operator(List<OrOperator> orOperator) {
            this.orOperator = orOperator;
        }

        public Operator() {
        }

        public List<OrOperator> getOrOperator() {
            return orOperator;
        }

        public void setOrOperator(List<OrOperator> orOperator) {
            this.orOperator = orOperator;
        }

        static class OrOperator{
            @JsonProperty("odrl:leftOperand")
            String leftOperand;
            @JsonProperty("odrl:operator")
            String operator;
            @JsonProperty("odrl:rightOperand")
            String rightOperand;

            public OrOperator(String leftOperand, String operator, String rightOperand) {
                this.leftOperand = leftOperand;
                this.operator = operator;
                this.rightOperand = rightOperand;
            }

            public OrOperator() {
            }

            public String getLeftOperand() {
                return leftOperand;
            }

            public void setLeftOperand(String leftOperand) {
                this.leftOperand = leftOperand;
            }

            public String getOperator() {
                return operator;
            }

            public void setOperator(String operator) {
                this.operator = operator;
            }

            public String getRightOperand() {
                return rightOperand;
            }

            public void setRightOperand(String rightOperand) {
                this.rightOperand = rightOperand;
            }
        }
    }

}
