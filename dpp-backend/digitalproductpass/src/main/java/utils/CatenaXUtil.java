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

import org.eclipse.tractusx.digitalproductpass.models.dtregistry.DigitalTwin;
import org.springframework.core.env.Environment;
import utils.exceptions.UtilException;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class consists exclusively of methods to operate on Catena-X related classes and models.
 *
 * <p> The methods defined here are intended to check, build or get Catena-X related information.
 *
 */
public final class CatenaXUtil {

    public final static String bpnNumberPattern = "BPN[LSA][A-Z0-9]{12}";
    public final static String edcDataEndpoint = "/api/v1/dsp";

    public final static String catenaXPrefix = "CX";

    /**
     * Checks if the given String contains a BPN number pattern in it.
     * <p>
     * @param   str
     *          the {@code String} object to check.
     *
     * @return  true if the BPN number pattern is found, false otherwise.
     *
     */
    public static Boolean containsBPN(String str) {
        return str.matches(".*" + bpnNumberPattern + ".*");
    }

    /**
     * Checks if the given String contains the known EDC data endpoint suffix.
     * <p>
     * @param   str
     *          the {@code String} object to check.
     *
     * @return  true if the String contains the EDC data endpoint, false otherwise.
     *
     */
    public static Boolean containsEdcEndpoint(String str) {
        return str.matches(".*" + edcDataEndpoint);
    }


    /**
     * Builds an EDC data endpoint by attaching needed suffix at the given {@code String} endpoint.
     * <p>
     * @param   endpoint
     *          the {@code String} base endpoint.
     *
     * @return  a {@code String} endpoint with the given endpoint attached with the EDC data endpoint suffix.
     *
     */
    public static String buildDataEndpoint(String endpoint) {
        if(endpoint.contains(edcDataEndpoint))
            return endpoint;
        return endpoint + edcDataEndpoint;
    }

    /**
     * Gets an aspect name from the semantic id
     * <p>
     * @param   semanticId
     *          the {@code String} object to get BPN number from.
     *
     * @return  the {@code String} BPN number found in the given String or null if it doesn't exist.
     *
     */
    public static String getAspectNameFromSemanticId(String semanticId){
        try {
            return semanticId.split(String.format("\\%s","#"))[1];
        }catch(Exception e){
            throw new UtilException(CatenaXUtil.class, e, "[ERROR] It was not possible to ge the semantic aspect!");
        }
    }

    /**
     * Build the search id from a digital twin.
     * <p>
     * @param   digitalTwin
     *          the {@code DigitalTwin} digital twin to build the dpp search id
     *
     * @return  the {@code String} BPN number found in the given String or null if it doesn't exist.
     *
     */
    public static String buildDppSearchId(DigitalTwin digitalTwin, String schema){
        try {
            Map<String, String> mappedSpecificAssetIds = digitalTwin.mapSpecificAssetIds();
            // all the mapped values are in lowercase to avoid case sensitivity
            for (String id: mappedSpecificAssetIds.keySet()){
                String value = mappedSpecificAssetIds.get(id);
                schema = schema.replaceAll(String.format("\\%s","<"+id+">"), value);
            }
            return schema;
        }catch(Exception e){
            throw new UtilException(CatenaXUtil.class, e, "[ERROR] It was not possible to build the dpp search id");
        }
    }

    /**
     * Gets the BPN number from a given String, if exists.
     * <p>
     * @param   str
     *          the {@code String} object to get BPN number from.
     *
     * @return  the {@code String} BPN number found in the given String or null if it doesn't exist.
     *
     */
    public static String getBPN(String str) {
        Pattern pattern = Pattern.compile(bpnNumberPattern);
        Matcher matcher = pattern.matcher(str);
        if (!matcher.find()) {
            return null;

        }
        return matcher.group();
    }
    /**
     * Builds the Readiness Endpoint for a given path suffix for the endpoint.
     * <p>
     * @param   env
     *          the {@code Environment} object of the application environment variables.
     *
     * @return  the built {@code String} endpoint by concatenating the EDC endpoint, management endpoint and the given path suffix.
     *
     * @throws  UtilException
     *          if the EDC endpoint or management endpoint in the Environment variables are invalid.
     */
    public static String buildReadinessApi(Environment env) {
        try {
            String edcEndpoint = env.getProperty("configuration.edc.endpoint");
            String readinessEndpoint = env.getProperty("configuration.edc.readiness");
            if (edcEndpoint == null || readinessEndpoint == null) {
                throw new UtilException(CatenaXUtil.class, "[ERROR] EDC endpoint is null or Readiness endpoint is null");
            }
            return edcEndpoint + readinessEndpoint;
        } catch (Exception e) {
            throw new UtilException(CatenaXUtil.class, e, "[ERROR] Invalid edc endpoint or management endpoint");
        }
    }
    /**
     * Builds the Management Endpoint for a given path suffix for the endpoint.
     * <p>
     * @param   env
     *          the {@code Environment} object of the application environment variables.
     * @param   path
     *          the {@code String} path suffix for the intended endpoint (e.g: transferPath, negotiationPath, etc.).
     *
     * @return  the built {@code String} endpoint by concatenating the EDC endpoint, management endpoint and the given path suffix.
     *
     * @throws  UtilException
     *          if the EDC endpoint or management endpoint in the Environment variables are invalid.
     */
    public static String buildManagementEndpoint(Environment env, String path) {
        try {
            String edcEndpoint = env.getProperty("configuration.edc.endpoint");
            String managementEndpoint = env.getProperty("configuration.edc.management");
            if (edcEndpoint == null || managementEndpoint == null) {
                throw new UtilException(CatenaXUtil.class, "[ERROR] EDC endpoint is null or Management endpoint is null");
            }
            return edcEndpoint + managementEndpoint + path;
        } catch (Exception e) {
            throw new UtilException(CatenaXUtil.class, e, "[ERROR] Invalid edc endpoint or management endpoint");
        }
    }

    /**
     * Builds the full Endpoint for a given partial endpoint.
     * <p>
     * @param   endpoint
     *          the {@code String} partial endpoint.
     *
     * @return  the built {@code String} endpoint by cleaning the given endpoint and adding the EDC data endpoint and BPN number (if applied).
     *
     * @throws  UtilException
     *          if the given endpoint is an invalid one.
     */
    public static String buildEndpoint(String endpoint) {
        try {
            if (CatenaXUtil.containsEdcEndpoint(endpoint)) {
                return endpoint;
            }
            String cleanUrl = HttpUtil.cleanUrl(endpoint);
            // Build Url
            if (CatenaXUtil.containsBPN(endpoint)) {
                String BPN = CatenaXUtil.getBPN(endpoint);
                return String.format("%s/" + BPN + edcDataEndpoint, cleanUrl);
            } else {
                return String.format("%s" + edcDataEndpoint, cleanUrl);
            }
        } catch (Exception e) {
            throw new UtilException(CatenaXUtil.class, e, "[ERROR] Invalid url [" + endpoint + "] given!");
        }

    }

}
