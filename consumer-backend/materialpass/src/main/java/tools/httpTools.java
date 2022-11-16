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

import net.catenax.ce.materialpass.http.models.Response;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import tools.exceptions.ToolException;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public final class httpTools {
    public static Set<String> getCurrentUserRoles(HttpServletRequest request){
        AccessToken user = httpTools.getCurrentUser(request); // Get user from request
        if(user == null) {
            logTools.printError("User is not authenticated!");
            return null;
        }
        return user.getRealmAccess().getRoles(); // Get roles from user
    }
    public static AccessToken getCurrentUser(HttpServletRequest request){
        KeycloakSecurityContext session = httpTools.getCurrentUserSession(request); // Get the session from the request
        if (session == null) {
            logTools.printError("Session does not exists!");
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
            logTools.printError("User not authenticated, principal is null!");
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
            throw new ToolException(httpTools.class, "Something went wrong and session key [" + key + "] was not found! " + e.getMessage());
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

    public static String getParamOrDefault(HttpServletRequest httpRequest, String param, String defaultPattern){
        String requestParam =  httpRequest.getParameter(param);
        if(requestParam == null) {
            return defaultPattern;
        }
        return requestParam;
    }
}
