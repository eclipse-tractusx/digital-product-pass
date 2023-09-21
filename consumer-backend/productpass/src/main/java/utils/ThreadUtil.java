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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public final class ThreadUtil {
    private ThreadUtil() {
        throw new IllegalStateException("Tool/Utility Class Illegal Initialization");
    }
    public static void runTask(Runnable runnable){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(runnable);
    }
    public static void runTask(Callable<Void> task,String name){
        List<Callable<Void>> taskList = new ArrayList<Callable<Void>>();
        taskList.add(task);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try
        {
            executor.invokeAll(taskList);
        }
        catch (InterruptedException ie)
        {
            //do something if you care about interruption;
        }
    }
    public static Thread runThread(Runnable runnable,String name){
        Thread thread = new Thread(runnable, name);
        thread.start();

        return thread;
    }
    public static Thread runThread(Runnable runnable){
        Thread thread = new Thread(runnable);
        thread.start();

        return thread;
    }

    public static void sleep(Integer milliseconds)
    {
        try
        {
            Thread.sleep(milliseconds);
        }
        catch(Exception e)
        {
            Thread.currentThread().interrupt();
        }
    }

}
