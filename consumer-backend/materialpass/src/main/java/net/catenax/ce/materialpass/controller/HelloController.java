package net.catenax.ce.materialpass.controller;

import net.catenax.ce.materialpass.bean.AppConfig;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tools.logTools;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @Autowired
    private Environment env;

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);
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
    String secure(HttpServletRequest request){

        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal=(KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();

        LOGGER.info("username: {}", accessToken.getPreferredUsername());
        LOGGER.info("emailId: {}", accessToken.getEmail());
        LOGGER.info("lastname: {}",accessToken.getFamilyName());
        LOGGER.info("firstname: {}", accessToken.getGivenName());
        LOGGER.info("realmName: {}", accessToken.getIssuer());

        return "secured";
    }


    @GetMapping("/recycler")
    public String index1(){
        try {
            return "You are logged in as Recycler role | " + "This are the received roles " + env.getProperty("env.roles");
        }catch (Exception e) {
            return "[EXCEPTION]: " + e;
        }
    }

    @GetMapping("/oem")
    public String index2(){
        try {
            return "You are logged in as OEM role | " + "This are the received roles " + env.getProperty("env.roles");
        }catch (Exception e) {
            return "[EXCEPTION]: " + e;
        }
    }

    @GetMapping("/logout")
    String logout(HttpServletRequest httpServletRequest) throws Exception{
        httpServletRequest.logout();
        return "Logged out successfully!";
    }
    @GetMapping("/login")
    String login(){
        return "Log in please!";
    }
}
