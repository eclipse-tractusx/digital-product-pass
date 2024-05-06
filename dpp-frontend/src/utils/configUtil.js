/**
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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
import passports from "@/config/templates/passports.json";
import metadata from "@/config/templates/metadata.json";

export default {
    /*getAttribute(attribute, sep = ".", defaultValue = null) {
        return jsonUtil.get(attribute, attributes, sep, defaultValue);
    },
    getAllAttributes() {
        return attributes;
    },
    filterAttribute(attributes) {
        if (!attributes) return null;
        if (!(attributes instanceof Object)) return attributes;
        let tmpAttributes = jsonUtil.copy(attributes);
        let tmpPropsData = {};
        for (let attr in tmpAttributes) {
            let tmpData = tmpAttributes[attr];

            if (tmpData == null) continue;
            tmpData = jsonUtil.flatternJson(tmpData);

            tmpPropsData = jsonUtil.extend(tmpPropsData, tmpData);
        }
        if (Object.keys(tmpPropsData).length < 0) return null;
        return tmpPropsData;
    }*/
    normalizePassport(responsePassport=null, responseMetadata=null, semanticId="urn:bamm:io.catenax.generic.digital_product_passport:1.0.0#DigitalProductPassport"){
        if(!jsonUtil.exists(semanticId, passports)){
            console.error("[ERROR] Semantic Id ["+semanticId+"] is not available in the passport templates and is not supported by the application");
            return null;
        }
        let passport = passports[semanticId]; //Get the passport by semanticId
        let response = {
            "metadata": metadata,
            "aspect": passport,
            "semanticId": semanticId
        }
        if(responsePassport){
            response["aspect"] = jsonUtil.extendDeep(passport, responsePassport);
        }
        if(responseMetadata){
            response["metadata"] = jsonUtil.extendDeep(metadata, responseMetadata);
        }
        return response;
    }
};
