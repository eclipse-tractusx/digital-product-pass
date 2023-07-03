/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2023 Contributors to the CatenaX (ng) GitHub Organisation.
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

package org.eclipse.tractusx.productpass.models.negotiation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogRequest {
    @JsonProperty("@context")
    JsonNode context;
    @JsonProperty("protocol")
    String protocol;
    @JsonProperty("providerUrl")
    String providerUrl;

    @JsonProperty("querySpec")
    QuerySpec querySpec;

    public CatalogRequest(JsonNode context, String providerUrl, QuerySpec querySpec) {
        this.context = context;
        this.protocol = "dataspace-protocol-http";
        this.providerUrl = providerUrl;
        this.querySpec = querySpec;
    }
    public CatalogRequest(JsonNode context, String protocol, String providerUrl, QuerySpec querySpec) {
        this.context = context;
        this.protocol = protocol;
        this.providerUrl = providerUrl;
        this.querySpec = querySpec;
    }

    public CatalogRequest() {
    }

    public JsonNode getContext() {
        return context;
    }

    public void setContext(JsonNode context) {
        this.context = context;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProviderUrl() {
        return providerUrl;
    }

    public void setProviderUrl(String providerUrl) {
        this.providerUrl = providerUrl;
    }

    public QuerySpec getQuerySpec() {
        return querySpec;
    }

    public void setQuerySpec(QuerySpec querySpec) {
        this.querySpec = querySpec;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class QuerySpec {

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

        public List<FilterExpression> getFilterExpression() {
            return filterExpression;
        }

        public void setFilterExpression(List<FilterExpression> filterExpression) {
            this.filterExpression = filterExpression;
        }
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class FilterExpression{
            @JsonProperty("operandLeft")
            String operandLeft;

            @JsonProperty("operator")
            String operator;

            @JsonProperty("operandRight")
            String operandRight;

            public FilterExpression(String operandLeft, String operator, String operandRight) {
                this.operandLeft = operandLeft;
                this.operator = operator;
                this.operandRight = operandRight;
            }

            public FilterExpression() {
            }

            public String getOperandLeft() {
                return operandLeft;
            }

            public void setOperandLeft(String operandLeft) {
                this.operandLeft = operandLeft;
            }

            public String getOperator() {
                return operator;
            }

            public void setOperator(String operator) {
                this.operator = operator;
            }

            public String getOperandRight() {
                return operandRight;
            }

            public void setOperandRight(String operandRight) {
                this.operandRight = operandRight;
            }
        }
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Range {
            @JsonProperty("from")
            Integer from;

            @JsonProperty("to")
            Integer to;

            public Range(Integer from, Integer to) {
                this.from = from;
                this.to = to;
            }

            public Range() {
            }

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

        @JsonProperty("sortField")
        String sortField;

        @JsonProperty("criterion")
        String criterion;
        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }

        public Range getRange() {
            return range;
        }

        public void setRange(Range range) {
            this.range = range;
        }

        public String getSortField() {
            return sortField;
        }

        public void setSortField(String sortField) {
            this.sortField = sortField;
        }

        public String getCriterion() {
            return criterion;
        }

        public void setCriterion(String criterion) {
            this.criterion = criterion;
        }

    }
}
