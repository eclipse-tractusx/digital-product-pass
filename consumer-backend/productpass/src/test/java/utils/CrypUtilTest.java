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

package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CrypUtilTest {

    String text = "I am a @Test!";

    String encodedText = "SSBhbSBhIEBUZXN0IQ==";

    String encodedTextUrl = "SSBhbSBhIEBUZXN0IQ==";

    String textHash = "258aab8b362e5d5e971c8e32e3f7072e9bd34647263bb107167b28d6a46d0650";

    @Test
    void toBase64() {
        String encoded = null;
        try{
            encoded = CrypUtil.toBase64(text);
            LogUtil.printTest("[CrypUtil.toBase64] Encoded Text: ["+encoded+"]");
        } catch(Exception e){
            fail("It was not possible to encode to base64: " + e.getMessage());
        }
        assertEquals(encodedText, encoded);
    }

    @Test
    void fromBase64() {
        String decoded = null;
        try{
            decoded = CrypUtil.fromBase64(encodedText);
            LogUtil.printTest("[CrypUtil.toBase64] Decoded Text: ["+decoded+"]");
        } catch(Exception e){
            fail("It was not possible to decode from base64: " + e.getMessage());
        }
        assertEquals(text, decoded);
    }

    @Test
    void toBase64Url() {
        String encoded = null;
        try{
            encoded = CrypUtil.toBase64Url(text);
            LogUtil.printTest("[CrypUtil.toBase64] Encoded Text Url: ["+encoded+"]");
        } catch(Exception e){
            fail("It was not possible to encode to base64: " + e.getMessage());
        }
        assertEquals(encodedText, encoded);
    }

    @Test
    void fromBase64Url() {
        String decoded = null;
        try{
            decoded = CrypUtil.fromBase64Url(encodedTextUrl);
            LogUtil.printTest("[CrypUtil.toBase64] Decoded Text Url: ["+decoded+"]");
        } catch(Exception e){
            fail("It was not possible to decode from base64: " + e.getMessage());
        }
        assertEquals(text, decoded);
    }

    @Test
    void sha256() {
        String hash = null;
        try{
            hash = CrypUtil.sha256(text);
            LogUtil.printTest("[CrypUtil.toBase64] Text HASH: ["+hash+"]");
        } catch(Exception e){
            fail("It was not possible to decode from base64: " + e.getMessage());
        }
        assertEquals(textHash, hash);
    }

}
