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

package org.eclipse.tractusx.digitalproductpass.config;

import com.sun.source.tree.Tree;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class consists exclusively to define the attributes and methods needed for the IRS configuration.
 **/
@Configuration
@ConfigurationProperties(prefix="configuration.irs")
public class IrsConfig {

    /** ATTRIBUTES **/
    Boolean enabled;
    String endpoint;
    TreeConfig tree;
    Paths paths;
    String callbackUrl;

    /** CONSTRUCTOR(S) **/
    public IrsConfig(Boolean enabled, String endpoint, TreeConfig tree, Paths paths, String callbackUrl) {
        this.enabled = enabled;
        this.endpoint = endpoint;
        this.tree = tree;
        this.paths = paths;
        this.callbackUrl = callbackUrl;
    }
    public IrsConfig() {
    }

    public IrsConfig(String endpoint, TreeConfig tree, Paths paths) {
        this.endpoint = endpoint;
        this.tree = tree;
        this.paths = paths;
    }

    public IrsConfig(String endpoint, TreeConfig tree, Paths paths, String callbackUrl) {
        this.endpoint = endpoint;
        this.tree = tree;
        this.paths = paths;
        this.callbackUrl = callbackUrl;
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

    public TreeConfig getTree() {
        return tree;
    }

    public void setTree(TreeConfig tree) {
        this.tree = tree;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /** INNER CLASSES **/

    /**
     * This class consists exclusively to define the attributes and methods needed for the IRS Tree configurations.
     **/
    public static class TreeConfig{

        /** ATTRIBUTES **/
        String fileName;

        Boolean indent;


        public TreeConfig(String fileName) {
            this.fileName = fileName;
        }

        public TreeConfig(String fileName, Boolean indent) {
            this.fileName = fileName;
            this.indent = indent;
        }

        public TreeConfig() {
        }

        /** GETTERS AND SETTERS **/
        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Boolean getIndent() {
            return indent;
        }

        public void setIndent(Boolean indent) {
            this.indent = indent;
        }
    }


    /**
     * This class consists exclusively to define the attributes and methods needed for the job path configuration.
     **/
    public static class Paths{
        String job;

        public Paths(String job) {
            this.job = job;
        }

        public Paths() {
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }
    }

}
