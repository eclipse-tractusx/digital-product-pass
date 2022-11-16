package net.catenax.ce.materialpass.http.models;

public class UserCredential {

    private String username;
    private String password;
    private String credentialId;

    private JWTToken jwt = null;

    public UserCredential(){

    }
    public UserCredential(String username, String password, String credentialId) {
        this.username = username;
        this.password = password;
        this.credentialId = credentialId;
    }

    public UserCredential(String username, String password, String credentialId, JWTToken jwt) {
        this.username = username;
        this.password = password;
        this.credentialId = credentialId;
        this.jwt = jwt;
    }

    public UserCredential(JWTToken jwt){
        this.jwt = jwt;
    }

    public JWTToken getJwt() {
        return jwt;
    }

    public void setJwt(JWTToken jwt) {
        this.jwt = jwt;
    }
    public void setJWTToken(String accessToken, String refreshToken){
        this.jwt = new JWTToken(accessToken, refreshToken);
    }
    public void cleanJwtToken(){
        this.jwt = new JWTToken();
    }
    public void deleteJwt(){
        this.jwt = null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

}
