/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2023 Contributors to the CatenaX (ng) GitHub Organisation.
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

import org.eclipse.tractusx.productpass.models.http.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import utils.exceptions.UtilException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class HttpUtil {

    private HttpUtil() {
        throw new IllegalStateException("Tool/Utility Class Illegal Initialization");
    }
    private static final String SUCCESS_TEXT = "Success";
    private static final ConfigUtil configurations = new ConfigUtil();

    private static final String GET_ERROR_MESSAGE = "It was not possible to do GET request to ";
    private static final String POST_ERROR_MESSAGE = "It was not possible to do POST request to ";


    public static Object getSessionValue(HttpServletRequest httpRequest, String key) {
        return httpRequest.getSession().getAttribute(key);
    }

    public static void setSessionValue(HttpServletRequest httpRequest, String key, Object value) {
        httpRequest.getSession().setAttribute(key, value);
    }

    public static Boolean isInSession(HttpServletRequest httpRequest, String key) {
        try {
            Object value = httpRequest.getSession().getAttribute(key);
            if (value != null) {
                return true;
            }
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, "Something went wrong and session key [" + key + "] was not found! ");
        }
        return false;
    }


    /**************************************************
     * HTTP Related Methods
     * @param httpRequest + Extra details
     **************************************************/

    public static String getHttpInfo(HttpServletRequest httpRequest, Integer status) {
        return "[" + httpRequest.getProtocol() + " " + httpRequest.getMethod() + "] " + status + ": " + httpRequest.getRequestURI();
    }

    public static String getParamOrDefault(HttpServletRequest httpRequest, String param, String defaultPattern) {
        String requestParam = httpRequest.getParameter(param);
        if (requestParam == null) {
            return defaultPattern;
        }
        return requestParam;
    }

    public static String getAuthorizationToken(HttpServletRequest httpRequest){
        final String authorizationHeaderValue = httpRequest.getHeader("Authorization");
        String token = null;
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
            token = authorizationHeaderValue.substring(7, authorizationHeaderValue.length());
        }
        if(token == null || token.isEmpty() || token.isBlank()){
            return null;
        }
        return token;
    }
    public static String buildUrl(String url, Map<String, ?> params, Boolean encode){
        StringBuilder finalUrl = new StringBuilder(url);
        for(Map.Entry<String, ?> entry : params.entrySet()){

            String value = String.valueOf(entry.getValue());
            if(encode) {
                value = URLEncoder.encode(value, StandardCharsets.UTF_8);
            }
            if(finalUrl.toString().contains("?")){
                finalUrl.append("&").append(entry.getKey()).append("=").append(value);
            }
            finalUrl.append("?").append(entry.getKey()).append("=").append(value);
        }
        return finalUrl.toString();
    }
    public static String mapToParams(Map<String, ?> params, Boolean encode){
        StringBuilder finalUrl = new StringBuilder();
        for(Map.Entry<String, ?> entry : params.entrySet()){

            String value = String.valueOf(entry.getValue());
            if(encode) {
                value = URLEncoder.encode(value, StandardCharsets.UTF_8);
            }
            finalUrl.append("&").append(entry.getKey()).append("=").append(value);
        }
        return finalUrl.toString();
    }

    public static Map<String, ?> encodeParams(Map<String, ?> params){
        Map<String, Object> encodedParams = new HashMap<>();
        for(Map.Entry<String, ?> entry : params.entrySet()){
            String value = String.valueOf(entry.getValue());
            value = URLEncoder.encode(value, StandardCharsets.UTF_8);
            encodedParams.put(entry.getKey(),value);
        }
        return encodedParams;
    }

    /**************************************************
     * Response Methods *******************************
     **************************************************/


    public static Response buildResponse(Response response, HttpServletResponse servletResponse){
        servletResponse.setStatus(response.getStatus());
        servletResponse.setHeader("Access-Control-Allow-Origin", "*");
        servletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST");
        return response;
    }
    public static Response getResponse() {
        return new Response(
                null,
                200
        );
    }

    public static Response getResponse(String message) {
        return new Response(
                message,
                200
        );
    }

    public static Response getResponse(String message, Object data) {
        return new Response(
                message,
                200,
                data
        );
    }
    public static Response getNotAuthorizedResponse() {
        return new Response(
                "Not Authorized",
                401
        );
    }
    public static void redirect(HttpServletResponse httpResponse, String url) {
        try {
            httpResponse.sendRedirect(url);
        } catch (IOException e) {
            throw new UtilException(HttpUtil.class, e, "It was not posible to redirect to [" + url + "]");
        }
    }

    public static URI buildUri(String url, Map<String, ?> params, Boolean encoded){
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        for(Map.Entry<String, ?> entry : params.entrySet()){
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        return builder.build(encoded).toUri();
    }

    /**************************************************
     * Generic Request Methods ************************
     **************************************************/
    public static ResponseEntity<?> doRequest(String url, Class<?> responseType, HttpMethod method, HttpEntity payload, Map<String, ?> params, Boolean retry, Boolean encode) {
        RestTemplate restTemplate = new RestTemplate();
        URI finalUri = HttpUtil.buildUri(url, params, encode);
        LogUtil.printDebug("["+ HttpUtil.class+"]: Calling URL->["+finalUri+"] with method->["+method.name()+"]");
        ResponseEntity<?> response = restTemplate.exchange(finalUri, method, payload, responseType);
        if (!retry || response != null) {
            LogUtil.printDebug("["+ HttpUtil.class+"]: Success! Response for URL->["+finalUri+"] received!");
            return response;
        }
        int i = 0;
        Integer maxRetries = (Integer) configurations.getConfigurationParam("maxRetries");
        if (maxRetries == null) {
            throw new UtilException(HttpUtil.class, "It was not possible to request to " + url+ " max retries not defined");
        }

        while (response == null && i < maxRetries) {
            response = restTemplate.exchange(finalUri, method, payload, responseType);
            LogUtil.printDebug("[" + i + "] Retrying request to " + url);
            i++;
        }

        if (response == null) {
            throw new UtilException(HttpUtil.class, "It was not possible execute request to " + url);
        }
        return response;
    }

    /// ============================================================
    /// REQUEST Methods --------------------------------------------
    /// ============================================================



    /*
     * GET With PARAMS + HEADERS
     */
    public static ResponseEntity<?> doGet(String url,  Class<?> responseType, HttpHeaders headers, Map<String, ?> params, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            return HttpUtil.doRequest(url, responseType, HttpMethod.GET, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, GET_ERROR_MESSAGE + url);
        }
    }

    /*
     * GET With HEADERS
     */
    public static ResponseEntity<?> doGet(String url,  Class<?> responseType, HttpHeaders headers, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            return HttpUtil.doRequest(url, responseType, HttpMethod.GET, requestEntity, HttpUtil.getParams(), retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, GET_ERROR_MESSAGE + url);
        }
    }

    /*
     * GET With PARAMS + HEADERS + BODY
     */
    public static ResponseEntity<?> doGet(String url,  Class<?> responseType, HttpHeaders headers, Map<String, ?> params, Object body, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Object> requestEntity = new HttpEntity<>(body, headers);
            return HttpUtil.doRequest(url, responseType, HttpMethod.GET, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, GET_ERROR_MESSAGE + url);
        }
    }

    /*
     * GET With PARAMS
     */
    public static ResponseEntity<?> doGet(String url,  Class<?> responseType, Map<String, ?> params, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(HttpUtil.getHeaders());
            return HttpUtil.doRequest(url, responseType, HttpMethod.GET, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, GET_ERROR_MESSAGE + url);
        }
    }

    /*
     * GET With BODY + PARAMS
     */
    public static ResponseEntity<?> doGet(String url,  Class<?> responseType, Map<String, ?> params,Object body, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Object> requestEntity = new HttpEntity<>(body, HttpUtil.getHeaders());
            return HttpUtil.doRequest(url, responseType, HttpMethod.GET, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, GET_ERROR_MESSAGE + url);
        }
    }
    /*
     * GET Without anything
     */
    public static ResponseEntity<?> doGet(String url,  Class<?> responseType, Boolean retry) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(HttpUtil.getHeaders());
            return HttpUtil.doRequest(url, responseType, HttpMethod.GET, requestEntity, HttpUtil.getParams(), retry, false);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, GET_ERROR_MESSAGE + url);
        }
    }


    /*
     * POST With PARAMS + HEADERS
     */
    public static ResponseEntity<?> doPost(String url,  Class<?> responseType, HttpHeaders headers, Map<String, ?> params, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            return HttpUtil.doRequest(url, responseType, HttpMethod.POST, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, POST_ERROR_MESSAGE + url);
        }
    }

    /*
     * POST With PARAMS + HEADERS + BODY
     */
    public static ResponseEntity<?> doPost(String url,  Class<?> responseType, HttpHeaders headers, Map<String, ?> params, Object body, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Object> requestEntity = new HttpEntity<>(body, headers);
            return HttpUtil.doRequest(url, responseType, HttpMethod.POST, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, POST_ERROR_MESSAGE + url);
        }
    }

    /*
     * POST With PARAMS
     */
    public static ResponseEntity<?> doPost(String url,  Class<?> responseType, Map<String, ?> params, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(HttpUtil.getHeaders());
            return HttpUtil.doRequest(url, responseType, HttpMethod.POST, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, POST_ERROR_MESSAGE + url);
        }
    }

    /*
    * POST With BODY + PARAMS
    */
    public static ResponseEntity<?> doPost(String url,  Class<?> responseType, Map<String, ?> params,Object body, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Object> requestEntity = new HttpEntity<>(body, HttpUtil.getHeaders());
            return HttpUtil.doRequest(url, responseType, HttpMethod.POST, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, POST_ERROR_MESSAGE + url);
        }
    }
    /*
     * POST With HEADERS
     */
    public static ResponseEntity<?> doPost(String url,  Class<?> responseType, HttpHeaders headers, Boolean retry) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            return HttpUtil.doRequest(url, responseType, HttpMethod.POST, requestEntity, HttpUtil.getParams(), retry, false);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, POST_ERROR_MESSAGE + url);
        }
    }
    /*
     * POST Without anything
     */
    public static ResponseEntity<?> doPost(String url,  Class<?> responseType, Boolean retry) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(HttpUtil.getHeaders());
            return HttpUtil.doRequest(url, responseType, HttpMethod.POST, requestEntity, HttpUtil.getParams(), retry, false);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, POST_ERROR_MESSAGE + url);
        }
    }
    public static HttpHeaders getHeaders() {
        return new HttpHeaders();
    }

    public static HttpHeaders getHeadersWithToken(String accessToken) {
        HttpHeaders headers =  new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        return headers;
    }
    public static Map<String, Object> getParams() {
        return new HashMap<String, Object>();
    }
}
