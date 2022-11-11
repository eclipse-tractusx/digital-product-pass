package tools;

import java.io.FileWriter;
import java.io.IOException;

public final class fileTools {

    /**
     * Static Tools to manage files, and store in logs the logTools
     *
     */
    public static void toFile(String filePath, String content, Boolean append){
        try {
            FileWriter fw = new FileWriter(filePath,append);
            fw.write(content);
            fw.close();
        }
        catch(IOException ioe)
        {
            logTools.printException(ioe, "It was not possible to create file ["+filePath+"]");
        }
    }

}
