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

package org.eclipse.tractusx.digitalproductpass.http.middleware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import utils.HttpUtil;
import utils.LogUtil;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class BaseInterceptor implements HandlerInterceptor {

    @Autowired
    HttpUtil httpUtil;

    @SuppressWarnings("Unused")
    @Override
    public boolean preHandle(
            HttpServletRequest httpRequest, HttpServletResponse httpResponse, Object handler) throws Exception {
        if(!httpRequest.getRequestURI().equals("/error")) {
            String httpInfo = httpUtil.getHttpInfo(httpRequest, httpResponse.getStatus());
            LogUtil.printHTTPMessage(httpInfo);
        }
        return true;
    }

    @SuppressWarnings("Unused")
    @Override
    public void postHandle(

            HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        //[NOTE]: Post Handle not implemented!
    }

    @SuppressWarnings("Unused")
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception exception) throws Exception{
        //[NOTE]: After Completion not implemented!
    }
}
