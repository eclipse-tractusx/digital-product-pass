package net.catenax.ce.materialpass.models;

import org.json.JSONObject;

import java.nio.file.Paths;

public abstract class DataModel extends JSONObject {
    protected String dataModelName;
    protected String dataModelDir;

    protected String dataModelPath;
    public DataModel(String dataModelName,String dataModelDir) {
        this.dataModelName = dataModelName;
        this.dataModelDir = dataModelDir;
        this.dataModelPath = this.buildDataModelPath();
    }
    public String buildDataModelPath(){
        return Paths.get(this.dataModelDir,this.dataModelName + ".json").toAbsolutePath().toString();
    }
}
