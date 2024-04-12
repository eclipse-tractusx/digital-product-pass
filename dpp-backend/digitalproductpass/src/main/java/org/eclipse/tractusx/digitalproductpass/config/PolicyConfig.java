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

package org.eclipse.tractusx.digitalproductpass.config;

import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * This class consists exclusively to define the attributes and methods needed for the Digital Twin Registry (DTR) configuration.
 **/
@Configuration
public class PolicyConfig {

    PermissionConfig permission;


    /**
     * This class consists exclusively to define the attributes and methods needed for edc policy permissions object inside a policy.
     **/
    public static class PermissionConfig {

        List<ConstraintConfig>  constraints;


        public PermissionConfig() {

        }

        /**
         * This class consists exclusively to define the attributes and methods needed for edc policy constraints inside a permission.
         **/
        public static class ConstraintConfig {

            String leftOperand;
            String operator;
            String rightOperand;

            public ConstraintConfig() {

            }

            public ConstraintConfig(String leftOperand, String operator, String rightOperand) {
                this.leftOperand = leftOperand;
                this.operator = operator;
                this.rightOperand = rightOperand;
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
