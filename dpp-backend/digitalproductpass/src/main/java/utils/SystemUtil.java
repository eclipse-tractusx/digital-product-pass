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

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

/**
 * This class consists exclusively of methods to operate on System information.
 *
 * <p> The methods defined here are intended to give information related with System's usage (Memory, Disk, etc.).
 *
 */
public final class SystemUtil {

    /**
     * Returns the Memory usage by the Application.
     * <p>
     *
     * @return  the result of the subtraction between the total memory and the estimated free memory left on the system.
     *
     */
    public static Long getMemoryUsage(){
        return SystemUtil.getTotalMemory() - SystemUtil.getFreeMemory();
    }

    /**
     * Returns the total amount of memory in the Java virtual machine.
     * <p>
     *
     * @return  the total amount of memory currently available for current and future objects, measured in bytes.
     *
     */
    public static Long getTotalMemory(){
        return Runtime.getRuntime().totalMemory();
    }

    /**
     * Returns the native process Id for the application.
     * <p>
     *
     * @return  the pid number given to the application by the Operating System.
     *
     */
    public static Long getPid(){
        return ProcessHandle.current().pid();
    }

    /**
     * Returns the remaining free memory in the Java virtual machine.
     * <p>
     *
     * @return an approximation to the total amount of memory currently available for future allocated objects, measured in bytes.
     *
     */
    public static Long getFreeMemory(){
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * Returns the amount of memory that is committed for the Java virtual machine to use.
     * This amount of memory is guaranteed for the Java virtual machine to use.
     * <p>
     *
     * @return the amount of committed memory in bytes as a String.
     *
     */
    public static String getCommitedMemory(){
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        return String.format("%.2f",(double)memoryMXBean.getHeapMemoryUsage().getCommitted()/1073741824);
    }

    /**
     * Returns the amount of memory that the Java virtual machine initially requests from the operating system for memory management.
     * <p>
     *
     * @return the initial size of memory in bytes as a String.
     *
     */
    public static String getInitialMemory(){
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        return String.format("%.2f",(double)memoryMXBean.getHeapMemoryUsage().getInit()/1073741824);
    }

    /**
     * Returns the current memory usage of the heap that is used for object allocation.
     * <p>
     *
     * @return the amount of used memory in bytes as a String.
     *
     */
    public static String getUsedHeapMemory(){
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        return java.lang.String.format("%.2f",(double)memoryMXBean.getHeapMemoryUsage().getUsed()/1073741824);
    }

    /**
     * Returns the maximum amount of memory that can be used for memory management.
     * <p>
     *
     * @return the maximum amount of memory in bytes as a String.
     *
     */
    public static String getMaxHeapMemory(){
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        return java.lang.String.format("%.2f",(double)memoryMXBean.getHeapMemoryUsage().getMax()/1073741824);
    }

    /**
     * Returns the amount of disk usage by the application.
     * <p>
     *
     * @return the result of the subtraction between the total size of the partition and the free space left on the system.
     *
     */
    public static Long getDiskUsage(){
        String path = FileUtil.getWorkdirPath();
        File diskPartition = new File(path);
        return diskPartition.getTotalSpace() - diskPartition.getFreeSpace();
    }

    /**
     * Gets an environment variable specified by the given {@code String} name
     * <p>
     * @param   name
     *          the name of the environment variable to look for.
     * @param   defaultValue
     *          the default value to return in case of not finding the environment variable.
     *
     * @return  a {@code String} object representing the target environment variable.
     *
     */
    public static String getEnvironmentVariable(String name, String defaultValue){
       return System.getenv().getOrDefault(name, defaultValue);
    }

}
