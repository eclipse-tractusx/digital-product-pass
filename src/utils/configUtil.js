/**
 * Catena-X - Product Passport Consumer Frontend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
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
import passport from "@/config/templates/passport.json";
import metadata from "@/config/templates/metadata.json";

export default {
    /*getAttribute(attribute, sep = ".", defaultValue = null) {
        return jsonUtil.get(attribute, attributes, sep, defaultValue);
    },
    getAllAttributes() {
        return attributes;
    },
    filterAttribute(attributes) {
        if (!attributes) return null;
        if (!(attributes instanceof Object)) return attributes;
        let tmpAttributes = jsonUtil.copy(attributes);
        let tmpPropsData = {};
        for (let attr in tmpAttributes) {
            let tmpData = tmpAttributes[attr];

            if (tmpData == null) continue;
            tmpData = jsonUtil.flatternJson(tmpData);

            tmpPropsData = jsonUtil.extend(tmpPropsData, tmpData);
        }
        if (Object.keys(tmpPropsData).length < 0) return null;
        return tmpPropsData;
    }*/
    normalizePassport(responsePassport=null, responseMetadata=null){
        let response = {
            "metadata": metadata,
            "passport": passport
        }
        if(responsePassport){
            response["passport"] = this.extendPassport(responsePassport);
        }
        if(responseMetadata){
            response["metadata"] = jsonUtil.extend(metadata, responseMetadata);
        }
        return response;
    },
    extendPassport(responsePassport=null){
        if(!responsePassport){
            return passport;
        }
        if (!(responsePassport instanceof Object)) return passport;
        // Deep Copy param into object
        let objects = JSON.parse(JSON.stringify(responsePassport));
        let retObject = JSON.parse(JSON.stringify(passport)); // Return/Final Object
        let keys = Object.keys(objects); // Keys that it contains

        while (keys.length > 0) {
            // While it still has keys
            for (let index in keys) {
                // Interate over keys
                let parentKey = keys.pop(index); // Get key value in array
                let parent = jsonUtil.get(parentKey, objects, ".", null); // Get current node value

                if (parent == null) {
                    // Skip null objects
                    continue;
                }

                if (!(parent instanceof Object)) {
                    // If current node is not a object
                    retObject = jsonUtil.set(parentKey, parent, retObject);
                    continue;
                }

                for (let childKey in parent) {
                    // Interate over children
                    let child = parent[childKey]; // Get children
                    
                    if (child == null) {
                        // Skip null children
                        continue;
                    }
                    let childstoreKey = jsonUtil.buildPath(parentKey, childKey);
                    if (!(child instanceof Object)) {
                        // If children is not a object is a property from the father
                        // Check if key is not existing
                        retObject = jsonUtil.set(childstoreKey, child, retObject);
                        continue;
                    }
                    //Child is a object
                    if (Object.keys(child).length > 0) {
                        // Add Object children to interation
                        keys.push(childstoreKey);
                    }
                }

            }
        }
        return retObject; // Return clean objects



    }
};