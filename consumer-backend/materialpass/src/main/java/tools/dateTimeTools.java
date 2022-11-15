package tools;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class dateTimeTools {

    /**
     * Static tools to get current dateTime used for logging.
     *
     */

    public static String getDateTimeFormatted(String pattern){
        String defaultPattern = "dd/MM/yyyy HH:mm:ss";
        if(pattern == null){
            pattern = defaultPattern;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

}
