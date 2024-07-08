#################################################################################
# Tractus-X - Digital Product Pass Verification Add-on
#
# Copyright (c) 2023, 2024 BMW AG
# Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
# Copyright (c) 2024 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the
# terms of the Apache License, Version 2.0 which is available at
# https://www.apache.org/licenses/LICENSE-2.0.
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
# either express or implied. See the
# License for the specific language govern in permissions and limitations
# under the License.
#
# SPDX-License-Identifier: Apache-2.0
#################################################################################

import sys
# Add the previous folder structure to the system path to import the utilities
sys.path.append("../")

# Define flask imports and configuration
from flask import Flask

app = Flask(__name__)
# Set up imports configuration
import traceback
import logging
from utilities.operators import op
import copy

logger = logging.getLogger('staging')
class sammSchemaParser:
    def __init__(self):
        self.baseSchema = dict()
        self.rootRef = "#"
        self.refKey = "$ref"
        self.pathSep = "#/"
        self.actualPathSep = "/-/"
        self.refPathSep="/"
        self.propertiesKey = "properties"
        self.itemKey = "items"
        self.schemaPrefix = "schema"
        self.aspectPrefix = "aspect"
        self.contextPrefix = "@context"
        self.complexTypes = ["object", "array"]
        self.parentRefs=list()
        self.parentRef = None
        self.recursionDepth = 2
        self.depht = 0
        self.initialJsonLd = {
            "@version": 1.1,
            self.schemaPrefix: "https://schema.org/"
        }
        self.contextTemplate = {
            "@version": 1.1,
            "id": "@id",
            "type": "@type"
        }
    
    def schema_to_jsonld(self, semanticId, schema, aspectPrefix="aspect"):
        try:
            self.baseSchema = copy.copy(schema)
            semanticParts = semanticId.split(self.rootRef)  
            if((len(semanticParts) < 2) or (semanticParts[1] == '')):
                raise Exception("Invalid semantic id, missing the model reference!")
            
            if not(aspectPrefix is None):
                self.aspectPrefix = aspectPrefix

        
            jsonLdContext = self.create_node(property=schema)
            
            if jsonLdContext is None:
                raise Exception("It was not possible to generated the json-ld!")
            
            responseContext = copy.copy(self.initialJsonLd)

            semanticPath = semanticParts[0]
            responseContext[self.aspectPrefix] = semanticPath + self.rootRef
            aspectName = semanticParts[1]
            jsonLdContext["@id"] = ":".join([self.aspectPrefix,aspectName])
            responseContext[aspectName] = jsonLdContext
            
            if "description" in schema:
                responseContext[aspectName]["@context"]["@definition"] = schema["description"]
            return {
                "@context": responseContext
            }
        except:
            traceback.print_exc()
            raise Exception("It was not possible to create jsonld schema")
    

    def expand_node(self, ref, actualref, key=None):
        try:
            ## Ref must not be None
            if (ref is None): return None
            ## Get expanded node
            expandedNode = self.get_schema_ref(ref=ref, actualref=actualref)

            newRef = self.actualPathSep.join([actualref, ref])

            if(expandedNode is None): return None
            return self.create_node(property=expandedNode, actualref=newRef, key=key)
        except:
            traceback.print_exc()
            logger.error("It was not possible to expand the node")
            return None

    def create_node(self, property, actualref="", key=None):
        try:
            ## Schema must be not none and type must be in the schema
            if (property is None) or (not "type" in property): return None
            
            ## Start by creating a simple node
            node = self.create_simple_node(property=property, key=key)

            ## If is not possible to create the simple node it is not possible to create any node
            if(node is None): return None

            propertyType = property["type"]

            if propertyType == "object":
                return self.create_object_node(property=property, node=node, actualref=actualref)
            
            if propertyType == "array":
                return self.create_array_node(property=property, node=node, actualref=actualref)
            
            return self.create_value_node(property=property, node=node)
        except:
            traceback.print_exc()
            logger.error("It was not possible to create the node")
            return None

    def create_value_node(self, property, node):
        try:
            ## If type exists add definition to the node
            if not ("type" in property): return None
            
            node["@type"] = self.schemaPrefix+":"+property["type"]
            return node
        except:
            traceback.print_exc()
            logger.error("It was not possible to create value node")
            return None
    
    def create_object_node(self, property, node, actualref):
        try:
            ## If object has not the properties key
            if not (self.propertiesKey in property): return None
            
            properties = property[self.propertiesKey]

            node[self.contextPrefix] = self.create_properties_context(properties=properties, actualref=actualref)
            return node
        except:
            traceback.print_exc()
            logger.error("It was not possible to create object node")
            return None

    def create_array_node(self, property, node, actualref=None):
        try:
            ## If array node has not the item key
            if not (self.itemKey in property): return None
            
            item = property[self.itemKey]
            node["@container"] = "@list" 

            ## If list is with different types of data, dont specify a type
            if(isinstance(item, list)):
                return node

            if not (self.refKey in item):
                return self.create_value_node(property=item, node=node)

            node[self.contextPrefix] = self.create_item_context(item=item, actualref=actualref)
            return node
        except:
            traceback.print_exc()
            logger.error("It was not possible to create the array node")
            return None

    
    
    def filter_key(self, key):
        cleanKey = key
        if ("@" in key): 
            cleanKey = key.replace("@","")
        
        if (" " in key): 
            cleanKey = key.replace(" ","-")
        return cleanKey


    def create_properties_context(self, properties, actualref):
        try:
            ## If no key is provided or node is empty
            if(properties is None): return None
            
            ## If no key is found
            if(not isinstance(properties, dict)): return None
            
            ## If no keys are provided in the properties
            if(len(properties.keys())  == 0): return None
            
            ## Create new context dict from template
            newContext = copy.copy(self.contextTemplate)
            oldProperties = copy.copy(properties)

            ## Fill the node context with the properties
            for propKey, prop in oldProperties.items():
                key = self.filter_key(key=propKey)
                prop = self.create_node_property(key=key, node=prop, actualref=actualref)
                if (prop is None):
                    continue
                

                newContext[key] = prop

            ## Add context properties to the node context
            return newContext
        except:
            traceback.print_exc()
            logger.error("It was not possible to create properties context")
            return None
        
    def create_item_context(self, item, actualref):
        try:
            ## If no key is provided or node is empty
            if(item is None): return None
            
            newContext = copy.copy(self.contextTemplate)
            ref = item[self.refKey]
            nodeItem = self.expand_node(ref=ref, actualref=actualref)

            ## If was not possible to get the reference return None
            if nodeItem is None: return None

            newContext.update(nodeItem)
            ## Overite the existing description of ref item

            if not ("description" in item):
                return newContext
            
            if not ("@context" in newContext):
                newContext["@context"] = dict()

            newContext["@context"]["@definition"]  = item["description"] 

            return newContext
        except:
            traceback.print_exc()
            logger.error("It was not possible to create the item context")
            return None
        
    def create_node_property(self, key, node, actualref):
        try:
            ## If no key is provided or node is empty
            if(key is None) or (node is None): return None

            ## Ref property must exist in a property inside properties
            if not (self.refKey in node): return None

            ## Get reference from the base schema
            ref = node[self.refKey]
            nodeProperty = self.expand_node(ref=ref, actualref=actualref, key=key)

            ## If was not possible to get the reference return None
            if nodeProperty is None: return None

            ## Overite the existing description of ref property
            if not ("description" in node):
                return nodeProperty
            
            if not ("@context" in nodeProperty):
                nodeProperty["@context"] = dict()

            nodeProperty["@context"]["@definition"]  = node["description"]

            return nodeProperty
        except:
            traceback.print_exc()
            logger.error("It was not possible to create node property")
            return None


    def create_simple_node(self, property, key=None):
        """
        Creates a simple node with key and object from a schema property
        Receives:
            key: :str: attribute key
            node: :dict: contains the node object with or without description and type
        Returns:
            response: :dict: json ld simple node with the information of the node object
        """
        try:
            ## If no key is provided or node is empty
            if (property is None): return None
            
            ## Create new json ld simple node
            newNode = dict()

            ## If the key is not none create a new node
            if not (key is None):
                newNode["@id"] = self.aspectPrefix+":"+key
            

            ## If description exists add definition to the node

            if not ("description" in property):
                return newNode
            
            if not ("@context" in newNode):
                newNode["@context"] = dict()

            newNode["@context"]["@definition"] = property["description"]

            return newNode
        except:
            traceback.print_exc()
            logger.error("It was not possible to create the simple node")
            return None

    def get_schema_ref(self, ref, actualref):
        """
        Creates a simple node with key and object from a schema property
        Receives:
            key: :str: attribute key
            node: :dict: contains the node object with or without description and type
        Returns:
            response: :dict: json ld simple node with the information of the node object
        """
        try:
            if(not isinstance(ref, str)): return None
            
            # If the actual reference is already found means we are going in a loop
            if not(ref in actualref):     
                path = ref.removeprefix(self.pathSep) 
                return op.get_attribute(self.baseSchema, attrPath=path, pathSep=self.refPathSep, defaultValue=None)
            
            if(self.depht >= self.recursionDepth):
                logger.warning(f"[WARNING] Infinite recursion detected in the following path: ref[{ref}] and acumulated ref[{actualref}]!")
                self.depht=0
                return None
            
            self.depht+=1
            
            path = ref.removeprefix(self.pathSep) 

            return op.get_attribute(self.baseSchema, attrPath=path, pathSep=self.refPathSep, defaultValue=None)
        except:
            traceback.print_exc()
            logger.error("It was not possible to get schema reference")
            return None
