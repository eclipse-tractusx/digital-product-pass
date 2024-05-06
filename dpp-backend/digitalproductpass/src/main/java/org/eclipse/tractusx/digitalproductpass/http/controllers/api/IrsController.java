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

package org.eclipse.tractusx.digitalproductpass.http.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.tractusx.digitalproductpass.config.IrsConfig;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.managers.TreeManager;
import org.eclipse.tractusx.digitalproductpass.models.http.Response;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.Search;
import org.eclipse.tractusx.digitalproductpass.models.irs.Job;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobHistory;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobResponse;
import org.eclipse.tractusx.digitalproductpass.models.manager.Node;
import org.eclipse.tractusx.digitalproductpass.models.manager.SearchStatus;
import org.eclipse.tractusx.digitalproductpass.models.manager.Status;
import org.eclipse.tractusx.digitalproductpass.services.AuthenticationService;
import org.eclipse.tractusx.digitalproductpass.services.IrsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.HttpUtil;
import utils.JsonUtil;
import utils.LogUtil;

import java.util.Map;
/**
 * This class consists exclusively to define the HTTP methods needed for the IRS component.
 **/
@RestController
@RequestMapping("/api/irs")
@Tag(name = "IRS Controller")
@SecurityRequirement(name = "BearerAuthentication")
public class IrsController {

    /** ATTRIBUTES **/
    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;

    private @Autowired AuthenticationService authService;

    private @Autowired HttpUtil httpUtil;
    private @Autowired JsonUtil jsonUtil;

    private @Autowired IrsConfig irsConfig;

    private @Autowired IrsService irsService;
    private @Autowired TreeManager treeManager;
    private @Autowired ProcessManager processManager;

    /** METHODS **/

    /**
     * HTTP GET method which receives and handles the call back from the IRS
     * <p>
     * @param   processId
     *          the {@code String} process id contained in the path of the url
     * @param   searchId
     *          the {@code String} unique hash which identifies the callback search
     * @param   id
     *          the {@code String} id from the globalAssetId given by the IRS
     * @param   state
     *          the {@code String} state of the IRS callback event
     *
     * @return this {@code Response} HTTP response with an OK message and 200 status code
     *
     */
    @RequestMapping(value = "/{processId}/{searchId}", method = RequestMethod.GET)
    @Operation(summary = "Endpoint called by the IRS to set status completed")
    public Response endpoint(@PathVariable String processId, @PathVariable String searchId, @RequestParam String id, @RequestParam String state) {
        Response response = httpUtil.getInternalError();
        try {
            if (!processManager.checkProcess(processId)) {
                return httpUtil.buildResponse(httpUtil.getNotFound("Process not found!"), httpResponse);
            }

            Status status = processManager.getStatus(processId);
            if(status == null){
                this.processManager.setJobChildrenFound(processId, -2);
                return httpUtil.buildResponse(httpUtil.getNotFound("No status is created"), httpResponse);
            }
            JobHistory jobHistory = status.getJob();
            if(jobHistory == null){
                LogUtil.printWarning("["+processId+"] Job not found! Retrying...");
                status = processManager.getStatus(processId);
                jobHistory = status.getJob();
                if(jobHistory == null) {
                    LogUtil.printError("["+processId+"] Job not found again!");
                    this.processManager.setJobChildrenFound(processId, -2);
                    return httpUtil.buildResponse(httpUtil.getNotFound(), httpResponse);
                }
            }
            String jobSearchId = jobHistory.getSearchId();
            if(jobSearchId == null){
                LogUtil.printError("["+processId+"] The search id was null! Not able to find the job!");
                this.processManager.setJobChildrenFound(processId, -2);
                return httpUtil.buildResponse(httpUtil.getNotAuthorizedResponse(), httpResponse);
            }
            if(!jobSearchId.equals(searchId)){
                LogUtil.printError("["+processId+"] The search id was not found in the job history again!");
                this.processManager.setJobChildrenFound(processId, -2);
                return httpUtil.buildResponse(httpUtil.getNotAuthorizedResponse(), httpResponse);
            }
            LogUtil.printMessage("["+processId+"] Job callback received with state ["+ state+"]. Requesting Job ["+jobHistory.getJobId()+"]!");
            JobResponse irsJob = this.irsService.getJob(jobHistory.getJobId());
            this.treeManager.populateTree(processId, jobHistory, irsJob);
            response = httpUtil.getResponse("OK");
            return httpUtil.buildResponse(response, httpResponse);
        } catch (Exception e) {
            response.message = e.getMessage();
            this.processManager.setJobChildrenFound(processId, -2);
            return httpUtil.buildResponse(response, httpResponse);
        }
    }
    /**
     * HTTP GET method which returns the current component tree in a simplified structure
     * <p>
     * @param   processId
     *          the {@code String} process id contained in the path of the url
     *
     * @return this {@code Response} HTTP response with the current component tree of the process
     *
     */
    @RequestMapping(value = "/{processId}/components", method = RequestMethod.GET)
    @Operation(summary = "Api called by the frontend to obtain the tree of components")
    public Response components(@PathVariable String processId) {
        Response response = httpUtil.getInternalError();
        if (!authService.isAuthenticated(httpRequest)) {
            response = httpUtil.getNotAuthorizedResponse();
            return httpUtil.buildResponse(response, httpResponse);
        }
        try {
            if (!processManager.checkProcess(processId)) {
                return httpUtil.buildResponse(httpUtil.getNotFound("Process not found!"), httpResponse);
            }

            Status status = processManager.getStatus(processId);
            if(status == null){
                return httpUtil.buildResponse(httpUtil.getNotFound("No status is created"), httpResponse);
            }
            if(!this.irsConfig.getEnabled()){
                return httpUtil.buildResponse(httpUtil.getForbiddenResponse("The children drill down functionality is not available!"), httpResponse);
            }
            if(!status.getChildren()){
                return httpUtil.buildResponse(httpUtil.getForbiddenResponse("The children drill down functionality is not available for this process!"), httpResponse);
            }

            response = httpUtil.getResponse();
            response.data = this.treeManager.getTreeComponents(processId); // Loads the tree components with a easy structure for frontend component
            return httpUtil.buildResponse(response, httpResponse);
        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }
    }
    /**
     * HTTP GET method which returns the current tree of digital twins which are found in this process
     * <p>
     * @param   processId
     *          the {@code String} process id contained in the path of the url
     *
     * @return this {@code Response} HTTP response with the current complete tree data model of the process id
     *
     */
    @RequestMapping(value = "/{processId}/tree", method = RequestMethod.GET)
    @Operation(summary = "Api called by the frontend to obtain the tree of components")
    public Response tree( @PathVariable String processId) {
        Response response = httpUtil.getInternalError();
        if (!authService.isAuthenticated(httpRequest)) {
            response = httpUtil.getNotAuthorizedResponse();
            return httpUtil.buildResponse(response, httpResponse);
        }
        try {
            if (!processManager.checkProcess(processId)) {
                return httpUtil.buildResponse(httpUtil.getNotFound("Process not found!"), httpResponse);
            }

            Status status = processManager.getStatus(processId);
            if(status == null){
                return httpUtil.buildResponse(httpUtil.getNotFound("No status is created"), httpResponse);
            }
            if(!this.irsConfig.getEnabled()){
                return httpUtil.buildResponse(httpUtil.getForbiddenResponse("The children drill down functionality is not available!"), httpResponse);
            }
            if(!status.getChildren()){
                return httpUtil.buildResponse(httpUtil.getForbiddenResponse("The children drill down functionality is not available for this process!"), httpResponse);
            }
            response = httpUtil.getResponse();
            response.data = this.treeManager.getTree(processId); // Loads the tree components with a easy structure for frontend component
            return httpUtil.buildResponse(response, httpResponse);
        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }
    }

