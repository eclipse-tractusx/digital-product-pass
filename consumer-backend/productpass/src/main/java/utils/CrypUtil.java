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

import com.google.common.hash.Hashing;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class CrypUtil {
    private CrypUtil() {
        throw new IllegalStateException("Tool/Utility Class Illegal Initialization");
    }
    public static String toBase64(String str){
        return Base64.getEncoder().encodeToString(str.getBytes());
    }
    public static String fromBase64(String base64){
        return new String(Base64.getDecoder().decode(base64));
    }
    public static String toBase64Url(String str){
        return Base64.getUrlEncoder().encodeToString(str.getBytes());
    }
    public static String fromBase64Url(String base64){
        return new String(Base64.getUrlDecoder().decode(base64));
    }


    public static String sha256(String digest){
        return Hashing.sha256()
                .hashString(digest, StandardCharsets.UTF_8)
                .toString();
    }
    public static String decodeFromUtf8(String encodedURL){
        return URLDecoder.decode(encodedURL, StandardCharsets.UTF_8);
    }
    public static String encodeToUtf8(String decodedURL){
        return URLEncoder.encode(decodedURL, StandardCharsets.UTF_8);
    }

}
