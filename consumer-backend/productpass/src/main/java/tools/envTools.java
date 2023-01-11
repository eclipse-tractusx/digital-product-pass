package tools;

import org.springframework.stereotype.Component;
import tools.exceptions.ToolException;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class envTools {

    private static final String  ENVIRONMENT_DIR = "config";

    private static final String ENVIRONMENT_FILE_NAME = "env";

    private static final String  ENVIRONMENT_FILE_PATH = ENVIRONMENT_DIR+"/"+ENVIRONMENT_FILE_NAME+".yml";

    public List<String> AVAILABLE_ENVIRONMENTS;


    public Map<String, Object> envConfig;

    public String defaultEnv;
    public String environment;
    public String getEnvironment() {
        return this.environment;
    }

    public envTools(){
        InputStream mainConfigContent  = fileTools.getResourceContent(this.getClass(), ENVIRONMENT_FILE_PATH);
        this.envConfig = yamlTools.parseYmlStream(mainConfigContent);

        this.defaultEnv = (String) this.getEnvironmentParam("default.environment", ".", null);

        if(defaultEnv == null){
            throw new ToolException(configTools.class,"[CRITICAL] Environment file ["+ ENVIRONMENT_FILE_PATH + "] does not contains the default.environment key!");
        }

        this.AVAILABLE_ENVIRONMENTS = (List<String>) this.getEnvironmentParam("default.available", ".", null);

        if(defaultEnv == null){
            throw new ToolException(configTools.class,"[CRITICAL] Environment file ["+ ENVIRONMENT_FILE_PATH + "] does not contains the default.environment key!");
        }

        if(!this.AVAILABLE_ENVIRONMENTS.contains(this.defaultEnv)){
            throw new ToolException(configTools.class,"[CRITICAL] Default environment ["+ defaultEnv+"] is not available!");
        }

        this.environment = (String) this.getEnvironmentParam("environment", ".", this.defaultEnv);

        if(this.environment.isBlank()){
            this.environment = this.defaultEnv;
        }

        if (!this.environment.equals(this.defaultEnv) && !this.AVAILABLE_ENVIRONMENTS.contains(this.environment)) {
            throw new ToolException(configTools.class, "[CRITICAL] Environment [" + this.environment + "] is not available!");
        }
    }

    public Object getEnvironmentParam(String param, String separator, Object defaultValue){
        if(this.envConfig == null){
            return defaultValue;
        }
        Object value = jsonTools.getValue(this.envConfig, param, separator, defaultValue);
        if (value == defaultValue) {
            throw new ToolException(configTools.class,"[ERROR] Environment Configuration param ["+param+"] not found!");
        }
        return value;
    }

}
