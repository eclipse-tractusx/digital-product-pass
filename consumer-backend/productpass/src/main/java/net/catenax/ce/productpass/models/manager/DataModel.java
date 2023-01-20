package net.catenax.ce.productpass.models.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import tools.jsonTools;

import java.nio.file.Paths;
import java.util.*;

public class DataModel extends HashMap<String, Object> {
    protected String dataModelName;
    protected String dataModelDir;
    protected String dataModelPath;
    public DataModel(String dataModelName,String dataModelDir) {
        this.dataModelName = dataModelName;
        this.dataModelDir = dataModelDir;
        this.dataModelPath = this.buildDataModelPath();
        super.put("name", this.dataModelName);
        super.put("data", new HashMap<String, Object>());
    }

    @Override
    public Object put(String key, Object value){
        Map<String, Object> tmpDataModel = (Map<String, Object>) super.get("data");
        tmpDataModel.put(key, value);
        super.put("data", tmpDataModel);
        return tmpDataModel;
    }

    public Object get(String key){
        Map<String, Object> tmpDataModel = (Map<String, Object>) super.get("data");
        return tmpDataModel.get(key);
    }

    public Map<String, Object> getData(){
        return (Map<String, Object>) super.get("data");
    }
    public String buildDataModelPath(){
        return Paths.get(this.dataModelDir,this.dataModelName + ".json").toAbsolutePath().toString();
    }

    public void save(){
        // Save not implemented
    }
}
