package net.catenax.ce.productpass.exceptions;

import tools.logTools;

public class ControllerException extends Exception{
        public ControllerException(String controllerName, String errorMessage) {
            super("["+controllerName+"] " + errorMessage);
            logTools.printException(this, "["+controllerName+"] " + errorMessage);
        }
        public ControllerException(String controllerName, Exception e, String errorMessage) {
            super("["+controllerName+"] " + errorMessage+", "+e.getMessage());
            logTools.printException(this, "["+controllerName+"] " + errorMessage);
        }

}
