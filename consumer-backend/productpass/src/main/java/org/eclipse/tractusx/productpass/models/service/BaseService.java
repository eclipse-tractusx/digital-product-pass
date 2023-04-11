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

package org.eclipse.tractusx.productpass.models.service;

import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.interfaces.ServiceInitializationInterface;

import java.util.List;

public abstract class BaseService implements ServiceInitializationInterface {

    public void checkEmptyVariables() throws ServiceInitializationException {
        List<String> missingVariables = this.getEmptyVariables(); //Check for variables
        if(!missingVariables.isEmpty()){ // Return exception if one is missing
            throw new ServiceInitializationException(this.getClass().getName()+".checkEmptyVariables", "This mandatory variables "+missingVariables+" are not set!");
        };
    }
    public void checkEmptyVariables(List<String> initializationOptionalVariables) throws ServiceInitializationException {
        List<String> missingVariables = this.getEmptyVariables(); //Check for variables
        if((!initializationOptionalVariables.equals(missingVariables)) && (!missingVariables.isEmpty())){ // Return exception if one is missing and is not optional
            throw new ServiceInitializationException(this.getClass().getName()+".checkEmptyVariables", "This mandatory variables "+missingVariables+" are not set!");
        };
    }

    public abstract List<String> getEmptyVariables(); // Return list of missing variables configured in service
}
