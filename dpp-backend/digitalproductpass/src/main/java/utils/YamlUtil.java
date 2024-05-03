/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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
 * distributed under the License is distributed on an "AS IS" BASIS
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the
 * License for the specific language govern in permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

package utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import utils.exceptions.UtilException;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * This class consists exclusively of methods to operate on YAML type files.
 *
 * <p> The methods defined here are intended to read, parse and dump YAML files.
 *
 */
@Component
public final class YamlUtil {
    private final FileUtil fileUtil;
    @Autowired
    public YamlUtil(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    /**
     * Reads the target YAML file defined by the filePath.
     * <p>
     * @param   filePath
     *          the path to the target YAML file as a String.
     *
     * @return  a {@code Map<String, Object>} object representing the target YAML file.
     *
     * @throws  UtilException
     *          if unable to read the YAML file.
     */
    public Map<String, Object> readFile(String filePath) {
        try {
            InputStream inputStream = new FileInputStream(fileUtil.newFile(filePath));
            Yaml yaml = new Yaml();
            return yaml.load(inputStream);
        } catch (Exception e) {
            throw new UtilException(YamlUtil.class, "There was an error in loading the yaml file [" + filePath + "], " + e.getMessage());
        }
    }

    /**
     * Parses the target YAML file defined by the fileContent.
     * <p>
     * @param   fileContent
     *          the path representation to the target YAML file as a String.
     *
     * @return  a {@code Map<String, Object>} object representing the target parsed YAML file.
     *
     * @throws  UtilException
     *          if unable to read the YAML file.
     */
    public Map<String, Object> parseYml(String fileContent) {
        try {
            Yaml yaml = new Yaml();
            return yaml.load(fileContent);
        } catch (Exception e) {
            throw new UtilException(YamlUtil.class, "There was an error in loading the yaml content! " + e.getMessage());
        }
    }

    /**
     * Parses the target YAML file defined by the fileContent.
     * <p>
     * @param   fileContent
     *          the path representation to the target YAML file as an InputStream.
     *
     * @return  a {@code Map<String, Object>} object representing the target parsed YAML file.
     *
     * @throws  UtilException
     *          if unable to read the YAML file.
     */
    @SuppressWarnings("Unused")
    public Map<String, Object> parseYmlStream(InputStream fileContent) {
        try {
            Yaml yaml = new Yaml();
            return yaml.load(fileContent);
        } catch (Exception e) {
            throw new UtilException(YamlUtil.class, "There was an error in loading the yaml content! " + e.getMessage());
        }
    }

    /**
     * Dumps the data defined by the {@code Map<String, Object>} map into a YAML File.
     * <p>
     * @param   map
     *          the {@code Map<String, Object} object representing the data to dump into a YAML file.
     * @param   indent
     *          the indent level for the YAML file, integer between 1 and 10.
     * @param   prettyPrint
     *          boolean to specify a pretty or compact dump
     *
     * @return  a {@code String} object representing the target parsed YAML file.
     *
     * @throws  UtilException
     *          if unable to dump the YAML file.
     */
    public String dumpYml(Map<String, Object> map, Integer indent, Boolean prettyPrint) {
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

    /**
     * Dumps the data defined by the {@code Object} map into a YAML File.
     * <p>
     * @param   obj
     *          the {@code Object} object representing the data to dump into a YAML file.
     * @param   indent
     *          the indent level for the YAML file, integer between 1 and 10.
     * @param   prettyPrint
     *          boolean to specify a pretty or compact dump
     *
     * @return  a {@code String} object representing the target parsed YAML file.
     *
     * @throws  UtilException
     *          if unable to dump the YAML file.
     */
    public String dumpYml(Object obj, Integer indent, Boolean prettyPrint) {
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

    @SuppressWarnings("Unfinished")
    public Map<String, Object> getValue(String key) {
        try {
            return null;
        } catch (Exception e) {
            throw new UtilException(YamlUtil.class, "There was an error in loading the yaml content! " + e.getMessage());
        }
    }
}
