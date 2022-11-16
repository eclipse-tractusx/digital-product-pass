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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class dateTimeTools {

    /**
     * Static tools to get current dateTime used for logging.
     *
     */

    public static String getDateTimeFormatted(String pattern){
        String defaultPattern = "dd/MM/yyyy HH:mm:ss";
        if(pattern == null){
            pattern = defaultPattern;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

}
