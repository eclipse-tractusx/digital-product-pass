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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Map;

public final class logTools {

    /**
     * Static Tools to print logs with format and current date.
     * Not available at the moment to add files, usage of fileTools.
     */

    public static final configTools configuration = new configTools();
    static Logger logger = LoggerFactory.getLogger(logTools.class);
    private static final Marker INFO = MarkerFactory.getMarker("INFO");

    private static final Marker DEBUG = MarkerFactory.getMarker("DEBUG");
    private static final Marker EXCEPTION = MarkerFactory.getMarker("EXCEPTION");
    private static final Marker WARNING = MarkerFactory.getMarker("WARNING");
    private static final Marker ERROR = MarkerFactory.getMarker("ERROR");

    private static final Map<Marker, Integer> LOGLEVELS = Map.of(
                    ERROR, 1,
                    EXCEPTION,2,
                    WARNING, 3,
                    INFO, 4,
                    DEBUG, 5
            );


    private static boolean checkLogLevel(Marker logLevel){
        Integer currentLevel = (Integer) configuration.getConfigurationParam("logLevel");
        Integer assignedLevel = LOGLEVELS.get(logLevel);
        return currentLevel >= assignedLevel;
    }
    public static void printMessage(String strMessage){
        Marker logLevel = INFO;
        if(!logTools.checkLogLevel(logLevel)){
            return;
        }
        String message = dateTimeTools.getDateTimeFormatted(null) + " " + strMessage;
        logger.info(logLevel, message);
    }
    public static void printException(Exception e, String strMessage){
        Marker logLevel = EXCEPTION;
        if(!logTools.checkLogLevel(logLevel)){
            return;
        }
        String message = dateTimeTools.getDateTimeFormatted(null) + " ["+e.getMessage()+"] "+strMessage;
        logger.trace(logLevel, message, e);
    }

    public static void printError(String strMessage){
        Marker logLevel = ERROR;
        if(!logTools.checkLogLevel(logLevel)){
            return;
        }
        String message = dateTimeTools.getDateTimeFormatted(null) + " " + strMessage;
        logger.error(logLevel, message);
    }
    public static void printWarning(String strMessage){
        Marker logLevel = WARNING;
        if(!logTools.checkLogLevel(logLevel)){
            return;
        }
        String message= dateTimeTools.getDateTimeFormatted(null) + " " + strMessage;
        logger.warn(logLevel,message);
    }
}
