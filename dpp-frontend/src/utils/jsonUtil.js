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

export default {
    exists(key, json) {
        try {
            if (Object.prototype.hasOwnProperty.call(json, key)) {
                return true;
            }
        } catch {
            //Skip
        }
        return false;
    },
    copy(json) {
        let returnValue = null;
        let err = false;
        try {
            Object.assign(json, returnValue); // Copy object with JS
        } catch {
            err = true;
        }
        if (err || returnValue == null) {
            try {
                returnValue = JSON.parse(JSON.stringify(json)); // If can not copy with JS copy with JSON
            } catch {
                throw new Error("Failed to copy Json");
            }
        }
        return returnValue;
    },
    get(ref, json, sep = ".", defaultReturn = null) {
        try {
            let tmpValue = json;
            let i = 0;
            let refs = ref.split(sep);
            while (i < refs.length) {
                const part = refs[i];
                if (!Object.prototype.hasOwnProperty.call(tmpValue, part)) {
                    return defaultReturn;
                }
                tmpValue = tmpValue[part];
                i++;
            }
            return tmpValue;
        } catch {
            return defaultReturn;
        }
    },
    getUniqueId(originalKey, json) {
        let uniqueKey = (" " + originalKey).slice(1); // Deep Copy String
        let i = 0;
        while (Object.prototype.hasOwnProperty.call(json, uniqueKey)) {
            // Search if the key exists in the JSON
            uniqueKey = originalKey + "_" + i; // If exists we add a number and check again
            i++;
        }
        return uniqueKey; //Return unique id
    },
    toJson(json){
        return JSON.parse(json);
    },
    toJsonString(json){
        return JSON.stringify(json);
    },
    flatternJsonAttributes(json, attributes=[], sep=".", allowNull = false, allowEmpty = false) {
        if (json == null) return null;
        if (!(json instanceof Object)) return json;
        // Deep Copy param into objects
        let objects = JSON.parse(JSON.stringify(json));
        if (!(objects instanceof Object)) return json;
        let retObjects = {}; // Return/Final Object
        let keys = attributes;

        while (keys.length > 0) {
            // While it still has keys
            for (let index in keys) {

                // Interate over keys
                let parentKey = keys[index]; // Get key value in array
                let parent = this.get(parentKey, objects, sep, null); // Get current node value
    
                if (parent == null) {
                    // If nulls are not allowed
                    continue;
                }

                let tmpObjects = this.deleteDeepKey(parentKey, objects, sep, null); // Delete current node from interation object
                if(tmpObjects == null){
                    continue;
                }
                objects = this.deepCopy(tmpObjects);

                if (!(parent instanceof Object)) {
                    // If current node is not a object
                    // Check if key is not existing
                    let cleanParentKey = this.getUniqueId(parentKey, retObjects);
                    retObjects[cleanParentKey] = parent; // Store value (string, int, array, etc...) (if is not object)
                    continue;
                }

                let tmpCleanParent = {}; // Clean parent without children
                for (let childKey in parent) {
                    // Interate over children
                    let child = parent[childKey]; // Get children

                    if (child == null && !allowNull) {
                        // If nulls are not allowed
                        continue;
                    }

                    if (!(child instanceof Object)) {
                        // If children is not a object is a property from the father
                        // Check if key is not existing
                        let childstoreKey = this.getUniqueId(childKey, tmpCleanParent);
                        tmpCleanParent[childstoreKey] = child; // Store property on father
                        continue;
                    }
                    //Child is a object
                    if (allowEmpty || Object.keys(child).length > 0) {
                        // If the children has keys store in the objects
                        // Check if key is not existing
                        let childstoreKey = this.getUniqueId(childKey, objects);
                        objects[childstoreKey] = child; // Store in the interation objects
                    }
                }

                if (allowEmpty || Object.keys(tmpCleanParent).length > 0) {
                    // If the father has content
                    let storeKey = this.getUniqueId(parentKey, retObjects);
                    retObjects[storeKey] = tmpCleanParent; // Store the father in the return objects
                }
            }
        }
        return retObjects; // Return clean objects
    },
    flatternJson(json, allowNull = false, allowEmpty = false) {
        if (json == null) return null;
        if (!(json instanceof Object)) return json;
        // Deep Copy param into objects
        let objects = JSON.parse(JSON.stringify(json));
        if (!(objects instanceof Object)) return json;
        let retObjects = {}; // Return/Final Object
        let keys = Object.keys(objects); // Keys that it contains

        while (keys.length > 0) {
            // While it still has keys
            for (let index in keys) {
                // Interate over keys
                let parentKey = keys[index]; // Get key value in array
                let parent = objects[parentKey]; // Get current node value
                delete objects[parentKey]; // Delete current node from interation object

                if (parent == null && !allowNull) {
                    // If nulls are not allowed
                    continue;
                }

                if (!(parent instanceof Object)) {
                    // If current node is not a object
                    // Check if key is not existing
                    let cleanParentKey = this.getUniqueId(parentKey, retObjects);
                    retObjects[cleanParentKey] = parent; // Store value (string, int, array, etc...) (if is not object)
                    continue;
                }

                let tmpCleanParent = {}; // Clean parent without children
                for (let childKey in parent) {
                    // Interate over children
                    let child = parent[childKey]; // Get children

                    if (child == null && !allowNull) {
                        // If nulls are not allowed
                        continue;
                    }

                    if (!(child instanceof Object)) {
                        // If children is not a object is a property from the father
                        // Check if key is not existing
                        let childstoreKey = this.getUniqueId(childKey, tmpCleanParent);
                        tmpCleanParent[childstoreKey] = child; // Store property on father
                        continue;
                    }
                    //Child is a object
                    if (allowEmpty || Object.keys(child).length > 0) {
                        // If the children has keys store in the objects
                        // Check if key is not existing
                        let childstoreKey = this.getUniqueId(childKey, objects);
                        objects[childstoreKey] = child; // Store in the interation objects
                    }
                }

                if (allowEmpty || Object.keys(tmpCleanParent).length > 0) {
                    // If the father has content
                    let storeKey = this.getUniqueId(parentKey, retObjects);
                    retObjects[storeKey] = tmpCleanParent; // Store the father in the return objects
                }
            }
            if (objects)
                // If objects is not undefined continue interation
                keys = Object.keys(objects); // Look for the keys again
        }
        return retObjects; // Return clean objects
    },
    buildPath(parentKey, key, sep = ".") {
        return [parentKey, key].join(sep);
    },
    mapJson(json, allowNull = false, allowEmpty = false) {
        if (json == null) return null;
        if (!(json instanceof Object)) return json;
        // Deep Copy param into object
        let objects = JSON.parse(JSON.stringify(json));
        let retKeys = []; // Return/Final Object
        let keys = Object.keys(objects); // Keys that it contains

        while (keys.length > 0) {
            // While it still has keys
            for (let index in keys) {
                // Interate over keys
                let parentKey = keys[index]; // Get key value in array
                let parent = objects[parentKey]; // Get current node value
                delete objects[parentKey]; // Delete current node from interation object

                if (parent == null && !allowNull) {
                    // If nulls are not allowed
                    continue;
                }

                if (!(parent instanceof Object)) {
                    // If current node is not a object
                    let cleanParentKey = parentKey;
                    retKeys.push(cleanParentKey);
                    continue;
                }

                let tmpCleanParent = {}; // Clean parent without children
                for (let childKey in parent) {
                    // Interate over children
                    let child = parent[childKey]; // Get children

                    if (child == null && !allowNull) {
                        // If nulls are not allowed
                        continue;
                    }

                    if (!(child instanceof Object)) {
                        // If children is not a object is a property from the father
                        // Check if key is not existing
                        let childstoreKey = this.buildPath(parentKey, childKey);
                        retKeys.push(childstoreKey); // Store property on father
                        continue;
                    }
                    //Child is a object
                    if (allowEmpty || Object.keys(child).length > 0) {
                        // If the children has keys store in the objects
                        // Check if key is not existing
                        let childstoreKey = this.buildPath(parentKey, childKey);
                        objects[childstoreKey] = child; // Store in the interation objects
                    }
                }

                if (allowEmpty || Object.keys(tmpCleanParent).length > 0) {
                    // If the father has content
                    let cleanParentKey = parentKey;
                    retKeys.push(cleanParentKey);
                }
            }
            if (objects)
                // If objects is not undefined continue interation
                keys = Object.keys(objects); // Look for the keys again
        }
        return retKeys; // Return clean objects
    },
    isIn(sourceObj, key) {
        try {
            if (Object.prototype.hasOwnProperty.call(sourceObj, key)) {
                return true;
            }
        } catch {
            //do nothing
        }
        return false;
    },
    extend(oldJson, newJson) {
        let tmpOldObj = this.copy(oldJson);

        try {
            Object.assign(tmpOldObj, newJson); // Copy object with JS

            return tmpOldObj;
        } catch {
            return oldJson;
        }
    },
    deleteKeys(sourceObj, keys) {
        let tempSourceObj = this.copy(sourceObj);
        for (let i = 0; i < keys.length; i++) {
            let element = keys[i];
            if (!this.isIn(tempSourceObj, element)) {
                continue
            }
            delete tempSourceObj[element];
        }
        return tempSourceObj;
    },
    deleteDeepKey(ref,json,  sep = ".", defaultReturn = null ) {
        try {
            let tmpJson = this.copy(json);
            let refs = ref.split(sep);
            if(refs.length == 1){
                if(!Object.prototype.hasOwnProperty.call(tmpJson, ref)){
                    throw new Error("deleteDeepKey: Key [" + ref + "] is not defined in json");
                }
                delete tmpJson[ref]
                return tmpJson;
            }
            let lastRef = refs.pop();
            let parentPath = refs.join(sep);
            let parent = this.get(parentPath, json, sep, null);
            if(parent == null){
                throw new Error("deleteDeepKey: Parent [" + parentPath + "] does not exist!");
            }
            if(!Object.prototype.hasOwnProperty.call(parent, lastRef)){
                throw new Error("deleteDeepKey: Key [" + lastRef + "] does not exists in parent!");
            }
            delete parent[lastRef];

            return this.set(parentPath, parent,tmpJson, sep, defaultReturn);
        } catch {
            return defaultReturn;
        }
    },
    set(ref, data, json, sep = ".", defaultReturn = null){
        try{
            let tmpObject = {};
            let refs = ref.split(sep);
            if(refs.length == 1){
                tmpObject = this.copy(json);
                tmpObject[ref] = data;
                return tmpObject;
            }
            let currentPath = JSON.parse(JSON.stringify(refs));
            let tmpValue = data;
            let part;
            let parentPath;
            let tmpParent = null;
            for(let i = refs.length - 1; i >= 0; i--){
                tmpObject = {}
                part = refs[i];
                currentPath = currentPath.filter(e => e !== part);
                parentPath = currentPath.join(sep);
                tmpParent = this.get(parentPath, json, sep, {});
                tmpObject[part] = tmpValue;
                tmpParent = this.extend(tmpParent, tmpObject);
                tmpValue = tmpParent;
            }
            if(tmpParent == null){
                return defaultReturn;
            }
            return this.extend(json, tmpParent);
        }catch {
            return defaultReturn;
        }
    },
    extendDeep(originJson, json){
        if(!json){
            return originJson;
        }
        if (!(json instanceof Object)) return originJson;
        // Deep Copy param into object
        let objects = JSON.parse(JSON.stringify(json));
        let retObject = JSON.parse(JSON.stringify(originJson)); // Return/Final Object
        let keys = Object.keys(objects); // Keys that it contains

        while (keys.length > 0) {
            // While it still has keys
            for (let index in keys) {
                // Interate over keys
                let parentKey = keys.pop(index); // Get key value in array
                let parent = this.get(parentKey, objects, ".", null); // Get current node value

                if (parent == null) {
                    // Skip null objects
                    continue;
                }

                if (!(parent instanceof Object)) {
                    // If current node is not a object
                    retObject = this.set(parentKey, parent, retObject);
                    continue;
                }

                for (let childKey in parent) {
                    // Interate over children
                    let child = parent[childKey]; // Get children
                    
                    if (child == null) {
                        // Skip null children
                        continue;
                    }
                    let childstoreKey = this.buildPath(parentKey, childKey);
                    if (!(child instanceof Object)) {
                        // If children is not a object is a property from the father
                        // Check if key is not existing
                        retObject = this.set(childstoreKey, child, retObject);
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
