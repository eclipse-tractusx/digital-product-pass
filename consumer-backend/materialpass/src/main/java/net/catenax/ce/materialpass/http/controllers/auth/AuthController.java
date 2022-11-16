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

package net.catenax.ce.materialpass.http.controllers.auth;

import net.catenax.ce.materialpass.http.models.KeycloakCredential;
import net.catenax.ce.materialpass.http.models.Response;
import net.catenax.ce.materialpass.http.models.UserCredential;
import org.keycloak.representations.AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.httpTools;
import tools.jsonTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@RestController
public class AuthController {
    // [Logic Methods] ----------------------------------------------------------------

    private Response loginFromHttpRequest(HttpServletRequest httpRequest){
        Response response = httpTools.getResponse();
        Set<String> roles = httpTools.getCurrentUserRoles(httpRequest);
        if(roles == null){
            response.message = "You have no assigned roles!";
        }else {
            response.message = "You are logged with this roles: " + roles.toString();
            AccessToken accessToken = httpTools.getCurrentUser(httpRequest);
            if(!httpTools.isInSession(httpRequest, "user")){

                // TODO: Get client credentials from hashiCorpVault
                KeycloakCredential keycloakCredential = new KeycloakCredential(
                        new UserCredential(
                                accessToken.getPreferredUsername(),
                                accessToken.getSubject(),
                                ""
                        )
                );
                httpTools.setSessionValue(httpRequest, "keycloakCredential",keycloakCredential);
            }
            KeycloakCredential currentKeycloakCredential = (KeycloakCredential) httpTools.getSessionValue(httpRequest, "keycloakCredential");
            currentKeycloakCredential.setClient_id(accessToken.getIssuedFor());

            response.data = jsonTools.getObjectArray(
                    currentKeycloakCredential,
                    accessToken
            );
        }
        return response;

    }

    // [API Services]  ----------------------------------------------------------------
    /*
    Map<String, String> asset = new HashMap<>();

    @GetMapping("/hello")
    public String index(){
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("id") String assetId) {
        if (!asset.containsKey(assetId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The asset id was not found in database");
        }

        System.out.println("Returning data for contract offer " + assetId);

        return asset.get(assetId);
    }

    @PostMapping("/{id}")
    public void store(@PathVariable("id") String assetId, @RequestBody String data) {
        System.out.println("Saving data for asset " + assetId);

        asset.put(assetId, data);
    }
     */


@GetMapping("/recycler")
    public String index1(HttpServletRequest httpRequest){
        try {
            Set<String> roles = httpTools.getCurrentUserRoles(httpRequest);
            if(roles == null){
                return "User roles is null!";
            }
            return "You are logged in as Recycler role | " + "This are the received roles " + roles.toString();
        }catch (Exception e) {
            return "[EXCEPTION]: " + e;
        }
    }

    @GetMapping("/oem")
    public String index2(HttpServletRequest httpRequest){
        try {
            Set<String> roles = httpTools.getCurrentUserRoles(httpRequest);
            if(roles == null){
                return "User roles is null!";
            }
            return "You are logged in as OEM role | " + "This are the received roles " + roles;
        }catch (Exception e) {
            return "[EXCEPTION]: " + e;
        }
    }

    @GetMapping("/logout")
    String logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception{
        httpRequest.logout();
        httpResponse.sendRedirect(httpRequest.getContextPath());
        return "Logged out successfully!";
    }

    @GetMapping("/login")
    Response login(HttpServletRequest httpRequest) throws Exception{
        return loginFromHttpRequest(httpRequest);
    }

}
