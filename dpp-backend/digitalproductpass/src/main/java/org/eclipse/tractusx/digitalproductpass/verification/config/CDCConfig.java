/*********************************************************************************
 *
 * Tractus-X - Digital Product Pass Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2024 CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
 *
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
 ********************************************************************************/

package org.eclipse.tractusx.digitalproductpass.verification.config;

import org.springframework.context.annotation.Configuration;

import java.util.List;
@Configuration
public class CDCConfig {

    List<SemanticKey> semanticIdKeys;

    public CDCConfig() {
    }

    public CDCConfig(List<SemanticKey> semanticIdKeys) {
        this.semanticIdKeys = semanticIdKeys;
    }

    public List<SemanticKey> getSemanticIdKeys() {
        return semanticIdKeys;
    }

    public void setSemanticIdKeys(List<SemanticKey> semanticIdKeys) {
        this.semanticIdKeys = semanticIdKeys;
    }


    public static class SemanticKey {
        String key;
        String value;

        public SemanticKey(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public SemanticKey() {
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
