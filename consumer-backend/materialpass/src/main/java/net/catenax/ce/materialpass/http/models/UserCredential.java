package net.catenax.ce.materialpass.http.models;

public class UserCredential {

    private String username;
    private String password;
    private String credentialId;
    public UserCredential(){

    }
    public UserCredential(String username, String password, String credentialId) {
        this.username = username;
        this.password = password;
        this.credentialId = credentialId;
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
