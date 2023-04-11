/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the
 * License for the specific language govern in permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

package utils;

import org.springframework.stereotype.Component;
import utils.exceptions.UtilException;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class EnvUtil {

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

    public EnvUtil(){
        InputStream mainConfigContent  = FileUtil.getResourceContent(this.getClass(), ENVIRONMENT_FILE_PATH);
        this.envConfig = YamlUtil.parseYmlStream(mainConfigContent);

        this.defaultEnv = (String) this.getEnvironmentParam("default.environment", ".", null);

        if(defaultEnv == null){
            throw new UtilException(ConfigUtil.class,"[CRITICAL] Environment file ["+ ENVIRONMENT_FILE_PATH + "] does not contains the default.environment key!");
        }

        this.AVAILABLE_ENVIRONMENTS = (List<String>) this.getEnvironmentParam("default.available", ".", null);

        if(defaultEnv == null){
            throw new UtilException(ConfigUtil.class,"[CRITICAL] Environment file ["+ ENVIRONMENT_FILE_PATH + "] does not contains the default.environment key!");
        }

        if(!this.AVAILABLE_ENVIRONMENTS.contains(this.defaultEnv)){
            throw new UtilException(ConfigUtil.class,"[CRITICAL] Default environment ["+ defaultEnv+"] is not available!");
        }

        this.environment = (String) this.getEnvironmentParam("environment", ".", this.defaultEnv);

        if(this.environment.isBlank()){
            this.environment = this.defaultEnv;
        }

        if (!this.environment.equals(this.defaultEnv) && !this.AVAILABLE_ENVIRONMENTS.contains(this.environment)) {
            throw new UtilException(ConfigUtil.class, "[CRITICAL] Environment [" + this.environment + "] is not available!");
        }
    }

    public Object getEnvironmentParam(String param, String separator, Object defaultValue){
        if(this.envConfig == null){
            return defaultValue;
        }
        Object value = JsonUtil.getValue(this.envConfig, param, separator, defaultValue);
        if (value == defaultValue) {
            throw new UtilException(ConfigUtil.class,"[ERROR] Environment Configuration param ["+param+"] not found!");
        }
        return value;
    }

}
