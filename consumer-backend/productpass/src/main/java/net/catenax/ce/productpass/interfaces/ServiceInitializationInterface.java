package net.catenax.ce.productpass.interfaces;

import net.catenax.ce.productpass.exceptions.ServiceInitializationException;

import java.util.List;

@org.springframework.stereotype.Service
public interface ServiceInitializationInterface {
    List<String> getEmptyVariables(); // Return the name of the variables that are not initialized
    void checkEmptyVariables() throws ServiceInitializationException; // Call checkVariables and add your initialization configuration
}
