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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.eclipse.tractusx.digitalproductpass.models.edc.Jwt;
import org.eclipse.tractusx.digitalproductpass.models.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * This class consists exclusively of methods to operate on the HTTP protocol.
 *
 * <p> The methods defined here are intended to get, set, parse and manipulate HTTP requests and responses.
 *
 */
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


    /**
     * Gets a session value of an HTTP Request.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     * @param   key
     *          the session's key to lookup for.
     *
     * @return  a {@code Object} object with the value of the given key.
     *
     */
    public Object getSessionValue(HttpServletRequest httpRequest, String key) {
        return httpRequest.getSession().getAttribute(key);
    }

    /**
     * Sets a session value on an HTTP Request.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     * @param   key
     *          the session's key to lookup for.
     * @param   value
     *          the object value to update.
     *
     * @return  a {@code Object} object with the value of the given key.
     *
     */
    public  void setSessionValue(HttpServletRequest httpRequest, String key, Object value) {
        httpRequest.getSession().setAttribute(key, value);
    }

    /**
     * Gets the session Id of an HTTP Request.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     *
     * @return  a {@code String} object with the value of the session Id.
     *
     */
    public String getSessionId(HttpServletRequest httpRequest) {
        return httpRequest.getSession().getId();
    }

    /**
     * Checks if a session key exists on the HTTP Request.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     * @param   key
     *          the session's key to lookup for.
     *
     * @return  true if the key exists, false otherwise.
     *
     */
    @SuppressWarnings("Unused")
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

    /**
     * Gets HTTP information like the protocol, method and the request URI used in the given HTTP Request and concatenates with the given HTTP response status
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     * @param   status
     *          the HTTP response status.
     *
     * @return  a {@code String} object with the HTTP information from the given HTTP Request concatenated with the given HTTP response status.
     *
     */
    public  String getHttpInfo(HttpServletRequest httpRequest, Integer status) {
        return "[" + httpRequest.getProtocol() + " " + httpRequest.getMethod() + "] " + status + ": " + httpRequest.getRequestURI();
    }

    /**
     * Gets HTTP information like the status, statusText and the message related with the given HTTP response.
     * <p>
     * @param   response
     *          the HTTP response.
     *
     * @return  a {@code String} object with the HTTP information from the given HTTP Request concatenated with the given HTTP response status.
     *
     */
    public  String getResponseHttpInfo(Response response) {
        return "[HTTP Response] " + response.status + " " + response.statusText+ ": " + response.getMessage();
    }

    /**
     * Gets the parameter from the HTTP request or a defaultValue if otherwise.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     * @param   param
     *          parameter to look up for in the given HTTP request.
     * @param   defaultPattern
     *          default value to return if the parameter doesn't exist.
     *
     * @return  a {@code String} with the value of the parameter, if found or the defaultPattern otherwise.
     *
     */
    public  String getParamOrDefault(HttpServletRequest httpRequest, String param, String defaultPattern) {
        String requestParam = httpRequest.getParameter(param);
        if (requestParam == null) {
            return defaultPattern;
        }
        return requestParam;
    }

    /**
     * Gets the authorization token for the given HTTP request.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     *
     * @return  a {@code String} with the value of the token to be able to communicate.
     *
     */
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
    /**
     * Builds the URL with the key/value pair within the given parameters map with or without enconding.
     * <p>
     * @param   url
     *          the base URL.
     * @param   params
     *          the Map with key/value pair from each parameter.
     * @param   encode
     *          if true will encode the value of each parameter.
     *
     * @return  a {@code String} with the build URL.
     *
     
    @SuppressWarnings("Unused")
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
    */
    /**
     * Parses the Map of parameters to a String as URL parameters structure.
     * <p>
     * @param   params
     *          the Map with key/value pair from each parameter.
     * @param   encode
     *          if true will encode the value of each parameter.
     *
     * @return  a {@code String} with all parameters as URL parameters structure.
     *
     */
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

    /**
     * Encodes the value of each parameter in the given Map.
     * <p>
     * @param   params
     *          the Map with key/value pair from each parameter.
     *
     * @return  a {@code Map} with the value of each parameter encoded.
     *
     */
    @SuppressWarnings("Unused")
    public  Map<String, ?> encodeParams(Map<String, ?> params){
        Map<String, Object> encodedParams = new HashMap<>();
        for(Map.Entry<String, ?> entry : params.entrySet()){
            String value = String.valueOf(entry.getValue());
            value = URLEncoder.encode(value, StandardCharsets.UTF_8);
            encodedParams.put(entry.getKey(),value);
        }
        return encodedParams;
    }

    /**
     * Gets the current Host for the given HTTP request.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     *
     * @return  a {@code String} with the URL of the current Host.
     *
     * @throws  UtilException
     *          if unable to get the host URL.
     */
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

    /**
     * Parses the given token as a {@code Jwt} object.
     * <p>
     * @param   token
     *          the {@code String} token.
     *
     * @return  a {@code Jwt} object parsed with the values from the token.
     *
     * @throws  UtilException
     *          if unable to parse the token.
     */
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

    /**
     * Gets the URL from the given HTTP request.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     *
     * @return  a {@code String} with the request's URL.
     *
     * @throws  UtilException
     *          if unable to get the URL.
     */
    @SuppressWarnings("Unused")
    public  String getCurrentUrl(HttpServletRequest httpRequest){
        try {
            return httpRequest.getRequestURL().toString();
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, "It was not posible to get current url");
        }
    }

    /**
     * Gets the Host from the given URL.
     * <p>
     * @param   url
     *          the {@code String} URL.
     *
     * @return  a {@code String} with the Host name of the URL.
     *
     */
    @SuppressWarnings("Unused")
    public  String getHost(String url) throws MalformedURLException {
        return new URL(url).getHost();
    }

    /**
     * Gets the Protocol from the given URL.
     * <p>
     * @param   url
     *          the {@code String} URL.
     *
     * @return  a {@code String} with the Protocol name of the URL.
     *
     */
    @SuppressWarnings("Unused")
    public  String getProtocol(String url) throws MalformedURLException {
        return new URL(url).getProtocol();
    }

    /**
     * Gets the Port from the given URL.
     * <p>
     * @param   url
     *          the {@code String} URL.
     *
     * @return  a {@code Integer} with the Port number of the URL.
     *
     */
    @SuppressWarnings("Unused")
    public  Integer getPort(String url) throws MalformedURLException {
        int port = new URL(url).getPort();
        return (port!=-1)?port:null;
    }

    /**
     * Gets the Authority part from the given URL.
     * <p>
     * @param   url
     *          the {@code String} URL.
     *
     * @return  a {@code String} with the Authority part of the URL.
     *
     */
    @SuppressWarnings("Unused")
    public  String getAuthority(String url) throws MalformedURLException {
        return new URL(url).getAuthority();
    }

    /**
     * Parses the given URL to a key/value pair Map with the protocol, host, port and authorirty part information.
     * <p>
     * @param   strUrl
     *          the {@code String} URL.
     *
     * @return  a {@code Map} object with the main URL parameters.
     *
     */
    @SuppressWarnings("Unused")
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

    /**
     * Parses the given {@code String} URL into a String format with protocol and authority information
     * <p>
     * @param   strUrl
     *          the {@code String} URL.
     *
     * @return  a {@code String} object with format result.
     *
     */
    public static String cleanUrl(String strUrl) throws MalformedURLException{
        URL url = new URL(strUrl);
        String protocol = url.getProtocol();
        String authority = url.getAuthority();
        return String.format("%s://%s", protocol, authority);
    }


    /**************************************************
     * Response Methods *******************************
     **************************************************/


    /**
     * Builds an {@code HttpServletResponse} object with the given response message.
     * <p>
     * @param   response
     *          the response message.
     * @param   servletResponse
     *          the HTTP response.
     *
     * @return  the {@code Response} given object after setting some parameters of HTTP response.
     *
     */
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

    @SuppressWarnings("Unused")
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
    @SuppressWarnings("Unused")
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

    @SuppressWarnings("Unused")
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

    /**
     * Builds an {@code URI} object with the given URL and parameters.
     * <p>
     * @param   url
     *          the base URL.
     * @param   params
     *          the Map with key/value pair from each parameter.
     * @param   encoded
     *          if true will encode the value of each parameter.
     *
     * @return  the built {@code URI} object set with the given URL and parameters with or without enconding.
     *
     */
    public  URI buildUri(String url, Map<String, ?> params, Boolean encoded){
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        for(Map.Entry<String, ?> entry : params.entrySet()){
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        return builder.build(encoded).toUri();
    }
    /**
     * Builds the URL with the key/value pair within the given parameters map with or without enconding.
     * <p>
     * @param   url
     *          the base URL.
     * @param   params
     *          the Map with key/value pair from each parameter.
     * @param   encoded
     *          if true will encode the value of each parameter.
     *
     * @return  a {@code String} with the build URL.
     */
    public String buildUrl(String url, Map<String, ?> params, Boolean encoded){
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        for(Map.Entry<String, ?> entry : params.entrySet()){
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        return builder.build(encoded).toUriString();
    }

    /**************************************************
     * Generic Request Methods ************************
     **************************************************/


    /**
     * Builds and make an HTTP request with the given parameters.
     * <p>
     * @param   url
     *          the base URL.
     * @param   responseType
     *          the class type of the response (e.g: a String, an Object, etc.)
     * @param   method
     *          the HTTP method (e.g: GET, POST, etc.)
     * @param   payload
     *          the payload for the request as an {@code HttpEntity}.
     * @param   params
     *          the Map with key/value pair from each parameter.
     * @param   retry
     *          if true it will retry to do the request a predefined couple of times.
     * @param   encode
     *          if true will encode the value of each parameter.
     *
     * @return  the {@code ResponseEntity} with the result of the request.
     *
     */
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

    /**
        ============================================================
        REQUEST Methods --------------------------------------------
        ============================================================
     **/

    /**
     * Builds and make an HTTP GET request with headers and parameters, without body.
     * <p>
     * @param   url
     *          the base URL.
     * @param   responseType
     *          the class type of the response (e.g: a String, an Object, etc.)
     * @param   headers
     *          the HTTP headers to configure.
     * @param   params
     *          the Map with key/value pair from each parameter.
     * @param   retry
     *          if true it will retry to do the request a predefined couple of times.
     * @param   encode
     *          if true will encode the value of each parameter.
     *
     * @return  the {@code ResponseEntity} with the result of the GET request.
     *
     */
    public ResponseEntity<?> doGet(String url,  Class<?> responseType, HttpHeaders headers, Map<String, ?> params, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            return this.doRequest(url, responseType, HttpMethod.GET, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, GET_ERROR_MESSAGE + url);
        }
    }

    /**
     * Builds and make an HTTP GET request with headers, without parameters and body.
     * <p>
     * @param   url
     *          the base URL.
     * @param   responseType
     *          the class type of the response (e.g: a String, an Object, etc.)
     * @param   headers
     *          the HTTP headers to configure.
     * @param   retry
     *          if true it will retry to do the request a predefined couple of times.
     * @param   encode
     *          if true will encode the value of each parameter.
     *
     * @return  the {@code ResponseEntity} with the result of the GET request.
     *
     */
    @SuppressWarnings("Unused")
    public ResponseEntity<?> doGet(String url,  Class<?> responseType, HttpHeaders headers, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            return this.doRequest(url, responseType, HttpMethod.GET, requestEntity, this.getParams(), retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, GET_ERROR_MESSAGE + url);
        }
    }


    /**
     * Builds and make an HTTP GET request with headers, parameters and body.
     * <p>
     * @param   url
     *          the base URL.
     * @param   responseType
     *          the class type of the response (e.g: a String, an Object, etc.)
     * @param   headers
     *          the HTTP headers to configure.
     * @param   params
     *          the Map with key/value pair from each parameter.
     * @param   body
     *          the {@code Object} object representing the body for the request.
     * @param   retry
     *          if true it will retry to do the request a predefined couple of times.
     * @param   encode
     *          if true will encode the value of each parameter.
     *
     * @return  the {@code ResponseEntity} with the result of the GET request.
     *
     */
    @SuppressWarnings("Unused")
    public  ResponseEntity<?> doGet(String url,  Class<?> responseType, HttpHeaders headers, Map<String, ?> params, Object body, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Object> requestEntity = new HttpEntity<>(body, headers);
            return this.doRequest(url, responseType, HttpMethod.GET, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, GET_ERROR_MESSAGE + url);
        }
    }

    /**
     * Builds and make an HTTP GET request with parameters, without headers and body.
     * <p>
     * @param   url
     *          the base URL.
     * @param   responseType
     *          the class type of the response (e.g: a String, an Object, etc.)
     * @param   params
     *          the Map with key/value pair from each parameter.
     * @param   retry
     *          if true it will retry to do the request a predefined couple of times.
     * @param   encode
     *          if true will encode the value of each parameter.
     *
     * @return  the {@code ResponseEntity} with the result of the GET request.
     *
     */
    @SuppressWarnings("Unused")
    public  ResponseEntity<?> doGet(String url,  Class<?> responseType, Map<String, ?> params, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(this.getHeaders());
            return this.doRequest(url, responseType, HttpMethod.GET, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, GET_ERROR_MESSAGE + url);
        }
    }

    /**
     * Builds and make an HTTP GET request with parameters and body, without headers.
     * <p>
     * @param   url
     *          the base URL.
     * @param   responseType
     *          the class type of the response (e.g: a String, an Object, etc.)
     * @param   params
     *          the Map with key/value pair from each parameter.
     * @param   body
     *          the {@code Object} object representing the body for the request.
     * @param   retry
     *          if true it will retry to do the request a predefined couple of times.
     * @param   encode
     *          if true will encode the value of each parameter.
     *
     * @return  the {@code ResponseEntity} with the result of the GET request.
     *
     */
    @SuppressWarnings("Unused")
    public  ResponseEntity<?> doGet(String url,  Class<?> responseType, Map<String, ?> params,Object body, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Object> requestEntity = new HttpEntity<>(body, this.getHeaders());
            return this.doRequest(url, responseType, HttpMethod.GET, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, GET_ERROR_MESSAGE + url);
        }
    }

    /**
     * Builds and make an empty HTTP GET request without headers, parameters and body.
     * <p>
     * @param   url
     *          the base URL.
     * @param   responseType
     *          the class type of the response (e.g: a String, an Object, etc.)
     * @param   retry
     *          if true it will retry to do the request a predefined couple of times.
     *
     * @return  the {@code ResponseEntity} with the result of the GET request.
     *
     */
    @SuppressWarnings("Unused")
    public  ResponseEntity<?> doGet(String url,  Class<?> responseType, Boolean retry) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(this.getHeaders());
            return this.doRequest(url, responseType, HttpMethod.GET, requestEntity, this.getParams(), retry, false);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, GET_ERROR_MESSAGE + url);
        }
    }


    /**
     * Builds and make an HTTP POST request with headers and parameters, without body.
     * <p>
     * @param   url
     *          the base URL.
     * @param   responseType
     *          the class type of the response (e.g: a String, an Object, etc.)
     * @param   headers
     *          the HTTP headers to configure.
     * @param   params
     *          the Map with key/value pair from each parameter.
     * @param   retry
     *          if true it will retry to do the request a predefined couple of times.
     * @param   encode
     *          if true will encode the value of each parameter.
     *
     * @return  the {@code ResponseEntity} with the result of the POST request.
     *
     */
    public  ResponseEntity<?> doPost(String url,  Class<?> responseType, HttpHeaders headers, Map<String, ?> params, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            return this.doRequest(url, responseType, HttpMethod.POST, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, POST_ERROR_MESSAGE + url);
        }
    }

    /**
     * Builds and make an HTTP POST request with headers, parameters and body.
     * <p>
     * @param   url
     *          the base URL.
     * @param   responseType
     *          the class type of the response (e.g: a String, an Object, etc.)
     * @param   headers
     *          the HTTP headers to configure.
     * @param   params
     *          the Map with key/value pair from each parameter.
     * @param   body
     *          the {@code Object} object representing the body for the request.
     * @param   retry
     *          if true it will retry to do the request a predefined couple of times.
     * @param   encode
     *          if true will encode the value of each parameter.
     *
     * @return  the {@code ResponseEntity} with the result of the POST request.
     *
     */
    public  ResponseEntity<?> doPost(String url,  Class<?> responseType, HttpHeaders headers, Map<String, ?> params, Object body, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Object> requestEntity = new HttpEntity<>(body, headers);
            return this.doRequest(url, responseType, HttpMethod.POST, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, POST_ERROR_MESSAGE + url);
        }
    }

    /**
     * Builds and make an HTTP POST request with headers, parameters, body and timeout
     * <p>
     * @param   url
     *          the base URL.
     * @param   responseType
     *          the class type of the response (e.g: a String, an Object, etc.)
     * @param   headers
     *          the HTTP headers to configure.
     * @param   params
     *          the Map with key/value pair from each parameter.
     * @param   body
     *          the {@code Object} object representing the body for the request.
     * @param   retry
     *          if true it will retry to do the request a predefined couple of times.
     * @param   encode
     *          if true will encode the value of each parameter.
     * @param   timeout
     *          if true will encode the value of each parameter.
     *
     * @return  the {@code ResponseEntity} with the result of the POST request.
     *
     */
    public  ResponseEntity<?> doPost(String url,  Class<?> responseType, HttpHeaders headers, Map<String, ?> params, Object body, Boolean retry, Boolean encode, Integer timeout) {
        try {
            ResponseEntity<?> response = null;
            HttpEntity<Object> requestEntity = new HttpEntity<>(body, headers);
            response =  ThreadUtil.timeout(timeout,() -> doRequest(url, responseType, HttpMethod.POST, requestEntity, params, retry, encode), null);
            if(response == null){
                LogUtil.printError("[TIMEOUT] Timeout reached! " +  POST_ERROR_MESSAGE + url);
            }
            return response;
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, POST_ERROR_MESSAGE + url);
        }
    }

    /**
     * Builds and make an HTTP POST request with parameters, without headers and body.
     * <p>
     * @param   url
     *          the base URL.
     * @param   responseType
     *          the class type of the response (e.g: a String, an Object, etc.)
     * @param   params
     *          the Map with key/value pair from each parameter.
     * @param   retry
     *          if true it will retry to do the request a predefined couple of times.
     * @param   encode
     *          if true will encode the value of each parameter.
     *
     * @return  the {@code ResponseEntity} with the result of the POST request.
     *
     */
    @SuppressWarnings("Unused")
    public  ResponseEntity<?> doPost(String url,  Class<?> responseType, Map<String, ?> params, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(this.getHeaders());
            return this.doRequest(url, responseType, HttpMethod.POST, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, POST_ERROR_MESSAGE + url);
        }
    }

    /**
     * Builds and make an HTTP POST request with parameters and body, without headers.
     * <p>
     * @param   url
     *          the base URL.
     * @param   responseType
     *          the class type of the response (e.g: a String, an Object, etc.)
     * @param   params
     *          the Map with key/value pair from each parameter.
     * @param   body
     *          the {@code Object} object representing the body for the request.
     * @param   retry
     *          if true it will retry to do the request a predefined couple of times.
     * @param   encode
     *          if true will encode the value of each parameter.
     *
     * @return  the {@code ResponseEntity} with the result of the POST request.
     *
     */
    @SuppressWarnings("Unused")
    public  ResponseEntity<?> doPost(String url,  Class<?> responseType, Map<String, ?> params,Object body, Boolean retry, Boolean encode) {
        try {
            HttpEntity<Object> requestEntity = new HttpEntity<>(body, this.getHeaders());
            return this.doRequest(url, responseType, HttpMethod.POST, requestEntity, params, retry, encode);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, POST_ERROR_MESSAGE + url);
        }
    }

    /**
     * Builds and make an HTTP POST request with headers, without parameters and body.
     * <p>
     * @param   url
     *          the base URL.
     * @param   responseType
     *          the class type of the response (e.g: a String, an Object, etc.)
     * @param   headers
     *          the HTTP headers to configure.
     * @param   retry
     *          if true it will retry to do the request a predefined couple of times.
     *
     * @return  the {@code ResponseEntity} with the result of the POST request.
     *
     */
    @SuppressWarnings("Unused")
    public  ResponseEntity<?> doPost(String url,  Class<?> responseType, HttpHeaders headers, Boolean retry) {
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            return this.doRequest(url, responseType, HttpMethod.POST, requestEntity, this.getParams(), retry, false);
        } catch (Exception e) {
            throw new UtilException(HttpUtil.class, e, POST_ERROR_MESSAGE + url);
        }
    }

    /**
     * Builds and make an empty HTTP POST request without headers, parameters and body.
     * <p>
     * @param   url
     *          the base URL.
     * @param   responseType
     *          the class type of the response (e.g: a String, an Object, etc.)
     * @param   retry
     *          if true it will retry to do the request a predefined couple of times.
     *
     * @return  the {@code ResponseEntity} with the result of the POST request.
     *
     */
    @SuppressWarnings("Unused")
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
