package tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import tools.exceptions.ToolException;

import java.util.*;

public final class jsonTools {
    private jsonTools() {
        throw new IllegalStateException("Tool/Utility Class Illegal Initialization");
    }

    public static ArrayList<Object> getObjectArray(Object... data){
        return new ArrayList<>(
                Arrays.asList(
                        data)
        );
    }
    public static Object loadJson(String jsonString, Class classType){
        Gson g = new Gson();

        return g.fromJson(jsonString, classType);
    }

    public static String escapeJson(String jsonString){
        return JSONObject.quote(jsonString);
    }

    public static JSONObject parseJson(String jsonString){
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            throw new ToolException(jsonTools.class, "I was not possible to parse JSON! -> [" + e.getMessage() + "]");
        }
    }

    public static Boolean isJson(Object object){
        try {
            JSONObject jsonObj =  new JSONObject(object);
            if(!jsonObj.isEmpty()){
                return true;
            }
        } catch (JSONException e) {
           // Do nothing
        }
        return false;
    }
    public static String toJson(Object object){
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ToolException(jsonTools.class, "I was not possible to format to JSON! -> [" + e.getMessage() + "]");
        }
    }




    public static String toJsonFile(String path, JSONObject json){
        try {
            return fileTools.toFile(path, jsonTools.dumpJson(json, 4), false);
        } catch (Exception e) {
            throw new ToolException(jsonTools.class, "I was not possible to create JSON file ["+path+"]! -> [" + e.getMessage() + "]");
        }
    }
    public static JSONObject fromJsonFile(String path){
        try {
            String fileContent = fileTools.readFile(path);
            return jsonTools.parseJson(fileContent);
        } catch (Exception e) {
            throw new ToolException(jsonTools.class, "I was not possible to create JSON file ["+path+"]! -> [" + e.getMessage() + "]");
        }
    }
    public static String dumpJson(JSONObject json, Integer indent){
        try {
            return json.toString(indent);
        } catch (JSONException e) {
            throw new ToolException(jsonTools.class, "I was not possible to dump JSON! -> [" + e.getMessage() + "]");
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
            for (String part : parts) {
                try{
                    tmpValue = (Map<String, Object>) tmpObject;
                }catch (Exception e){
                    //Uncomment for debug logTools.printError("[DEBUG] It was not possible to parse to map");
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
            throw new ToolException(jsonTools.class, "It was not possible to get value in path ["+keyPath+"] -> [" + e.getMessage() + "]");
        }
    }
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
            Map<String, Object> tmpObject;
            String[] parts = keyPath.split(String.format("\\%s",pathSep));
            // If is simple path just add it
            if(parts.length == 1){
                try{
                    tmpObject = (Map<String, Object>) sourceObj;
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
                    tmpParent = (Map<String, Object>) jsonTools.getValue(sourceObj, parentPath, pathSep, null);
                }catch (Exception e){
                    //Uncomment for debug logTools.printError("[DEBUG] It was not possible to parse to map the parent, because it already exists as another type");
                    throw new ToolException(jsonTools.class, "It was not possible to get value in path ["+keyPath+"] -> [" + e.getMessage() + "] ["+e.getClass()+"]");
                }
                if(tmpParent == null){
                    tmpParent = new HashMap<>();
                }
                tmpObject.put(part, tmpValue); //Add value to object
                tmpObject.putAll(tmpParent); //Merge with father and existing values
                tmpValue = tmpObject; // Update the next value
            }
            return tmpValue;
        } catch (Exception e) {
            throw new ToolException(jsonTools.class, "It was not possible to set value in path ["+keyPath+"] -> [" + e.getMessage() + "] ["+e.getClass()+"]");
        }
    }
    public static JsonNode toJsonNode(String json){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json,JsonNode.class);
        } catch (Exception e) {
            throw new ToolException(jsonTools.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }
    public static Object bindJsonNode(JsonNode jsonNode, Class bindClass){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.treeToValue(jsonNode, bindClass);
        } catch (Exception e) {
            throw new ToolException(jsonTools.class, "It was not possible to parse json -> [" + e.getMessage() + "]");
        }
    }
}
