package net.catenax.ce.productpass.exceptions;

import tools.logTools;

public class ServiceException extends RuntimeException{
        public ServiceException(String serviceName, String errorMessage) {
            super("["+serviceName+"] " + errorMessage);
            logTools.printException(this, "["+serviceName+"] " + errorMessage);
        }
        public ServiceException(String serviceName,Exception e, String errorMessage) {
            super("["+serviceName+"] " + errorMessage+", "+e.getMessage());
            logTools.printException(this, "["+serviceName+"] " + errorMessage);
        }

}
