package net.catenax.ce.materialpass.exceptions;

public class ControllerException extends Exception{
        public ControllerException(String controllerName, String errorMessage) {
            super("["+controllerName+"] " + errorMessage);
        }
        public ControllerException(String controllerName, Exception e, String errorMessage) {
            super("["+controllerName+"] " + errorMessage+", "+e.getMessage());
        }

}
