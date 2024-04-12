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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * This class consists exclusively to define the attributes and methods needed for the Digital Twin Registry (DTR) configuration.
 **/
@Configuration
public class PolicyConfig {

    /** ATTRIBUTES **/

    List<Policies> policies;

    /** CONSTRUCTOR(S) **/
    public PolicyConfig() {
    }

    public PolicyConfig(List<Policies> policies) {
        this.policies = policies;
    }

    /** GETTERS AND SETTERS **/
    public List<Policies> getPolicies() {
        return policies;
    }

    public void setPolicies(List<Policies> policies) {
        this.policies = policies;
    }


    /** INNER CLASSES **/

    /**
     * This class consists exclusively to define the attributes and methods needed for the EDC policies configuration.
     **/
    public static class Policies {

        /** ATTRIBUTES **/
        List<UsagePolicies> usagePolicies;
        List<AccessPolicies> accessPolicies;

        /** CONSTRUCTOR(S) **/
        public Policies() {

        }

        public Policies(List<UsagePolicies> usagePolicies, List<AccessPolicies> accessPolicies) {
            this.usagePolicies = usagePolicies;
            this.accessPolicies = accessPolicies;
        }

        /** GETTERS AND SETTERS **/
        public List<UsagePolicies> getUsagePolicies() {
            return usagePolicies;
        }

        public void setUsagePolicies(List<UsagePolicies> usagePolicies) {
            this.usagePolicies = usagePolicies;
        }

        public List<AccessPolicies> getAccessPolicies() {
            return accessPolicies;
        }

        public void setAccessPolicies(List<AccessPolicies> accessPolicies) {
            this.accessPolicies = accessPolicies;
        }
    }

    /**
     * This class consists exclusively to define the attributes and methods needed to process usage policies for asset.
     **/
    static class UsagePolicies {

        /** ATTRIBUTES **/

        String leftOperand;
        String operator;
        String rightOperand;

        /** CONSTRUCTOR(S) **/
        public UsagePolicies() {
        }

        public UsagePolicies(String leftOperand, String operator, String rightOperand) {
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

        //        public UsagePolicies(List<Operator> constraints) {
//            this.constraints = constraints;
//        }
//
//        /** GETTERS AND SETTERS **/
//        public List<Operator> getConstraints() {
//            return constraints;
//        }
//
//        public void setConstraints(List<Operator> constraints) {
//            this.constraints = constraints;
//        }
    }

    /**
     * This class consists exclusively to define the attributes and methods needed to process access policies for DTR.
     **/
    static class AccessPolicies {

        /** ATTRIBUTES **/
        String leftOperand;
        String operator;
        String rightOperand;
//        List<Operator> constraints;

        /** CONSTRUCTOR(S) **/
        public AccessPolicies() {
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

        //        public AccessPolicies(List<Operator> constraints) {
//            this.constraints = constraints;
//        }
//        /** GETTERS AND SETTERS **/
//        public List<Operator> getConstraints() {
//            return constraints;
//        }
//
//        public void setConstraints(List<Operator> constraints) {
//            this.constraints = constraints;
//        }
    }
    /**
     * This class consists exclusively to define the attributes and methods needed for the OPERATOR.
     **/
    static class Operator {
        List<AndOperator> andOperator;

        public Operator(List<AndOperator> andOperator) {
            this.andOperator = andOperator;
        }

        public Operator() {
        }

        public List<AndOperator> getAndOperator() {
            return andOperator;
        }

        public void setAndOperator(List<AndOperator> andOperator) {
            this.andOperator = andOperator;
        }
    }

    /**
     * This class consists exclusively to define the attributes and methods needed for the operands of AND OPERATOR.
     **/
    static class AndOperator{
        String leftOperand;
        String operator;
        String rightOperand;

        public AndOperator(String leftOperand, String operator, String rightOperand) {
            this.leftOperand = leftOperand;
            this.operator = operator;
            this.rightOperand = rightOperand;
        }

        public AndOperator() {
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
