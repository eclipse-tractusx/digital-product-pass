package net.catenax.ce.materialpass.managers;

import tools.logTools;
import tools.reflectionTools;

public class AssetManager extends Manager{
    public AssetManager(){
        super();
        this.setManager(reflectionTools.getCurrentClassName(this.getClass()));
        logTools.printMessage("[DEBUG] "+this.getDataModelName()+" created! : ["+this.dataModelPath +"]");
    }

    @Override
    public String getDataModelName(){
        return "assetDataModel";
    }

}
