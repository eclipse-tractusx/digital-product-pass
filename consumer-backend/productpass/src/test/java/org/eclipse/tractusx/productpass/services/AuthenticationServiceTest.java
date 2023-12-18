package org.eclipse.tractusx.productpass.services;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.models.auth.JwtToken;
import org.eclipse.tractusx.productpass.models.auth.UserInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import utils.*;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private final String mockedTokenPath = "/src/test/resources/dpp/token/MockedToken.json";
    private final String mockedUserInfoPath = "/src/test/resources/dpp/token/MockedUserInfo.json";
    private JwtToken mockedToken;
    private UserInfo mockedUserInfo;
    @Mock
    private VaultService vaultService;
    @Mock
    private Environment env;
    private HttpUtil httpUtil;
    private JsonUtil jsonUtil;
    private YamlUtil yamlUtil;
    private FileUtil fileUtil;
    @Mock
    private HttpServletRequest httpRequest;

    @BeforeAll
    void setUpAll() throws ServiceInitializationException {
        MockitoAnnotations.openMocks(this);
        httpUtil = new HttpUtil(env);
        fileUtil = new FileUtil();
        jsonUtil = new JsonUtil(fileUtil);
        yamlUtil = new YamlUtil(fileUtil);

        Map<String, Object> application = yamlUtil.readFile(FileUtil.getWorkdirPath() + "/src/main/resources/application.yml");
        Map<String, Object> configuration = (Map<String, Object>) jsonUtil.toMap(application.get("configuration"));
        Map<String, Object> keycloak = (Map<String, Object>) jsonUtil.toMap(configuration.get("keycloak"));
        when(env.getProperty("configuration.keycloak.tokenUri", String.class,  "")).thenReturn(keycloak.get("tokenUri").toString());
        when(env.getProperty("configuration.keycloak.userInfoUri", String.class, "")).thenReturn(keycloak.get("userInfoUri").toString());
        authenticationService = Mockito.spy(new AuthenticationService(vaultService, env, httpUtil, jsonUtil));

        mockedToken = (JwtToken) jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + mockedTokenPath, JwtToken.class);
        mockedUserInfo = (UserInfo) jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + mockedUserInfoPath, UserInfo.class);
        doReturn(mockedToken).when(authenticationService).getToken();
        doReturn(mockedUserInfo).when(authenticationService).getUserInfo(mockedToken.getAccessToken());
    }

    @BeforeEach
    void setUp() {
        Map<String, Object> vaultConfig = yamlUtil.readFile(fileUtil.getDataDir() + "/VaultConfig/vault.token.yml");
        Map<String, Object> client = (Map<String, Object>) jsonUtil.toMap(vaultConfig.get("client"));

        when(vaultService.getLocalSecret("client.id")).thenReturn(client.get("id").toString());
        when(vaultService.getLocalSecret("client.secret")).thenReturn(client.get("secret").toString());
    }


    @Test
    void getToken() {

        JwtToken jwtToken = authenticationService.getToken();

        assertNotNull(jwtToken);
        assertNotNull(jwtToken.getAccessToken());
        assertFalse(jwtToken.getAccessToken().isEmpty());
    }

    @Test
    void isAuthenticated() {
        JwtToken jwtToken = authenticationService.getToken();

        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken.getAccessToken());

        assertTrue(authenticationService.isAuthenticated(httpRequest));
    }

    @Test
    void getUserInfo() {
        JwtToken jwtToken = authenticationService.getToken();

        UserInfo userInfo = authenticationService.getUserInfo(jwtToken.getAccessToken());

        assertNotNull(userInfo);
    }
}
