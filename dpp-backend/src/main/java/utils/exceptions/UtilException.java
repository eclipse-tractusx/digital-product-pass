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

package utils.exceptions;

/**
 * This Exception class consists exclusively to treat Runtime Exceptions thrown in the Utils class.
 *
 * <p> The methods defined here are intended to gather information like the Class that thrown the exception, the message error and the exception for logging.
 *
 */
public class UtilException extends RuntimeException  {

    /**
     * Logs the given util class name and error message.
     * <p>
     * @param   tool
     *          the {@code Class} util class (e.g: JsonUtil, LogUtil, CatenaXUtil, etc.).
     * @param   errorMessage
     *          the {@code String} error message.
     *
     */
    public UtilException(Class tool, String errorMessage) {
        super("["+tool.getName()+"] " + errorMessage);
    }

    /**
     * Logs the given util class name, the {@code Exception} object and the error message.
     * <p>
     * @param   tool
     *          the {@code Class} util class (e.g: JsonUtil, LogUtil, CatenaXUtil, etc.).
     * @param   e
     *          the {@code Exception} object thrown.
     * @param   errorMessage
     *          the {@code String} error message.
     *
     */
    public UtilException(Class tool, Exception e, String errorMessage) {
        super("["+tool.getName()+"] " + errorMessage+", "+e.getMessage());
    }
}
