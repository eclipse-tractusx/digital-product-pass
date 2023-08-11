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
import org.checkerframework.checker.units.qual.C;
import org.eclipse.tractusx.productpass.models.edc.Jwt;
import org.eclipse.tractusx.productpass.models.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import utils.exceptions.UtilException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HttpUtil {

    private Environment env;

    @Autowired
    JsonUtil jsonUtil;
    public Integer maxRetries;

    @Autowired
    public HttpUtil(Environment env) {
        this.maxRetries = env.getProperty("maxRetries", Integer.class, 5);
    }

    private  final String GET_ERROR_MESSAGE = "It was not possible to do GET request to ";
    private  final String POST_ERROR_MESSAGE = "It was not possible to do POST request to ";


    public Object getSessionValue(HttpServletRequest httpRequest, String key) {
        return httpRequest.getSession().getAttribute(key);
    }

    public  void setSessionValue(HttpServletRequest httpRequest, String key, Object value) {
        httpRequest.getSession().setAttribute(key, value);
    }
    public String getSessionId(HttpServletRequest httpRequest) {
        return httpRequest.getSession().getId();
    }


    public  Boolean isInSession(HttpServletRequest httpRequest, String key) {
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

    public  String getHttpInfo(HttpServletRequest httpRequest, Integer status) {
        return "[" + httpRequest.getProtocol() + " " + httpRequest.getMethod() + "] " + status + ": " + httpRequest.getRequestURI();
    }
    public  String getResponseHttpInfo(Response response) {
        return "[HTTP Response] " + response.status + " " + response.statusText+ ": " + response.getMessage();
    }

    public  String getParamOrDefault(HttpServletRequest httpRequest, String param, String defaultPattern) {
        String requestParam = httpRequest.getParameter(param);
        if (requestParam == null) {
            return defaultPattern;
        }
        return requestParam;
    }

    public  String getAuthorizationToken(HttpServletRequest httpRequest){
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
    public  String buildUrl(String url, Map<String, ?> params, Boolean encode){
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
    public  String mapToParams(Map<String, ?> params, Boolean encode){
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

    public  Map<String, ?> encodeParams(Map<String, ?> params){
        Map<String, Object> encodedParams = new HashMap<>();
        for(Map.Entry<String, ?> entry : params.entrySet()){
            String value = String.valueOf(entry.getValue());
            value = URLEncoder.encode(value, StandardCharsets.UTF_8);
            encodedParams.put(entry.getKey(),value);
        }
        return encodedParams;
    }

    public  String getCurrentHost(HttpServletRequest httpRequest){
        try {
            return ServletUriComponentsBuilder.fromRequestUri(httpRequest)
                    .replacePath(null)
                    .build()
                    .toUriString();
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, "It was not able to return current host url");
        }
    }

    public Jwt parseToken(String token){
        try {
            String[] chunks = token.split("\\.");
            Jwt jwt = new Jwt();

            String header = CrypUtil.fromBase64Url(chunks[0]);
            String payload = CrypUtil.fromBase64Url(chunks[1]);
            jwt.setHeader((Map<String, Object>) jsonUtil.parseJson(header));
            jwt.setPayload((Map<String, Object>) jsonUtil.parseJson(payload));
            return jwt;
        }catch(Exception e){
            throw new UtilException(HttpUtil.class, e, "It was not possible to parse JWT Token");
        }

    }
    public  String getCurrentUrl(HttpServletRequest httpRequest){
        try {
            return httpRequest.getRequestURL().toString();
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, "It was not posible to get current url");
        }
    }

    public  String getHost(String url) throws MalformedURLException {
        return new URL(url).getHost();
    }
    public  String getProtocol(String url) throws MalformedURLException {
        return new URL(url).getProtocol();
    }
    public  Integer getPort(String url) throws MalformedURLException {
        int port = new URL(url).getPort();
        return (port!=-1)?port:null;
    }
    public  String getAuthority(String url) throws MalformedURLException {
        return new URL(url).getAuthority();
    }

    public  HashMap<String, Object> splitUrl(String strUrl) throws MalformedURLException{
        HashMap<String, Object> retObj = new HashMap<>();
        URL url = new URL(strUrl);
        retObj.put("protocol", url.getProtocol());
        retObj.put("host", url.getHost());
        int port = url.getPort();
        retObj.put("port",(port!=-1)?port:null);
        retObj.put("authority", url.getAuthority());
        return retObj;
    }
    public static String cleanUrl(String strUrl) throws MalformedURLException{
        URL url = new URL(strUrl);
        String protocol = url.getProtocol();
        String authority = url.getAuthority();
        return String.format("%s://%s", protocol, authority);
    }


    /**************************************************
     * Response Methods *******************************
     **************************************************/


    public  Response buildResponse(Response response, HttpServletResponse servletResponse){
        servletResponse.setStatus(response.getStatus());
        servletResponse.setHeader("Access-Control-Allow-Origin", "*");
        servletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        if(response.getStatus() != 200){
            LogUtil.printHTTPErrorMessage(this.getResponseHttpInfo(response));
        }
        return response;
    }
    public  Response getResponse() {
        return new Response(
                null,
                200
        );
    }

    public  Response getResponse(String message) {
        return new Response(
                message,
                200
        );
    }

    public  Response getResponse(String message, Object data) {
        return new Response(
                message,
                200,
                data
        );
    }
    public  Response getBadRequest() {
        return new Response(
                null,
                400,
                "Bad Request"
        );
    }
    public  Response getBadRequest(String message) {
        return new Response(
                message,
                400,
                "Bad Request"
        );
    }
    public  Response getNotFound() {
        return new Response(
                null,
                404,
                "Not Found"
        );
    }

    public  Response getNotFound(String message) {
        return new Response(
                message,
                404,
                "Not Found"
        );
    }


    public  Response getInternalError() {
        return new Response(
                null,
                500,
                "Internal Server Error"
        );
    }

    public  Response getInternalError(String message) {
        return new Response(
                message,
                500,
                "Internal Server Error"
        );
    }
    public Response getForbiddenResponse(String message) {
        return new Response(
                message,
                403,
                "Forbidden"
        );
    }
    public Response getForbiddenResponse() {
        return new Response(
                null,
                403,
                "Forbidden"
        );
    }
    public  Response getNotAuthorizedResponse() {
        return new Response(
                null,
                401,
                "Not Authorized"
        );
    }
    public  void redirect(HttpServletResponse httpResponse, String url) {
        try {
            httpResponse.sendRedirect(url);
        } catch (IOException e) {
            throw new UtilException(HttpUtil.class, e, "It was not posible to redirect to [" + url + "]");
        }
    }

    public  URI buildUri(String url, Map<String, ?> params, Boolean encoded){
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        for(Map.Entry<String, ?> entry : params.entrySet()){
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        return builder.build(encoded).toUri();
    }

    /**************************************************
     * Generic Request Methods ************************
     **************************************************/
    public ResponseEntity<?> doRequest(String url, Class<?> responseType, HttpMethod method, HttpEntity payload, Map<String, ?> params, Boolean retry, Boolean encode) {
        RestTemplate restTemplate = new RestTemplate();
        URI finalUri = this.buildUri(url, params, encode);
        LogUtil.printDebug("["+ HttpUtil.class+"]: Calling URL->["+finalUri+"] with method->["+method.name()+"]");
        ResponseEntity<?> response = restTemplate.exchange(finalUri, method, payload, responseType);
        if (!retry || response != null) {
            LogUtil.printDebug("["+ HttpUtil.class+"]: Success! Response for URL->["+finalUri+"] received!");
            return response;
        }
        int i = 0;
        Integer maxRetries = this.maxRetries;
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
    public ResponseEntity<?> doGet(String url,  Class<?> responseType, HttpHeaders headers, Map<String, ?> params, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            return this.doRequest(url, responseType, HttpMethod.GET, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, GET_ERROR_MESSAGE + url);
        }
    }

    /*
     * GET With HEADERS
     */
    public ResponseEntity<?> doGet(String url,  Class<?> responseType, HttpHeaders headers, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            return this.doRequest(url, responseType, HttpMethod.GET, requestEntity, this.getParams(), retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, GET_ERROR_MESSAGE + url);
        }
    }

    /*
     * GET With PARAMS + HEADERS + BODY
     */
    public  ResponseEntity<?> doGet(String url,  Class<?> responseType, HttpHeaders headers, Map<String, ?> params, Object body, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Object> requestEntity = new HttpEntity<>(body, headers);
            return this.doRequest(url, responseType, HttpMethod.GET, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, GET_ERROR_MESSAGE + url);
        }
    }

    /*
     * GET With PARAMS
     */
    public  ResponseEntity<?> doGet(String url,  Class<?> responseType, Map<String, ?> params, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(this.getHeaders());
            return this.doRequest(url, responseType, HttpMethod.GET, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, GET_ERROR_MESSAGE + url);
        }
    }

    /*
     * GET With BODY + PARAMS
     */
    public  ResponseEntity<?> doGet(String url,  Class<?> responseType, Map<String, ?> params,Object body, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Object> requestEntity = new HttpEntity<>(body, this.getHeaders());
            return this.doRequest(url, responseType, HttpMethod.GET, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, GET_ERROR_MESSAGE + url);
        }
    }
    /*
     * GET Without anything
     */
    public  ResponseEntity<?> doGet(String url,  Class<?> responseType, Boolean retry) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(this.getHeaders());
            return this.doRequest(url, responseType, HttpMethod.GET, requestEntity, this.getParams(), retry, false);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, GET_ERROR_MESSAGE + url);
        }
    }


    /*
     * POST With PARAMS + HEADERS
     */
    public  ResponseEntity<?> doPost(String url,  Class<?> responseType, HttpHeaders headers, Map<String, ?> params, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            return this.doRequest(url, responseType, HttpMethod.POST, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, POST_ERROR_MESSAGE + url);
        }
    }

    /*
     * POST With PARAMS + HEADERS + BODY
     */
    public  ResponseEntity<?> doPost(String url,  Class<?> responseType, HttpHeaders headers, Map<String, ?> params, Object body, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Object> requestEntity = new HttpEntity<>(body, headers);
            return this.doRequest(url, responseType, HttpMethod.POST, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, POST_ERROR_MESSAGE + url);
        }
    }

    /*
     * POST With PARAMS
     */
    public  ResponseEntity<?> doPost(String url,  Class<?> responseType, Map<String, ?> params, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(this.getHeaders());
            return this.doRequest(url, responseType, HttpMethod.POST, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, POST_ERROR_MESSAGE + url);
        }
    }

    /*
    * POST With BODY + PARAMS
    */
    public  ResponseEntity<?> doPost(String url,  Class<?> responseType, Map<String, ?> params,Object body, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Object> requestEntity = new HttpEntity<>(body, this.getHeaders());
            return this.doRequest(url, responseType, HttpMethod.POST, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, POST_ERROR_MESSAGE + url);
        }
    }
    /*
     * POST With HEADERS
     */
    public  ResponseEntity<?> doPost(String url,  Class<?> responseType, HttpHeaders headers, Boolean retry) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            return this.doRequest(url, responseType, HttpMethod.POST, requestEntity, this.getParams(), retry, false);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, POST_ERROR_MESSAGE + url);
        }
    }
    /*
     * POST Without anything
     */
    public  ResponseEntity<?> doPost(String url,  Class<?> responseType, Boolean retry) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(this.getHeaders());
            return this.doRequest(url, responseType, HttpMethod.POST, requestEntity, this.getParams(), retry, false);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, POST_ERROR_MESSAGE + url);
        }
    }
    public  HttpHeaders getHeaders() {
        return new HttpHeaders();
    }

    public  HttpHeaders getHeadersWithToken(String accessToken) {
        HttpHeaders headers =  new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        return headers;
    }
    public  Map<String, Object> getParams() {
        return new HashMap<String, Object>();
    }
}
