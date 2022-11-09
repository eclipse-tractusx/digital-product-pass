package net.catenax.ce.materialpass.filter;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

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
