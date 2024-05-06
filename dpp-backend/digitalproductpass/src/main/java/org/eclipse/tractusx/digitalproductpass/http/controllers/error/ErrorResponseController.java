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

package org.eclipse.tractusx.digitalproductpass.http.controllers.error;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.tractusx.digitalproductpass.models.http.Response;
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

import java.util.Map;

/**
 * This class consists exclusively to define the HTTP methods to handle errors.
 **/
@Controller
@Hidden     // hide this endpoint from api documentation  - swagger-ui
public class ErrorResponseController implements ErrorController {

    /** ATTRIBUTES **/
    @Autowired
    private ErrorAttributes errorAttributes;
    @Autowired
    HttpUtil httpUtil;

    /** METHODS **/

    /**
     * HTTP POST method to get the Digital Twin for the given processId and endpointId in the URL.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     * @param   httpResponse
     *          the HTTP response.
     *
     * @return this {@code Response} HTTP response with the error message.
     *
     */
    @RequestMapping(value="/error",  method = {RequestMethod.GET})
    @ResponseBody
    public Response handleError(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        ErrorAttributeOptions options = ErrorAttributeOptions
                .defaults()
                .including(ErrorAttributeOptions.Include.MESSAGE)
                ;
        ServletWebRequest servletWebRequest = new ServletWebRequest(httpRequest);
        Map<String, Object> errors = this.errorAttributes.getErrorAttributes(servletWebRequest, options);
        Response response = new Response().mapError(errors);
        if(response.message.equals("No message available")) {
            response.message = null;
        }
        String httpInfo = httpUtil.getHttpInfo(httpRequest, response.getStatus());
        if(errors.containsKey("path") && !errors.get("path").equals("/passport/not-found")){
            // Redirect to error page
            String currentHost = httpUtil.getCurrentHost(httpRequest);
            String errorUrl = String.join("/",currentHost, "passport/not-found");
            httpUtil.redirect(httpResponse,errorUrl);
        }
        LogUtil.printHTTPMessage(httpInfo + " " + response.errorString());
        return response;
    }
}
