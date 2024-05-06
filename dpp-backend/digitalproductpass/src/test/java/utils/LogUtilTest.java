/*********************************************************************************
 *
 * Tractus-X - Digital Product Pass Application
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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;
class LogUtilTest {
    
    @Test
    void printTest() {
        try {
            String message = "Test Message";
            LogUtil.printTest(message);
        } catch(Exception e){
            fail("It was not possible to print log: " + e.getMessage());
        }
    }

    @Test
    void printMessage() {
        try{
            String message = "Info Message";
            LogUtil.printMessage(message);
        } catch(Exception e){
            fail("It was not possible to print log: " + e.getMessage());
        }
    }

    @Test
    void printHTTPMessage() {
        try{
            String message = "HTTP Incoming Message";
            LogUtil.printHTTPMessage(message);
        } catch(Exception e){
            fail("It was not possible to print log: " + e.getMessage());
        }
    }

    @Test
    void printException() {
        try{
            String message = "HTTP Incoming Message";
            LogUtil.printException(new Exception("Test Exception"),message);
        } catch(Exception e){
            fail("It was not possible to print log: " + e.getMessage());
        }
    }

    @Test
    void printError() {
        try{
            String message = "Error Message";
            LogUtil.printError(message);
        } catch(Exception e){
            fail("It was not possible to print log: " + e.getMessage());
        }
    }

    @Test
    void printWarning() {
        try{
            String message = "Warning Message";
            LogUtil.printWarning(message);
        } catch(Exception e){
            fail("It was not possible to print log: " + e.getMessage());
        }
    }

    @Test
    void printDebug() {
        try{
            String message = "Debug Message";
            LogUtil.printDebug(message);
        } catch(Exception e){
            fail("It was not possible to print log: " + e.getMessage());
        }
    }

    @Test
    void printFatal() {
        try{
            String message ="Fatal Message";
            LogUtil.printFatal(message);
        } catch(Exception e){
            fail("It was not possible to print log: " + e.getMessage());
        }
    }
}