    /**
     * HTTP GET method which returns the current tree of digital twins which are found in this process
     * <p>
     * @param   processId
     *          the {@code String} process id contained in the path of the url
     *
     * @return this {@code Response} HTTP response with the current complete tree data model of the process id
     *
     */
    @RequestMapping(value = "/{processId}/state", method = RequestMethod.GET)
    @Operation(summary = "Api called by the frontend to check if the process is finished")
    public Response state( @PathVariable String processId) {
        Response response = httpUtil.getInternalError();
        if (!authService.isAuthenticated(httpRequest)) {
            response = httpUtil.getNotAuthorizedResponse();
            return httpUtil.buildResponse(response, httpResponse);
        }
        try {
            if (!processManager.checkProcess(processId)) {
                return httpUtil.buildResponse(httpUtil.getNotFound("Process not found!"), httpResponse);
            }

            Status status = processManager.getStatus(processId);
            if(status == null){
                return httpUtil.buildResponse(httpUtil.getNotFound("No status is created"), httpResponse);
            }
            if(!this.irsConfig.getEnabled()){
                response = httpUtil.getResponse("The children drill down functionality is not available!");
                response.status = 503;
                response.statusText = "Service Unavailable";
                return httpUtil.buildResponse(response, httpResponse);
            }
            if(!status.getChildren()){
                response = httpUtil.getResponse("The children drill down functionality is not available for this process!");
                response.status = 503;
                response.statusText = "Service Unavailable";
                return httpUtil.buildResponse(response, httpResponse);
            }
            response = httpUtil.getResponse();
            Integer children = status.getJob().getChildren();

            switch (children){

                case -2:
                    response.status = 400;
                    response.statusText = "Bad Request";
                    response.message = "Something went wrong while searching for the children";
                    break;

                case -1:
                    response.status = 204;
                    response.statusText = "No Content";
                    response.message = "Searching for the children";
                    break;

                case 0:
                    response.status = 404;
                    response.statusText = "Not Found";
                    response.message = "No children available";
                    break;

                default:
                    response.status = 200;
                    response.message = "[" + children + "] children found";
                    response.data = status.getJob();
            }
            return httpUtil.buildResponse(response, httpResponse);
        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }
    }

}
