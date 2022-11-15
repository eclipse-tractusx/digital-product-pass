package tools;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class yamlTools {
    public static Map<String, Object> readFile(String filePath){
        try {
            InputStream inputStream = new FileInputStream(fileTools.newFile(filePath));
            Yaml yaml = new Yaml();
            return yaml.load(inputStream);
        }catch (Exception e){
            logTools.printException(e, "There was an error in loading the yaml file [" + filePath + "]");
        }
        return null;
    }
}
