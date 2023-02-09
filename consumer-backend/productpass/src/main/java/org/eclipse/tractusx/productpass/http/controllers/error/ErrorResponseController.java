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

package org.eclipse.tractusx.productpass.http.controllers.error;

import io.swagger.v3.oas.annotations.Hidden;
import org.eclipse.tractusx.productpass.models.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import utils.HttpUtil;
import utils.LogUtil;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@Hidden     // hide this endpoint from api documentation  - swagger-ui
public class ErrorResponseController implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(value="/error",  method = {RequestMethod.GET})
    @ResponseBody
    public Response handleError(HttpServletRequest httpRequest) {
        ErrorAttributeOptions options = ErrorAttributeOptions
                .defaults()
                .including(ErrorAttributeOptions.Include.MESSAGE)
                ;
        ServletWebRequest servletWebRequest = new ServletWebRequest(httpRequest);
        Map<String, Object> errors = this.errorAttributes.getErrorAttributes(servletWebRequest, options);
        Response response = new Response().mapError(errors);
        String httpInfo = HttpUtil.getHttpInfo(httpRequest, response.getStatus());
        LogUtil.printHTTPMessage(httpInfo + " " + response.errorString());
        return response;
    }
}
