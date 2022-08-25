import { BASE_URL } from "@/services/service.const"
import axios from "axios";

export default class wrapper {

    constructor(){
        
    }
    // Step 1: Request contract offers from the catalog
    getContractOfferCatalog(providerUrl) {
        //providerUrl = "https://materialpass.int.demo.catena-x.net/consumer/api/v1/ids/data";
        return new Promise(resolve => {

        axios.post(`${BASE_URL}/consumer/controlplane/data/catalog?providerUrl=${providerUrl}`, 
            {
                'X-Api-Key': 'password'
            }
        )
            .then((response) => {
            resolve(response.data);
            })
            .catch((e) => {
            this.errors.push(e)
            resolve('rejected');
            });
        });
    }
    // Step 2: Negotiate a contract based on contract offer retrieved in previous step
    doContractNegotiation(contractOffer){

        return new Promise(resolve => {

            axios.post(`${BASE_URL}/consumer/controlplane/data/contractnegotiations`, contractOffer, {
            headers: {
                'X-Api-Key': 'password'
                }
            })
            .then((response) => {
            resolve(response.data);
            })
            .catch((e) => {
            this.errors.push(e)
            resolve('rejected');
            });
        });
    }
    // Step 3: Get the contract agreement id to verify successful negotiation
    getAgreementId(uuid){
        return new Promise(resolve => {

        axios.get(`${BASE_URL}/consumer/controlplane/data/contractnegotiations/${uuid}`, {
            headers: {
                'X-Api-Key': 'password',
                "accept": "application/json"
            }})
            .then((response) => {
            console.log('check_state : ' + response.data.state)
            console.log('Agreement Id: ' + response.data.contractAgreementId)
            console.log('Agreement state: ' + response.data.state)
            resolve(response.data);
            })
            .catch((e) => {
            this.errors.push(e)
            alert(e)
            resolve('rejected');
            });
        })
    }
    // Step 4.1: Initiate data transfer process based on agreement id from previous step
    initiateTransfer(contractAgreementId, assetId){

    return new Promise(resolve => {

        var payload = {
            "id": "TransferProcessId",
            "connectorId": "foo",
            "connectorAddress": "https://.../api/v1/ids/data",
            "contractId": contractAgreementId,
            "assetId": assetId,
            "managedResources": false,
            "dataDestination": {
                "type": "HttpProxy"
            },
        }
      axios.post(`${BASE_URL}/consumer/data/transferprocess`, payload, {
        headers: {
            'X-Api-Key': 'password'
          }
      })
        .then((response) => {
          console.log(response.data)
          resolve(response.data);
        })
        .catch((e) => {
          this.errors.push(e)
          resolve('rejected');
        });
    })     
    }
    // Step 4.2: Check the status of data transfer using transfer process id
    getTransferProcess(transferId){

    return new Promise(resolve => {
      axios.get(`${BASE_URL}/consumer/data/transferprocess/${transferId}`, {
        headers: {
            'X-Api-Key': 'password'
          }
      })
        .then((response) => {
          console.log(response.data)
          resolve(response.data);
        })
        .catch((e) => {
          this.errors.push(e)
          resolve('rejected');
        });
    })     
    }
    // Step 4.3: Query transferred data from consumer backend system for further verification
    verifyDataTransfer(transferProcessId){

        return new Promise(resolve => {
        //axios.get(`${BACKEND_URL}/${transferProcessId}
        axios.get(`${BASE_URL}/${transferProcessId}`, {
            headers: {
                'Accept': 'application/octet-stream'
            }
        })
        .then((response) => {
          console.log(response.data)
          resolve(response.data);
        })
        .catch((e) => {
          this.errors.push(e)
          resolve('rejected');
        });
    }) 
    }
    async startTransferProcess(providerEndpoint) {
        var contractId = "";
        var assetId = "";
        var negotiationId = await this.getContractOfferCatalog(providerEndpoint);

        // Check agreement status //
        // Status: INITIAL, REQUESTED, CONFIRMED
        // first call to get the initial status
        var data = await this.getAgreementId(negotiationId)
        if (data == "rejected")
            return;
        else
            contractId = data.contractAgreementId
        
        
        // Check the agreement status until it is of status CONFIRMED
        while(data.state != "CONFIRMED"){        
            data = await this.getAgreementId(negotiationId)
            console.log("Agreement state:  ", data.state + '_' + data.contractAgreementId)
            contractId = data.contractAgreementId
        }

        // initiate data transfer
        var response = await this.initiateTransfer(contractId, assetId);
        var transferProcess = this.getTransferProcess(response.transferProcessId);
        console.log(transferProcess)
        
        // query transferred data from consumer backend
        return await this.verifyDataTransfer(response.transferProcessId);
    }
}