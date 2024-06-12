package org.eclipse.tractusx.digitalproductpass.verification.models.CertifiedCredential;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CredentialSubject {
    /** ATTRIBUTES **/
    @JsonProperty("id")
    String id;

    @JsonProperty("DigitalProductPassport")
    DigitalProductPassport digitalProductPassport;

    /** CONSTRUCTOR(S) **/
    public CredentialSubject(String id, DigitalProductPassport digitalProductPassport) {
        this.id = id;
        this.digitalProductPassport = digitalProductPassport;
    }

    public CredentialSubject() {
    }

    /** GETTERS AND SETTERS **/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DigitalProductPassport getDigitalProductPassport() {
        return digitalProductPassport;
    }

    public void setDigitalProductPassport(DigitalProductPassport digitalProductPassport) {
        this.digitalProductPassport = digitalProductPassport;
    }
}
