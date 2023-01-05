package net.catenax.ce.productpass.exceptions;

public class ServiceInitializationException extends Exception{
        public ServiceInitializationException(String serviceName, String errorMessage) {
            super("["+serviceName+"] " + errorMessage);
        }
        public ServiceInitializationException(String serviceName, Exception e, String errorMessage) {
            super("["+serviceName+"] " + errorMessage+", "+e.getMessage());
        }

}
