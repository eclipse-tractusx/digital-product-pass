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

package org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.DidDocument;

import java.util.List;

/**
 * This class consists exclusively to define attributes related to the Catalog requests.
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogRequest extends DidDocument {

    /** ATTRIBUTES **/
    @JsonProperty("protocol")
    String protocol;
    @JsonProperty("counterPartyAddress")
    String counterPartyAddress;
    @JsonProperty("counterPartyId")
    String counterPartyId;
    @JsonProperty("querySpec")
    QuerySpec querySpec;

    /** CONSTRUCTOR(S) **/
    public CatalogRequest(JsonNode context, String counterPartyAddress, QuerySpec querySpec) {
        this.context = context;
        this.protocol = "dataspace-protocol-http";
        this.counterPartyAddress = counterPartyAddress;
        this.querySpec = querySpec;
    }

    public CatalogRequest(JsonNode context, String protocol, String counterPartyAddress, QuerySpec querySpec) {
        this.context = context;
        this.protocol = protocol;
        this.counterPartyAddress = counterPartyAddress;
        this.querySpec = querySpec;
    }

    public CatalogRequest() {
    }

    public CatalogRequest(String id, String type, String protocol, String counterPartyAddress, String counterPartyId, QuerySpec querySpec) {
        super(id, type);
        this.protocol = protocol;
        this.counterPartyAddress = counterPartyAddress;
        this.counterPartyId = counterPartyId;
        this.querySpec = querySpec;
    }

    public CatalogRequest(String protocol, String counterPartyAddress, String counterPartyId, QuerySpec querySpec) {
        this.protocol = protocol;
        this.counterPartyAddress = counterPartyAddress;
        this.counterPartyId = counterPartyId;
        this.querySpec = querySpec;
    }

    public CatalogRequest(String id, String type, JsonNode context, String protocol, String counterPartyAddress, String counterPartyId, QuerySpec querySpec) {
        super(id, type, context);
        this.protocol = protocol;
        this.counterPartyAddress = counterPartyAddress;
        this.counterPartyId = counterPartyId;
        this.querySpec = querySpec;
    }

    public CatalogRequest(String type, String protocol, String counterPartyAddress, String counterPartyId, QuerySpec querySpec) {
        super(type);
        this.protocol = protocol;
        this.counterPartyAddress = counterPartyAddress;
        this.counterPartyId = counterPartyId;
        this.querySpec = querySpec;
    }

    public CatalogRequest(JsonNode context, String protocol, String counterPartyAddress, String counterPartyId, QuerySpec querySpec) {
        super(context);
        this.protocol = protocol;
        this.counterPartyAddress = counterPartyAddress;
        this.counterPartyId = counterPartyId;
        this.querySpec = querySpec;
    }
    public CatalogRequest(JsonNode context, String protocol, String counterPartyAddress, String counterPartyId, QuerySpec querySpec, String type) {
        super(context, type);
        this.protocol = protocol;
        this.counterPartyAddress = counterPartyAddress;
        this.counterPartyId = counterPartyId;
        this.querySpec = querySpec;
    }

    /** GETTERS AND SETTERS **/
    public String getProtocol() {
        return protocol;
    }
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getCounterPartyAddress() {
        return counterPartyAddress;
    }

    public void setCounterPartyAddress(String counterPartyAddress) {
        this.counterPartyAddress = counterPartyAddress;
    }

    public QuerySpec getQuerySpec() {
        return querySpec;
    }

    public void setQuerySpec(QuerySpec querySpec) {
        this.querySpec = querySpec;
    }

    public String getCounterPartyId() {
        return counterPartyId;
    }

    public void setCounterPartyId(String counterPartyId) {
        this.counterPartyId = counterPartyId;
    }

    /** INNER CLASSES **/
    /**
     * This class consists exclusively to define attributes related to the Query specification of the Catalog request.
     **/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class QuerySpec {

        /** ATTRIBUTES **/
        @JsonProperty("filterExpression")
        List<FilterExpression> filterExpression;
        @JsonProperty("offset")
        Integer offset;
        @JsonProperty("limit")
        Integer limit;
        @JsonProperty("filter")
        String filter;
        @JsonProperty("range")
        Range range;
        @JsonProperty("sortField")
        String sortField;
        @JsonProperty("criterion")
        String criterion;

        /** CONSTRUCTOR(S) **/
        @SuppressWarnings("Unused")
        public QuerySpec(Integer offset, Integer limit, String filter, Range range, String sortField, String criterion) {
            this.offset = offset;
            this.limit = limit;
            this.filter = filter;
            this.range = range;
            this.sortField = sortField;
            this.criterion = criterion;
        }
        public QuerySpec() {
        }

        /** GETTERS AND SETTERS **/
        @SuppressWarnings("Unused")
        public List<FilterExpression> getFilterExpression() { return filterExpression; }
        public void setFilterExpression(List<FilterExpression> filterExpression) {
            this.filterExpression = filterExpression;
        }
        @SuppressWarnings("Unused")
        public Integer getOffset() {
            return offset;
        }
        @SuppressWarnings("Unused")
        public void setOffset(Integer offset) {
            this.offset = offset;
        }
        @SuppressWarnings("Unused")
        public Integer getLimit() {
            return limit;
        }
        @SuppressWarnings("Unused")
        public void setLimit(Integer limit) {
            this.limit = limit;
        }
        @SuppressWarnings("Unused")
        public String getFilter() {
            return filter;
        }
        @SuppressWarnings("Unused")
        public void setFilter(String filter) {
            this.filter = filter;
        }
        @SuppressWarnings("Unused")
        public Range getRange() {
            return range;
        }
        @SuppressWarnings("Unused")
        public void setRange(Range range) {
            this.range = range;
        }
        @SuppressWarnings("Unused")
        public String getSortField() {
            return sortField;
        }
        @SuppressWarnings("Unused")
        public void setSortField(String sortField) {
            this.sortField = sortField;
        }
        @SuppressWarnings("Unused")
        public String getCriterion() {
            return criterion;
        }
        @SuppressWarnings("Unused")
        public void setCriterion(String criterion) {
            this.criterion = criterion;
        }

        /** INNER CLASSES **/
        /**
         * This class consists exclusively to define attributes related to the filter expression property of the Query specification.
         **/
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class FilterExpression {

            /** ATTRIBUTES **/
            @JsonProperty("operandLeft")
            String operandLeft;
            @JsonProperty("operator")
            String operator;
            @JsonProperty("operandRight")
            String operandRight;

            /** CONSTRUCTOR(S) **/
            public FilterExpression(String operandLeft, String operator, String operandRight) {
                this.operandLeft = operandLeft;
                this.operator = operator;
                this.operandRight = operandRight;
            }
            @SuppressWarnings("Unused")
            public FilterExpression() {
            }

            /** GETTERS AND SETTERS **/
            @SuppressWarnings("Unused")
            public String getOperandLeft() {
                return operandLeft;
            }
            @SuppressWarnings("Unused")
            public void setOperandLeft(String operandLeft) {
                this.operandLeft = operandLeft;
            }
            @SuppressWarnings("Unused")
            public String getOperator() {
                return operator;
            }
            @SuppressWarnings("Unused")
            public void setOperator(String operator) {
                this.operator = operator;
            }
            @SuppressWarnings("Unused")
            public String getOperandRight() {
                return operandRight;
            }
            @SuppressWarnings("Unused")
            public void setOperandRight(String operandRight) {
                this.operandRight = operandRight;
            }
        }

        /**
         * This class consists exclusively to define attributes to specify the range property of the Query specification.
         **/
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Range {

            /** ATTRIBUTES **/
            @JsonProperty("from")
            Integer from;
            @JsonProperty("to")
            Integer to;

            /** CONSTRUCTOR(S) **/
            @SuppressWarnings("Unused")
            public Range(Integer from, Integer to) {
                this.from = from;
                this.to = to;
            }
            @SuppressWarnings("Unused")
            public Range() {
            }

            /** GETTERS AND SETTERS **/
            public Integer getFrom() {
                return from;
            }
            public void setFrom(Integer from) {
                this.from = from;
            }
            public Integer getTo() {
                return to;
            }
            public void setTo(Integer to) {
                this.to = to;
            }
        }
    }
}
