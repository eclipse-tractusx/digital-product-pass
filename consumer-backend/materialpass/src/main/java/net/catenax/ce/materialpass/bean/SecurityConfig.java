package net.catenax.ce.materialpass.bean;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter
{
    private final KeycloakClientRequestFactory keycloakClientRequestFactory;

    public SecurityConfig(KeycloakClientRequestFactory keycloakClientRequestFactory)
    {
        this.keycloakClientRequestFactory = keycloakClientRequestFactory;
    }

    /**
     * define the actual constraints of the app.
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        super.configure(http);
        http
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
                .and()
                .authorizeRequests();
    }

    /**
     * define the session auth strategy so that no session is created
     *
     * @return concrete implementation of session authentication strategy
     */
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy()
    {
        return new NullAuthenticatedSessionStrategy();
    }

    /**
     * registers the Keycloakauthenticationprovider in spring context and sets its
     * mapping strategy for roles/authorities (mapping to spring seccurities'
     * default ROLE_... for authorities ).
     *
     * @param auth
     *          SecurityBuilder to build authentications and add details like
     *          authproviders etc.
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        KeycloakAuthenticationProvider keyCloakAuthProvider = keycloakAuthenticationProvider();
        keyCloakAuthProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());

        auth.authenticationProvider(keyCloakAuthProvider);
    }

    /**
     * Sets keycloaks config resolver to use springs application.properties
     * instead of keycloak.json (which is standard)
     *
     * @return
     */
    @Bean
    public KeycloakConfigResolver KeyCloakConfigResolver()
    {
        return new KeycloakSpringBootConfigResolver();
    }

    /**
     * Spring Boot attempts to eagerly register filter beans with the web
     * application context. Therefore, when running the Keycloak Spring Security
     * adapter in a Spring Boot environment, it may be necessary to add two
     * FilterRegistrationBeans to your security configuration to prevent the
     * Keycloak filters from being registered twice.
     *
     * @param filter
     * @return
     */
    @Bean
    public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
            KeycloakAuthenticationProcessingFilter filter)
    {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    /**
     * Spring Boot attempts to eagerly register filter beans with the web
     * application context. Therefore, when running the Keycloak Spring Security
     * adapter in a Spring Boot environment, it may be necessary to add two
     * FilterRegistrationBeans to your security configuration to prevent the
     * Keycloak filters from being registered twice.
     *
     * @param filter
     * @return
     */
    @Bean
    public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(KeycloakPreAuthActionsFilter filter)
    {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }
}
