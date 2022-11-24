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

import org.yaml.snakeyaml.Yaml;
import tools.exceptions.ToolException;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public final class yamlTools {
    public static Map<String, Object> readFile(String filePath){
        try {
            InputStream inputStream = new FileInputStream(fileTools.newFile(filePath));
            Yaml yaml = new Yaml();
            return yaml.load(inputStream);
        }catch (Exception e){
            throw new ToolException(yamlTools.class, "There was an error in loading the yaml file [" + filePath + "], " + e.getMessage());
        }
    }
    public static Map<String, Object> parseYml(String fileContent){
        try {
            Yaml yaml = new Yaml();
            return yaml.load(fileContent);
        }catch (Exception e){
            throw new ToolException(yamlTools.class, "There was an error in loading the yaml content! " + e.getMessage());
        }
    }
    public static Map<String, Object> parseYmlStream(InputStream fileContent){
        try {
            Yaml yaml = new Yaml();
            return yaml.load(fileContent);
        }catch (Exception e){
            throw new ToolException(yamlTools.class, "There was an error in loading the yaml content! " + e.getMessage());
        }
    }
}
