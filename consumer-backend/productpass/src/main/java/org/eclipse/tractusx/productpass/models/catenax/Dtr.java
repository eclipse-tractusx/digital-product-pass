package org.eclipse.tractusx.productpass.models.catenax;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dtr {
    @JsonProperty("bpn")
    private String bpn;
    @JsonProperty("connectorEndpoint")
    private List<String> connectorEndpoint;

    public Dtr(String bpn, List<String> connectorEndpoint) {
        this.bpn = bpn;
        this.connectorEndpoint = connectorEndpoint;
    }

    public Dtr() {
    }

    public String getBpn() {
        return bpn;
    }

    public void setBpn(String bpn) {
        this.bpn = bpn;
    }

    public List<String> getConnectorEndpoint() {
        return connectorEndpoint;
    }

    public void setConnectorEndpoint(List<String> connectorEndpoint) {
        this.connectorEndpoint = connectorEndpoint;
    }
}
