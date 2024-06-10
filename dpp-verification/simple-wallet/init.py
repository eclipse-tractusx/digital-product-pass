#################################################################################
# Tractus-X - Digital Product Passport Verification Add-on
#
# Copyright (c) 2024 BMW AG
# Copyright (c) 2024 CGI Deutschland B.V. & Co. KG
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

"""
Digital Product Pass Simple Wallet Application

What is the DPP Simple Wallet?

This simple wallet application is an extention testing application for the Digital Product Pass Verification Add-On.

It provides a functional wallet able to:
  - Sign verifiable credentials with JsonWebSignature2020 proofs
  - Verify verifiable credentials with JsonWebSignature2020 proofs
  - Manage private and public keys, providing via DID Documents public keys in JsonWebKey2020 format

Additionally the simple wallet contains a extension for creating JSON-LD @contexts using Catena-X SAMM Models Schemas.
This allows the verifiable credentials to be valid JSON-LDs documents and enables the context of the existing and future modeled documents.

Created by [Mathias Brunkow Moser - @matbmoser - CGI] - [2024]
"""

import sys

# Add the previous folder structure to the system path to import the utilities
sys.path.append("../")

# Define flask imports and configuration
from flask import Flask, request, jsonify

app = Flask(__name__)

# Set up imports configuration
import argparse
import logging.config
import logging
from datetime import timedelta
import traceback
from utilities.httpUtils import HttpUtils
from utilities.operators import op
from utilities.cryptool import cryptool
import yaml
from passport import sammSchemaParser

op.make_dir("logs")
op.make_dir("test")
# Load the config file application/vc+ld+jwt
with open('./config/logging.yml', 'rt') as f:
    # Read the yaml configuration
    config = yaml.safe_load(f.read())
    # Set logging filename with datetime
    config["handlers"]["file"]["filename"] = f'logs/{op.get_filedatetime()}-wallet.log'
    logging.config.dictConfig(config)

# Configure the logging module with the config file


def get_arguments():
    """
    Commandline argument handling. Return the populated namespace.

    Returns:
        args: :func:`parser.parse_args`
    """
    
    parser = argparse.ArgumentParser()
    
    parser.add_argument("--port", default=7777, \
                        help="The server port where it will be available", required=False, type=int)
    
    parser.add_argument("--host", default="localhost", \
                        help="The server host where it will be available", required=False, type=str)
    
    parser.add_argument("--debug", default=False, action="store_false", \
                    help="Enable and disable the debug", required=False)
    
    args = parser.parse_args()
    return args


@app.get("/health")
def check_health():
    """
    Retrieves health information from the server

    Returns:
        response: :obj:`status, timestamp`
    """
    logger.debug("[HEALTH CHECK] Retrieving positive health information!")
    return jsonify({
        "status": "RUNNING",
        "timestamp": op.timestamp() 
    })

@app.get("/<bpn>/did.json")
def generate_public_key(bpn):
    basePath = "./keys/"+bpn
    keyPath = basePath+"/key.jwt"
    
    if(not op.path_exists(keyPath)):
        return HttpUtils.get_error_response(status=404,message="This issuer does not exists!")
    
    key = cryptool.loadJwkKey(keyPath=keyPath)

    if(key is None):
        return HttpUtils.get_error_response(status=500,message="Internal Server Error")
    
    publicKey = key.export_public(as_dict=True)

    rootdid = cryptool.urlToDidWeb(url=request.root_url)

    did = ":".join([rootdid, bpn])

    if(not "kid" in publicKey):
        return HttpUtils.get_error_response(status=404,message="The method does not exist!")

    methodPathId = "#".join([did, publicKey["kid"]])

    didDocument = {
        "id": did,
        "verificationMethod": [
            {
                "publicKeyJwt": publicKey,
                "controller": did, 
                "id": methodPathId,
                "type": "JsonWebKey2020"
            }
        ],
        "@context": [
            "https://www.w3.org/ns/did/v1",
            "https://w3c.github.io/vc-jws-2020/contexts/v1"
        ]
    }
    return HttpUtils.response(op.to_json(didDocument))

@app.post("/verify")
def verify_credential():
    try:
        body = HttpUtils.get_body(request)
        verified = False
        try:
            verified = cryptool.verifyVerifiableCredential(credential=body)
        except Exception as e:
            return HttpUtils.response(data={
                "verified": False,
                "message": str(e)
            }, status=400)

        if(not verified):
            return HttpUtils.response(data={
                "verified": False,
                "message": "Verification Failed! Verifiable Credential is not valid!"
            }, status=400)

        logger.info(msg=f"Verifiable Credential with ID: [{str(body["id"])}] was verified!")

        return HttpUtils.response({
            "verified": True,
            "message": "Verified Credential is Valid! The proof was verified!"
        })
    except Exception as e:
        logger.exception(str(e))
        traceback.print_exc()

    return HttpUtils.response(data={
                "verified": False,
                "message": "Verification Failed! Verifiable Credential is not valid!"
            }, status=400)

