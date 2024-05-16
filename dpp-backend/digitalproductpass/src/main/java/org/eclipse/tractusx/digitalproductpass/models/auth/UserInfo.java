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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class consists exclusively to define attributes and methods related to the needed User's information.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfo {

    /** ATTRIBUTES **/
    @JsonProperty("sub")
    String sub;
    @JsonProperty("email_verified")
    String email_verified;
    @JsonProperty("bpn")
    String bpn;
    @JsonProperty("name")
    String name;
    @JsonProperty("preferred_username")
    String preferred_username;
    @JsonProperty("locale")
    String locale;
    @JsonProperty("given_name")
    String given_name;
    @JsonProperty("family_name")
    String family_name;
    @JsonProperty("email")
    String email;

    /** CONSTRUCTOR(S) **/

    @SuppressWarnings("Unused")
    public UserInfo() {
    }

    public UserInfo(String sub, String email_verified, String bpn, String name, String preferred_username, String locale, String given_name, String family_name, String email) {
        this.sub = sub;
        this.email_verified = email_verified;
        this.bpn = bpn;
        this.name = name;
        this.preferred_username = preferred_username;
        this.locale = locale;
        this.given_name = given_name;
        this.family_name = family_name;
        this.email = email;
    }

    public UserInfo(String sub, String email_verified, String name, String preferred_username, String locale, String given_name, String family_name, String email) {
        this.sub = sub;
        this.email_verified = email_verified;
        this.name = name;
        this.preferred_username = preferred_username;
        this.locale = locale;
        this.given_name = given_name;
        this.family_name = family_name;
        this.email = email;
    }

    /** GETTERS AND SETTERS **/
    @SuppressWarnings("Unused")
    public String getSub() {
        return sub;
    }
    @SuppressWarnings("Unused")
    public void setSub(String sub) {
        this.sub = sub;
    }
    @SuppressWarnings("Unused")
    public String getEmail_verified() {
        return email_verified;
    }
    @SuppressWarnings("Unused")
    public void setEmail_verified(String email_verified) {
        this.email_verified = email_verified;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @SuppressWarnings("Unused")
    public String getPreferred_username() {
        return preferred_username;
    }
    @SuppressWarnings("Unused")
    public void setPreferred_username(String preferred_username) {
        this.preferred_username = preferred_username;
    }
    @SuppressWarnings("Unused")
    public String getLocale() {
        return locale;
    }
    @SuppressWarnings("Unused")
    public void setLocale(String locale) {
        this.locale = locale;
    }
    @SuppressWarnings("Unused")
    public String getGiven_name() {
        return given_name;
    }
    @SuppressWarnings("Unused")
    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }
    @SuppressWarnings("Unused")
    public String getFamily_name() {
        return family_name;
    }
    @SuppressWarnings("Unused")
    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }
    @SuppressWarnings("Unused")
    public String getEmail() {
        return email;
    }
    @SuppressWarnings("Unused")
    public void setEmail(String email) {
        this.email = email;
    }

    public String getBpn() {
        return bpn;
    }

    public void setBpn(String bpn) {
        this.bpn = bpn;
    }
}
