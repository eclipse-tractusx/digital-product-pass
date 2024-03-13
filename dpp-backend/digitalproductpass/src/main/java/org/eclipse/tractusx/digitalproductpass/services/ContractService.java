/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

package org.eclipse.tractusx.digitalproductpass.services;

import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.eclipse.tractusx.digitalproductpass.config.DiscoveryConfig;
import org.eclipse.tractusx.digitalproductpass.config.DtrConfig;
import org.eclipse.tractusx.digitalproductpass.config.PassportConfig;
import org.eclipse.tractusx.digitalproductpass.config.ProcessConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ControllerException;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceException;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.digitalproductpass.managers.DtrSearchManager;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.models.catenax.BpnDiscovery;
import org.eclipse.tractusx.digitalproductpass.models.catenax.Dtr;
import org.eclipse.tractusx.digitalproductpass.models.catenax.EdcDiscoveryEndpoint;
import org.eclipse.tractusx.digitalproductpass.models.edc.AssetSearch;
import org.eclipse.tractusx.digitalproductpass.models.edc.DataPlaneEndpoint;
import org.eclipse.tractusx.digitalproductpass.models.http.Response;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.DiscoverySearch;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.Search;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.TokenRequest;
import org.eclipse.tractusx.digitalproductpass.models.manager.History;
import org.eclipse.tractusx.digitalproductpass.models.manager.Process;
import org.eclipse.tractusx.digitalproductpass.models.manager.SearchStatus;
import org.eclipse.tractusx.digitalproductpass.models.manager.Status;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.Dataset;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.Set;
import org.eclipse.tractusx.digitalproductpass.models.service.BaseService;
import org.eclipse.tractusx.digitalproductpass.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
/**
 * This class consists exclusively of methods to operate on executing the Data Plane operations.
 *
 * <p> The methods defined here are intended to do every needed operations in order to be able to transfer data or passport from Data Plane Endpoint.
 *
 */
@Service
public class ContractService extends BaseService {

    /** ATTRIBUTES **/
    private @Autowired DataTransferService dataService;
    private @Autowired VaultService vaultService;
    private @Autowired AasService aasService;
    private @Autowired AuthenticationService authService;
    private @Autowired PassportConfig passportConfig;
    private @Autowired DiscoveryConfig discoveryConfig;
    private @Autowired DtrConfig dtrConfig;

    private @Autowired EdcUtil edcUtil;
    private @Autowired Environment env;
    @Autowired
    ProcessManager processManager;
    @Autowired
    DtrSearchManager dtrSearchManager;
    private @Autowired ProcessConfig processConfig;
    @Autowired
    CatenaXService catenaXService;
    @Autowired
    HttpUtil httpUtil;
    private @Autowired JsonUtil jsonUtil;

    /** CONSTRUCTOR(S) **/
    public ContractService() throws ServiceInitializationException {
        this.checkEmptyVariables();
    }

    /** METHODS **/
    /**
     * Does the contract agreement for a contract negotiation
     * <p>
     * @param   httpRequest
     *          the {@code HttpServletRequest} the http request object
     * @param   httpResponse
     *          the {@code HttpServletResponse} the http response object
     * @param   tokenRequestBody
     *          the {@code TokenRequest} token request body.
     *
     * @return  a {@code Response} response object of the method
     *
     * @throws  ServiceException
     *           if unable to do the contract agreement
     */
    public Response doContractAgreement(HttpServletRequest httpRequest, HttpServletResponse httpResponse, TokenRequest tokenRequestBody) throws ServiceException {
        try {
            Response response = httpUtil.getInternalError();
            Long signedAt = DateTimeUtil.getTimestamp();

            // Check for processId
            String processId = tokenRequestBody.getProcessId();
            if (!processManager.checkProcess(httpRequest, processId)) {
                response = httpUtil.getBadRequest("The process id does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }


            Process process = processManager.getProcess(httpRequest, processId);
            if (process == null) {
                response = httpUtil.getBadRequest("The process id does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Get status to check for contract id
            String contractId = tokenRequestBody.getContractId();
            Status status = processManager.getStatus(processId);

            // Check if was already declined
            if (status.historyExists("contract-decline")) {
                response = httpUtil.getForbiddenResponse("This contract was declined! Please request a new one");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check if negotiation is canceled
            if (status.historyExists("negotiation-canceled")) {
                response = httpUtil.getForbiddenResponse("This negotiation has been canceled! Please request a new one");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check if was already agreed
            if (status.historyExists("contract-agreed")) {
                response = httpUtil.getForbiddenResponse("This contract is already agreed!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check if there is a contract available
            if (!status.historyExists("contract-dataset")) {
                response = httpUtil.getBadRequest("No contract is available!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check if the contract id is correct
            History history = status.getHistory("contract-dataset");
            if (!history.getId().contains(contractId)) {
                response = httpUtil.getBadRequest("This contract id does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Load all the available contracts
            Map<String, Dataset> availableContracts = processManager.loadDatasets(processId);
            String seedId = String.join("|", availableContracts.keySet()); // Generate Seed
            // Check the validity of the token
            String expectedToken = processManager.generateToken(process, seedId);
            String token = tokenRequestBody.getToken();
            if (!expectedToken.equals(token)) {
                response = httpUtil.getForbiddenResponse("The token is invalid!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            Dataset dataset = availableContracts.get(contractId);

            if (dataset == null) {
                response.message = "The Contract Selected was not found!";
                return httpUtil.buildResponse(response, httpResponse);
            }

            String policyId = tokenRequestBody.getPolicyId();
            DataTransferService.NegotiateContract contractNegotiation = null;
            // Check if policy is available!
            if (policyId != null) {
                Set policy = edcUtil.getPolicyById(dataset, policyId);
                if (policy == null) {
                    response = httpUtil.getBadRequest("The policy selected does not exists!");
                    return httpUtil.buildResponse(response, httpResponse);
                }
                contractNegotiation = dataService
                        .new NegotiateContract(
                        processManager.loadDataModel(httpRequest),
                        processId,
                        status.getBpn(),
                        dataset,
                        processManager.getStatus(processId),
                        policy
                );
            } else {
                // If the policy is not selected get the first one by default
                contractNegotiation = dataService
                        .new NegotiateContract(
                        processManager.loadDataModel(httpRequest),
                        processId,
                        status.getBpn(),
                        dataset,
                        processManager.getStatus(processId)
                );
            }
            String statusPath = processManager.setAgreed(httpRequest, processId, signedAt, contractId, policyId);
            if (statusPath == null) {
                response.message = "Something went wrong when agreeing with the contract!";
                return httpUtil.buildResponse(response, httpResponse);
            }
            LogUtil.printMessage("[PROCESS " + processId + "] Contract [" + contractId + "] Agreed! Starting negotiation...");
            processManager.startNegotiation(httpRequest, processId, contractNegotiation);
            LogUtil.printStatus("[PROCESS " + processId + "] Negotiation for [" + contractId + "] started!");

            response = httpUtil.getResponse("The contract was agreed successfully! Negotiation started!");
            response.data = processManager.getStatus(processId);
            return httpUtil.buildResponse(response, httpResponse);
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"doContractAgreement",
                    e,
                    "It was not possible to do the contract agreement!");
        }
    }



    /**
     * Creates an empty variables List.
     * <p>
     *
     * @return an empty {@code Arraylist}.
     *
     */
    @Override
    public List<String> getEmptyVariables() {
        return new ArrayList<>();
    }
}
