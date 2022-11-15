package tools;

import tools.exceptions.ToolException;

import java.nio.file.Paths;
import java.util.Map;

public class configTools {

    private static final String rootPath = System.getProperty("user.dir");
    private static final String configurationFileName = "toolsConfiguration.yml";
    private final String relativeConfigurationFilePath = Paths.get("config", configurationFileName).normalize().toString();
    private Map<String, Object> configuration;

    public configTools(){
        String configurationFilePath = fileTools.getResourcePath(this.getClass(), relativeConfigurationFilePath);
        this.configuration = yamlTools.readFile(configurationFilePath);
    }
    public Map<String, Object> getConfiguration(){
        if (this.configuration == null) {
            throw new ToolException(configTools.class,"[CRITICAL] Configuration file ["+relativeConfigurationFilePath+"] not loaded!");
        }
        return this.configuration;
    }
    public void loadConfiguration(String configurationFile){
        this.configuration = yamlTools.readFile(configurationFile);
    }
    public Object getConfigurationParam(String param){
        if(this.configuration == null){
            return null;
        }
        Object value = configuration.get(param);
        if (value == null) {
            throw new ToolException(configTools.class,"[ERROR] Configuration param ["+param+"] not found!");
        }
        return value;
    }
}
