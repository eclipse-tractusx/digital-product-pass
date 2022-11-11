package tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class logTools {

    /**
     * Static Tools to print logs with format and current date.
     * Not available at the moment to add files, usage of fileTools.
     */

    static Logger logger = LoggerFactory.getLogger(logTools.class);


    public static void printMessage(String strMessage){
        String message = dateTimeTools.getDateTimeFormatted(null) + " " + strMessage;
        logger.info(message);
    }
    public static void printException(Exception e, String strMessage){
        String message = dateTimeTools.getDateTimeFormatted(null) + " [EXCEPTION] ["+e.getMessage()+"] "+strMessage;
        logger.error(message, e);
    }

    public static void printError(String strMessage){
        String message = dateTimeTools.getDateTimeFormatted(null) + " [ERROR] " + strMessage;
        logger.error(message);
    }
    public static void printWarning(String strMessage){
        String message= dateTimeTools.getDateTimeFormatted(null) + " [WARNING] " + strMessage;
        logger.warn(message);
    }
}
