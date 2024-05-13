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

package utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.exceptions.UtilException;
import org.apache.commons.lang3.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * This class consists exclusively of methods to operate on JSON type files.
 *
 * <p> The methods defined here are intended to load, save, parse and manipulate JSON files or objects.
 *
 */
@Component
public final class JsonUtil {

    private final FileUtil fileUtil;

    @Autowired
    public JsonUtil(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

        public ArrayList<Object> getObjectArray(Object... data){
        return new ArrayList<>(
                Arrays.asList(
                        data)
        );
    }

    /**
     * Loads the JSON file from the given file path and maps it to an object.
     * <p>
     * @param   jsonString
     *          the path representation to the target JSON file as a String.
     * @param   classType
     *          the class type to map the json structure from the file to an object.
     *
     * @return  a {@code Object} object mapped with the json file structure.
     *
     * @throws  UtilException
     *          if unable to load the JSON file.
     */
    public Object loadJson(String jsonString, Class<?> classType){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, classType);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to load JSON in object -> [" + e.getMessage() + "]");
        }
    }

    /**
     * Loads the JSON file from the given file path as a String.
     * <p>
     * @param   jsonString
     *          the path representation to the target JSON file as a String.
     *
     * @return  a {@code String} object with the json file data.
     *
     * @throws  UtilException
     *          if unable to load the JSON file.
     */
    public String escapeJson(String jsonString){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);
            mapper.getFactory().configure(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature(), true);
            return mapper.writeValueAsString(jsonString);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to escape JSON -> [" + e.getMessage() + "]");
        }
    }

    /**
     * Parses the JSON file from the given file path as an Object.
     * <p>
     * @param   jsonString
     *          the path representation to the target JSON file as a String.
     *
     * @return  a {@code Object} object with the json file data.
     *
     * @throws  UtilException
     *          if unable to load the JSON file.
     */
    public Object parseJson(String jsonString){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, Object.class);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to parse JSON! -> [" + e.getMessage() + "]");
        }
    }

    /**
     * Parses the JSON file from the given file path and maps it to the specified Class type.
     * <p>
     * @param   jsonString
     *          the path representation to the target JSON file as a String.
     * @param   bindClass
     *          the class type to map the json structure from the file to an object.
     *
     * @return  a {@code Object} object mapped with the json file structure.
     *
     * @throws  UtilException
     *          if unable to load the JSON file.
     */
    public Object parseJson(String jsonString, Class<?> bindClass){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, bindClass);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to parse JSON! -> [" + e.getMessage() + "]");
        }
    }

    @SuppressWarnings("Unused")
    public ObjectNode newJson(){
        return JsonNodeFactory.instance.objectNode();
    }

    /**
     * Creates an object of JsonNode type.
     * <p>
     *
     * @return  a {@code JsonNode} object.
     *
     */
    public JsonNode newJsonNode(){
        return JsonNodeFactory.instance.objectNode();
    }

    /**
     * Checks if the given JSON file path is actually a JSON file by checking the components tree.
     * <p>
     * @param   jsonString
     *          the path representation to the target JSON file as a String.
     *
     * @return  true if the file is a JSON file and false otherwise or if some exception is thrown.
     *
     */
    public Boolean isJson(String jsonString){
        try {
            ObjectMapper mapper = new ObjectMapper()
                    .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
            mapper.readTree(jsonString);
        } catch (JacksonException e) {
            return false;
        }
        return true;
    }

    /**
     * Parses the given object into a JSON type structure with or without a pretty print.
     * <p>
     * @param   json
     *          the object data to save into a JSON file.
     * @param   prettyPrint
     *          boolean to specify a pretty or compact structure in the JSON file.
     *
     * @return  a {@code String} object representing the given object data with a JSON type structure.
     *
     * @throws  UtilException
     *          if unable to load the JSON file.
     */
    public String toJson(Object json, Boolean prettyPrint){
        try {
            ObjectMapper mapper = new ObjectMapper();
            if(prettyPrint) {
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                mapper.writerWithDefaultPrettyPrinter();
            };
            return mapper.writeValueAsString(json);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to dump JSON! -> [" + e.getMessage() + "]");
        }
    }

    /**
     * Parses the given object into a JSON file with or without a pretty print.
     * <p>
     * @param   path
     *          the path representation to the target JSON file as a String.
     * @param   json
     *          the object data to save into a JSON file.
     * @param   prettyPrint
     *          boolean to specify a pretty or compact structure in the JSON file.
     *
     * @return  a {@code String} object representing the path to the JSON file.
     *
     * @throws  UtilException
     *          if unable to load the JSON file.
     */
    synchronized public String toJsonFile(String path, Object json, Boolean prettyPrint){
        try {
            return fileUtil.toFile(path, this.toJson(json, prettyPrint), false);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to create JSON file ["+path+"]! -> [" + e.getMessage() + "]");
        }
    }

    /**
     * Parses the given JSON file path to an Object.
     * <p>
     * @param   path
     *          the path representation to the target JSON file as a String.
     *
     * @return  a {@code Object} object mapped with the json file structure.
     *
     * @throws  UtilException
     *          if unable to load the JSON file.
     */
    @SuppressWarnings("Unused")
    synchronized public Object fromJsonFile(String path){
        try {
            String fileContent = fileUtil.readFile(path);
            return this.parseJson(fileContent);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to create JSON file ["+path+"]! -> [" + e.getMessage() + "]");
        }
    }

    /**
     * Parses the given JSON file path to an object of the specified Class type.
     * <p>
     * @param   path
     *          the path representation to the target JSON file as a String.
     * @param   bindClass
     *          the class type to map the json structure from the file to an object.
     *
     * @return  a {@code Object} object mapped with the json file structure.
     *
     * @throws  UtilException
     *          if unable to load the JSON file.
     */
    synchronized public Object fromJsonFileToObject(String path, Class<?> bindClass){
        try {
            if(!fileUtil.pathExists(path)) {
                LogUtil.printError("The file does not exists in [" + path + "]!");
                return null;
            }
            String fileContent = fileUtil.readFile(path);
            return this.parseJson(fileContent, bindClass);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to create JSON file ["+path+"]! -> [" + e.getMessage() + "]");
        }
    }

    /**
     * Gets a specific value from a JSON file
     * <p>
     * @param   path
     *          the {@code String}  path representation to the target JSON file as a String.
     * @param   keyPath
     *          a {@code String} load specific attribute path
     * @param   pathSep
     *          the {@code String} path separator used
     * @param   defaultValue
     *          the {@code Object} default value returned when the {@code keyPath} is not existing
     * @return  a {@code Object} of the intended path or a default values object
     *
     * @throws  UtilException
     *          if unable to load the JSON file.
     */
    synchronized public Object getFileValue(String path, String keyPath, String pathSep, Object defaultValue){
        try {
            if(!fileUtil.pathExists(path)) {
                LogUtil.printError("The file does not exists in [" + path + "]!");
                return null;
            }
            String fileContent = fileUtil.readFile(path);
            Object json = this.parseJson(fileContent, Object.class);
            return this.getValue(json, keyPath, pathSep, defaultValue);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to get JSON file ["+path+"]! -> [" + e.getMessage() + "]");
        }
    }

    /**
     * Sets a value in a JSON file
     * <p>
     * @param   path
     *          the {@code String}  path representation to the target JSON file as a String.
     * @param   keyPath
     *          a {@code String} load specific attribute path
     * @param   keyValue
     *          the {@code Object} to be stored in the key path
     * @param   pathSep
     *          the {@code String} path separator used
     * @param   prettyPrint
     *          the {@code Boolean} pretty printing condition
     *
     * @return  a {@code Object} of the intended path or a default values object
     *
     * @throws  UtilException
     *          if unable to load the JSON file.
     */
    synchronized public String setFileValue(String path, String keyPath, String pathSep, Object keyValue, Boolean prettyPrint){
        try {
            if(!fileUtil.pathExists(path)) {
                LogUtil.printError("The file does not exists in [" + path + "]!");
                return null;
            }
            String fileContent = fileUtil.readFile(path);
            Object rawJson = this.parseJson(fileContent, Object.class);
            Object parseJson = this.setValue(rawJson, keyPath, keyValue, pathSep, null);
            if(parseJson == null){
                LogUtil.printError("It was not possible to save the value ["+keyPath+"] in the JSON file!");
                return null;
            }
            return this.toJsonFile(path, parseJson, prettyPrint);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to get JSON file ["+path+"]! -> [" + e.getMessage() + "]");
        }
    }

    /**
     * Checks if a given key exists within the given object.
     * <p>
     * @param   sourceObj
     *          Map, JsonNode or other Object able to parse to a Map type object.
     * @param   key
     *          the key name to lookup for in the given object.
     *
     * @return  true if the key exists in the given object, false otherwise.
     *
     * @throws  UtilException
     *          if unable to parse the given object to a Map type class.
     */
    public Boolean keyExists(Object sourceObj,String key){
        try {
            if(sourceObj == null){
                //Uncomment for debug logTools.printError("[DEBUG] Object == null!");
                return false;
            }
            if(key == null || key.isEmpty()){
                //Uncomment for debug logTools.printError("[DEBUG] keyPath empty or pathSep empty!");
                return false;
            }
            Map<String, Object> tmpValue;
            ObjectMapper mapper = new ObjectMapper();
            try {
                tmpValue = mapper.convertValue(sourceObj, new TypeReference<Map<String, Object>>() {});
                return tmpValue.containsKey(key);
            }catch (Exception e) {
                return false;
            }
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, e,  "It was not possible to check if the json key exists!");
        }
    }

    /**
     * Checks if a given key exists within the given object with depper search.
     * <p>
     * @param   sourceObj
     *          Map, JsonNode or other Object able to parse to a Map type object.
     * @param   key
     *          the key name to lookup for in the given object.
     * @param   pathSep
     *          separator used to read keyPath like "." or "/" for example
     * @param   allowEmpty
     *          when true checks if the key exists even if the value is empty.
     *
     * @return  true if the key exists in the given object, false otherwise.
     *
     * @throws  UtilException
     *          if unable to parse the given object to a Map type class.
     */
    @SuppressWarnings("Unused")
    public Boolean keyExistsDeep(Object sourceObj, String key, String pathSep, Boolean allowEmpty){
        try {
            if(sourceObj == null){
                //Uncomment for debug logTools.printError("[DEBUG] Object == null!");
                return false;
            }
            if(key == null || key.isEmpty() || pathSep.equals("")){
                //Uncomment for debug logTools.printError("[DEBUG] keyPath empty or pathSep empty!");
                return false;
            }
            Object trigger = null;

            trigger = this.getValue(sourceObj, key, pathSep, null);
            if(trigger == null){
                return false;
            }
            return allowEmpty || !trigger.equals("");
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, e,  "It was not possible to check for json keys!");
        }
    }

    /**
     * Checks if a list of given keys exists within the given object.
     * <p>
     * @param   sourceObj
     *          Map, JsonNode or other Object able to parse to a Map type object.
     * @param   keyPaths
     *          list of keys to look for in the given object.
     * @param   pathSep
     *          separator used to read keyPath like "." or "/" for example
     * @param   allowEmpty
     *          when true checks if the keys exists even if the value is empty.
     *
     * @return  true if all the keys exists in the given object, false otherwise.
     *
     * @throws  UtilException
     *          if unable to parse the given object to a Map type class.
     */
    public Boolean checkJsonKeys(Object sourceObj, List<String> keyPaths, String pathSep, Boolean allowEmpty){
        try {
            if(sourceObj == null){
                //Uncomment for debug logTools.printError("[DEBUG] Object == null!");
                return false;
            }
            if(keyPaths == null || keyPaths.isEmpty() || pathSep.equals("")){
                //Uncomment for debug logTools.printError("[DEBUG] keyPath empty or pathSep empty!");
                return false;
            }
            Object trigger = null;
            for (String keyPath : keyPaths) {

                trigger = this.getValue(sourceObj, keyPath, pathSep, null);
                if(trigger == null){
                    return false;
                }
                if(!allowEmpty && trigger.equals("")){
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, e,  "It was not possible to check for json keys!");
        }
    }


    /**
     * Gets the value of a given key from the given object.
     * <p>
     * @param   sourceObj
     *          Map, JsonNode or other Object able to parse to a Map type object.
     * @param   keyPath
     *          Path to the key in the given object.
     * @param   pathSep
     *          separator used to read keyPath like "." or "/" for example
     * @param   defaultValue
     *          to specify a default value if the value for the given keyPath doesn't exist.
     *
     * @return  an {@Code Object} value of the given key or the default value if an error occurs.
     *
     * @throws  UtilException
     *          if unable to parse the given object to a Map type class.
     */
    public Object getValue(Object sourceObj, String keyPath, String pathSep, Object defaultValue){
        try {
            if(sourceObj == null){
                //Uncomment for debug logTools.printError("[DEBUG] Object == null!");
                return defaultValue;
            }
            if(keyPath == null || keyPath.equals("") || pathSep.equals("")){
                //Uncomment for debug logTools.printError("[DEBUG] keyPath empty or pathSep empty!");
                return defaultValue;
            }
            String[] parts = keyPath.split(String.format("\\%s",pathSep));
            if(parts.length < 1){
                //Uncomment for debug logTools.printError("[DEBUG] Not able to split path!");
                return defaultValue;
            }
            Map<String, Object> tmpValue;
            Object tmpObject = sourceObj;
            ObjectMapper mapper = new ObjectMapper();
            for (String part : parts) {

                try{
                    tmpValue = mapper.convertValue(tmpObject, new TypeReference<Map<String, Object>>(){});
                }catch (Exception e){
                    LogUtil.printError("[DEBUG] It was not possible to parse to map");
                    return defaultValue;
                }
                if (!tmpValue.containsKey(part)) {
                    //Uncomment for debug logTools.printError("[DEBUG] Key is not available");
                    return defaultValue;
                }
                tmpObject = tmpValue.get(part);
            }
            return tmpObject;
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to get value in path ["+keyPath+"] -> [" + e.getMessage() + "]");
        }
    }

    /**
     * Sets the value of a given key from the given object.
     * <p>
     * @param   sourceObj
     *          Map, JsonNode or other Object able to parse to a Map type object.
     * @param   keyPath
     *          Path to the key in the given object.
     * @param   keyValue
     *          The value to update.
     * @param   pathSep
     *          separator used to read keyPath like "." or "/" for example
     * @param   defaultValue
     *          to specify a default value if the value for the given keyPath doesn't exist.
     *
     * @return  an {@Code Object} of type Map updated with the given value.
     *
     * @throws  UtilException
     *          if unable to parse the given object to a Map type class.
     */
    public Object setValue(Object sourceObj, String keyPath, Object keyValue, String pathSep, Object defaultValue){
        try {
            if(sourceObj == null){
                //Un comment for debug logTools.printError("[DEBUG] Object == null!");
                return defaultValue;
            }
            if(keyPath == null || keyPath.equals("") || pathSep.equals("")){
                //Un comment for debug logTools.printError("[DEBUG] keyPath empty or pathSep empty!");
                return defaultValue;
            }

            if(keyValue== null){
                //Un comment for debug logTools.printError("[DEBUG] keyValue empty or pathSep empty!");
                return defaultValue;
            }
            Map<String, Object> tmpObject = null;
            String[] parts = keyPath.split(String.format("\\%s",pathSep));
            ObjectMapper mapper = new ObjectMapper();

            // If is simple path just add it
            if(parts.length == 1){
                try{
                    tmpObject = mapper.convertValue(sourceObj, new TypeReference<Map<String, Object>>(){});
                }catch (Exception e){
                    //Uncomment for debug logTools.printError("[DEBUG] It was not possible to parse to map");
                    return defaultValue;
                }
                tmpObject.put(keyPath, keyValue);
                return tmpObject;
            }

            // If is a complex path
            List<String> currentPath = new LinkedList<String>(Arrays.asList(parts.clone()));
            Object tmpValue = keyValue;
            String part;
            String parentPath;
            Map<String, Object> tmpParent = null;
            for(int i = parts.length - 1; i >= 0; i--){
                tmpObject = new HashMap<>();
                part = parts[i]; // Get element from the list
                currentPath.remove(part); // Remove part of path from list (Go to parent path)
                parentPath = String.join(pathSep, currentPath); // Get current path from parent in sourceObj
                try {
                    tmpParent = mapper.convertValue(this.getValue(sourceObj, parentPath, pathSep, null), new TypeReference<Map<String, Object>>(){});
                }catch (Exception e){
                    //LogUtil.printError("[DEBUG] It was not possible to parse to map the parent, because it already exists as another type");
                    throw new UtilException(JsonUtil.class, "It was not possible to get value in path ["+keyPath+"] -> [" + e.getMessage() + "] ["+e.getClass()+"]");
                }
                if(tmpParent == null){
                    tmpParent = new HashMap<>();
                }
                tmpObject.put(part, tmpValue); //Add value to object
                tmpParent.putAll(tmpObject); //Merge with father and existing values
                tmpValue = tmpParent; // Update the next value
            }
            if(tmpParent == null) {
                return defaultValue;
            }
            // Update sourceObject in father
            Map<String, Object> mappedSource = mapper.convertValue(sourceObj, new TypeReference<Map<String, Object>>(){});
            mappedSource.putAll(tmpParent);
            tmpValue = mappedSource;
            return tmpValue; // Return HashMap
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to set value in path ["+keyPath+"] -> [" + e.getMessage() + "] ["+e.getClass()+"]");
        }
    }

    /**
     * Parses the JSON string object to a JsonNode object.
     * <p>
     * @param   json
     *          the json object as a String
     *
     * @return  a {@code JsonObject} object parsed with the json data.
     *
     * @throws  UtilException
     *          if unable to parse the json string.
     */
    public JsonNode toJsonNode(String json){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json,JsonNode.class);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }

    /**
     * Parses the JSON string object to a JsonNode object.
     * <p>
     * @param   json
     *          the json object as a Object
     *
     * @return  a {@code JsonObject} object parsed with the json data.
     *
     * @throws  UtilException
     *          if unable to parse the json string.
     */
    public JsonNode toJsonNode(Object json){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.convertValue(json,JsonNode.class);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }
    /**
     * Parses the JSON Map object to a JsonNode object.
     * <p>
     * @param   json
     *          the json object as a map type class.
     *
     * @return  a {@code JsonObject} object parsed with the json data.
     *
     * @throws  UtilException
     *          if unable to parse the json map.
     */
    public JsonNode toJsonNode(Map<String, Object> json){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.valueToTree(json);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }

      /**
     * Parses the JSON object to a Map type object.
     * <p>
     * @param   path
     *          the {@code String} that contains the old path
     * @param   pathSep
     *          the {@code String} separator of the path
     * @param   newPathSep
     *          the {@code String} separator of the new path to be translated
     *
     * @return  a {@code JsonObject} object parsed with the json data.
     *
     * @throws  UtilException
     *          if unable to parse the json object.
     */
    public String translatePathSep(String path, String pathSep, String newPathSep){
        try{
            String newPath = StringUtil.deepCopy(path); // Deep copy string
            if(newPath.startsWith(pathSep)){ // If the path starts with the pathSep remove it so the search can be efficient
                newPath = newPath.substring(1);
            }
            String[] parts = newPath.split(String.format("\\%s",pathSep)); // Split the path in order to get the parts
            return String.join(String.format("\\%s",newPathSep), parts); // Join the path with the children
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, e, "It was not possible to translate te pathSep");
        }
    }

    /**
     * Parses the JSON object to a Map type object.
     * <p>
     * @param   obj
     *          the json object with json properties annotations.
     *
     * @return  a {@code JsonObject} object parsed with the json data.
     *
     * @throws  UtilException
     *          if unable to parse the json object.
     */
    public Map<?,?> toMap(Object obj){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.convertValue(obj, Map.class);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }
    /**
     * Binds the JsonNode object to the given class type object.
     * <p>
     * @param   jsonNode
     *          the json object as a JsonNode.
     * @param   bindClass
     *          the class type to map the JsonNode structure.
     *
     * @return  a {@code Object} object parsed with the json data of the given type class.
     *
     * @throws  UtilException
     *          if unable to parse the json object.
     */
    public Object bindJsonNode(JsonNode jsonNode, Class<?> bindClass){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.treeToValue(jsonNode, bindClass);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }

    /**
     * Binds the json Map object to the given class type object.
     * <p>
     * @param   json
     *          the json object as a Map.
     * @param   bindClass
     *          the class type to map the json map structure.
     *
     * @return  a {@code Object} object parsed with the json data of the given type class.
     *
     * @throws  UtilException
     *          if unable to parse the json object.
     */
    @SuppressWarnings("Unused")
    public Object bindMap(Map<String,Object> json, Class<?> bindClass){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.convertValue(mapper.valueToTree(json), bindClass);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }

    /**
     * Binds the json object to the given class type object.
     * <p>
     * @param   json
     *          the json object with json properties annotations.
     * @param   bindClass
     *          the class type to map the json object structure.
     *
     * @return  a {@code Object} object parsed with the json data of the given type class.
     *
     * @throws  UtilException
     *          if unable to parse the json object.
     */
    public Object bindObject(Object json, Class<?> bindClass){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return this.bindJsonNode(mapper.valueToTree(json), bindClass);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }
    /**
     * Binds the json object to the given class type object.
     * <p>
     * @param   json
     *          the json object with json properties annotations.
     * @param   bindClass
     *          the class type to map the json object structure.
     *
     * @return  a {@code T Class} object parsed with the json data of the given type class.
     *
     * @throws  UtilException
     *          if unable to parse the json object.
     */
    public <T> T convertObject(Object json, Class<T> bindClass){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.convertValue(json, bindClass);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }
    /**
     * Checks if class can be cast to a specific binding class
     * <p>
     * @param   json
     *          the json object with json properties annotations.
     * @param   bindClass
     *          the class type to map the json object structure.
     *
     * @return  a {@code Boolean} object parsed with the json data of the given type class.
     *
     * @throws  UtilException
     *          if unable to parse the json object.
     */
    public <T> boolean isType(Object json, Class<T> bindClass){
        try {
            T castedObj = this.convertObject(json, bindClass);
            return ObjectUtils.isNotEmpty(castedObj);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }


    /**
     * Binds the json object to the given type reference object.
     * <p>
     * @param   json
     *          the json object with json properties annotations.
     * @param   reference
     *          the type reference of an object to bind the json object.
     *
     * @return  a {@code Object} object parsed with the json data of the given reference type object.
     *
     * @throws  UtilException
     *          if unable to parse the json object.
     */
    public Object bindReferenceType (Object json, TypeReference<?> reference) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.convertValue(json, reference);
        }  catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to get reference type -> [" + e.getMessage() + "]");
        }
    }
    /**
     * Binds the json object to the given type reference object.
     * <p>
     * @param   json
     *          the json object with json properties annotations.
     * @param   reference
     *          the type reference of an object to bind the json object.
     *
     * @return  a {@code Object} object parsed with the json data of the given reference type object.
     *
     * @throws  UtilException
     *          if unable to parse the json object.
     */
    public <T> T bind (Object json, TypeReference<T> reference) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.convertValue(json, reference);
        }  catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to get reference type -> [" + e.getMessage() + "]");
        }
    }

    public List<?> mapToList(Map<String, ?> map){
        try{
            return Arrays.asList(map.values().toArray());
        }  catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was possible to the map to a list");
        }
    }

}
