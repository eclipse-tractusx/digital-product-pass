package tools;

import net.catenax.ce.materialpass.http.models.Response;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public final class httpTools {
    public static Set<String> getCurrentUserRoles(HttpServletRequest request){
        AccessToken user = httpTools.getCurrentUser(request); // Get user from request
        if(user == null) {
            logTools.printError("User is not authenticated!");
            return null;
        }
        return user.getRealmAccess().getRoles(); // Get roles from user
    }
    public static AccessToken getCurrentUser(HttpServletRequest request){
        KeycloakSecurityContext session = httpTools.getCurrentUserSession(request); // Get the session from the request
        if (session == null) {
            logTools.printError("Session does not exists!");
            return null;
        }
        return session.getToken(); // Return user info
    }
    public static KeycloakPrincipal getCurrentUserPrincipal(HttpServletRequest request){
        return (KeycloakPrincipal) request.getUserPrincipal();// Get the user data from the request
    }
    public static KeycloakSecurityContext getCurrentUserSession(HttpServletRequest request){
        KeycloakPrincipal principal = httpTools.getCurrentUserPrincipal(request); // Get the principal to access the session
        if (principal == null) {
            logTools.printError("User not authenticated, principal is null!");
            return null;
        }
        return principal.getKeycloakSecurityContext();
    }

    public static String getHttpInfo(HttpServletRequest httpRequest, Integer status){
        return "[" + httpRequest.getProtocol() + " " + httpRequest.getMethod() + "] " + status + ": " + httpRequest.getRequestURI();
    }
    public static Response getResponse(){
        return new Response(
                "",
                200,
                "Success"
        );
    }

    public static String getParamOrDefault(HttpServletRequest httpRequest, String param, String defaultPattern){
        String requestParam =  httpRequest.getParameter(param);
        if(requestParam == null) {
            return defaultPattern;
        }
        return requestParam;
    }
}
