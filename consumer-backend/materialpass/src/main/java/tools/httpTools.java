package tools;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
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
    public static KeycloakAuthenticationToken getCurrentUserAuthToken(HttpServletRequest request){
        return (KeycloakAuthenticationToken) request.getUserPrincipal();// Get the user data from the request
    }
    public static KeycloakPrincipal getCurrentUserPrincipal(HttpServletRequest request){
        KeycloakAuthenticationToken token = httpTools.getCurrentUserAuthToken(request); // Get the auth token to access the session
        if (token == null) {
            logTools.printError("User not authenticated, token is null!");
            return null;
        }
        return (KeycloakPrincipal) token.getPrincipal();
    }
    public static KeycloakSecurityContext getCurrentUserSession(HttpServletRequest request){
        KeycloakPrincipal principal = httpTools.getCurrentUserPrincipal(request); // Get the principal to access the session
        if (principal == null) {
            logTools.printError("User not authenticated, principal is null!");
            return null;
        }
        return principal.getKeycloakSecurityContext();
    }
}
