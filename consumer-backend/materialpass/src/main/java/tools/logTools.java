package tools;

public final class logTools {

    /**
     * Static Tools to print logs with format and current date.
     * Not available at the moment to add files, usage of fileTools.
     */

    public static void printMessage(String strMessage){
        System.out.println(dateTimeTools.getDateTimeFormatted(null) + " " + strMessage);
    }
    public static void printException(Exception e, String strMessage){
        System.err.println(dateTimeTools.getDateTimeFormatted(null) + " [EXCEPTION] ["+e.getMessage()+"] "+strMessage);
    }
    public static void printError(String strMessage){
        System.err.println(dateTimeTools.getDateTimeFormatted(null) + " [ERROR] " + strMessage);
    }
    public static void printWarning(String strMessage){
        System.out.println(dateTimeTools.getDateTimeFormatted(null) + " [WARNING] " + strMessage);
    }
}
