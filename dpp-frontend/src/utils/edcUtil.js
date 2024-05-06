/**
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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
 */

import jsonUtil from "@/utils/jsonUtil.js";

export default {
    parsePolicyConstraints(policy) {
        /**
         * Parses and simplifies the permissions, prohibitions and obligations of a given policy according to https://www.w3.org/ns/odrl/2
         * @param {object} policy - Contains the policy element that needs to be parsed and simplified
         * 
         * @returns {object} - Contains the permissions, prohibions and obligations of a policy normalized and parsed
         * @throws {Error} - When an excpetion is produced when parsint the policy
         */
        try {
            if (policy == null || policy == {} || policy.length == 0) {
                throw new Error("The incomming policy is empty!");
            }
            // Initialize policy constraints based on the odrl parameters
            // https://www.w3.org/ns/odrl/2/ODRL22.json
            let permissions = [];
            let prohibitions = [];
            let obligations = [];

            // Get policy constraints and normalize them based on the orld attributes
            permissions = this.parseProperties(jsonUtil.get("odrl:permission", policy, ".", []));
            prohibitions = this.parseProperties(jsonUtil.get("odrl:prohibitions", policy, ".", []));
            obligations = this.parseProperties(jsonUtil.get("odrl:obligations", policy, ".", []));

            // Returned normalized policy constraints for visualization
            return {
                "permissions": permissions,
                "prohibitions": prohibitions,
                "obligations": obligations
            }
        } catch (e) {
            throw new Error("It was not possible to parse the policy constraints [" + e.message + "]!");
        }
    },
    parseProperties(property, defaultValue = []) {
        /**
         * Parses and simplifies the permissions, prohibitions and obligations according to https://www.w3.org/ns/odrl/2
         * @param {object} property - Contains the permissions, prohibitions or obligations
         * @param {array|object|string} defaultValue - Default value to be returned when failed
         * 
         * @returns {object} - Contains the parsed constrains and the action type of the property
         */
        try {
            // Check for empty values and return default value
            if (property == null || property == {} || property.length == 0) {
                return defaultValue;
            }
            // Init variables
            let parsedProperty = []

            // If just one property is available do the parsing for just this value
            if (typeof property === 'object' && !Array.isArray(property)) {
                parsedProperty.push({
                    "actionType": this.parseActionType(jsonUtil.get("odrl:action", property, ".", null)),
                    "constraints": this.parseContraints(jsonUtil.get("odrl:constraint", property, ".", []))
                })
                return parsedProperty;
            }

            // Do the parsing for every value if more then one are available
            property.forEach((propertyValue) => {
                parsedProperty.push({
                    "actionType": this.parseActionType(jsonUtil.get("odrl:action", propertyValue, ".", null)),
                    "constraints": this.parseContraints(jsonUtil.get("odrl:constraint", propertyValue, ".", []))
                });
            });

            // Parse action type and the constraints associated with the permissions
            return parsedProperty;
        } catch (e) {
            console.error("It was not possible to parse the policy permissions [" + e.message + "]!");
            return defaultValue;
        }

    },
    parseActionType(actionType, defaultValue = null) {
        /**
         * Parses and simplifies the action type according to https://www.w3.org/ns/odrl/2
         * @param {object|string} actionType - Contains the action type object or string
         * @param {array|object|string} defaultValue - Default value to be returned when failed
         * 
         * @returns {string} - Simplied action type as string
         */
        try {
            // Check for empty values and return default value
            if (actionType == null || actionType == {} || actionType.length == 0) {
                return defaultValue;
            }
            // If attribute is an object it needs to contain the type
            if (typeof actionType === 'object' && !Array.isArray(actionType)) {
                return jsonUtil.get("odrl:type", actionType, ".", null)
            }
            // If the action is a string or value it needs to be added like it is
            return actionType;
        } catch (e) {
            console.error("It was not possible to parse the policy action type [" + e.message + "]!");
            return defaultValue;
        }
    },
    parseContraints(constraints, defaultValue = []) {
        /**
         * Parses and simplifies a list of one or more constaints with operator according to the https://www.w3.org/ns/odrl/2
         * @param {object} constraints - Contraints to be parsed with operator
         * @param {array|object|string} defaultValue - Default value to be returned when failed
         * 
         * @returns {object} - Object containing simplified contraint operator and list of constaints
         */
        try {
            // Check for empty values and return default value
            if (constraints == null || constraints == {} || constraints.length == 0) {
                return defaultValue;
            }
            // Initialized the values
            let parsedConstraints = [];
            let constraintsList = [];
            let operator = null;
            let possibleOperators = [
                "odrl:or",
                "odrl:and",
                "odrl:xone",
                "odrl:andSequence"
            ]

            // Get the first operator that appears (shall always be one available)
            operator = possibleOperators.find((selectedOperator) => jsonUtil.exists(selectedOperator, constraints));
            // No special operator is available so constraints will be in raw available
            if (!operator) {
                // Return no operator and the constraints
                let parsedConstraint = this.parseConstraint(constraints); //Parse the constraint
                if (!parsedConstraint || parsedConstraint == []) return; // If is null or not available do not store
                parsedConstraints.push(parsedConstraint); // Add to parsed constraint if it worked
                return {
                    "operator": null,
                    "constraint": parsedConstraints
                }
            }

            //Get list of constraints from operator
            constraintsList = jsonUtil.get(operator, constraints, ".", []);
            constraintsList.forEach((constraint) => {
                let parsedConstraint = this.parseConstraint(constraint); //Parse the constraint
                if (!parsedConstraint || parsedConstraint == []) return; // If is null or not available do not store
                parsedConstraints.push(parsedConstraint); // Add to parsed constraint if it worked
            });

            // Return the operator
            return {
                "operator": operator,
                "constraint": parsedConstraints
            }

        } catch (e) {
            console.error("It was not possible to parse the policy constraints [" + e.message + "]!");
            return defaultValue;
        }
    },
    parseConstraint(constraint, defaultValue = null) {
        /**
         * Parses and simplifies a constaint according to the https://www.w3.org/ns/odrl/2
         * @param {object} constraint - Contraint to be parsed
         * @param {array|object|string} defaultValue - Default value to be returned when failed
         * 
         * @returns {object} - Object containing simplified leftOperand, operator, and rightOperand as strings
         */
        try {
            // Check for empty constraint
            if (constraint == null || constraint == {} || constraint.length == 0) {
                return defaultValue;
            }
            //Initialize the variables
            let leftOperand = "";
            let rightOperand = "";
            let operator = "";

            //Check if the properties exist
            if (!jsonUtil.exists("odrl:leftOperand", constraint) || !jsonUtil.exists("odrl:rightOperand", constraint) || !jsonUtil.exists("odrl:operator", constraint)) {
                throw new Error("The constraint structure is not valid, because of missing attributes"); // Throw an exception that will be caught and handled as an error
            }

            //Check for operand types
            let rawOperator = jsonUtil.get("odrl:operator", constraint, ".", null);
            operator = rawOperator;
            if (typeof rawOperator !== "string") {
                operator = jsonUtil.get("@id", operator, ".", null)
            }

            // Parse the left operand
            let rawLeftOperand = jsonUtil.get("odrl:leftOperand", constraint, ".", null);
            leftOperand = rawLeftOperand;
            // If is not a string convert the object or array to string
            if (typeof rawLeftOperand !== "string") {
                leftOperand = jsonUtil.toJsonString(rawLeftOperand)
            }

            // Parse the right operand
            let rawRightOperand = jsonUtil.get("odrl:rightOperand", constraint, ".", null);
            rightOperand = rawRightOperand;
            // If is not a string convert the object or array to string
            if (typeof rawRightOperand !== "string") {
                rightOperand = jsonUtil.toJsonString(rawRightOperand)
            }

            // Return parsed constraint
            return {
                "leftOperand": leftOperand,
                "operator": operator,
                "rightOperand": rightOperand
            }

        } catch (e) {
            console.error("It was not possible to parse the policy constraint [" + e.message + "]!");
            return defaultValue;
        }
    }
};
