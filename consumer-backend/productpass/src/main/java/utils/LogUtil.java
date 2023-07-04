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

import org.apache.juli.logging.Log;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;
public final class LogUtil {

    /**
     * Static Tools to print logs with format and current date.
     */
    static Logger logger = LogManager.getLogger(LogUtil.class);
    private static final Level INFO = Level.forName("INFO", 400);
    private static final Level HTTP = Level.forName("HTTP", 420);
    private static final Level STATUS = Level.forName("STATUS", 430);
    private static final Level DEBUG = Level.forName("DEBUG", 500);
    private static final Level EXCEPTION = Level.forName( "EXCEPTION", 100);
    private static final Level WARNING = Level.forName("WARNING", 300);
    private static final Level HTTPError = Level.forName("HTTP ERROR", 200);
    private static final Level ERROR = Level.forName("ERROR", 200);
    private static final Level FATAL = Level.forName("FATAL", 200);
    private static final Level TEST = Level.forName("TEST", 400);



    public static void printTest(String strMessage){
        Level logLevel = TEST;
        Long pid = SystemUtil.getPid();
        String memoryUsage = SystemUtil.getUsedHeapMemory();
        String message = "|"+pid+"|"+ memoryUsage+"| [" + logLevel.name()+"] " + strMessage;
        ThreadUtil.runThread(new LogPrinter(logLevel, message), "testLogThread");
    }
    public static void printMessage(String strMessage){
        LogUtil.printLog(INFO, strMessage);
    }
    public static void printHTTPMessage(String strMessage){
        LogUtil.printLog(HTTP, strMessage);
    }
    public static void printHTTPErrorMessage(String strMessage){
        LogUtil.printLog(HTTPError, strMessage);
    }
    public static void printException(Exception e, String strMessage){
        String message = " ["+e.getMessage()+"] "+strMessage;
        LogUtil.printLog(EXCEPTION, message);
    }
    public static void printError(String strMessage){
        LogUtil.printLog(ERROR, strMessage);
    }
    public static void printWarning(String strMessage){
        LogUtil.printLog(WARNING, strMessage);
    }

    public static void printDebug(String strMessage) {
        Long pid = SystemUtil.getPid();
        String memoryUsage = SystemUtil.getUsedHeapMemory();
        String message = "|"+pid+"|"+ memoryUsage+"| [DEBUG] " + strMessage;
        logger.debug(message);
    }
    public static void printStatus(String strMessage) {
        LogUtil.printLog(STATUS, strMessage);
    }

    public static void printLog(Level logLevel, String strMessage){
        //String date = dateTimeTools.getDateTimeFormatted(null);
        Long pid = SystemUtil.getPid();
        String memoryUsage = SystemUtil.getUsedHeapMemory();
        String message = "|"+pid+"|"+ memoryUsage+"| [" + logLevel.name()+"] " + strMessage;
        logger.log(logLevel, message);
    }
    public static void printFatal(String strMessage){
        LogUtil.printLog(FATAL, strMessage);
    }

    private static class LogPrinter implements Runnable {

        private String message;
        private Level logLevel;

        public LogPrinter(Level logLevel, String strMessage) {
            this.logLevel = logLevel;
            this.message = strMessage;
        }
        public void run() {
            logger.log(this.logLevel,this.message);
        }
    }

}
