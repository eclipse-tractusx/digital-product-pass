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

package org.eclipse.tractusx.productpass.models.auth;

public class UserCredential {

    private String username;
    private String password;
    private String credentialId;

    private JwtToken jwt = null;

    public UserCredential(){

    }
    public UserCredential(String username, String password, String credentialId) {
        this.username = username;
        this.password = password;
        this.credentialId = credentialId;
    }

    public UserCredential(String username, String password, String credentialId, JwtToken jwt) {
        this.username = username;
        this.password = password;
        this.credentialId = credentialId;
        this.jwt = jwt;
    }

    public UserCredential(JwtToken jwt){
        this.jwt = jwt;
    }

    public JwtToken getJwt() {
        return jwt;
    }

    public void setJwt(JwtToken jwt) {
        this.jwt = jwt;
    }
    public void setJwtToken(String accessToken, String refreshToken){
        this.jwt = new JwtToken(accessToken, refreshToken);
    }
    public void cleanJwtToken(){
        this.jwt = new JwtToken();
    }
    public void deleteJwt(){
        this.jwt = null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

}
