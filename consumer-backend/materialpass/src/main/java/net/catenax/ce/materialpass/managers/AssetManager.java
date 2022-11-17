package net.catenax.ce.materialpass.managers;

import tools.logTools;
import tools.reflectionTools;

public class AssetManager extends Manager{
    public AssetManager(){
        super(reflectionTools.getCurrentClassName(AssetManager.class));
        this.dataModelName = "assetDataModel";
        this.dataModelPath = this.buildDataModelPath();
        this.dataModel = this.loadDataModel();
        logTools.printMessage("[DEBUG] Assets DataModel created! : ["+this.dataModelPath +"]");
    }



}
