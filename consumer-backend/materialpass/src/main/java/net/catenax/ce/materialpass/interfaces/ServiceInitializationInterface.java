package net.catenax.ce.materialpass.interfaces;

import net.catenax.ce.materialpass.exceptions.ServiceInitializationException;

import java.util.List;

@org.springframework.stereotype.Service
public interface ServiceInitializationInterface {
    List<String> getEmptyVariables(); // Return the name of the variables that are not initialized
    void checkEmptyVariables() throws ServiceInitializationException; // Call checkVariables and add your initialization configuration
}
