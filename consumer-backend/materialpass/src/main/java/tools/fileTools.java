package tools;

import tools.exceptions.ToolException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;


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
    public static File newFile(String filePath){
        return new File(filePath);
    }

    public static String getWorkdirPath(){
        return Paths.get(System.getProperty("user.dir")).toString();
    }

    public static String getBaseClassDir(Class selectedClass){
        return selectedClass.getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    public static String getResourcePath(Class selectedClass, String resourcePath){
        try {
            URI uri = selectedClass.getClassLoader().getResource(resourcePath);
            System.out.println(uri);
            if (uri != null) {
                return fileTools.normalizePath(uri.toString());
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ToolException(fileTools.class,"[ERROR] Something when wrong when reading file in path [" + resourcePath + "]");
        }

        throw new ToolException(fileTools.class,"[ERROR] File not found in class path [" + resourcePath + "]");
    }

    public static String normalizePath(String path) {
        try {
            return new File(path).getCanonicalPath();
        } catch (IOException e) {
            throw new ToolException(fileTools.class,"[ERROR] It was not possible to normalize path ["+path+"]");
        }
    }
    public static String getClassFile(Class selectedClass){
        return selectedClass.getName().replace(".", File.pathSeparator) + ".java";
    }
    public static String getClassPackageDir(Class selectedClass){
        return selectedClass.getPackageName().replace(".", File.pathSeparator);
    }
}
