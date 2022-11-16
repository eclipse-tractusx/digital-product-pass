/**********************************************************
 *
 * Catena-X - Material Passport Consumer Backend
 *
 * Copyright (c) 2022: CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2022 Contributors to the CatenaX (ng) GitHub Organisation.
 *
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 **********************************************************/

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
