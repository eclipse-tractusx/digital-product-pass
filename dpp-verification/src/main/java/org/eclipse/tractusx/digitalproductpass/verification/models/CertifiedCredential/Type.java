package org.eclipse.tractusx.digitalproductpass.verification.models.CertifiedCredential;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Type {
    /** ATTRIBUTES **/
    @JsonProperty("type")
    String type;

    /** CONSTRUCTOR(S) **/
    public Type(String type) {
        this.type = type;
    }

    public Type() {
    }
    /** GETTERS AND SETTERS **/
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
