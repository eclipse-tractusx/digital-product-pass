package net.catenax.ce.productpass.exceptions;

import tools.logTools;

public class ServiceInitializationException extends Exception{
        public ServiceInitializationException(String serviceName, String errorMessage) {
            super("["+serviceName+"] " + errorMessage);
            logTools.printException(this, "["+serviceName+"] " + errorMessage);
        }
        public ServiceInitializationException(String serviceName, Exception e, String errorMessage) {
            super("["+serviceName+"] " + errorMessage+", "+e.getMessage());
            logTools.printException(this, "["+serviceName+"] " + errorMessage);
        }

}
