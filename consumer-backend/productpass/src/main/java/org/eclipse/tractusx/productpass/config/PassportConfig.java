/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
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
 ********************************************************************************/

package org.eclipse.tractusx.productpass.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * This class consists exclusively to define the attributes and methods needed for the Passport configuration.
 **/
@Configuration
@ConfigurationProperties(prefix="configuration.passport")
public class PassportConfig {

    /** ATTRIBUTES **/
    private BatteryPass batteryPass;
    private DigitalProductPass digitalProductPass;

    /** GETTERS AND SETTERS **/
    public BatteryPass getBatteryPass() {
        return batteryPass;
    }
    public void setBatteryPass(BatteryPass batteryPass) {
        this.batteryPass = batteryPass;
    }
    public DigitalProductPass getDigitalProductPass() {
        return digitalProductPass;
    }
    public void setDigitalProductPass(DigitalProductPass digitalProductPass) {
        this.digitalProductPass = digitalProductPass;
    }

    /** INNER CLASSES **/

    /**
     * This class consists exclusively to define the attributes and methods needed for the BatterPass configuration.
     **/
    public static class BatteryPass extends DigitalProductPass {
    }

    /**
     * This class consists exclusively to define the attributes and methods needed for the DigitalProductPass configuration.
     **/
    public static class DigitalProductPass {

        /** ATTRIBUTES **/
        private List<String> versions;
        private String semanticId;
        private String aspectId;
        private String fullSemanticId;

        /** CONSTRUCTOR(S) **/
        @SuppressWarnings("Unused")
        public DigitalProductPass(List<String> versions, String semanticId, String aspectId) {
            this.versions = versions;
            this.semanticId = semanticId;
            this.aspectId = aspectId;
        }

        public DigitalProductPass() {
        }

        /** GETTERS AND SETTERS **/
        public List<String> getVersions() {
            return this.versions;
        }
        public void setVersions(List<String> versions) {
            this.versions = versions;
        }
        public String getSemanticId() {
            return semanticId;
        }
        public void setSemanticId(String semanticId) {
            this.semanticId = semanticId;
        }
        @SuppressWarnings("Unused")
        public String getAspectId() {
            return aspectId;
        }
        @SuppressWarnings("Unused")
        public void setAspectId(String aspectId) {
            this.aspectId = aspectId;
        }

        /** METHODS **/

        /**
         * Builds the semanticId to search the submodel inside the Digital twin.
         * It concatenates the semanticId, the passport version and the aspectId.
         * <p>
         * @param   version
         *          the {@code String} intended passport's version .
         *
         * @return this {@code String} with the built semanticId.
         *
         */
        public String getFullSemanticId(String version) {
            if (this.fullSemanticId == null) {
                this.fullSemanticId = semanticId + ":" + versions.stream().filter(v -> v.equalsIgnoreCase(version)).findFirst().get() + "#" + aspectId;
            }
            return fullSemanticId;
        }
    }

}
