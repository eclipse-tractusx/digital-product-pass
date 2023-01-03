package tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import tools.exceptions.ToolException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

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
                System.out.println("[DEBUG] Object == null!");
                return defaultValue;
            }
            if(keyPath == null || keyPath.equals("") || pathSep.equals("")){
               System.out.println("[DEBUG] keyPath empty or pathSep empty!");
                return defaultValue;
            }
            String[] parts = keyPath.split(String.format("\\%s",pathSep));
            if(parts.length < 1){
                return defaultValue;
            }
            Map<String, Object> tmpValue;
            Object tmpObject = sourceObj;
            for (String part : parts) {
                try{
                    tmpValue = (Map<String, Object>) tmpObject;
                }catch (Exception e){
                    return defaultValue;
                }
                if (!tmpValue.containsKey(part)) {
                    return defaultValue;
                }
                tmpObject = tmpValue.get(part);
            }
            return tmpObject;
        } catch (Exception e) {
            throw new ToolException(jsonTools.class, "It was not possible to get value in path ["+keyPath+"] -> [" + e.getMessage() + "]");
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
