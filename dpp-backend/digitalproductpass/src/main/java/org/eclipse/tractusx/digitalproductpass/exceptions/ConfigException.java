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
 * This class consists exclusively to define methods to handle and log exceptions caused in the Configuration classes.
 **/
@SuppressWarnings("Unused")
public class ConfigException extends RuntimeException {

    /**
     * Logs the given configuration name and error message.
     * <p>
     * @param   configurationName
     *          the {@code String} configurationName name (e.g: the class name where the exception occurred).
     * @param   errorMessage
     *          the {@code String} error message.
     *
     */
    @SuppressWarnings("Unused")
    public ConfigException(String configurationName, String errorMessage) {
        super("["+configurationName+"] " + errorMessage);
        LogUtil.printException(this, "["+configurationName+"] " + errorMessage);
    }

    /**
     * Logs the given configuration name, the {@code Exception} object and the error message.
     * <p>
     * @param   configurationName
     *          the {@code String} configuration name (e.g: the class name where the exception occurred).
     * @param   e
     *          the {@code Exception} object thrown.
     * @param   errorMessage
     *          the {@code String} error message.
     *
     */
    @SuppressWarnings("Unused")
    public ConfigException(String configurationName, Exception e, String errorMessage) {
        super("["+configurationName+"] " + errorMessage+", "+e.getMessage());
        LogUtil.printException(this, "["+configurationName+"] " + errorMessage);
    }

}
