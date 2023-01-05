package net.catenax.ce.productpass.managers;

import tools.logTools;
import tools.reflectionTools;

public class PassportV1Manager extends PassportManager {

    public PassportV1Manager(){
        super();
        this.setManager(reflectionTools.getCurrentClassName(this.getClass()));
        logTools.printMessage("[DEBUG] "+this.getDataModelName()+" created! : ["+this.dataModelPath +"]");
    }

    @Override
    public String getDataModelName(){
        return "passportV1DataModel";
    }
}
