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

export default class BackendService {
  async getPassport(version, assetId, jwtToken) {
    return new Promise(resolve => {
      axios.get(`${BACKEND_URL}/api/passport/${version}/${assetId}`, {
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
          if(e.response.data){
            resolve(e.response.data);
          }else if(e.request){
            resolve(e.request);
          }else{
            resolve(e.message)
          }
          
        });
    });
  }
}