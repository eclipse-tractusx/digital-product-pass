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

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import utils.exceptions.UtilException;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public final class YamlUtil {
    private YamlUtil() {
        throw new IllegalStateException("Tool/Utility Class Illegal Initialization");
    }

    public static Map<String, Object> readFile(String filePath) {
        try {
            InputStream inputStream = new FileInputStream(FileUtil.newFile(filePath));
            Yaml yaml = new Yaml();
            return yaml.load(inputStream);
        } catch (Exception e) {
            throw new UtilException(YamlUtil.class, "There was an error in loading the yaml file [" + filePath + "], " + e.getMessage());
        }
    }

    public static Map<String, Object> parseYml(String fileContent) {
        try {
            Yaml yaml = new Yaml();
            return yaml.load(fileContent);
        } catch (Exception e) {
            throw new UtilException(YamlUtil.class, "There was an error in loading the yaml content! " + e.getMessage());
        }
    }

    public static Map<String, Object> parseYmlStream(InputStream fileContent) {
        try {
            Yaml yaml = new Yaml();
            return yaml.load(fileContent);
        } catch (Exception e) {
            throw new UtilException(YamlUtil.class, "There was an error in loading the yaml content! " + e.getMessage());
        }
    }

    public static String dumpYml(Map<String, Object> map, Integer indent, Boolean prettyPrint) {
        try {
            DumperOptions options = new DumperOptions();
            options.setIndent(indent);
            options.setPrettyFlow(prettyPrint);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            return yaml.dump(map);
        } catch (Exception e) {
            throw new UtilException(YamlUtil.class, "It was not possible to dump map into yaml " + e.getMessage());
        }
    }
    public static String dumpYml(Object obj, Integer indent, Boolean prettyPrint) {
        try {
            DumperOptions options = new DumperOptions();
            options.setIndent(indent);
            options.setPrettyFlow(prettyPrint);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            return yaml.dump(obj);
        } catch (Exception e) {
            throw new UtilException(YamlUtil.class, "It was not possible to dump map into yaml " + e.getMessage());
        }
    }
}
