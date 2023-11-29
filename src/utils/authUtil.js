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

import jsonUtil from "@/utils/jsonUtil.js";
import cryptUtil from "@/utils/cryptUtil.js";
export default {
    unwrapToken(token) {
        try {
            return jsonUtil.toJson(
                cryptUtil.decodeUrl(
                    cryptUtil.fromBase64(
                        token.split('.')[1]
                            .replace(/-/g, '+')
                            .replace(/_/g, '/'))
                        .split('').map(
                            (c) => {
                                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
                            }
                        ).join('')
                )
            );
        } catch (e) {
            console.error.log("[ERROR] It was not possible to parse token: " + e); // Print error message
            return null;
        }
    },
    checkBpn(token, bpn){
        let parsedToken = this.unwrapToken(token);
        if(parsedToken == null){
            return false;
        }
        if(!jsonUtil.exists(parsedToken, "bpn")){
            return false;
        }
        let tokenBpn = jsonUtil.get("bpn", parsedToken, ".", null);
        if(bpn == null){
            return false;
        }
        return bpn === tokenBpn;
    }
    
}