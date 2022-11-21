package net.catenax.ce.materialpass.managers;

import tools.logTools;
import tools.reflectionTools;

public class AssetManager extends Manager{
    public AssetManager(){
        this.dataModelName = "assetDataModel";
        setManager(reflectionTools.getCurrentClassName(this.getClass()));
        logTools.printMessage("[DEBUG] Assets DataModel created! : ["+this.dataModelPath +"]");
    }



}
