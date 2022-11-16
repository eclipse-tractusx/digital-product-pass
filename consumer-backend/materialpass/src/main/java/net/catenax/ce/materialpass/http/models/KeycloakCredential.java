package net.catenax.ce.materialpass.http.models;

import tools.httpTools;

import javax.servlet.http.HttpServletRequest;

public class KeycloakCredential {
    private String session_code;
    private String execution;
    private String client_id;
    private String tab_id;
    private UserCredential userCredential;

    public KeycloakCredential(){

    }
    public KeycloakCredential(String session_code, String execution, String client_id, String tab_id, String username, String password, String credentialId ) {
        this.session_code = session_code;
        this.execution = execution;
        this.client_id = client_id;
        this.tab_id = tab_id;
        this.userCredential = new UserCredential(username, password, credentialId);
    }
    public KeycloakCredential(String session_code, String execution, String client_id, String tab_id, UserCredential UserCredential) {
        this.session_code = session_code;
        this.execution = execution;
        this.client_id = client_id;
        this.tab_id = tab_id;
        this.userCredential = new UserCredential();
    }
    public KeycloakCredential(String session_code, String execution, String client_id, String tab_id) {
        this.session_code = session_code;
        this.execution = execution;
        this.client_id = client_id;
        this.tab_id = tab_id;
    }

    public void mapKeycloakResponse(HttpServletRequest request){
        this.session_code = httpTools.getParamOrDefault(request, "session_code", null);
        this.execution = httpTools.getParamOrDefault(request, "execution", null);
        this.client_id = httpTools.getParamOrDefault(request, "client_id", null);
        this.tab_id = httpTools.getParamOrDefault(request, "tab_id", null);
    }
    public String getSession_code() {
        return  session_code;
    }

    public void setSession_code(String session_code) {
        this.session_code = session_code;
    }

    public String getExecution() {
        return execution;
    }

    public void setExecution(String execution) {
        this.execution = execution;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getTab_id() {
        return tab_id;
    }

    public void setTab_id(String tab_id) {
        this.tab_id = tab_id;
    }

    public UserCredential getUserCredential() {
        return userCredential;
    }

    public void setUserCredential(UserCredential userCredential) {
        this.userCredential = userCredential;
    }
}
