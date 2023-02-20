/**
 * Copyright 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { SERVER_URL } from "@/services/service.const";
import axios from "axios";

export default class BackendService {
  async getPassportV1(assetId, jwtToken) {
    return this.getPassport("v1", assetId, jwtToken);
  }
  async getPassport(version, assetId, jwtToken) {
    return new Promise(resolve => {
      setTimeout(() => {
        axios.get(`${SERVER_URL}/api/passport/${version}/${assetId}`, {
          headers: {
            'Accept': 'application/json',
            'Authorization': "Bearer "+ jwtToken
          }
        }
        )
          .then((response) => {
            resolve(response.data);
          })
          .catch((e) => {
            console.error("getPassport -> " + e);
            resolve('rejected');
          });
      }, 2000);
    });
  }
}