@app.post("/<bpn>/sign")
def sign_credential(bpn):
    """
    Signs a credential using the private key provided in the configuration

    Receives:
        vc: :vc: unsigned verifiable credential
    Returns:
        response: :vc: Signed verifiable credential
    """
    try:
        body = HttpUtils.get_body(request)
        
        if(not op.path_exists("./keys")):
            op.make_dir("keys")

        basePath = "./keys/"+bpn
        if(not op.path_exists(basePath)):
            op.make_dir(basePath)

        privateKeyPath = basePath+"/private_key.pem"
        if(not op.path_exists(privateKeyPath)):
            private_key = cryptool.generateEd25519PrivateKey()
            public_key = private_key.public_key()
            cryptool.storePublicKey(public_key=cryptool.publicKeyToString(public_key), keysDir=basePath)
            cryptool.storePrivateKey(private_key=cryptool.privateJwkKeyToPemString(private_key=private_key),keysDir=basePath)
            logger.info(f"Created Keys for [{bpn}]!")
        else:
            private_key = cryptool.loadPrivateKey(keysDir=basePath)
            public_key = cryptool.loadPrivateKey(keysDir=basePath)

        keyPath = basePath+"/key.jwt"
        if(not op.path_exists(keyPath)):
            key = cryptool.loadJwkPublicKey(public_key_pem=cryptool.publicJwkKeyToPemString(public_key))
            publicJwtKey = key.export_public(as_dict=True)
            cryptool.storeJwkKey(key=key,keyPath=keyPath)
            logger.info(f"Created Public JWT Key with id [{publicJwtKey['kid']}] for [{bpn}]!")
        else:
            key = cryptool.loadJwkKey(keyPath=keyPath)
            publicJwtKey = key.export_public(as_dict=True)

        try:
            vc = cryptool.issueJwsVerifiableCredential(
                    walletUrl=request.root_url,                              
                    methodId=publicJwtKey["kid"],
                    expirationTimedelta=timedelta(weeks=24),
                    issuerId=bpn, 
                    private_key=private_key, 
                    credential=body
                )
        except Exception as e:
            return HttpUtils.get_error_response(status=500,message=str(e))
        
        cryptool.storeCredential(id=vc["id"].replace("urn:uuid:", ""), credential=vc, issuerId=bpn)

        logger.info(msg=f"Verifiable Credential with ID: [{str(vc["id"])}] was issued by IssuerId: [{str(bpn)}]!")

        return HttpUtils.response(op.to_json(vc))

    except Exception as e:
        logger.exception(str(e))
        traceback.print_exc()

    return HttpUtils.get_error_response(message="Error when parsing schema!")


@app.post("/context")
def context():
    """
    Generates a context for the verifiable credentials

    Receives:
        vc: :vc: unsigned verifiable credential
    Returns:
        response: :vc: schema
    """
    try:
        body = HttpUtils.get_body(request)
        
        semanticId = op.get_attribute(body, "semanticId")
        schema = op.get_attribute(body, "schema")
        aspectPrefix = op.get_attribute(body, "shortName")
        if not semanticId:
           HttpUtils.get_error_response(message="No semantic id specified", status=403)
        if not schema:
           HttpUtils.get_error_response(message="No schema specified", status=403)

        schemaParser = sammSchemaParser.sammSchemaParser()
        return HttpUtils.response(schemaParser.schema_to_jsonld(semanticId=semanticId, schema=schema, aspectPrefix=aspectPrefix))
    except Exception as e:
        logger.exception(str(e))
        traceback.print_exc()

    return HttpUtils.get_error_response(message="Error when parsing schema!")






if __name__ == '__main__':
    print("\n"+
        " __   __   __      __           __        ___                         ___ ___ \n"+
        "|  \\ |__) |__)    /__` |  |\\/| |__) |    |__     |  |  /\\  |    |    |__   |  \n"+ 
        "|__/ |    |       .__/ |  |  | |    |___ |___    |/\\| /~~\\ |___ |___ |___  |  \n"+                                                                           
        "\n\t\t\t\t\t\t\t\t\tv1.0.0")


    print("-------------------------------------------------------------------------------\n")
    print("Catena-X Digital Product Pass Verification Add-on")
    print("Copyright (c) 2023, 2024: BMW AG\n"
          +"Copyright (c) 2023, 2024: CGI Deutschland B.V. & Co. KG\n"
          +"Copyright (c) 2024: Contributors to the Eclipse Foundation\n")
    print("-------------------------------------------------------------------------------\n")
    print("Starting Logging, listening to requests...\n")
    # Initialize the server environment and get the comand line arguments
    args = get_arguments()
    # Configure the logging configuration depending on the configuration stated
    logger = logging.getLogger('staging')
    if(args.debug):
        logger = logging.getLogger('development')
    # Start the flask application     
    app.run(host=args.host, port=args.port, debug=args.debug)
    print("Closing the application... Thank you for using the DPP Simple Wallet!")