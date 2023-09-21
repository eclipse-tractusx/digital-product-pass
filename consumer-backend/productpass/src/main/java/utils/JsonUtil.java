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
import org.eclipse.tractusx.productpass.models.catenax.EdcDiscoveryEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import utils.exceptions.UtilException;

import java.io.File;
import java.util.*;


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
    public Object loadJson(String jsonString, Class<?> classType){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, classType);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to load JSON in object -> [" + e.getMessage() + "]");
        }
    }

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

    public Object parseJson(String jsonString){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, Object.class);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to parse JSON! -> [" + e.getMessage() + "]");
        }
    }
    public Object parseJson(String jsonString, Class<?> bindClass){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, bindClass);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to parse JSON! -> [" + e.getMessage() + "]");
        }
    }
    public ObjectNode newJson(){
        return JsonNodeFactory.instance.objectNode();
    }
    public JsonNode newJsonNode(){
        return JsonNodeFactory.instance.objectNode();
    }

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
    public String toJsonFile(String path, Object json, Boolean prettyPrint){
        try {
            return fileUtil.toFile(path, this.toJson(json, prettyPrint), false);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to create JSON file ["+path+"]! -> [" + e.getMessage() + "]");
        }
    }
    public Object fromJsonFile(String path){
        try {
            String fileContent = fileUtil.readFile(path);
            return this.parseJson(fileContent);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to create JSON file ["+path+"]! -> [" + e.getMessage() + "]");
        }
    }
    public Object fromJsonFileToObject(String path, Class<?> bindClass){
        try {
            if(!fileUtil.pathExists(path)) {
                LogUtil.printError("The file does not exists in [" + path + "]!");
                return null;
            }
            String fileContent = fileUtil.readFile(path);
            return this.parseJson(fileContent, bindClass);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "I was not possible to create JSON file ["+path+"]! -> [" + e.getMessage() + "]");
        }
    }

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
    public JsonNode toJsonNode(String json){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json,JsonNode.class);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }

    public JsonNode toJsonNode(Map<String, Object> json){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.valueToTree(json);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }


    public Map<?,?> toMap(Object obj){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.convertValue(obj, Map.class);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }

    public Object bindJsonNode(JsonNode jsonNode, Class<?> bindClass){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.treeToValue(jsonNode, bindClass);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }
    public Object bindMap(Map<String,Object> json, Class<?> bindClass){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.convertValue(mapper.valueToTree(json), bindClass);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }
    public Object bindObject(Object json, Class<?> bindClass){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return this.bindJsonNode(mapper.valueToTree(json), bindClass);
        } catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }

    public Object bindReferenceType (Object json, TypeReference<?> reference) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.convertValue(json, reference);
        }  catch (Exception e) {
            throw new UtilException(JsonUtil.class, "It was not possible to get reference type -> [" + e.getMessage() + "]");
        }
    }
}
