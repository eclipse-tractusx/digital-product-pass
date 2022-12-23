package net.catenax.ce.materialpass.models;

import org.json.JSONObject;

import java.nio.file.Paths;

public class DataModel extends JSONObject {
    protected String dataModelName;
    protected String dataModelDir;
    protected String dataModelPath;
    public DataModel(String dataModelName,String dataModelDir) {
        this.dataModelName = dataModelName;
        this.dataModelDir = dataModelDir;
        this.dataModelPath = this.buildDataModelPath();
        super.put("name", this.dataModelName);
        super.put("data", new JSONObject());

    }

    @Override
    public JSONObject put(String key, Object value){
        JSONObject tmpDataModel = (JSONObject) super.get("data");
        tmpDataModel.put(key, value);
        super.put("data", tmpDataModel);
        return tmpDataModel;
    }

    @Override
    public Object get(String key){
        JSONObject tmpDataModel = (JSONObject) super.get("data");
        return tmpDataModel.get(key);
    }

    public JSONObject getData(){
        return (JSONObject) super.get("data");
    }
    public String buildDataModelPath(){
        return Paths.get(this.dataModelDir,this.dataModelName + ".json").toAbsolutePath().toString();
    }

    public void save(){
        // Save not implemented
    }
}
