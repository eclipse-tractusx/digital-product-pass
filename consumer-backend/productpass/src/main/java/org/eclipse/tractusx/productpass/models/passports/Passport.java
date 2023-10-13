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

package org.eclipse.tractusx.productpass.models.passports;


import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.io.IOException;
import java.util.List;

/**
 * This class consists exclusively to define attributes and methods that are common to the passports used in the Application.
 * Also used to perform polymorphism in the Application with all the passports that extends it.
 **/

public class Passport extends JsonNode {
    public Passport() {
    }

    @Override
    public <T extends JsonNode> T deepCopy() {
        return null;
    }

    @Override
    public JsonToken asToken() {
        return null;
    }

    @Override
    public JsonParser.NumberType numberType() {
        return null;
    }

    @Override
    public JsonNode get(int index) {
        return null;
    }

    @Override
    public JsonNode path(String fieldName) {
        return null;
    }

    @Override
    public JsonNode path(int index) {
        return null;
    }

    @Override
    public JsonParser traverse() {
        return null;
    }

    @Override
    public JsonParser traverse(ObjectCodec objectCodec) {
        return null;
    }

    @Override
    protected JsonNode _at(JsonPointer ptr) {
        return null;
    }

    @Override
    public JsonNodeType getNodeType() {
        return null;
    }

    @Override
    public String asText() {
        return null;
    }

    @Override
    public JsonNode findValue(String fieldName) {
        return null;
    }

    @Override
    public JsonNode findPath(String fieldName) {
        return null;
    }

    @Override
    public JsonNode findParent(String fieldName) {
        return null;
    }

    @Override
    public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar) {
        return null;
    }

    @Override
    public List<String> findValuesAsText(String fieldName, List<String> foundSoFar) {
        return null;
    }

    @Override
    public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar) {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {

    }

    @Override
    public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {

    }
    // Here will be included then next release Generic Passport Structure
}
