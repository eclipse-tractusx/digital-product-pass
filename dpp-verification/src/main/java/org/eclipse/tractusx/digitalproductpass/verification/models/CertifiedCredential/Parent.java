package org.eclipse.tractusx.digitalproductpass.verification.models.CertifiedCredential;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Parent {

    /** ATTRIBUTES **/
    @JsonProperty("@id")
    String id;

    @JsonProperty("checksum")
    String checksum;

    /** CONSTRUCTOR(S) **/
    public Parent(String id, String checksum) {
        this.id = id;
        this.checksum = checksum;
    }

    public Parent() {
    }

    /** GETTERS AND SETTERS **/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}
