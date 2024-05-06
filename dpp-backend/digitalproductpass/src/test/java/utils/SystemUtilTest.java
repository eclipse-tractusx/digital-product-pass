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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class SystemUtilTest {

    @Test
    void getPid() {
        Long PID = null;
        try{
            PID = SystemUtil.getPid();
            LogUtil.printTest("PID: ["+PID+"]");
        } catch(Exception e){
            fail("It was not possible to get PID: " + e.getMessage());
        }
        assertNotNull(PID);
    }

    @Test
    void getUsedHeapMemory() {
        String memoryHeap = null;
        try{
            memoryHeap = SystemUtil.getUsedHeapMemory();
            LogUtil.printTest("Memory Heap: ["+memoryHeap+"]");
        } catch(Exception e){
            fail("It was not possible to get Used Memory Heap: " + e.getMessage());
        }
        assertNotNull(memoryHeap);
    }

    @Test
    void getMemoryUsage() {
        Long var = null;
        try{
            var = SystemUtil.getMemoryUsage();
            LogUtil.printTest("Memory Usage: ["+var+"]");
        } catch(Exception e){
            fail("It was not possible to get Memory Usage: " + e.getMessage());
        }
        assertNotNull(var);
    }

    @Test
    void getTotalMemory() {
        Long var = null;
        try{
            var = SystemUtil.getTotalMemory();
            LogUtil.printTest("Total Memory: ["+var+"]");
        } catch(Exception e){
            fail("It was not possible to get Total Memory: " + e.getMessage());
        }
        assertNotNull(var);
    }

    @Test
    void getFreeMemory() {
        Long var = null;
        try{
            var = SystemUtil.getTotalMemory();
            LogUtil.printTest("Free Memory: ["+var+"]");
        } catch(Exception e){
            fail("It was not possible to get Free Memory: " + e.getMessage());
        }
        assertNotNull(var);
    }

    @Test
    void getCommitedMemory() {
        String var = null;
        try{
            var = SystemUtil.getCommitedMemory();
            LogUtil.printTest("Committed Memory: ["+var+"]");
        } catch(Exception e){
            fail("It was not possible to get Committed Memory: " + e.getMessage());
        }
        assertNotNull(var);
    }

    @Test
    void getInitialMemory() {
        String var = null;
        try{
            var = SystemUtil.getInitialMemory();
            LogUtil.printTest("Initial Memory: ["+var+"]");
        } catch(Exception e){
            fail("It was not possible to get Initial Memory: " + e.getMessage());
        }
        assertNotNull(var);
    }

    @Test
    void getMaxHeapMemory() {
        String var = null;
        try{
            var = SystemUtil.getMaxHeapMemory();
            LogUtil.printTest("Max Heap Memory: ["+var+"]");
        } catch(Exception e){
            fail("It was not possible to get Max Heap Memory: " + e.getMessage());
        }
        assertNotNull(var);
    }


    @Test
    void getDiskUsage() {
        Long var = null;
        try{
            var = SystemUtil.getDiskUsage();
            LogUtil.printTest("Disk Usage: ["+var+"]");
        } catch(Exception e){
            fail("It was not possible to get Disk Usage: " + e.getMessage());
        }
        assertNotNull(var);
    }


}
