package tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class logTools {

    /**
     * Static Tools to print logs with format and current date.
     * Not available at the moment to add files, usage of fileTools.
     */

    static Logger logger = LoggerFactory.getLogger(httpTools.class);

    public static void printMessage(String strMessage){
        String message = dateTimeTools.getDateTimeFormatted(null) + " " + strMessage;
        System.out.println(message);
        logger.info(message);
    }
    public static void printException(Exception e, String strMessage){
        String message = dateTimeTools.getDateTimeFormatted(null) + " [EXCEPTION] ["+e.getMessage()+"] "+strMessage;
        System.err.println(message);
        logger.error(message, e);
    }

    public static void printError(String strMessage){
        String message = dateTimeTools.getDateTimeFormatted(null) + " [ERROR] " + strMessage;
        System.err.println(message);
        logger.error(message);
    }
    public static void printWarning(String strMessage){
        String message= dateTimeTools.getDateTimeFormatted(null) + " [WARNING] " + strMessage;
        System.out.println(message);
        logger.warn(message);
    }
}
