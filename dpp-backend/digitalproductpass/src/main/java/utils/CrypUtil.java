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

package utils;

import com.google.common.hash.Hashing;
import utils.exceptions.UtilException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;


/**
 * This class consists exclusively of methods related to Cryptography.
 *
 * <p> The methods defined here are intended to encode, decode, encrypt, decrypt or hash data and messages.
 *
 */
public final class CrypUtil {
    private CrypUtil() {
        throw new IllegalStateException("Tool/Utility Class Illegal Initialization");
    }

    /**
     * Encode a {@code String} message to Base64.
     * <p>
     * @param   str
     *          the {@code String} message to encode.
     *
     * @return  a {@code String} base64 encoded message.
     *
     */
    public static String toBase64(String str){
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    /**
     * Encode a {@code byte[]} data to Base64.
     * <p>
     * @param   bytes
     *          the {@code byte[]} data to encode.
     *
     * @return  a {@code String} base64 encoded data.
     *
     */
    public static String toBase64(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Decode a {@code String} message from Base64.
     * <p>
     * @param   base64
     *          the {@code String} base 64 encoded message.
     *
     * @return  the {@code String} message decoded.
     *
     */
    public static String fromBase64(String base64){
        return new String(Base64.getDecoder().decode(base64));
    }

    /**
     * Decode a {@code byte[]} data from Base64.
     * <p>
     * @param   base64
     *           {@code String} base64 encoded datat.
     *
     * @return  he {@code byte[]} decoded data.
     *
     */
    public static byte[] fromBase64ToByte(String base64){
        return Base64.getDecoder().decode(base64);
    }

    /**
     * Encode a {@code String} message to Base64 URL.
     * <p>
     * @param   str
     *          the {@code String} message to encode.
     *
     * @return  a {@code String} base64 URL encoded message.
     *
     */
    public static String toBase64Url(String str){
        return Base64.getUrlEncoder().encodeToString(str.getBytes());
    }

    /**
     * Decode a {@code String} message from Base64 URL.
     * <p>
     * @param   base64
     *          the {@code String} base 64 URL encoded message.
     *
     * @return  the {@code String} message decoded.
     *
     */
    public static String fromBase64Url(String base64){
        return new String(Base64.getUrlDecoder().decode(base64));
    }

    /**
     * Check if a {@code String} message is base64 encoded.
     * <p>
     * @param   str
     *          the {@code String} encoded message.
     *
     * @return  true if the {@code String} message is base64 encoded, false otherwise.
     *
     */
    public static Boolean isBase64(String str){
        return org.apache.commons.net.util.Base64.isArrayByteBase64(str.getBytes()); // Check if string is Base64 encoded
    }

    /**
     * Generates an Universally Unique Identifier (UUID).
     * <p>
     *
     * @return  a {@code String} random UUID.
     *
     */
    public static String getUUID(){
        return UUID.randomUUID().toString();
    }

    /**
     * Creates a SHA256 hash of the given {@code String} message.
     * <p>
     * @param   digest
     *          the {@code String} message to hash code.
     *
     * @return  a {@code String} SHA256 hash coded message.
     *
     */
    public static String sha256(final String digest){
        return Hashing.sha256()
                .hashString(digest, StandardCharsets.UTF_8)
                .toString();
    }

    /**
     * Creates a MD5 hash of the given {@code String} message.
     * <p>
     * @param   digest
     *          the {@code String} message to hash code.
     *
     * @return  a {@code String} MD5 hash coded message.
     *
     */
    public static String md5(final String digest){
        return Hashing.md5()
                .hashString(digest, StandardCharsets.UTF_8)
                .toString();
    }

    /**
     * Creates a SHA1 hash of the given {@code String} message.
     * <p>
     * @param   digest
     *          the {@code String} message to hash code.
     *
     * @return  a {@code byte[]} sha256 hash coded data.
     *
     * @throws  UtilException
     *          if unable to generate the sha1 hash.
     */
    public static byte[] sha1Bytes(final String digest){
        try {
            return MessageDigest.getInstance("SHA-1").digest(digest.getBytes("UTF-8"));
        } catch (Exception e)
        {
            throw new UtilException(CrypUtil.class,"It was not possible to generate sha1 hash" + e.getMessage()) ;
        }
    }

    /**
     * Decode a {@code String} message from UTF-8.
     * <p>
     * @param   encodedURL
     *          the {@code String} URL encoded message.
     *
     * @return  the {@code String} message decoded.
     *
     */
    @SuppressWarnings("Unused")
    public static String decodeFromUtf8(String encodedURL){
        return URLDecoder.decode(encodedURL, StandardCharsets.UTF_8);
    }

    /**
     * Encode a {@code String} message to UTF-8.
     * <p>
     * @param   decodedURL
     *          the {@code String} message to encode.
     *
     * @return  the {@code String} URL message encoded as UTF-8.
     *
     */
    @SuppressWarnings("Unused")
    public static String encodeToUtf8(String decodedURL){
        return URLEncoder.encode(decodedURL, StandardCharsets.UTF_8);
    }

    /**
     * Creates an Advanced Encryption Secret (AES) key from a given secret key.
     * <p>
     * @param   secret
     *          the {@code String} secret key.
     *
     * @return  the {@code SecretKeySpec} object with the generated AES key.
     *
     * @throws  UtilException
     *          if unable to generate the AES key.
     */
    public static SecretKeySpec buildAesKey(final String secret) {
        try {
            byte[] bytesKey = CrypUtil.sha1Bytes(secret);
            return new SecretKeySpec(Arrays.copyOf(bytesKey, 16), "AES");
        } catch (Exception e) {
            throw new UtilException(CrypUtil.class,"It was not possible to set key " + e.getMessage()) ;
        }
    }

    /**
     * Encrypts a given message with AES cypher.
     * <p>
     * @param   decoded
     *          the {@code String} message to encrypt.
     * @param   key
     *          the {@code String} secret key to encrypt.
     *
     * @return  the {@code String} object with the AES encrypted message.
     *
     * @throws  UtilException
     *          if unable encrypt the message.
     */
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

    /**
     * Decrypts a given AES encrypted message.
     * <p>
     * @param   encoded
     *          the {@code String} AES encrypted message.
     * @param   key
     *          the {@code String} secret key to decrypt.
     *
     * @return  the {@code String} message decrypted.
     *
     * @throws  UtilException
     *          if unable to decrypt the message.
     */
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
