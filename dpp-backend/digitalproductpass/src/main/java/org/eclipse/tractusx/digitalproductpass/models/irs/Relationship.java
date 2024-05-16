/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
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

package org.eclipse.tractusx.digitalproductpass.models.irs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * This class consists exclusively to define attributes related to the Relationships from the Job Response from the IRS.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Relationship {

    /** ATTRIBUTES **/
    @JsonProperty("catenaXId")
    public String catenaXId;
    @JsonProperty("linkedItem")
    public Item linkedItem;
    @JsonProperty("aspectType")
    public String aspectType;
    @JsonProperty("bpn")
    public String bpn;

    /** CONSTRUCTOR(S) **/
    public Relationship() {
    }

    public Relationship(String catenaXId, Item linkedItem, String aspectType, String bpn) {
        this.catenaXId = catenaXId;
        this.linkedItem = linkedItem;
        this.aspectType = aspectType;
        this.bpn = bpn;
    }

    /** GETTERS AND SETTERS **/
    public String getCatenaXId() {
        return catenaXId;
    }

    public void setCatenaXId(String catenaXId) {
        this.catenaXId = catenaXId;
    }

    public Item getLinkedItem() {
        return linkedItem;
    }

    public void setLinkedItem(Item linkedItem) {
        this.linkedItem = linkedItem;
    }

    public String getAspectType() {
        return aspectType;
    }

    public void setAspectType(String aspectType) {
        this.aspectType = aspectType;
    }

    public String getBpn() {
        return bpn;
    }

    public void setBpn(String bpn) {
        this.bpn = bpn;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Item {

        @JsonProperty("quantity")
        Quantity quantity;
        @JsonProperty("licecycleContext")
        String licecycleContext;
        @JsonProperty("assembledOn")
        String assembledOn;
        @JsonProperty("lastModifiedOn")
        String lastModifiedOn;
        @JsonProperty("childCatenaXId")
        String childCatenaXId;

        public Item(Quantity quantity, String licecycleContext, String assembledOn, String lastModifiedOn, String childCatenaXId) {
            this.quantity = quantity;
            this.licecycleContext = licecycleContext;
            this.assembledOn = assembledOn;
            this.lastModifiedOn = lastModifiedOn;
            this.childCatenaXId = childCatenaXId;
        }

        public Item() {
        }

        public Quantity getQuantity() {
            return quantity;
        }

        public void setQuantity(Quantity quantity) {
            this.quantity = quantity;
        }

        public String getLicecycleContext() {
            return licecycleContext;
        }

        public void setLicecycleContext(String licecycleContext) {
            this.licecycleContext = licecycleContext;
        }

        public String getAssembledOn() {
            return assembledOn;
        }

        public void setAssembledOn(String assembledOn) {
            this.assembledOn = assembledOn;
        }

        public String getLastModifiedOn() {
            return lastModifiedOn;
        }

        public void setLastModifiedOn(String lastModifiedOn) {
            this.lastModifiedOn = lastModifiedOn;
        }

        public String getChildCatenaXId() {
            return childCatenaXId;
        }

        public void setChildCatenaXId(String childCatenaXId) {
            this.childCatenaXId = childCatenaXId;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Quantity{
            @JsonProperty("quantityNumber")
            Double quantityNumber;
            @JsonProperty("measurementUnit")
            MeasurementUnit measurementUnit;

            public Quantity(Double quantityNumber, MeasurementUnit measurementUnit) {
                this.quantityNumber = quantityNumber;
                this.measurementUnit = measurementUnit;
            }

            public Quantity() {
            }

            public Double getQuantityNumber() {
                return quantityNumber;
            }

            public void setQuantityNumber(Double quantityNumber) {
                this.quantityNumber = quantityNumber;
            }

            public MeasurementUnit getMeasurementUnit() {
                return measurementUnit;
            }

            public void setMeasurementUnit(MeasurementUnit measurementUnit) {
                this.measurementUnit = measurementUnit;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            @JsonInclude(JsonInclude.Include.NON_NULL)
            public static class MeasurementUnit{
                @JsonProperty("datatypeURI")
                String datatypeURI;
                @JsonProperty("lexicalValue")
                String lexicalValue;

                public MeasurementUnit(String datatypeURI, String lexicalValue) {
                    this.datatypeURI = datatypeURI;
                    this.lexicalValue = lexicalValue;
                }

                public MeasurementUnit() {
                }

                public String getDatatypeURI() {
                    return datatypeURI;
                }

                public void setDatatypeURI(String datatypeURI) {
                    this.datatypeURI = datatypeURI;
                }

                public String getLexicalValue() {
                    return lexicalValue;
                }

                public void setLexicalValue(String lexicalValue) {
                    this.lexicalValue = lexicalValue;
                }
            }

        }
    }

}
