package org.eclipse.tractusx.digitalproductpass.verification.models.CertifiedCredential;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Proof {

    /** ATTRIBUTES **/
    @JsonProperty("type")
    String type;

    @JsonProperty("proofPurpose")
    String proofPurpose;

    @JsonProperty("verificationMethod")
    String verificationMethod;

    @JsonProperty("created")
    String created;

    @JsonProperty("jws")
    String jws;

    /** CONSTRUCTOR(S) **/
    public Proof(String type, String proofPurpose, String verificationMethod, String created, String jws) {
        this.type = type;
        this.proofPurpose = proofPurpose;
        this.verificationMethod = verificationMethod;
        this.created = created;
        this.jws = jws;
    }

    public Proof() {
    }

    /** GETTERS AND SETTERS **/
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProofPurpose() {
        return proofPurpose;
    }

    public void setProofPurpose(String proofPurpose) {
        this.proofPurpose = proofPurpose;
    }

    public String getVerificationMethod() {
        return verificationMethod;
    }

    public void setVerificationMethod(String verificationMethod) {
        this.verificationMethod = verificationMethod;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getJws() {
        return jws;
    }

    public void setJws(String jws) {
        this.jws = jws;
    }
}
