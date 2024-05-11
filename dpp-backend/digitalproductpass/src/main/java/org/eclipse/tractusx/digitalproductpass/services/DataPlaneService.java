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

package org.eclipse.tractusx.digitalproductpass.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceException;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.digitalproductpass.models.edc.EndpointDataReference;
import org.eclipse.tractusx.digitalproductpass.models.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class consists exclusively of methods to operate on executing the Data Plane operations.
 *
 * <p> The methods defined here are intended to do every needed operations in order to be able to transfer data or passport from Data Plane Endpoint.
 *
 */
@Service
public class DataPlaneService extends BaseService {

    /** CONSTANTS **/
    public static final String AUTHORIZATION_KEY = "Authorization";

    /** ATTRIBUTES **/
    @Autowired
    HttpUtil httpUtil;
    @Autowired
    JsonUtil jsonUtil;

    public Environment env;

    /** CONSTRUCTOR(S) **/
    public DataPlaneService() throws ServiceInitializationException {
        this.checkEmptyVariables();
    }

    /** METHODS **/

    /**
     * Gets the Transfer data from the given data plane endpoint.
     * <p>
     * @param   endpointData
     *          the {@code DataPlaneEndpoint} object with data plane endpoint data.
     *
     * @return  a {@code Object} object with the body of the response.
     *
     * @throws  ServiceException
     *           if unable to get the transfer data.
     */
    public Object getTransferData(EndpointDataReference endpointData) {
        try {
            Map<String, Object> params = httpUtil.getParams();
            HttpHeaders headers =  new HttpHeaders();
            String authKey = AUTHORIZATION_KEY;
            if(env != null){
                authKey =  env.getProperty("configuration.edc.authorizationKey", AUTHORIZATION_KEY);
            }
            headers.add(authKey, endpointData.getPayload().getDataAddress().getProperties().getAuthorization());
            ResponseEntity<?> response = httpUtil.doGet(endpointData.getPayload().getDataAddress().getProperties().getEndpoint(), Object.class, headers, params, true, true);
            return response.getBody();
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getTransferData",
                    e,
                    "It was not possible to get transfer from transfer id ["+endpointData.getId()+"]");
        }
    }
    /**
     * Gets the Transfer data from the given data plane endpoint.
     * <p>
     * @param   endpointData
     *          the {@code DataPlaneEndpoint} object with data plane endpoint data.
     *
     * @return  a {@code Object} object with the body of the response.
     *
     * @throws  ServiceException
     *           if unable to get the transfer data.
     */
    public JsonNode getTransferDataFromEndpoint(EndpointDataReference endpointData, String dataPlaneEndpoint) {
        try {
            Map<String, Object> params = httpUtil.getParams();
            HttpHeaders headers =  new HttpHeaders();
            String authKey = AUTHORIZATION_KEY;
            if(env != null){
                authKey =  env.getProperty("configuration.edc.authorizationKey", AUTHORIZATION_KEY);
            }
            headers.add(authKey, endpointData.getPayload().getDataAddress().getProperties().getAuthorization());
            ResponseEntity<?> response = httpUtil.doGet(dataPlaneEndpoint, String.class, headers, params, true, true);
            return jsonUtil.toJsonNode((String) response.getBody());
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getPassport",
                    e,
                    "It was not possible to get data with transfer id ["+endpointData.getId()+"]");
        }
    }

    /**
     * Parses the Transfer Data to a Passport from the given data plane endpoint.
     * <p>
     * @param   endpointData
     *          the {@code DataPlaneEndpoint} object with data plane endpoint data.
     *
     * @return  a {@code Passport} object parsed with transfer data.
     *
     * @throws  ServiceException
     *           if unable to parse the data to the passport.
     */
    public JsonNode getPassport(EndpointDataReference endpointData) {
        try {
            return jsonUtil.toJsonNode(this.getTransferData(endpointData));
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getPassport",
                    e,
                    "It was not possible to get and parse passport for transfer ["+endpointData.getId()+"]");
        }
    }
    /**
     * Parses the Transfer Data to a Passport from the given data plane endpoint string.
     * <p>
     * @param   endpointData
     *          the {@code DataPlaneEndpoint} object with data plane endpoint data.
     * @param   dataPlaneEndpoint
     *          the {@code String} is the data plane endpoint url
     *
     * @return  a {@code Passport} object parsed with transfer data.
     *
     * @throws  ServiceException
     *           if unable to parse the data to the passport.
     */
    public JsonNode getPassportFromEndpoint(EndpointDataReference endpointData, String dataPlaneEndpoint) {
        try {
            return this.getTransferDataFromEndpoint(endpointData, dataPlaneEndpoint);
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getPassport",
                    e,
                    "It was not possible to get and parse passport for transfer ["+endpointData.getId()+"]");
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
