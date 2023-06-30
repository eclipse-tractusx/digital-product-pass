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
import utils.exceptions.UtilException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public final class CrypUtil {
    private CrypUtil() {
        throw new IllegalStateException("Tool/Utility Class Illegal Initialization");
    }
    public static String toBase64(String str){
        return Base64.getEncoder().encodeToString(str.getBytes());
    }
    public static String toBase64(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }
    public static String fromBase64(String base64){
        return new String(Base64.getDecoder().decode(base64));
    }
    public static byte[] fromBase64ToByte(String base64){
        return Base64.getDecoder().decode(base64);
    }
    public static String toBase64Url(String str){
        return Base64.getUrlEncoder().encodeToString(str.getBytes());
    }
    public static String fromBase64Url(String base64){
        return new String(Base64.getUrlDecoder().decode(base64));
    }

    public static String getUUID(){
        return UUID.randomUUID().toString();
    }

    public static String sha256(final String digest){
        return Hashing.sha256()
                .hashString(digest, StandardCharsets.UTF_8)
                .toString();
    }

    public static byte[] sha1Bytes(final String digest){
        try {
            return MessageDigest.getInstance("SHA-1").digest(digest.getBytes("UTF-8"));
        } catch (Exception e)
        {
            throw new UtilException(CrypUtil.class,"It was not possible to generate sha1 hash" + e.getMessage()) ;
        }
    }
    public static String decodeFromUtf8(String encodedURL){
        return URLDecoder.decode(encodedURL, StandardCharsets.UTF_8);
    }
    public static String encodeToUtf8(String decodedURL){
        return URLEncoder.encode(decodedURL, StandardCharsets.UTF_8);
    }
    public static SecretKeySpec buildAesKey(final String secret) {
        try {
            byte[] bytesKey = CrypUtil.sha1Bytes(secret);
            return new SecretKeySpec(Arrays.copyOf(bytesKey, 16), "AES");
        } catch (Exception e) {
            throw new UtilException(CrypUtil.class,"It was not possible to set key " + e.getMessage()) ;
        }
    }
    public static String encryptAes(final String decoded, final String key) {
        try {
            SecretKeySpec secretKey = CrypUtil.buildAesKey(key);
            Cipher encryptor = Cipher.getInstance("AES/ECB/PKCS5Padding");
            encryptor.init(Cipher.ENCRYPT_MODE, secretKey);
            return CrypUtil.toBase64(encryptor.doFinal(decoded.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new UtilException(CrypUtil.class,"It was not possible encrypt data" + e.getMessage()) ;
        }
    }

    public static String decryptAes(final String encoded, final String key) {
        try {
            SecretKeySpec secretKey = CrypUtil.buildAesKey(key);
            Cipher decryptor = Cipher.getInstance("AES/ECB/PKCS5Padding");
            decryptor.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(decryptor.doFinal(CrypUtil.fromBase64ToByte(encoded)));
        } catch (Exception e) {
            throw new UtilException(CrypUtil.class, "It was not possible encrypt dat" + e.getMessage());
        }
    }

}
