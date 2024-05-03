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

package org.eclipse.tractusx.digitalproductpass.models.auth;

/**
 * This class consists exclusively to define attributes and methods related to the JSON Web Token (JWT) token used
 * for authentication in the Application.
 **/
public class UserCredential {

    /** ATTRIBUTES **/
    private String username;
    private String password;
    private String credentialId;
    private JwtToken jwt = null;

    /** CONSTRUCTOR(S) **/
    public UserCredential(){

    }
    public UserCredential(String username, String password, String credentialId) {
        this.username = username;
        this.password = password;
        this.credentialId = credentialId;
    }
    @SuppressWarnings("Unused")
    public UserCredential(String username, String password, String credentialId, JwtToken jwt) {
        this.username = username;
        this.password = password;
        this.credentialId = credentialId;
        this.jwt = jwt;
    }
    @SuppressWarnings("Unused")
    public UserCredential(JwtToken jwt){
        this.jwt = jwt;
    }

    /** GETTERS AND SETTERS **/
    public JwtToken getJwt() {
        return jwt;
    }
    public void setJwt(JwtToken jwt) {
        this.jwt = jwt;
    }
    @SuppressWarnings("Unused")
    public void setJwtToken(String accessToken, String refreshToken){
        this.jwt = new JwtToken(accessToken, refreshToken);
    }
    @SuppressWarnings("Unused")
    public String getUsername() {
        return username;
    }
    @SuppressWarnings("Unused")
    public void setUsername(String username) {
        this.username = username;
    }
    @SuppressWarnings("Unused")
    public String getPassword() {
        return password;
    }
    @SuppressWarnings("Unused")
    public void setPassword(String password) {
        this.password = password;
    }
    @SuppressWarnings("Unused")
    public String getCredentialId() {
        return credentialId;
    }
    @SuppressWarnings("Unused")
    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    /** METHODS **/
    @SuppressWarnings("Unused")
    public void cleanJwtToken(){
        this.jwt = new JwtToken();
    }
    @SuppressWarnings("Unused")
    public void deleteJwt(){
        this.jwt = null;
    }

}
