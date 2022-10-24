import { SERVER_URL } from "@/services/service.const";
import axios from "axios";

export default class wrapper {

  constructor(){
        
  }
  // Step 1: Request contract offers from the catalog
  getContractOfferCatalog(providerUrl) {
    //providerUrl = "https://materialpass.int.demo.catena-x.net/provider/api/v1/ids/data";
    return new Promise(resolve => {

      axios.get(`${SERVER_URL}/consumer/data/catalog?providerUrl=${providerUrl}`, 
        {
          'x-api-key': 'password'
          
        }
      )
        .then((response) => {
          resolve(response.data);
        })
        .catch((e) => {
          this.errors.push(e);
          resolve('rejected');
        });
    });
  }
  // Step 2: Negotiate a contract based on contract offer retrieved in previous step
  doContractNegotiation(contractOffer){

    return new Promise(resolve => {

      axios.post(`${SERVER_URL}/consumer/data/contractnegotiations`, contractOffer, {
        headers: {
          'X-Api-Key': 'password'
        }
      })
        .then((response) => {
          resolve(response.data);
        })
        .catch((e) => {
          this.errors.push(e);
          resolve('rejected');
        });
    });
  }
  // Step 3: Get the contract agreement id to verify if the negotiation is aprroved or declined
  getAgreementId(uuid){
    return new Promise(resolve => {

      axios.get(`${SERVER_URL}/consumer/data/contractnegotiations/${uuid}`, {
        headers: {
          'X-Api-Key': 'password',
          "accept": "application/json"
        }})
        .then((response) => {
          console.log('check_state : ' + response.data.state);
          console.log('Agreement Id: ' + response.data.contractAgreementId);
          console.log('Agreement state: ' + response.data.state);
          resolve(response.data);
        })
        .catch((e) => {
          this.errors.push(e);
          alert(e);
          resolve('rejected');
        });
    });
  }
  // Step 4.1: Initiate data transfer process based on agreement id from previous step
  initiateTransfer(contractAgreementId, assetId, payload){

    return new Promise(resolve => {

      var requestBody = {
        "id": payload.TransferProcessId,
        "connectorId": payload.connectorId,
        "connectorAddress": payload.connectorAddress,
        "contractId": contractAgreementId,
        "assetId": assetId,
        "managedResources": false,
        "dataDestination": {
          "type": payload.type
        },
        };
        console.log(requestBody);
        axios.post(`${SERVER_URL}/consumer/data/transferprocess`, requestBody, {
        headers: {
          'X-Api-Key': 'password'
        }
      })
        .then((response) => {
          console.log(response.data);
          resolve(response.data);
        })
        .catch((e) => {
          this.errors.push(e);
          resolve('rejected');
        });
    });     
  }
  // Step 4.2: Verify data transfer status
  getTransferProcessById(transferId){

    return new Promise(resolve => {
      axios.get(`${SERVER_URL}/consumer/data/transferprocess/${transferId}`, {
        headers: {
          'X-Api-Key': 'password'
        }
      })
        .then((response) => {
          console.log(response.data);
          resolve(response.data);
        })
        .catch((e) => {
          this.errors.push(e);
          resolve('rejected');
        });
    });     
  }
  // Step 4.3: Query transferred data from consumer backend system 
  getDataFromConsumerBackend(transferProcessId){

    return new Promise(resolve => {
      
      axios.get(`${SERVER_URL}/consumer_backend/${transferProcessId}`, {
        headers: {
          'Accept': 'application/octet-stream'
        }
      })
        .then((response) => {
          console.log(response.data);
          resolve(response.data);
        })
        .catch((e) => {
          this.errors.push(e);
          resolve('rejected');
        });
    }); 
  }
    
  async performEDCDataTransfer(assetId, providerEndpoint) {
    var contractId = "";
    //var assetId_ = "";
    var response = await this.getContractOfferCatalog(providerEndpoint);
    var offer = response;
      
    var offer = response.contractOffers.filter(offer => {
      return offer.asset.id.includes(assetId);
    });
    console.log(offer[0]);
    var payload = {
      "connectorId": "foo",
      "connectorAddress": "https://materialpass.int.demo.catena-x.net/provider/api/v1/ids/data",
      "offer": {
        "offerId": "101:foo",
        "assetId": "101",
        "policy": {
          "uid": "null",
          "prohibitions": [],
          "obligations": [],
          "permissions": [
            {
              "edctype": "dataspaceconnector:permission",
              "action": {
                "type": "USE"
              },
              "target": "101",
              "constraints": []
            }
          ]
        }
      }
    };
    console.log(payload);
    var negotiationId = await this.doContractNegotiation(payload);
    console.log(negotiationId);


    // Check agreement status //
    // Status: INITIAL, REQUESTED, CONFIRMED
    // first call to get the initial status 
    var data = await this.getAgreementId(negotiationId.id);
    if (data == "rejected")
      return;
    else
      contractId = data.contractAgreementId;
        
        
    // Check the agreement status until it is of status CONFIRMED
      while (data.state != "CONFIRMED") {
        data = await this.getAgreementId(negotiationId.id);        
      console.log("Agreement state:  ", data.state + '_' + data.contractAgreementId);
      contractId = data.contractAgreementId;
    }
    console.log(contractId);

    // initiate data transfer
    var requestBody = {
        TransferProcessId: "1118",
        connectorId: "foo",
      connectorAddress: "https://materialpass.int.demo.catena-x.net/provider/api/v1/ids/data",
      contractAgreementId: contractId,
      assetId: "101",
      type: "HttpProxy"
    };
    var response1 = await this.initiateTransfer(contractId, assetId, requestBody);
    console.log(response1.id);
    var transferProcess = this.getTransferProcessById(response1.id);
    //console.log("transfer id" + transferProcess.id);
        
    // query transferred data from consumer backend
    return await this.getDataFromConsumerBackend(requestBody.TransferProcessId);
  }
}
