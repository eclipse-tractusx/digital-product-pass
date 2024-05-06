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

package org.eclipse.tractusx.digitalproductpass.http.controllers.auth;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.tractusx.digitalproductpass.models.auth.JwtToken;
import org.eclipse.tractusx.digitalproductpass.models.auth.UserInfo;
import org.eclipse.tractusx.digitalproductpass.models.http.Response;
import org.eclipse.tractusx.digitalproductpass.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import utils.HttpUtil;

/**
 * This class consists exclusively to define the HTTP methods for authentication purposes.
 **/
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller")
@SecurityRequirement(name = "BearerAuthentication")
public class AuthController {
    // [Logic Methods] -------------
    // ---------------------------------------------------
    /** ATTRIBUTES **/
    @Autowired
    private Environment env;
    @Autowired
    HttpUtil httpUtil;
    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    final static String clientIdPath = "keycloak.resource";
    private @Autowired AuthenticationService authService;

    // [API Services]  ----------------------------------------------------------------
    /*
     */

    /** METHODS **/
    @RequestMapping(method = RequestMethod.GET)
    @Hidden
    public Response index() throws Exception{
        httpUtil.redirect(httpResponse,"/passport");
        return httpUtil.getResponse("Redirect to Login");
    }

    /**
     * The HTTP GET method to request the logout.
     * <p>
     *
     * @return  a {@code Response} HTTP response with the status.
     *
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @Hidden
    public Response logout() throws Exception{
        Response response = httpUtil.getResponse();
        httpRequest.logout();
        httpUtil.redirect(httpResponse,"/passport");
        response.message = "Logged out successfully!";
        return response;
    }

    /**
     * The HTTP GET method to request the login.
     * <p>
     *
     * @return  a {@code Response} HTTP response with the status.
     *
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @Hidden
    public Response login() throws Exception{
        Response response = httpUtil.getResponse();
        httpUtil.redirect(httpResponse,"/passport");
        return response;
    }

    /**
     * The HTTP GET method to check if the user is authenticated.
     * <p>
     *
     * @return  a {@code Response} HTTP response with the status.
     *
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    @Operation(summary = "Checks the user logged in status", responses = {
            @ApiResponse(description = "Content of Data Field in Response", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Boolean.class)))
    })
    public Response check(){
        Boolean check = authService.isAuthenticated(httpRequest);
        return httpUtil.getResponse(check ? "User Authenticated":"User not Authenticated", check);
    }

    /**
     * The HTTP GET method to request the authentication token.
     * <p>
     *
     * @return  a {@code Response} HTTP response with the status.
     *
     */
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
            return httpUtil.buildResponse(httpUtil.getNotAuthorizedResponse(), httpResponse);
        }

        Response response = httpUtil.getResponse();
        response.data = authService.getToken();
        return response;
    }

    /**
     * The HTTP POST method to request the user's information related to the authentication token..
     * <p>
     *
     * @return  a {@code Response} HTTP response with the status.
     *
     */
    @RequestMapping(value = "/userInfo", method = RequestMethod.POST)
    @Operation(security = {@SecurityRequirement(name = "Bearer Authorization")},
            summary = "Returns user info related to JWT Token", responses = {
            @ApiResponse(description = "Default Response Structure", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "Content of Data Field in Response", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserInfo.class))),
    })
    public Response getUserInfo(){
        Response response = httpUtil.getNotAuthorizedResponse();
        // Check if user is Authenticated
        if(!authService.isAuthenticated(httpRequest)){
            return httpUtil.buildResponse(response, httpResponse);
        }
        String token = httpUtil.getAuthorizationToken(httpRequest);
        if(token == null || token.isEmpty() || token.isBlank()){
            return httpUtil.buildResponse(response, httpResponse);
        }
        UserInfo userInfo = null;
        try {
            userInfo = authService.getUserInfo(token);
        }catch (Exception e){
            return httpUtil.buildResponse(response, httpResponse);
        }

        if(userInfo==null){
            response.message = "No user info available";
            return httpUtil.buildResponse(response, httpResponse);
        }
        response.message = null;
        response.status = 200;
        response.data = userInfo;
        return httpUtil.buildResponse(response, httpResponse);
    }

}
