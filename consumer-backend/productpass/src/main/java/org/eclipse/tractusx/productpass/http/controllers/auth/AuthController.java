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

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.eclipse.tractusx.productpass.models.auth.Credential;
import org.eclipse.tractusx.productpass.models.auth.JwtToken;
import org.eclipse.tractusx.productpass.models.auth.UserInfo;
import org.eclipse.tractusx.productpass.models.http.Response;
import org.eclipse.tractusx.productpass.models.auth.UserCredential;
import org.eclipse.tractusx.productpass.services.AuthenticationService;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import utils.HttpUtil;
import utils.JsonUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.LogUtil;

import java.util.HashMap;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller")
@SecurityRequirement(name = "BearerAuthentication")
public class AuthController {
    // [Logic Methods] -------------
    // ---------------------------------------------------
    @Autowired
    private Environment env;

    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    final static String clientIdPath = "keycloak.resource";
    private @Autowired AuthenticationService authService;

    // [API Services]  ----------------------------------------------------------------
    /*
     */
    @RequestMapping(method = RequestMethod.GET)
    @Hidden
    public Response index() throws Exception{
        HttpUtil.redirect(httpResponse,"/passport");
        return HttpUtil.getResponse("Redirect to Login");
    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @Hidden
    public Response logout() throws Exception{
        Response response = HttpUtil.getResponse();
        httpRequest.logout();
        HttpUtil.redirect(httpResponse,"/passport");
        response.message = "Logged out successfully!";
        return response;
    }
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @Hidden
    public Response login() throws Exception{
        Response response = HttpUtil.getResponse();
        HttpUtil.redirect(httpResponse,"/passport");
        return response;
    }

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    @Operation(summary = "Checks the user logged in status", responses = {
            @ApiResponse(description = "Content of Data Field in Response", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Boolean.class)))
    })
    public Response check(){
        Boolean check = authService.isAuthenticated(httpRequest);
        return HttpUtil.getResponse(check ? "User Authenticated":"User not Authenticated", check);
    }


    @RequestMapping(value = "/token", method = RequestMethod.GET)
    @Operation(summary = "Returns access token", responses = {
            @ApiResponse(description = "Default Response Structure", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "Content of Data Field in Response", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JwtToken.class)))
    })
    public Response getToken(){
        // Check if user is Authenticated
        if(!authService.isAuthenticated(httpRequest)){
            return HttpUtil.buildResponse(HttpUtil.getNotAuthorizedResponse(), httpResponse);
        }

        Response response = HttpUtil.getResponse();
        response.data = authService.getToken();
        return response;
    }
    @RequestMapping(value = "/userInfo", method = RequestMethod.POST)
    @Operation(security = {@SecurityRequirement(name = "Bearer Authorization")},
            summary = "Returns user info related to JWT Token", responses = {
            @ApiResponse(description = "Default Response Structure", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "Content of Data Field in Response", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserInfo.class))),
    })
    public Response getUserInfo(){
        Response response = HttpUtil.getNotAuthorizedResponse();
        // Check if user is Authenticated
        if(!authService.isAuthenticated(httpRequest)){
            return HttpUtil.buildResponse(response, httpResponse);
        }
        String token = HttpUtil.getAuthorizationToken(httpRequest);
        if(token == null || token.isEmpty() || token.isBlank()){
            return HttpUtil.buildResponse(response, httpResponse);
        }
        UserInfo userInfo = null;
        try {
            userInfo = authService.getUserInfo(token);
        }catch (Exception e){
            return HttpUtil.buildResponse(response, httpResponse);
        }

        if(userInfo==null){
            response.message = "No user info available";
            return HttpUtil.buildResponse(response, httpResponse);
        }
        response.message = null;
        response.status = 200;
        response.data = userInfo;
        return HttpUtil.buildResponse(response, httpResponse);
    }

}
