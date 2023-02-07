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

import { CX_REGISTRY_URL } from "@/services/service.const";
import axios from "axios";


export default class AasServices {

  getAasShellId(assetIds, requestHeaders) {
    return new Promise((resolve) => {

      axios.get(`${CX_REGISTRY_URL}/registry/lookup/shells?assetIds=${assetIds}`,
        {
          headers: requestHeaders
        }
      )
        .then((response) => {
          resolve(response.data);
        })
        .catch((e) => {
          console.error("getAasShellId -> " + e);
          resolve("rejected");
        });
    });
  }
  getShellDescriptor(aasShellId, requestHeaders) {
    return new Promise((resolve) => {
      axios
        .get(`${CX_REGISTRY_URL}/registry/registry/shell-descriptors/${aasShellId}`,
          {
            headers: requestHeaders
          }
        )
        .then((response) => {
          resolve(response.data);
        })
        .catch((e) => {
          console.error("getShellDescriptor -> " + e);
          resolve("rejected");
        });
    });
  }
  getSubmodelDescriptor(shellDescriptor, requestHeaders) {
    return new Promise((resolve) => {
      axios
        .get(
          `${CX_REGISTRY_URL}/registry/registry/shell-descriptors/${shellDescriptor.identification}/submodel-descriptors/${shellDescriptor.submodelDescriptors[0].identification}`,
          {
            headers: requestHeaders
          }
        )
        .then((response) => {
          resolve(response.data);
        })
        .catch((e) => {
          console.error("getSubmodelDescriptor ->" + e);
          resolve("rejected");
        });
    });
  }
}
