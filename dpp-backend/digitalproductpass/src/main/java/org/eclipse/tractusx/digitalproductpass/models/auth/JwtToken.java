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

/*********************************************************************************
 *
 * Tractus-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class consists exclusively to define attributes and methods related to the JSON Web Token (JWT) token used
 * for authentication in the Application.
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtToken {

    /** ATTRIBUTES **/
    @JsonProperty("access_token")
    String accessToken;
    @JsonProperty("refresh_token")
    String refreshToken;
    @JsonProperty("expires_in")
    Integer expiresIn;
    @JsonProperty("refresh_expires_in")
    Integer refreshExpiresIn;
    @JsonProperty("token_type")
    String tokenType;
    @JsonProperty("id_token")
    String idToken;
    @JsonProperty("not-before-policy")
    String notBeforePolicy;
    @JsonProperty("scope")
    String scope;

    /** CONSTRUCTOR(S) **/
    public JwtToken(){

    }
    public JwtToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    /** GETTERS AND SETTERS **/
    public String getAccessToken() {
        return accessToken;
    }
    @SuppressWarnings("Unused")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    @SuppressWarnings("Unused")
    public String getRefreshToken() {
        return refreshToken;
    }
    @SuppressWarnings("Unused")
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    @SuppressWarnings("Unused")
    public Integer getExpiresIn() {
        return expiresIn;
    }
    @SuppressWarnings("Unused")
    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }
    @SuppressWarnings("Unused")
    public Integer getRefreshExpiresIn() {
        return refreshExpiresIn;
    }
    @SuppressWarnings("Unused")
    public void setRefreshExpiresIn(Integer refreshExpiresIn) {
        this.refreshExpiresIn = refreshExpiresIn;
    }
    @SuppressWarnings("Unused")
    public String getTokenType() {
        return tokenType;
    }
    @SuppressWarnings("Unused")
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    @SuppressWarnings("Unused")
    public String getIdToken() {
        return idToken;
    }
    @SuppressWarnings("Unused")
    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
    @SuppressWarnings("Unused")
    public String getNotBeforePolicy() {
        return notBeforePolicy;
    }
    @SuppressWarnings("Unused")
    public void setNotBeforePolicy(String notBeforePolicy) {
        this.notBeforePolicy = notBeforePolicy;
    }
    @SuppressWarnings("Unused")
    public String getScope() {
        return scope;
    }
    @SuppressWarnings("Unused")
    public void setScope(String scope) {
        this.scope = scope;
    }
}
