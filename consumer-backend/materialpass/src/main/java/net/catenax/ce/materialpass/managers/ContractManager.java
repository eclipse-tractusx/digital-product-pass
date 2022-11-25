package net.catenax.ce.materialpass.managers;

import tools.logTools;
import tools.reflectionTools;

public class ContractManager extends Manager {
    public ContractManager() {
        super();
        this.setManager(reflectionTools.getCurrentClassName(this.getClass()));
        logTools.printMessage("[DEBUG] " + this.getDataModelName() + " created! : [" + this.dataModelPath + "]");
    }

    @Override
    public String getDataModelName() {
        return "contractDataModel";
    }

}
