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

package net.catenax.ce.materialpass.http.controllers.auth;

import net.catenax.ce.materialpass.models.Credential;
import net.catenax.ce.materialpass.models.Response;
import net.catenax.ce.materialpass.models.UserCredential;
import net.catenax.ce.materialpass.services.AuthenticationService;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tools.httpTools;
import tools.jsonTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {
    // [Logic Methods] ----------------------------------------------------------------
    @Autowired
    private Environment env;

    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    final static String clientIdPath = "keycloak.resource";

    private @Autowired AuthenticationService authService;
    private Response loginFromHttpRequest(){
        Response response = httpTools.getResponse();
        Set<String> roles = httpTools.getUserClientRoles(this.httpRequest,env.getProperty(clientIdPath));

        if(roles == null) {
            response.message = "You have no assigned roles!";
            return response;
        }

        response.message = "You are logged with this roles: " + roles.toString();
        AccessToken accessToken = httpTools.getUser(this.httpRequest);
        if(!httpTools.isInSession(this.httpRequest, "user")){

            // TODO: Get client credentials from hashiCorpVault
            Credential Credential = new Credential(
                    new UserCredential(
                            accessToken.getPreferredUsername(),
                            accessToken.getSubject(),
                            ""
                    )
            );
            httpTools.setSessionValue(this.httpRequest, "Credential",Credential);
        }
        Credential currentCredential = (Credential) httpTools.getSessionValue(this.httpRequest, "Credential");
        currentCredential.setClient_id(accessToken.getIssuedFor());

        response.data = jsonTools.getObjectArray(
                currentCredential,
                accessToken
        );

        return response;

    }

    // [API Services]  ----------------------------------------------------------------
    /*
     */
    @RequestMapping(method = RequestMethod.GET)
    Response index() throws Exception{
        httpTools.redirect(httpResponse,"/auth/login");
        return httpTools.getResponse("Redirect to Login");
    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    Response logout() throws Exception{
        Response response = httpTools.getResponse();
        httpRequest.logout();
        httpTools.redirect(httpResponse,"/auth/login");
        response.message = "Logged out successfully!";
        return response;
    }
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    Response check(){
        Boolean check = httpTools.isAuthenticated(httpRequest);
        return httpTools.getResponse(check ? "User Authenticated":"User not Authenticated", check);
    }
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    Response login() throws Exception{
        return loginFromHttpRequest();
    }

    @RequestMapping(value = "/token", method = RequestMethod.GET)
    Response getToken(){
        Response response = httpTools.getResponse();
        response.data = authService.getToken();
        return response;
    }

    @RequestMapping(value = "/recycler", method = RequestMethod.GET)
    public Response recycler(){
        Response response = httpTools.getResponse();

        Set<String> roles = httpTools.getUserClientRoles(httpRequest,env.getProperty("keycloak.resource"));
        response.message = "You are logged in as Recycler role | " + "This are the received roles " + roles.toString();
        return response;
    }

    @RequestMapping(value = "/oem", method = RequestMethod.GET)
    public Response oem(){
        Response response = httpTools.getResponse();

        Set<String> roles = httpTools.getUserClientRoles(httpRequest,env.getProperty("keycloak.resource"));
        response.message = "You are logged in as OEM role | " + "This are the received roles " + roles;
        return response;
    }

}
