package tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import tools.exceptions.ToolException;

import java.util.ArrayList;
import java.util.Arrays;

public class jsonTools {
    public static ArrayList<Object> getObjectArray(Object... data){
        return new ArrayList<Object>(
                Arrays.asList(
                        data)
        );
    }
    public static Object loadJson(String jsonString, Class classType){
        Gson g = new Gson();

        return g.fromJson(jsonString, classType);
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

}
