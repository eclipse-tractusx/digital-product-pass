package net.catenax.ce.productpass.managers;

import tools.logTools;
import tools.reflectionTools;

public class PassportManager extends Manager{
    public PassportManager(){
        super();
        this.setManager(reflectionTools.getCurrentClassName(this.getClass()));
        logTools.printMessage("[DEBUG] "+this.getDataModelName()+" created! : ["+this.dataModelPath +"]");
    }

    @Override
    public String getDataModelName(){
        return "passportDataModel";
    }

}
