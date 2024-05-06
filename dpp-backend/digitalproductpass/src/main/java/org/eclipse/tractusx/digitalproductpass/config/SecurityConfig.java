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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * This class consists exclusively to define the attributes and methods needed for the Vault service configuration.
 **/
@Configuration
@ConfigurationProperties(prefix="configuration.security")
public class SecurityConfig {

    /** ATTRIBUTES **/
    private AuthorizationConfig authorization;
    private StartUpCheckConfig startUpChecks;
    private AuthenticationConfig authentication;

    public SecurityConfig(AuthorizationConfig authorization, StartUpCheckConfig startUpChecks, AuthenticationConfig authentication) {
        this.authorization = authorization;
        this.startUpChecks = startUpChecks;
        this.authentication = authentication;
    }

    public SecurityConfig() {
    }

    /** GETTERS AND SETTERS **/
    public AuthorizationConfig getAuthorization() {
        return authorization;
    }

    public void setAuthorization(AuthorizationConfig authorization) {
        this.authorization = authorization;
    }

    public StartUpCheckConfig getStartUpChecks() {
        return startUpChecks;
    }

    public void setStartUpChecks(StartUpCheckConfig startUpChecks) {
        this.startUpChecks = startUpChecks;
    }

    public AuthenticationConfig getAuthentication() {
        return authentication;
    }

    public void setAuthentication(AuthenticationConfig authentication) {
        this.authentication = authentication;
    }

    /** INNER CLASSES **/

    /**
     * This class defined the authorization configuration
     **/
    public static class AuthorizationConfig{

        private Boolean bpnAuth;

        private Boolean roleAuth;

        public AuthorizationConfig(Boolean bpnAuth, Boolean roleAuth) {
            this.bpnAuth = bpnAuth;
            this.roleAuth = roleAuth;
        }

        public AuthorizationConfig() {
        }

        public Boolean getBpnAuth() {
            return bpnAuth;
        }

        public void setBpnAuth(Boolean bpnAuth) {
            this.bpnAuth = bpnAuth;
        }

        public Boolean getRoleAuth() {
            return roleAuth;
        }

        public void setRoleAuth(Boolean roleAuth) {
            this.roleAuth = roleAuth;
        }
    }
    /**
     * This class defined the StartUpChecks configuration
     **/
    public static class StartUpCheckConfig{

        private Boolean bpnCheck;

        private Boolean edcCheck;

        public StartUpCheckConfig(Boolean bpnCheck, Boolean edcCheck) {
            this.bpnCheck = bpnCheck;
            this.edcCheck = edcCheck;
        }

        public StartUpCheckConfig() {
        }

        public Boolean getBpnCheck() {
            return bpnCheck;
        }

        public void setBpnCheck(Boolean bpnCheck) {
            this.bpnCheck = bpnCheck;
        }

        public Boolean getEdcCheck() {
            return edcCheck;
        }

        public void setEdcCheck(Boolean edcCheck) {
            this.edcCheck = edcCheck;
        }
    }

    public static class AuthenticationConfig {
        private String header;


        public AuthenticationConfig() {
        }

        public AuthenticationConfig(String header) {
            this.header = header;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }
    }

}
