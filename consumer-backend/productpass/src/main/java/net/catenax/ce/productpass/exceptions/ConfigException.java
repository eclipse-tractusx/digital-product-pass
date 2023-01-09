package net.catenax.ce.productpass.exceptions;

import tools.logTools;

public class ConfigException extends RuntimeException{
        public ConfigException(String configuration, String errorMessage) {
            super("["+configuration+"] " + errorMessage);
            logTools.printException(this, "["+configuration+"] " + errorMessage);
        }
        public ConfigException(String configuration, Exception e, String errorMessage) {
            super("["+configuration+"] " + errorMessage+", "+e.getMessage());
            logTools.printException(this, "["+configuration+"] " + errorMessage);
        }

}
