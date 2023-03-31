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

package org.eclipse.tractusx.productpass.http.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.eclipse.tractusx.productpass.models.dtregistry.SubModel;
import org.eclipse.tractusx.productpass.models.http.Response;
import org.eclipse.tractusx.productpass.models.negotiation.Catalog;
import org.eclipse.tractusx.productpass.models.passports.Passport;
import org.eclipse.tractusx.productpass.models.passports.PassportV3;
import org.eclipse.tractusx.productpass.services.AasService;
import org.eclipse.tractusx.productpass.services.AuthenticationService;
import org.eclipse.tractusx.productpass.services.DataTransferService;
import org.eclipse.tractusx.productpass.services.VaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.HttpUtil;
import utils.LogUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ThreadUtil;

@RestController
@RequestMapping("/api/data")
@Tag(name = "Data Controller")
@SecurityRequirement(name = "BearerAuthentication")
public class DataController {
    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    private @Autowired DataTransferService dataService;
    private @Autowired VaultService vaultService;
    private @Autowired AasService aasService;
    private @Autowired AuthenticationService authService;

    @RequestMapping(value = "/passport/{transferId}", method = {RequestMethod.GET})
    @Operation(summary = "Returns product passport by transfer process Id", responses = {
            @ApiResponse(description = "Default Response Structure", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "Content of Data Field in Response", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PassportV3.class)))
    })
    public Response getPassport(@PathVariable("transferId") String transferId, @RequestParam(value="version", required = false, defaultValue = "v3.0.1") String version) {
        // Check if user is Authenticated
        if(!authService.isAuthenticated(httpRequest)){
            Response response = HttpUtil.getNotAuthorizedResponse();
            return HttpUtil.buildResponse(response, httpResponse);
        }
        Response response = HttpUtil.getResponse();
        Passport passport = null;
        if(version.equals("v3.0.1")) { // Currently supporting just version v3
            passport = dataService.getPassportV3(transferId);
        }else{
            response.message = "Version is not available!";
            response.status = 400;
            response.data = null;
            return HttpUtil.buildResponse(response, httpResponse);
        }
        if (passport == null) {
            response.message = "Passport for transfer [" + transferId + "] not found!";
            response.status = 404;
            response.data = null;
            return HttpUtil.buildResponse(response, httpResponse);
        }
        response.data = passport;
        LogUtil.printMessage("Passport for transfer [" + transferId + "] retrieved successfully!");
        return HttpUtil.buildResponse(response, httpResponse);
    }
}
