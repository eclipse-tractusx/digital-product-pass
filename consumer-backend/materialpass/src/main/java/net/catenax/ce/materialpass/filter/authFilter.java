/**********************************************************
 *
 * Catena-X - Material Passport Consumer Backend
 *
 * Copyright (c) 2022: CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2022 Contributors to the CatenaX (ng) GitHub Organisation.
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
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 **********************************************************/

package net.catenax.ce.materialpass.filter;

import javax.servlet.http.HttpServletRequest;

public class authFilter {

    private HttpServletRequest request;

/*    public void filter(ContainerRequestContext ctx) throws IOException {

        final String requestTokenHeader = ctx.getHeaderString(HttpHeaders.AUTHORIZATION);
        String name = request.getUserPrincipal().getName();
        String authenticatedUser = OIDCRequestUtil.getAuthenticatedUser(request).get().toString();
        System.out.println("Logged in user: "+ name);
        System.out.println("Authenticated user: "+ authenticatedUser);

        AuthenticatedUser authenticated_User = OIDCRequestUtil.getAuthenticatedUser(request).get();
        List<String> eg = authenticated_User.getEntitlementGroups();

        if (securityContext != null && securityContext.getUserPrincipal() instanceof KeycloakPrincipal) {
            KeycloakPrincipal principal = ((KeycloakPrincipal) securityContext.getUserPrincipal());
            AccessToken token = principal.getKeycloakSecurityContext().getToken();
            System.out.println("User logged in: " + token.getPreferredUsername());
        } else {
            System.out.println("Unable to access Keycloak security context.");
        }

    }*/

}
