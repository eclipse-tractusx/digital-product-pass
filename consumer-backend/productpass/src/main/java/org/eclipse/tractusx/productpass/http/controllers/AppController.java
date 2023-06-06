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

package org.eclipse.tractusx.productpass.http.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.eclipse.tractusx.productpass.exceptions.ControllerException;
import org.eclipse.tractusx.productpass.models.edc.DataPlaneEndpoint;
import org.eclipse.tractusx.productpass.models.http.Response;
import org.eclipse.tractusx.productpass.models.passports.Passport;
import org.eclipse.tractusx.productpass.services.DataPlaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import utils.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@Tag(name = "Public Controller")
public class AppController {

    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;

    @Autowired
    HttpUtil httpUtil;
    @Autowired
    EdcUtil edcUtil;

    @Autowired
    Environment env;

    @Autowired
    PassportUtil passportUtil;

    @Autowired
    DataPlaneService dataPlaneService;


    @GetMapping("/")
    @Hidden                     // hides this endpoint from api documentation - swagger-ui
    public Response index(){
        httpUtil.redirect(httpResponse,"/passport");
        return httpUtil.getResponse("Redirect to UI");
    }


    @GetMapping("/health")
    @Operation(summary = "Returns the backend health status", responses = {
            @ApiResponse(description = "Gets the application health", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)))
    })
    public Response health(){
        Response response = httpUtil.getResponse(
                "RUNNING",
                200
        );
        response.data = DateTimeUtil.getDateTimeFormatted(null);
        return response;
    }

    @RequestMapping(value = "/endpoint", method = RequestMethod.POST)
    public Response endpoint(@RequestBody Object body){
        try{

            DataPlaneEndpoint endpointData = edcUtil.parseDataPlaneEndpoint(body);
            if(endpointData == null){
                throw new ControllerException(this.getClass().getName(),"The endpoint data request is empty!");
            }
            if(endpointData.getEndpoint().isEmpty()){
                throw new ControllerException(this.getClass().getName(),"The data plane endpoint address is empty!");
            }
            if(endpointData.getAuthCode().isEmpty()){
                throw new ControllerException(this.getClass().getName(),"The authorization code is empty!");
            }
            if(endpointData.getOfferId().isEmpty()){
                throw new ControllerException(this.getClass().getName(),"The Offer Id is empty!");
            }

            Passport passport = dataPlaneService.getPassport(endpointData);
            Boolean prettyPrint = env.getProperty("passport.dataTransfer.indent", Boolean.class, true);
            Boolean encrypt = env.getProperty("passport.dataTransfer.encrypt", Boolean.class, true);
            String passportPath = passportUtil.savePassport(passport, endpointData, prettyPrint, encrypt);
            LogUtil.printMessage("[EDC] Passport Transfer Data ["+endpointData.getId()+"] Saved Successfully in ["+passportPath+"]!");

        }catch(Exception e) {
            LogUtil.printException(e, "This request is not allowed! It must contain the valid attributes from an EDC endpoint");
            return httpUtil.buildResponse(httpUtil.getForbiddenResponse(), httpResponse);
        }
        return httpUtil.buildResponse(httpUtil.getResponse(), httpResponse);
    }



}
