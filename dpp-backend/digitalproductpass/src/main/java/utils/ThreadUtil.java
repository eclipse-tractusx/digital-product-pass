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

import utils.exceptions.UtilException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * This class consists exclusively of methods to operate on a Thread level.
 *
 * <p> The methods defined here are intended ease the use of Threads.
 *
 */
public final class ThreadUtil {
    private ThreadUtil() {
        throw new IllegalStateException("Tool/Utility Class Illegal Initialization");
    }

    /**
     * Runs a Task represented by the {@code Runnable} runnable.
     * <p>
     * @param   runnable
     *          the Runnable task that does not return a result.
     */
    @SuppressWarnings("Unused")
    public static void runTask(Runnable runnable){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(runnable);
    }

    @SuppressWarnings("Unfinished")
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

    /**
     * Creates and starts a Thread defined by a {@code Runnable} task and a {@code String} name.
     * <p>
     * @param   runnable
     *          the Runnable task that does not return a result.
     * @param   name
     *          the name given to this new thread.
     *
     * @return  a {@code Thread} thread initialized to perform the intended task with the given name.
     *
     */
    public static Thread runThread(Runnable runnable,String name){
        Thread thread = new Thread(runnable, name);
        thread.start();

        return thread;
    }

    /**
     * Creates and starts a nameless Thread defined by a {@code Runnable} task.
     * <p>
     * @param   runnable
     *          the Runnable task that does not return a result.
     *
     * @return  a {@code Thread} thread initialized to perform the intended task.
     *
     */
    public static Thread runThread(Runnable runnable){
        Thread thread = new Thread(runnable);
        thread.start();

        return thread;
    }

    /**
     * Puts the scheduled thread in the runtime that this method is called to sleep for a given time.
     * <p>
     * Note: In case of an exception, the thread put to sleep is interrupted.
     * <p>
     * @param   milliseconds
     *          the length of time to sleep in milliseconds.
     */
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
    /**
     * Sets a timeout for a function added in a callable
     * <p>
     * @param   milliseconds
     *          the {@code Integer} length of time of the timeout in milliseconds.
     * @param   function
     *          the {@code Callable<V>} function to be executed and then give the timeout if not returned before
     * @param   timeoutResponse
     *          the {@code <V>} timeout response to be returned when the function execution time reached the timeout
     */
    public static <V> V timeout(Integer milliseconds, Callable<V> function, V timeoutResponse)
    {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            boolean timeout = false;
            V returnObject = null;
            try {
                Future<V> future = executor.submit(function);
                returnObject = future.get(milliseconds, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                timeout = true;
            }
            executor.shutdownNow();
            if(timeout){
                return timeoutResponse;
            }
            return returnObject;
        } catch (Exception e) {
            throw new UtilException(ThreadUtil.class, e, "Failed to execute the timeout functions!");
        }
    }
}
