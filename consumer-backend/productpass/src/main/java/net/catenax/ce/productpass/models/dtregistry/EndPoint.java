package net.catenax.ce.productpass.models.dtregistry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EndPoint {
    @JsonProperty("interface")
    String interfaceName;

    ProtocolInformation protocolInformation = new ProtocolInformation();

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public ProtocolInformation getProtocolInformation() {
        return protocolInformation;
    }

    public void setProtocolInformation(String endpointAddress, String endpointProtocol, String endpointProtocolVersion) {
        this.protocolInformation = new ProtocolInformation(endpointAddress, endpointProtocol, endpointProtocolVersion);
    }

    public static class ProtocolInformation{
        @JsonProperty("endpointAddress")
        String endpointAddress;

        @JsonProperty("endpointProtocol")
        String endpointProtocol;

        @JsonProperty("endpointProtocolVersion")
        String endpointProtocolVersion;

        public ProtocolInformation(String endpointAddress, String endpointProtocol, String endpointProtocolVersion) {
            this.endpointAddress = endpointAddress;
            this.endpointProtocol = endpointProtocol;
            this.endpointProtocolVersion = endpointProtocolVersion;
        }

        public ProtocolInformation() {
        }

        public String getEndpointAddress() {
            return endpointAddress;
        }

        public void setEndpointAddress(String endpointAddress) {
            this.endpointAddress = endpointAddress;
        }

        public String getEndpointProtocol() {
            return endpointProtocol;
        }

        public void setEndpointProtocol(String endpointProtocol) {
            this.endpointProtocol = endpointProtocol;
        }

        public String getEndpointProtocolVersion() {
            return endpointProtocolVersion;
        }

        public void setEndpointProtocolVersion(String endpointProtocolVersion) {
            this.endpointProtocolVersion = endpointProtocolVersion;
        }
    }
}
