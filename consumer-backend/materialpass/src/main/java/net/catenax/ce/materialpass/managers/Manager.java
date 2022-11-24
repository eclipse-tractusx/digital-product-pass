package net.catenax.ce.materialpass.managers;

import net.catenax.ce.materialpass.models.DataModel;
import org.json.JSONObject;
import tools.fileTools;
import tools.jsonTools;

import java.nio.file.Paths;

public abstract class Manager {
    protected String dataModelName;
    protected String dataDir;
    protected String tmpDir;

    public DataModel dataModel;
    protected String dataModelPath;

    public void setManager(String className){
        this.dataDir = fileTools.createDataDir(className);
        this.tmpDir = fileTools.createTmpDir(className);
        this.dataModelName = this.getDataModelName();
        this.dataModel = new DataModel(this.dataModelName, this.dataDir);
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    public String getDataModelPath() {
        return dataModelPath;
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
    public String getDataModelName(){
        return "dataModel";
    }

    public DataModel loadDataModel(){
        this.dataModelPath = this.buildDataModelPath();
        if(!fileTools.pathExists(this.dataModelPath)){
            jsonTools.toJsonFile(this.dataModelPath, new JSONObject());
        }
        return (DataModel) jsonTools.fromJsonFile(this.dataModelPath);
    }
    public String saveDataModel(){
        this.dataModelPath = this.buildDataModelPath();
        return jsonTools.toJsonFile(this.dataModelPath, this.dataModel);
    }


}
