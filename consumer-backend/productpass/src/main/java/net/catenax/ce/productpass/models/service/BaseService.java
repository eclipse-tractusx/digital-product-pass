package net.catenax.ce.productpass.models.service;

import net.catenax.ce.productpass.exceptions.ServiceInitializationException;
import net.catenax.ce.productpass.interfaces.ServiceInitializationInterface;

import java.util.List;

public abstract class BaseService implements ServiceInitializationInterface {

    public void checkEmptyVariables() throws ServiceInitializationException {
        List<String> missingVariables = this.getEmptyVariables(); //Check for variables
        if(!missingVariables.isEmpty()){ // Return exception if one is missing
            throw new ServiceInitializationException(this.getClass().getName()+".checkEmptyVariables", "This mandatory variables "+missingVariables+" are not set!");
        };
    }
    public void checkEmptyVariables(List<String> initializationOptionalVariables) throws ServiceInitializationException {
        List<String> missingVariables = this.getEmptyVariables(); //Check for variables
        if((!initializationOptionalVariables.equals(missingVariables)) && (!missingVariables.isEmpty())){ // Return exception if one is missing and is not optional
            throw new ServiceInitializationException(this.getClass().getName()+".checkEmptyVariables", "This mandatory variables "+missingVariables+" are not set!");
        };
    }

    public abstract List<String> getEmptyVariables(); // Return list of missing variables configured in service
}
