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

package org.eclipse.tractusx.productpass.http.controllers.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.eclipse.tractusx.productpass.models.auth.Credential;
import org.eclipse.tractusx.productpass.models.http.Response;
import org.eclipse.tractusx.productpass.models.auth.UserCredential;
import org.eclipse.tractusx.productpass.services.AuthenticationService;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import utils.HttpUtil;
import utils.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {
    // [Logic Methods] -------------
    // ---------------------------------------------------
    @Autowired
    private Environment env;

    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    final static String clientIdPath = "keycloak.resource";
    private @Autowired AuthenticationService authService;
    private Response loginFromHttpRequest(){
        Response response = HttpUtil.getResponse();
        Set<String> roles = HttpUtil.getUserClientRoles(this.httpRequest,env.getProperty(clientIdPath));


        if(roles == null) {
            response.message = "You have no assigned roles!";
            return response;
        }


        response.message = "You are logged with this roles: " + roles.toString();
        AccessToken accessToken = HttpUtil.getUser(this.httpRequest);
        if(!HttpUtil.isInSession(this.httpRequest, "user")){

            // TODO: Get client credentials from hashiCorpVault
            Credential Credential = new Credential(
                    new UserCredential(
                            accessToken.getPreferredUsername(),
                            accessToken.getSubject(),
                            ""
                    )
            );
            HttpUtil.setSessionValue(this.httpRequest, "Credential",Credential);
        }
        Credential currentCredential = (Credential) HttpUtil.getSessionValue(this.httpRequest, "Credential");
        currentCredential.setClient_id(accessToken.getIssuedFor());

        response.data = JsonUtil.getObjectArray(
                currentCredential,
                accessToken
        );

        return response;

    }

    // [API Services]  ----------------------------------------------------------------
    /*
     */
    @RequestMapping(method = RequestMethod.GET)
    @Operation(summary = "Performs authentication against backend service")
    public Response index() throws Exception{
        HttpUtil.redirect(httpResponse,"/auth/login");
        return HttpUtil.getResponse("Redirect to Login");
    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @Operation(summary = "Performs logout operation against backend service")
    public Response logout() throws Exception{
        Response response = HttpUtil.getResponse();
        httpRequest.logout();
        HttpUtil.redirect(httpResponse,"/auth/login");
        response.message = "Logged out successfully!";
        return response;
    }
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    @Operation(summary = "Checks the user logged in status", responses = {
            @ApiResponse(description = "", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)))
    })
    public Response check(){
        Boolean check = HttpUtil.isAuthenticated(httpRequest);
        return HttpUtil.getResponse(check ? "User Authenticated":"User not Authenticated", check);
    }
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @Operation(summary = "Performs authentication against backend service")
    public Response login() throws Exception{
        return loginFromHttpRequest();
    }

    @RequestMapping(value = "/token", method = RequestMethod.GET)
    @Operation(summary = "Returns access token", responses = {
            @ApiResponse(description = "", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)))
    })
    public Response getToken(){
        Response response = HttpUtil.getResponse();
        response.data = authService.getToken();
        return response;
    }

}
