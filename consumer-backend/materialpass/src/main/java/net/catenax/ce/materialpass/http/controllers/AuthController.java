package net.catenax.ce.materialpass.http.controllers;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tools.httpTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class AuthController {

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

    @GetMapping("/secured")
    AccessToken secure(HttpServletRequest request){

        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal=(KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();

        return accessToken;
    }


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
    String login(HttpServletRequest httpRequest) throws Exception{
        return "Logged in successfully!";
    }

}
