/**********************************************************
 *
 * Catena-X - Material Passport Consumer Backend
 *
 * Copyright (c) 2022: CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2022 Contributors to the CatenaX (ng) GitHub Organisation.
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
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 **********************************************************/

package tools;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
public final class logTools {
    private logTools() {
        throw new IllegalStateException("Tool/Utility Class Illegal Initialization");
    }

    /**
     * Static Tools to print logs with format and current date.
     */
    public static final configTools configuration = new configTools();
    public static final Boolean asyncLog = (Boolean) configuration.getConfigurationParam("logTools.async", ".",  false);
    static Logger logger = LogManager.getLogger(logTools.class);
    private static final Level INFO = Level.forName("INFO", 400);
    private static final Level HTTP = Level.forName("HTTP", 420);
    private static final Level DEBUG = Level.forName("DEBUGGER", 450);
    private static final Level EXCEPTION = Level.forName( "EXCEPTION", 100);
    private static final Level WARNING = Level.forName("WARNING", 300);
    private static final Level ERROR = Level.forName("ERROR", 200);
    private static final Level FATAL = Level.forName("FATAL", 200);
    private static final Level TEST = Level.forName("TEST", 400);
    private static final Map<Level, Integer> LOGLEVELS = Map.of(
                    FATAL,1,
                    ERROR, 2,
                    EXCEPTION,3,
                    WARNING, 5,
                    HTTP, 6,
                    INFO, 7,
                    DEBUG, 8
            );



    private static boolean checkLogLevel(Level logLevel){
        Integer currentLevel = (Integer) configuration.getConfigurationParam("logTools.level", ".", null);
        Integer assignedLevel = LOGLEVELS.get(logLevel);
        return currentLevel >= assignedLevel;
    }
    public static void printTest(String strMessage){
        Level logLevel = TEST;
        Long pid = systemTools.getPid();
        String memoryUsage = systemTools.getUsedHeapMemory();
        String message = "|"+pid+"|"+ memoryUsage+"| [" + logLevel.name()+"] " + strMessage;
        threadTools.runThread(new LogPrinter(logLevel, message), "testLogThread");
    }
    public static void printMessage(String strMessage){
        Level logLevel = INFO;
        if(!logTools.checkLogLevel(logLevel)){
            return;
        }

        logTools.printLog(logLevel, strMessage);

    }
    public static void printHTTPMessage(String strMessage){
        Level logLevel = HTTP;
        if(!logTools.checkLogLevel(logLevel)){
            return;
        }
        logTools.printLog(logLevel, strMessage);
    }
    public static void printException(Exception e, String strMessage){
        Level logLevel = EXCEPTION;
        if(!logTools.checkLogLevel(logLevel)){
            return;
        }
        String message = " ["+e.getMessage()+"] "+strMessage;
        logTools.printLog(logLevel, message);
    }
    public static void printError(String strMessage){
        Level logLevel = ERROR;
        if(!logTools.checkLogLevel(logLevel)){
            return;
        }
        logTools.printLog(logLevel, strMessage);
    }
    public static void printWarning(String strMessage){
        Level logLevel = WARNING;
        if(!logTools.checkLogLevel(logLevel)){
            return;
        }
        logTools.printLog(logLevel, strMessage);
    }

    public static void printDebug(String strMessage) {
        Level logLevel = DEBUG;
        if(!logTools.checkLogLevel(logLevel)){
            return;
        }
        logTools.printLog(logLevel, strMessage);
    }

    public static void printLog(Level logLevel, String strMessage){
        //String date = dateTimeTools.getDateTimeFormatted(null);
        Long pid = systemTools.getPid();
        String memoryUsage = systemTools.getUsedHeapMemory();
        String message = "|"+pid+"|"+ memoryUsage+"| [" + logLevel.name()+"] " + strMessage;
        if(asyncLog){
            threadTools.runThread(new LogPrinter(logLevel, message), "logThread");
        }else {
            logger.log(logLevel, message);
        }
    }
    public static void printFatal(String strMessage){
        Level logLevel = FATAL;
        if(!logTools.checkLogLevel(logLevel)){
            return;
        }
        logTools.printLog(logLevel, strMessage);
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
