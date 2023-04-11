/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
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
 ********************************************************************************/

package utils;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.handler.codec.serialization.ObjectEncoder;
import utils.exceptions.UtilException;

import java.util.*;

public final class JsonUtil {
    private JsonUtil() {
        throw new IllegalStateException("Tool/Utility Class Illegal Initialization");
    }

    public static ArrayList<Object> getObjectArray(Object... data){
        return new ArrayList<>(
                Arrays.asList(
                        data)
        );
    }
    public static Object loadJson(String jsonString, Class<?> classType){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, classType);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to load JSON in object -> [" + e.getMessage() + "]");
        }
    }

    public static String escapeJson(String jsonString){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);
            mapper.getFactory().configure(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature(), true);
            return mapper.writeValueAsString(jsonString);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to escape JSON -> [" + e.getMessage() + "]");
        }
    }

    public static Object parseJson(String jsonString){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, Object.class);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to parse JSON! -> [" + e.getMessage() + "]");
        }
    }
    public static ObjectNode newJson(){
        return JsonNodeFactory.instance.objectNode();
    }

    public static Boolean isJson(String jsonString){
        try {
            ObjectMapper mapper = new ObjectMapper()
                    .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
            mapper.readTree(jsonString);
        } catch (JacksonException e) {
            return false;
        }
        return true;
    }
    public static String toJson(Object json, Boolean prettyPrint){
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
    public static String toJsonFile(String path, Object json, Boolean prettyPrint){
        try {
            return FileUtil.toFile(path, JsonUtil.toJson(json, prettyPrint), false);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to create JSON file ["+path+"]! -> [" + e.getMessage() + "]");
        }
    }
    public static Object fromJsonFile(String path){
        try {
            String fileContent = FileUtil.readFile(path);
            return JsonUtil.parseJson(fileContent);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to create JSON file ["+path+"]! -> [" + e.getMessage() + "]");
        }
    }

    public static Object getValue(Object sourceObj, String keyPath, String pathSep, Object defaultValue){
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
    /*
    * @param sourceObject: HashMap, JsonNode or other Object Able to parse to HashMap
    * @param keyPath: Path to key in json
    * @param keyValue: Value to update
    * @param pathSep: Separator used to read keyPath like "." or "/" for example
    * @param defaultValue: Default value returned in case of minor error.
    *
    * @throws UtilExeception when error is fatal
    *
    * @returns:
    *   defaultValue if error
    *   HashMap updated with new value (keyValue) in keyPath
     */
    public static Object setValue(Object sourceObj, String keyPath, Object keyValue, String pathSep, Object defaultValue){
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
                    tmpParent = mapper.convertValue(JsonUtil.getValue(sourceObj, parentPath, pathSep, null), new TypeReference<Map<String, Object>>(){});
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
    public static JsonNode toJsonNode(String json){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json,JsonNode.class);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }

    public static Map<?,?> toMap(Object obj){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.convertValue(obj, Map.class);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }

    public static Object bindJsonNode(JsonNode jsonNode, Class bindClass){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.treeToValue(jsonNode, bindClass);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }
}
