
/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2023 Contributors to the CatenaX (ng) GitHub Organisation.
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

package org.eclipse.tractusx.productpass.models.auth;

import utils.HttpUtil;

import jakarta.servlet.http.HttpServletRequest;

public class Credential {
    private String session_code;
    private String execution;
    private String client_id;

    private String client_secret;
    private String tab_id;
    private UserCredential userCredential;

    public Credential(){

    }
    public Credential(UserCredential userCredential){
        this.userCredential = userCredential;
    }
    public Credential(String session_code, String execution, String client_id, String client_secret, String tab_id, String username, String password, String credentialId ) {
        this.session_code = session_code;
        this.execution = execution;
        this.client_id = client_id;
        this.tab_id = tab_id;
        this.userCredential = new UserCredential(username, password, credentialId);
    }
    public Credential(String session_code, String execution, String client_id, String tab_id, UserCredential userCredential) {
        this.session_code = session_code;
        this.execution = execution;
        this.client_id = client_id;
        this.tab_id = tab_id;
        this.userCredential = userCredential;
    }
    public Credential(String session_code, String execution, String client_id, String tab_id) {
        this.session_code = session_code;
        this.execution = execution;
        this.client_id = client_id;
        this.tab_id = tab_id;
        this.userCredential = new UserCredential();
    }

    public Credential(String session_code, String execution, String client_id, String client_secret, String tab_id, UserCredential userCredential) {
        this.session_code = session_code;
        this.execution = execution;
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.tab_id = tab_id;
        this.userCredential = userCredential;
    }

    public void mapKeycloakResponse(HttpServletRequest request){
        this.session_code = HttpUtil.getParamOrDefault(request, "session_code", null);
        this.execution = HttpUtil.getParamOrDefault(request, "execution", null);
        this.client_id = HttpUtil.getParamOrDefault(request, "client_id", null);
        this.tab_id = HttpUtil.getParamOrDefault(request, "tab_id", null);
    }
    public String getSession_code() {
        return  session_code;
    }

    public void setSession_code(String session_code) {
        this.session_code = session_code;
    }

    public String getExecution() {
        return execution;
    }

    public void setExecution(String execution) {
        this.execution = execution;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getTab_id() {
        return tab_id;
    }

    public void setTab_id(String tab_id) {
        this.tab_id = tab_id;
    }

    public UserCredential getUserCredential() {
        return userCredential;
    }

    public void setUserCredential(UserCredential userCredential) {
        this.userCredential = userCredential;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }
}
