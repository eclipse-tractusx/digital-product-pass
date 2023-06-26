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
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import utils.exceptions.UtilException;

import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CatenaXUtil {

    private final static String bpnNumberPattern = "BPN[LSA][A-Z0-9]{12}";
    private final static String edcDataEndpoint = "/api/v1/dsp";

    public static Boolean containsBPN(String str){
        return str.matches(".*"+bpnNumberPattern+".*");
    }
    public static Boolean containsEdcEndpoint(String str){
        return str.matches(".*"+edcDataEndpoint);
    }
    public static String getBPN(String str){
        Pattern pattern = Pattern.compile(bpnNumberPattern);
        Matcher matcher = pattern.matcher(str);
        if(!matcher.find())
        {
            return null;

        }
        return matcher.group();
    }
    public static String buildManagementEndpoint(Environment env, String path){
        try {
        String edcEndpoint = env.getProperty("configuration.edc.endpoint");
        String managementEndpoint = env.getProperty("configuration.edc.management");
        if(edcEndpoint == null || managementEndpoint == null){
            throw new UtilException(CatenaXUtil.class,"[ERROR] EDC endpoint is null or Management endpoint is null");
        }
        return edcEndpoint + managementEndpoint + path;
        }catch (Exception e){
            throw new UtilException(CatenaXUtil.class,e, "[ERROR] Invalid edc endpoint or management endpoint");
        }
    }

    public static String buildEndpoint(String endpoint){
        try {
            if (CatenaXUtil.containsEdcEndpoint(endpoint)) {
                return endpoint;
            }
            String cleanUrl = HttpUtil.cleanUrl(endpoint);
            // Build Url
            if (CatenaXUtil.containsBPN(endpoint)){
                String BPN = CatenaXUtil.getBPN(endpoint);
                return String.format("%s/"+BPN+edcDataEndpoint,cleanUrl);
            }else{
                return String.format("%s"+edcDataEndpoint,cleanUrl);
            }
        }catch (Exception e){
            throw new UtilException(CatenaXUtil.class,e,"[ERROR] Invalid url ["+endpoint+"] given!");
        }

    }

}
