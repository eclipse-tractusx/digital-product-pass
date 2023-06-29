/**
 * Catena-X - Product Passport Consumer Frontend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the
 * License for the specific language govern in permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
import { BACKEND_URL } from "@/services/service.const";
import axios from "axios";
import jsonUtil from "@/utils/jsonUtil.js";
import threadUtil from "../utils/threadUtil";
export default class BackendService {

  async getPassport(version, id, authentication) {
    return new Promise(resolve => {
      let negotiationResponse = null;
      // Try to get the negotiation contract
      try {
        negotiationResponse = this.searchContract(version, id, authentication);
      } catch (e) {
        resolve(negotiationResponse);
      }
      // Check if the negotiation status is successful or if the data is not retrieved
      if (!negotiationResponse || (jsonUtil.exists("status", negotiationResponse) && negotiationResponse["status"] != 200) || !jsonUtil.exists("data", negotiationResponse)) {
        resolve(negotiationResponse);
      }

      // Get negotiation property
      let negotiation = negotiation["data"];
      let processStatus = null;

      // Check if the attributes in data exist

      let token = jsonUtil.get("token", negotiation, ".", null);
      let contractId = jsonUtil.get("contract.@id", negotiation, ".", null);
      let processId = jsonUtil.get("id", negotiation, ".", null);
      
      // If is not existing we return an error imitating the response
      if (!token || !contractId || !processId) {
        resolve(
          this.getErrorMessage(
            "The contract request was not valid",
            500,
            "Internal Server Error"
          )
        )
      }

      // Sign the contract request
      try{
        processStatus = this.signContract(negotiation, authentication);
      }catch (e) {
        resolve(processStatus);
      }
      // Check if the process status is successful or if the data is not retrieved
      if (!processStatus || (jsonUtil.exists("status", processStatus) && processStatus["status"] != 200) || !jsonUtil.exists("data", processStatus)) {
        resolve(processStatus);
      }
      
      
      let status = jsonUtil.get("data.status", processStatus, ".", null);
      if(status == "FAILED"){
        resolve(this.getErrorMessage(
          "The negotiation process has failed",
          500,
          "Internal Server Error"
        ))
      }

      let loopBreakStatus = ["COMPLETED", "FAILED", "DECLINED"]
      let maxRetries = 20;
      let waitingTime = 1000; // Half second
      let retries = 0;
      let statusResponse = null; 
      
      while(retries < maxRetries){
        statusResponse = this.getStatus(processId, authentication)
        status = jsonUtil.get("data.status", statusResponse);
        if(loopBreakStatus.includes(status) || status == null){
          break;
        }
        threadUtil.sleep(waitingTime);
        retries++;
      }

      if(status == "COMPLETED"){
        resolve(this.retrievePassport(negotiation, authentication));  
      }

      resolve(this.getErrorMessage(
        "Failed to retrieve passport!",
        500,
        "Internal Server Error"
      ))
    });
  }
  getErrorMessage(message, status, statusText) {
    return {
      "message": message,
      "status": status,
      "statusText": statusText
    }
  }
  getHeaders(authentication) {
    return {
      headers: {
        'Accept': 'application/json',
        'Authorization': "Bearer " + authentication.getAccessToken()
      }
    }

  }
  getRequestBody(negotiation) {
    return {
      "processId": negotiation["id"],
      "contractId": negotiation["contract"]["id"],
      "token": negotiation["token"]
    }
  }

  getSearchBody(id, version) {
    return {
      "id": id,
      "version": version
    }
  }
  async getStatus(processId, authentication) {
    return new Promise(resolve => {
      axios.get(`${BACKEND_URL}/api/contract/status/`+processId, this.getHeaders(authentication))
        .then((response) => {
          resolve(response.data);
        })
        .catch((e) => {
          if (e.response.data) {
            resolve(e.response.data);
          } else if (e.request) {
            resolve(e.request);
          } else {
            resolve(e.message)
          }

        });
    });
  }
  async retrievePassport(negotiation, authentication) {
    return new Promise(resolve => {
      let body = this.getRequestBody(negotiation);
      axios.post(`${BACKEND_URL}/api/passport`, body, this.getHeaders(authentication))
        .then((response) => {
          resolve(response.data);
        })
        .catch((e) => {
          if (e.response.data) {
            resolve(e.response.data);
          } else if (e.request) {
            resolve(e.request);
          } else {
            resolve(e.message)
          }

        });
    });
  }
  async signContract(negotiation, authentication) {
    return new Promise(resolve => {
      let body = this.getRequestBody(negotiation);
      axios.post(`${BACKEND_URL}/api/contract/sign`, body, this.getHeaders(authentication))
        .then((response) => {
          resolve(response.data);
        })
        .catch((e) => {
          if (e.response.data) {
            resolve(e.response.data);
          } else if (e.request) {
            resolve(e.request);
          } else {
            resolve(e.message)
          }

        });
    });
  }
  async searchContract(id, version, authentication) {
    return new Promise(resolve => {
      let body = this.getSearchBody(id, version);
      axios.post(`${BACKEND_URL}/api/contract/search`, body, this.getHeaders(authentication))
        .then((response) => {
          resolve(response.data);
        })
        .catch((e) => {
          if (e.response.data) {
            resolve(e.response.data);
          } else if (e.request) {
            resolve(e.request);
          } else {
            resolve(e.message)
          }

        });
    });
  }
}