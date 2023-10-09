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

package org.eclipse.tractusx.productpass.http.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.tractusx.productpass.config.IrsConfig;
import org.eclipse.tractusx.productpass.managers.ProcessManager;
import org.eclipse.tractusx.productpass.managers.TreeManager;
import org.eclipse.tractusx.productpass.models.dtregistry.DigitalTwin3;
import org.eclipse.tractusx.productpass.models.http.Response;
import org.eclipse.tractusx.productpass.models.irs.Job;
import org.eclipse.tractusx.productpass.models.irs.JobHistory;
import org.eclipse.tractusx.productpass.models.irs.JobResponse;
import org.eclipse.tractusx.productpass.models.manager.Node;
import org.eclipse.tractusx.productpass.models.manager.Status;
import org.eclipse.tractusx.productpass.services.AuthenticationService;
import org.eclipse.tractusx.productpass.services.IrsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.HttpUtil;
import utils.JsonUtil;
import utils.LogUtil;

import java.util.Map;

@RestController
@RequestMapping("/api/irs")
@Tag(name = "IRS Controller")
@SecurityRequirement(name = "BearerAuthentication")
public class IrsController {

    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;

    private @Autowired AuthenticationService authService;

    private @Autowired HttpUtil httpUtil;
    private @Autowired JsonUtil jsonUtil;

    private @Autowired IrsConfig irsConfig;

    private @Autowired IrsService irsService;
    private @Autowired TreeManager treeManager;
    private @Autowired ProcessManager processManager;

    @RequestMapping(value = "/{processId}/{searchId}", method = RequestMethod.GET)
    @Operation(summary = "Endpoint called by the IRS to set status completed")
    public Response endpoint(@PathVariable String processId,@PathVariable String searchId, @RequestParam String id, @RequestParam String state) {
        Response response = httpUtil.getInternalError();
        LogUtil.printMessage(jsonUtil.toJson(httpRequest, true));
        try {
            if (!processManager.checkProcess(processId)) {
                return httpUtil.buildResponse(httpUtil.getNotFound("Process not found!"), httpResponse);
            }

            Status status = processManager.getStatus(processId);
            if(status == null){
                return httpUtil.buildResponse(httpUtil.getNotFound("No status is created"), httpResponse);
            }

            if(!status.getJobs().containsKey(searchId)){
                return httpUtil.buildResponse(httpUtil.getNotFound("The search id is invalid!"), httpResponse);
            }

            JobHistory jobHistory = status.getJobId(searchId);

            LogUtil.printMessage("["+processId+"] Requesting Job ["+id+"] after state ["+state+"]");
            JobResponse irsJob = this.irsService.getJob(id);
            LogUtil.printMessage(jsonUtil.toJson(irsJob, true));
            this.treeManager.updateNode(processId, jobHistory.getPath(), irsJob);
            response = httpUtil.getResponse("IRS is not available at the moment!");
            return httpUtil.buildResponse(response, httpResponse);
        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }
    }

    @RequestMapping(value = "/{processId}/tree", method = RequestMethod.GET)
    @Operation(summary = "Api called by the frontend to obtain the tree of components")
    public Response tree( @PathVariable String processId) {
        Response response = httpUtil.getInternalError();
        if (!authService.isAuthenticated(httpRequest)) {
            response = httpUtil.getNotAuthorizedResponse();
            return httpUtil.buildResponse(response, httpResponse);
        }
        try {
            response = httpUtil.getResponse("IRS is not available at the moment!");
            return httpUtil.buildResponse(response, httpResponse);
        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }
    }




}
