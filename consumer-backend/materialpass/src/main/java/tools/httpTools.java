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

import net.catenax.ce.materialpass.models.Response;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.web.client.RestTemplate;
import tools.exceptions.ToolException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public final class httpTools {

    private static final configTools configurations = new configTools();

    public static Set<String> getCurrentUserRealmRoles(HttpServletRequest request){
        AccessToken user = httpTools.getCurrentUser(request); // Get user from request
        if(user == null) {
            return null;
        }
        return user.getRealmAccess().getRoles(); // Get roles from user
    }
    public static void redirect(HttpServletResponse httpResponse, String url){
        try {
            httpResponse.sendRedirect(url);
        } catch (IOException e) {
            throw new ToolException(httpTools.class, e, "It was not posible to redirect to ["+url+"]");
        }
    }
    public static Set<String> getCurrentUserClientRoles(HttpServletRequest request, String clientId){
        AccessToken user = httpTools.getCurrentUser(request); // Get user from request
        if(user == null) {
            return null;
        }
        return user.getResourceAccess(clientId).getRoles(); // Get roles from user
    }
    public static AccessToken getCurrentUser(HttpServletRequest request){
        KeycloakSecurityContext session = httpTools.getCurrentUserSession(request); // Get the session from the request
        if (session == null) {
            return null;
        }
        return session.getToken(); // Return user info
    }
    public static KeycloakPrincipal getCurrentUserPrincipal(HttpServletRequest request){
        return (KeycloakPrincipal) request.getUserPrincipal();// Get the user data from the request
    }
    public static String getJWTToken(HttpServletRequest request){
        AccessToken token = httpTools.getCurrentUser(request);
        logTools.printMessage(jsonTools.toJson(token));
        return crypTools.toBase64Url(jsonTools.toJson(token));
    }
    public static KeycloakSecurityContext getCurrentUserSession(HttpServletRequest request){
        KeycloakPrincipal principal = httpTools.getCurrentUserPrincipal(request); // Get the principal to access the session
        if (principal == null) {
            return null;
        }
        return principal.getKeycloakSecurityContext();
    }


    public static Object getSessionValue(HttpServletRequest httpRequest, String key){
        return httpRequest.getSession().getAttribute(key);
    }
    public static void setSessionValue(HttpServletRequest httpRequest, String key, Object value){
        httpRequest.getSession().setAttribute(key, value);
    }

    public static Boolean isInSession(HttpServletRequest httpRequest, String key){
        try{
            Object value = httpRequest.getSession().getAttribute(key);
            if(value != null){
                return true;
            }
        }catch(Exception e){
            throw new ToolException(httpTools.class, e, "Something went wrong and session key [" + key + "] was not found! ");
        }
        return false;
    }

    public static String getHttpInfo(HttpServletRequest httpRequest, Integer status){
        return "[" + httpRequest.getProtocol() + " " + httpRequest.getMethod() + "] " + status + ": " + httpRequest.getRequestURI();
    }
    public static Response getResponse(){
        return new Response(
                "",
                200,
                "Success"
        );
    }
    public static Response getResponse(String message){
        return new Response(
                message,
                200,
                "Success"
        );
    }
    public static Response getResponse(String message, Object data){
        return new Response(
                message,
                200,
                "Success",
                data
        );
    }

    public static Boolean isAuthenticated(HttpServletRequest httpRequest){
        KeycloakPrincipal principal = httpTools.getCurrentUserPrincipal(httpRequest);
        return principal != null;
    }
    public static String getParamOrDefault(HttpServletRequest httpRequest, String param, String defaultPattern){
        String requestParam =  httpRequest.getParameter(param);
        if(requestParam == null) {
            return defaultPattern;
        }
        return requestParam;
    }

    public static Object doGet(RestTemplate restTemplate, String url, Class responseType, Map<String, ?> params, Boolean retry) {
        try {
            Object response = restTemplate.getForObject(url, responseType, params);
            if(!retry && response != null) {
                return response;
            }
            int i = 0;
            Integer maxRetries  = (Integer) configurations.getConfigurationParam("maxRetries");
            if(maxRetries == null) {
                throw new ToolException(httpTools.class, "It was not possible to do GET request to " + url);
            }

            while(response == null && i < maxRetries) {
                response = restTemplate.getForObject(url, responseType, params);
                logTools.printDebug("["+i+"] Retrying GET request to " + url);
                i++;
            }

            if(response != null){
                return response;
            }
        } catch (Exception e) {
            throw new ToolException(httpTools.class, e, "It was not possible to do GET request to " + url);
        }
        throw new ToolException(httpTools.class, "It was not possible to do GET request to " + url);
    }
    public static Object doGet(RestTemplate restTemplate, String url, Class responseType, Boolean retry) {
        try {
            Object response = restTemplate.getForObject(url, responseType);
            if(!retry && response != null) {
                return response;
            }
            int i = 0;
            Integer maxRetries  = (Integer) configurations.getConfigurationParam("maxRetries");
            if(maxRetries == null) {
                throw new ToolException(httpTools.class, "It was not possible to do GET request to " + url);
            }

            while(response == null && i < maxRetries) {
                response = restTemplate.getForObject(url, responseType);
                logTools.printDebug("["+i+"] Retrying GET request to " + url);
                i++;
            }

            if(response != null){
                return response;
            }
        } catch (Exception e) {
            throw new ToolException(httpTools.class, e, "It was not possible to do GET request to " + url);
        }
        throw new ToolException(httpTools.class, "It was not possible to do GET request to " + url);
    }
    public static Object doPost(RestTemplate restTemplate, String url, Object body, Class responseType,Map<String, ?> params, Boolean retry) {
        try {
            Object response = restTemplate.postForObject(url, body, responseType, params);
            if(!retry && response != null) {
                return response;
            }
            int i = 0;
            Integer maxRetries  = (Integer) configurations.getConfigurationParam("maxRetries");
            if(maxRetries == null) {
                throw new ToolException(httpTools.class, "It was not possible to do POST request to " + url);
            }

            while(response == null && i < maxRetries) {
                response = restTemplate.postForObject(url, body, responseType, params);
                logTools.printDebug("["+i+"] Retrying POST request to " + url);
                i++;
            }

            if(response != null){
                return response;
            }
        } catch (Exception e) {
            throw new ToolException(httpTools.class, e, "It was not possible to do POST request to " + url);
        }
        throw new ToolException(httpTools.class, "It was not possible to do POST request to " + url);
    }
    public static Object doPost(RestTemplate restTemplate, String url, Object body, Class responseType, Boolean retry) {
        try {
            Object response = restTemplate.postForObject(url, body, responseType);
            if(!retry && response != null) {
                return response;
            }
            int i = 0;
            Integer maxRetries  = (Integer) configurations.getConfigurationParam("maxRetries");
            if(maxRetries == null) {
                throw new ToolException(httpTools.class, "It was not possible to do POST request to " + url);
            }

            while(response == null && i < maxRetries) {
                response = restTemplate.postForObject(url, body, responseType);
                logTools.printDebug("["+i+"] Retrying POST request to " + url);
                i++;
            }

            if(response != null){
                return response;
            }
        } catch (Exception e) {
            throw new ToolException(httpTools.class, e, "It was not possible to do POST request to " + url);
        }
        throw new ToolException(httpTools.class, "It was not possible to do POST request to " + url);
    }

}
