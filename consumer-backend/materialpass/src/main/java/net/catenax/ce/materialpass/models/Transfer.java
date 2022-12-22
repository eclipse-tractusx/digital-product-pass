package net.catenax.ce.materialpass.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transfer {
    @JsonProperty("id")
    String id;
    @JsonProperty("createdAt")
    String createdAt;
    @JsonProperty("properties")
    JsonNode properties;;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public JsonNode getProperties() {
        return properties;
    }

    public void setProperties(JsonNode properties) {
        this.properties = properties;
    }
}
/*{
        "createdAt": 1670331155010,
        "updatedAt": 1670331195645,
        "contractAgreementId": null,
        "counterPartyAddress": "https://materialpass.int.demo.catena-x.net/provider/api/v1/ids/data",
        "errorDetail": "Retry limited exceeded: Received class de.fraunhofer.iais.eis.RejectionMessageImpl but expected [class de.fraunhofer.iais.eis.RequestInProcessMessageImpl].",
        "id": "86ad5ccf-d759-4602-a24c-02ddd26472e1",
        "protocol": "ids-multipart",
        "state": "ERROR",
        "type": "CONSUMER"
        }
        {
        "createdAt": 1668157971617,
        "updatedAt": 1668157980874,
        "id": "7ff1c26c-367c-4277-aa2a-7f47c93849c3",
        "type": "CONSUMER",
        "state": "COMPLETED",
        "stateTimestamp": 1668157980874,
        "errorDetail": null,
        "dataRequest": {
        "assetId": "1",
        "contractId": "1:ff7e8eaa-91d1-4b69-b43a-aee095909c84",
        "connectorId": "foo"
        },
        "dataDestination": {
        "properties": {
        "type": "HttpProxy"
        }
        }
        }
        {
        "id": "{{transferProcessId}}",
        "connectorId": "foo",
        "connectorAddress": "https://materialpass.int.demo.catena-x.net/provider/api/v1/ids/data",
        "contractId": "{{contractAgreementId}}",
        "assetId": "{{assetId}}",
        "managedResources": "false",
        "dataDestination": {
        "type": "HttpProxy"
        }
        }
*/
