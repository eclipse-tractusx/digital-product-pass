/*********************************************************************************
 *
 * Tractus-X - Digital Product Pass Application
 *
 * Copyright (c) 2022, 2024 BMW AG
 * Copyright (c) 2022, 2024 Henkel AG & Co. KGaA
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CatenaXUtilTest {
    String edcUrlWithoutDsp = "https://edc-url.com/";
    String edcUrlWithoutDspBar = "https://edc-url.com";
    String edcUrlWithPathWithoutDsp = "https://edc-url.com/provider/";
    String edcUrlWithPathWithoutDspBar = "https://edc-url.com/provider";
    String edcUrlWithDsp = "https://edc-url.com"+CatenaXUtil.edcDataEndpoint;
    String edcUrlWithPathDsp = "https://edc-url.com/provider"+CatenaXUtil.edcDataEndpoint;
    String edcUrlWithBpnDsp = "https://edc-url.com/BPNL000000000000"+CatenaXUtil.edcDataEndpoint;
    String edcUrlWithBpnWithoutDsp = "https://edc-url.com/BPNL000000000000";
    String edcUrlWithPort = "https://edc-url.com:8888/BPNL000000000000";
    String edcUrlWithBpnWithoutDspMorePath = "https://edc-url.com/BPNL000000000000/this/is/more/path";
    @Test
    void buildEdcUrlWithoutDsp() {
        String output = CatenaXUtil.buildDspEndpoint(edcUrlWithoutDsp);
        LogUtil.printTest("[CatenaXUtil.buildEdcUrlWithoutDsp] Input: ["+edcUrlWithoutDsp+"] Output: ["+output+"]");
        assertEquals(edcUrlWithDsp, output);
    }
    @Test
    void buildEdcUrlWithoutDspBar() {
        String output = CatenaXUtil.buildDspEndpoint(edcUrlWithoutDspBar);
        LogUtil.printTest("[CatenaXUtil.buildEdcUrlWithoutDspBar] Input: ["+edcUrlWithoutDspBar+"] Output: ["+output+"]");
        assertEquals(edcUrlWithDsp, output);
    }
    @Test
    void buildEdcUrlWithPathWithoutDsp() {
        String output = CatenaXUtil.buildDspEndpoint(edcUrlWithPathWithoutDsp);
        LogUtil.printTest("[CatenaXUtil.buildEdcUrlWithPathWithoutDsp] Input: ["+edcUrlWithPathWithoutDsp+"] Output: ["+output+"]");
        assertEquals(edcUrlWithPathDsp, output);
    }

    @Test
    void buildEdcUrlWithPathWithoutDspBar() {
        String output = CatenaXUtil.buildDspEndpoint(edcUrlWithPathWithoutDspBar);
        LogUtil.printTest("[CatenaXUtil.buildEdcUrlWithPathWithoutDspBar] Input: ["+edcUrlWithPathWithoutDspBar+"] Output: ["+output+"]");
        assertEquals(edcUrlWithPathDsp, output);
    }
    @Test
    void buildEdcUrlWithBpnDsp() {
        String output = CatenaXUtil.buildDspEndpoint(edcUrlWithBpnDsp);
        LogUtil.printTest("[CatenaXUtil.buildEdcUrlWithBpnDsp] Input: ["+edcUrlWithBpnDsp+"] Output: ["+output+"]");
        assertEquals(edcUrlWithBpnDsp, output);
    }

    @Test
    void buildEdcUrlWithBpnWithoutDsp() {
        String output = CatenaXUtil.buildDspEndpoint(edcUrlWithBpnWithoutDsp);
        LogUtil.printTest("[CatenaXUtil.buildEdcUrlWithBpnWithoutDsp] Input: ["+edcUrlWithBpnWithoutDsp+"] Output: ["+output+"]");
        assertEquals(edcUrlWithBpnDsp, output);
    }

    @Test
    void buildEdcUrlWithBpnWithoutDspMorePath() {
        String output = CatenaXUtil.buildDspEndpoint(edcUrlWithBpnWithoutDspMorePath);
        LogUtil.printTest("[CatenaXUtil.edcUrlWithBpnWithoutDspMorePath] Input: ["+edcUrlWithBpnWithoutDspMorePath+"] Output: ["+output+"]");
        assertEquals(edcUrlWithBpnWithoutDspMorePath+CatenaXUtil.edcDataEndpoint,output);
    }
    @Test
    void buildEdcWithPort() {
        String output = CatenaXUtil.buildDspEndpoint(edcUrlWithPort);
        LogUtil.printTest("[CatenaXUtil.buildEdcWithPort] Input: ["+edcUrlWithPort+"] Output: ["+output+"]");
        assertEquals(edcUrlWithPort+CatenaXUtil.edcDataEndpoint, output);
    }


}