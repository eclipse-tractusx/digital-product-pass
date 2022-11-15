package net.catenax.ce.materialpass.http.controllers;

import net.catenax.ce.materialpass.http.models.KeycloakCredential;
import net.catenax.ce.materialpass.http.models.Response;
import net.catenax.ce.materialpass.http.models.UserCredential;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tools.httpTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@RestController
public class AuthController {
    // [Logic Methods] ----------------------------------------------------------------

    private Response loginFromHttpRequest(HttpServletRequest httpRequest, UserCredential userCredential){
        Response response = httpTools.getResponse();
        KeycloakCredential keycloakCredential = new KeycloakCredential();

        keycloakCredential.mapKeycloakResponse(httpRequest);

        keycloakCredential.setUserCredential(userCredential);
        httpTools.getParamOrDefault(httpRequest, "session_code", null);
        if(keycloakCredential.getUserCredential().getUsername() != null){

            response.message = "It works!";
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
    String login(HttpServletRequest httpRequest, @RequestBody UserCredential userCredential) throws Exception{
        Response httpResponse = loginFromHttpRequest(httpRequest, userCredential);
        return httpResponse.message;
    }

}
