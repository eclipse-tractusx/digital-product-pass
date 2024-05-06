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

package org.eclipse.tractusx.digitalproductpass.exceptions;

import utils.LogUtil;


/**
 * This class consists exclusively to define methods to handle and log exceptions caused in the Data Model classes.
 **/
public class DataModelException extends RuntimeException{

    /**
     * Logs the given data model name and error message.
     * <p>
     * @param   dataModelName
     *          the {@code String} data model name (e.g: the class name where the exception occurred).
     * @param   errorMessage
     *          the {@code String} error message.
     *
     */
    public DataModelException(String dataModelName, String errorMessage) {
        super("["+dataModelName+"] " + errorMessage);
        LogUtil.printException(this, "["+dataModelName+"] " + errorMessage);
    }

    /**
     * Logs the given data model name, the {@code Exception} object and the error message.
     * <p>
     * @param   dataModelName
     *          the {@code String} data model name (e.g: the class name where the exception occurred).
     * @param   e
     *          the {@code Exception} object thrown.
     * @param   errorMessage
     *          the {@code String} error message.
     *
     */
    public DataModelException(String dataModelName, Exception e, String errorMessage) {
        super("["+dataModelName+"] " + errorMessage+", "+e.getMessage());
        LogUtil.printException(this, "["+dataModelName+"] " + errorMessage);
    }

}
