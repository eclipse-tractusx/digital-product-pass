package org.eclipse.tractusx.digitalproductpass.verification.models.CertifiedCredential;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class consists exclusively to define attributes related to the Verifiable Credential.
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifiableCredential {

    /** ATTRIBUTES **/
    @JsonProperty("@context")
    Context context;

    @JsonProperty("type")
    Type type;
    @JsonProperty("issuer")
    String issuer;
    @JsonProperty("parent")
    Parent parent;
    @JsonProperty("semanticId")
    String semanticId;
    @JsonProperty("credentialSubject")
    CredentialSubject credentialSubject;
    @JsonProperty("id")
    String id;
    @JsonProperty("validFrom")
    String validFrom;
    @JsonProperty("validUntil")
    String validUntil;
    @JsonProperty("proof")
    Proof proof;

    /** CONSTRUCTOR(S) **/
    public VerifiableCredential(Context context, Type type, String issuer, Parent parent, String semanticId, CredentialSubject credentialSubject, String id, String validFrom, String validUntil, Proof proof) {
        this.context = context;
        this.type = type;
        this.issuer = issuer;
        this.parent = parent;
        this.semanticId = semanticId;
        this.credentialSubject = credentialSubject;
        this.id = id;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.proof = proof;
    }

    public VerifiableCredential() {
    }

    /** GETTERS AND SETTERS **/
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public String getSemanticId() {
        return semanticId;
    }

    public void setSemanticId(String semanticId) {
        this.semanticId = semanticId;
    }

    public CredentialSubject getCredentialSubject() {
        return credentialSubject;
    }

    public void setCredentialSubject(CredentialSubject credentialSubject) {
        this.credentialSubject = credentialSubject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public Proof getProof() {
        return proof;
    }

    public void setProof(Proof proof) {
        this.proof = proof;
    }
}

