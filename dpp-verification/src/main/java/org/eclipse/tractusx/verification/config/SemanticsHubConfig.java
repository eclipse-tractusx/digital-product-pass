/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Verification Add-on
 *
 * Copyright (c) 2022 BMW AG
 * Copyright (c) 2023 CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.tractusx.verification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class consists exclusively to define the attributes and methods needed for the IRS configuration.
 **/
@Configuration
@ConfigurationProperties(prefix="configuration.semanticshub")
public class SemanticsHubConfig {

    /** ATTRIBUTES **/
    String endpoint;
    Paths paths;

    /** CONSTRUCTOR(S) **/
    public SemanticsHubConfig(String endpoint, Paths paths) {
        this.endpoint = endpoint;
        this.paths = paths;
    }
    public SemanticsHubConfig() {
    }

    /** GETTERS AND SETTERS **/

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Paths getPaths() {
        return paths;
    }

    public void setPaths(Paths paths) {
        this.paths = paths;
    }

    /** INNER CLASSES **/

    /**
     * This class consists exclusively to define the attributes and methods needed for the json schema and ttl file paths path configuration.
     **/
    public static class Paths{
        String jsonSchema;
        String ttlFile;

        public Paths(String jsonSchema, String ttlFile) {
            this.jsonSchema = jsonSchema;
            this.ttlFile = ttlFile;
        }

        public Paths() {
        }

        public String getJsonSchema() {
            return jsonSchema;
        }

        public void setJsonSchema(String jsonSchema) {
            this.jsonSchema = jsonSchema;
        }

        public String getTtlFile() {
            return ttlFile;
        }

        public void setTtlFile(String ttlFile) {
            this.ttlFile = ttlFile;
        }
    }

}
