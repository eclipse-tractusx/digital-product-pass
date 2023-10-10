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

package org.eclipse.tractusx.productpass.services;

import org.apache.juli.logging.Log;
import org.eclipse.tractusx.productpass.exceptions.ServiceException;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.models.edc.DataPlaneEndpoint;
import org.eclipse.tractusx.productpass.models.passports.DigitalProductPassport;
import org.eclipse.tractusx.productpass.models.passports.Passport;
import org.eclipse.tractusx.productpass.models.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DataPlaneService extends BaseService {

    @Autowired
    HttpUtil httpUtil;

    @Autowired
    JsonUtil jsonUtil;

    public DataPlaneService() throws ServiceInitializationException {
        this.checkEmptyVariables();
    }
    public Object getTransferData(DataPlaneEndpoint endpointData) {
        try {
            Map<String, Object> params = httpUtil.getParams();
            HttpHeaders headers =  new HttpHeaders();
            headers.add(endpointData.getAuthKey(), endpointData.getAuthCode());
            ResponseEntity<?> response = httpUtil.doGet(endpointData.getEndpoint(), Object.class, headers, params, true, true);
            return response.getBody();
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getTransferData",
                    e,
                    "It was not possible to get transfer from transfer id ["+endpointData.getId()+"]");
        }
    }
    public Passport getPassport(String semanticId, DataPlaneEndpoint endpointData) {
        try {
            String aspectName = CatenaXUtil.getAspectNameFromSemanticId(semanticId); // Get aspect name from semantic Id
            String packagePath = this.getClass().getPackageName().replace("services", "models.passports");
            Class<?> passportClass = ReflectionUtil.instanceClass(packagePath, aspectName);
            return (Passport) jsonUtil.bindObject(this.getTransferData(endpointData), passportClass);
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getPassport",
                    e,
                    "It was not possible to get and parse passport for transfer ["+endpointData.getId()+"]");
        }
    }

    @Override
    public List<String> getEmptyVariables() {
        return new ArrayList<>();
    }
}
