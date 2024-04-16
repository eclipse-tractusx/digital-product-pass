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

import javax.annotation.Nullable;
import java.util.List;

/**
 * This class consists exclusively to define the attributes and methods needed for the Digital Twin Registry (DTR) configuration.
 * Based on the <a href="https://github.com/catenax-eV/cx-odrl-profile">https://github.com/catenax-eV/cx-odrl-profile</a> configuration.
 **/
@Configuration
public class PolicyConfig {

    Boolean enabled;
    // Permissions for usage and access
    // Check https://github.com/catenax-eV/cx-odrl-profile for policy configuration guidelines
    List<ActionConfig> permission;
    List<ActionConfig> obligation; // Obligations can also be configured
    List<ActionConfig> prohibition; // Prohibitions can also be configured

    public PolicyConfig(Boolean enabled, List<ActionConfig> permission, List<ActionConfig> obligation, List<ActionConfig> prohibition) {
        this.enabled = enabled;
        this.permission = permission;
        this.obligation = obligation;
        this.prohibition = prohibition;
    }

    public PolicyConfig() {
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<ActionConfig> getPermission() {
        return permission;
    }

    public void setPermission(List<ActionConfig> permission) {
        this.permission = permission;
    }

    public List<ActionConfig> getObligation() {
        return obligation;
    }

    public void setObligation(List<ActionConfig> obligation) {
        this.obligation = obligation;
    }

    public List<ActionConfig> getProhibition() {
        return prohibition;
    }

    public void setProhibition(List<ActionConfig> prohibition) {
        this.prohibition = prohibition;
    }


    /**
     * This class consists exclusively to define the attributes and methods needed for edc policy permissions object inside a policy.
     **/
    public static class ActionConfig {

        String action;

        @Nullable
        String logicConstraint;

        List<ConstraintConfig> constraints;


        public ActionConfig() {

        }

        public ActionConfig(String prefix, List<ConstraintConfig> constraints) {
            this.action = prefix;
            this.constraints = constraints;
        }

        public ActionConfig(String prefix, @Nullable String logicConstraint, List<ConstraintConfig> constraints) {
            this.action = prefix;
            this.logicConstraint = logicConstraint;
            this.constraints = constraints;
        }

        public String getPrefix() {
            return action;
        }

        public void setPrefix(String prefix) {
            this.action = prefix;
        }

        public List<ConstraintConfig> getConstraints() {
            return constraints;
        }

        public void setConstraints(List<ConstraintConfig> constraints) {
            this.constraints = constraints;
        }

        @Nullable
        public String getLogicConstraint() {
            return logicConstraint;
        }

        public void setLogicConstraint(@Nullable String logicConstraint) {
            this.logicConstraint = logicConstraint;
        }
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
