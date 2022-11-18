package net.catenax.ce.materialpass.managers;

import org.json.JSONObject;
import tools.fileTools;
import tools.jsonTools;

import java.nio.file.Paths;

public abstract class Manager {
    protected String dataModelName;
    protected String dataDir;
    protected String tmpDir;

    public JSONObject dataModel;
    protected String dataModelPath;

    public Manager(String className){
        this.dataDir = fileTools.createDataDir(className);
        this.tmpDir = fileTools.createTmpDir(className);
    }

    public JSONObject getDataModel() {
        return dataModel;
    }

    public String getDataModelPath() {
        return dataModelPath;
    }

    public String getDataModelName() {
        return dataModelName;
    }

    public String getDataDir() {
        return dataDir;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public String getTmpDir() {
        return tmpDir;
    }

    public void setTmpDir(String tmpDir) {
        this.tmpDir = tmpDir;
    }
    public String buildDataModelPath(){
        return Paths.get(this.dataDir,this.dataModelName + ".json").toAbsolutePath().toString();
    }
    public JSONObject loadDataModel(){
        this.dataModelPath = this.buildDataModelPath();
        if(!fileTools.pathExists(this.dataModelPath)){
            jsonTools.toJsonFile(this.dataModelPath, new JSONObject());
        }
        return jsonTools.fromJsonFile(this.dataModelPath);
    }
    public String saveDataModel(){
        this.dataModelPath = this.buildDataModelPath();
        return jsonTools.toJsonFile(this.dataModelPath, this.dataModel);
    }


}
