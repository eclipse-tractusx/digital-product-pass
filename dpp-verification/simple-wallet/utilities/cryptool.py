#################################################################################
# Tractus-X - Digital Product Passport Verification Add-on
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

from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import serialization
import datetime
from utilities.operators import op
from utilities.httpUtils import HttpUtils
from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey
from pyld import jsonld
from urllib.parse import urljoin, urlparse
from cryptography.exceptions import InvalidSignature
from base64 import urlsafe_b64encode, urlsafe_b64decode
import json
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives.serialization import load_pem_private_key
import hashlib
import uuid
from jwcrypto import jwk
import logging

logger = logging.getLogger('staging')

SUPPORTED_VERIFICATION_TYPES = ["JsonWebSignature2020"]

import copy

class cryptool:
    @staticmethod
    def encodeBase64NoPadding(to_encode):
        return urlsafe_b64encode(to_encode).rstrip(b'=')

    @staticmethod
    def decodeBase64NoPadding(to_decode):
        return urlsafe_b64decode(to_decode+"==")

    @staticmethod
    def loadPrivateKey(private_key_content):
            private_key = load_pem_private_key(
            private_key_content, password=None, backend=default_backend())
            return private_key

    @staticmethod
    def storeJwkKey(key, keyPath="./key.jwt"):
        op.to_json_file(source_object=key.export(as_dict=True), json_file_path=keyPath)

    @staticmethod
    def loadJwkPublicKey(public_key_pem):
        return jwk.JWK.from_pem(public_key_pem)


    @staticmethod
    def loadJwkKey(keyPath):
        return jwk.JWK.from_json(op.to_json(op.read_json_file(keyPath)))

    @staticmethod
    def urlToDidWeb(url):
        """
            Created according to: https://w3c-ccg.github.io/did-method-web/#did-method-operations
        """
        newUrl = url
        newUrl = newUrl.replace("https://", "")
        newUrl = newUrl.replace("http://", "")
        newUrl = newUrl.replace(":", "%3A")
        newUrl = newUrl.replace("/", ":")
        return "did:web:"+newUrl.removesuffix(":")

    @staticmethod
    def resolveDidWeb(did_web):
        """
            Resolved according to: https://w3c-ccg.github.io/did-method-web/#did-method-operations
        """
        newUrl = did_web
        newUrl = newUrl.replace("did:web:", "")
        newUrl = newUrl.replace(":","/")
        newUrl = newUrl.replace("%3A",":")
        if("localhost" in newUrl) or ("127.0.0.1" in newUrl):
            newUrl = "http://" + newUrl
        else:
            newUrl = "https://" + newUrl
    
        path = urlparse(newUrl).path
        if(path is None) or (path == ""):
            path = "/.well-known"
        newUrl = urljoin(newUrl, path) 
        return newUrl + "/did.json"
    

    @staticmethod
    def issueJwsVerifiableCredential(walletUrl, methodId, issuerId, expirationTimedelta, private_key, credential):
        try:
            ## Generate the DID web for the wallet url
            didWeb = cryptool.urlToDidWeb(url=walletUrl)

            ## Expand verifiable credential to check if its a valid json-ld
            try:
                jsonld.expand(credential, {'keepFreeFloatingNodes': False})
            except Exception as e:
                raise RuntimeError(f"It was not possible to expand the json-ld credential! Invalid JSON-LD! Reason: [{str(e)}]")

            ## Issuance date and expiration date
            issuance_date = datetime.datetime.now(datetime.UTC).replace(microsecond=0)
            expiration_date = issuance_date + expirationTimedelta

            ## Prepare the issuer id and the id from the credential
            issuerDid = f"{didWeb}:{issuerId}"
            id = uuid.uuid4()
            ## Add the information to the credential
            credentialAttributes = {
                "id": f"urn:uuid:{id}",
                "issuer": issuerDid,
                "validFrom": issuance_date.strftime('%Y-%m-%dT%H:%M:%SZ'),
                "validUntil": expiration_date.strftime('%Y-%m-%dT%H:%M:%SZ')
            }

            credential.update(credentialAttributes)

            ## Prepare the header with the specification
            header = {
                'typ': 'vc+ld',
                'b64': False,
                'crv': 'Ed25519'
            }
            
            ## Prepare the content to sign
            to_sign = cryptool.encodeBase64NoPadding(json.dumps(header).encode('utf-8')) + b'.' + cryptool.encodeBase64NoPadding(json.dumps(credential).encode('utf-8'))
            decodedSignature = private_key.sign(data=to_sign)
            signature = cryptool.encodeBase64NoPadding(decodedSignature)
            

            ## Build the payload of the signature
            ## Build the jws signature
            jws = (cryptool.encodeBase64NoPadding(json.dumps(header).encode('utf-8')) + b'..' + signature).decode()

            ## Add the information to the proof
            credential["proof"] = {
                "type": "JsonWebSignature2020",
                "proofPurpose": "assertionMethod",
                "verificationMethod": f"{issuerDid}#{methodId}",
                "created": issuance_date.strftime('%Y-%m-%dT%H:%M:%SZ'),
                "jws": jws
            }

            return credential
        
        except Exception as e:
            raise RuntimeError(f"It was not possible to sign the verifiable credential! Reason: {str(e)}")

    @staticmethod
    def verifyVerifiableCredential(credential):
        global SUPPORTED_VERIFICATION_TYPES

        if("proof" not in credential):
            raise RuntimeError("Proof is not available in the Verifiable Credential!")

        if("expirationDate" in credential):
            currentDate = datetime.datetime.now(datetime.UTC)
            expirationDate = datetime.datetime.fromisoformat(credential["expirationDate"])
            if(expirationDate is None):
                raise RuntimeError("Invalid expiration date format!")
            
            if(currentDate >= expirationDate):
                raise RuntimeError("The verifiable credential is not valid! Expiration date has passed!")

        proof = credential["proof"]
        if("type" not in proof):
            raise RuntimeError("Verification Signature Type not found in the Verifiable Credential!")
        
        verificationType = proof["type"]
        if not (verificationType in SUPPORTED_VERIFICATION_TYPES):
            raise RuntimeError("Verification Signature Type is not supported!")
        
        try:
            if(verificationType == "JsonWebSignature2020"):
                resolution = cryptool.verifyJwsProof(proof, payload=credential)
            else:
                raise RuntimeError("Verification Signature Type is not supported!")

            if(not resolution):
                raise RuntimeError(f"It was not possible to verify the signature! Verifiable Credential is not valid!")
            
        except Exception as e:
            raise RuntimeError(f"It was not possible to verify the signature!  REASON: [{e}]")
    
        return True 


    @staticmethod
    def verifyJwsProof(proof, payload):
        if("jws" not in proof):
            raise RuntimeError("Verification Signature is not available")

        signature = proof["jws"]
        if(signature == ""):
            raise RuntimeError("Verification Signature is empty!")
        
        if("verificationMethod" not in proof):
            raise RuntimeError("Verification Method not found in the Verifiable Credential!")
        
        didMethod = proof["verificationMethod"]
        resolvedUrl = cryptool.resolveDidWeb(did_web=didMethod)
        try:
            content = HttpUtils.do_get(url=resolvedUrl, allow_redirects=True)
        except:
            raise RuntimeError(f"The content from the DID [{didMethod}] was not found in resolved URL [{resolvedUrl}]!")
        
        if(content is None):
            raise RuntimeError(f"No resposne received from resolved URL [{resolvedUrl}]!")
        
        if(content.content is None) or (content.content == ""):
            raise RuntimeError(f"No DID content received from resolved URL [{resolvedUrl}]!")

        try:
            didDocument = op.json_string_to_object(content.content)
        except:
            raise RuntimeError(f"The DID document is not a valid JSON!")
        if(didDocument is None):
            raise RuntimeError(f"The DID document its not available!")
        
        if not("verificationMethod" in didDocument):
            raise RuntimeError(f"The DID document has no verification method available!")

        publicKeysMethods = didDocument["verificationMethod"]
        publicKeyMethod = op.search_element_by_field(array=publicKeysMethods,id=didMethod, field="id")
        if (publicKeyMethod is None):
            raise RuntimeError(f"The public key method is not found in the DID document public keys list!")

        if not("type" in publicKeyMethod):
            raise RuntimeError(f"No type found in the public key method!")
        
        if(publicKeyMethod["type"] != "JsonWebKey2020"):
            raise RuntimeError(f"Public key method is not supported!")
        
        if not("publicKeyJwt" in publicKeyMethod):
            raise RuntimeError(f"No public key object found in the public key method!")
        
        publicKeyJwt = publicKeyMethod["publicKeyJwt"]
        
        key = jwk.JWK.from_json(key=op.to_json(publicKeyJwt))
        publicKeyPem = key.export_to_pem(private_key=False)
        public_key = cryptool.loadPublicKeyFromString(publicKeyPem)

        JWSignature = signature.split(".")
        header = cryptool.decodeBase64NoPadding(JWSignature[0]).decode("utf-8")
        signature = cryptool.decodeBase64NoPadding(JWSignature[2])
        credential = copy.deepcopy(payload)
        del credential["proof"]
        
        to_verify = cryptool.encodeBase64NoPadding(json.dumps(json.loads(header)).encode('utf-8')) + b'.' + cryptool.encodeBase64NoPadding(json.dumps(credential).encode('utf-8'))
        try:
            public_key.verify(signature=signature, data=to_verify)
        except InvalidSignature:
            raise RuntimeError("The credential is not verified! The signature was not able to be verified againts the data!")
        except Exception:
            raise RuntimeError("The credential is unverifiable! Something went wrong during the verification process!")
        
        return True 
    
    @staticmethod
    def generateEd25519PrivateKey():
        return Ed25519PrivateKey.generate()
    
    @staticmethod
    def privateJwkKeyToPemString(private_key):
        return private_key.private_bytes(
                encoding=serialization.Encoding.PEM,
                format=serialization.PrivateFormat.PKCS8,
                encryption_algorithm=serialization.NoEncryption()
            )
    
    @staticmethod
    def publicJwkKeyToPemString(public_key):
        return public_key.public_bytes(
            encoding=serialization.Encoding.PEM,
            format=serialization.PublicFormat.SubjectPublicKeyInfo
            )
    
    @staticmethod
    def loadPublicKeyFromString(stringKey):
        public_key = serialization.load_pem_public_key(
                stringKey,
                backend=default_backend()
            )
        return public_key
    
    @staticmethod
    def loadPrivateKeyFromString(stringKey):
        private_key = serialization.load_pem_private_key(
                    stringKey,
                    password=None,
                    backend=default_backend()
                )
        return private_key
    
    @staticmethod  
    def sha512(input):
        return hashlib.sha512(str(input).encode('utf-8')).hexdigest()


    @staticmethod
    def storePrivateKey(private_key, keysDir="./keys"):
        if(not op.path_exists(keysDir)):
            op.make_dir(keysDir)
        with open(f"{keysDir}/private_key.pem", 'wb') as f:
            f.write(private_key)

    @staticmethod
    def storePublicKey(public_key, keysDir="./keys"):
        if(not op.path_exists(keysDir)):
            op.make_dir(keysDir)
        with open(f"{keysDir}/public_key.pem", 'wb') as f:
            f.write(public_key)

    
    @staticmethod
    def storeCredential(id, credential, issuerId, dir="./credentials"):
        if(not op.path_exists(dir)):
            op.make_dir(dir)

        date = op.get_filedate()
        completeDir = f"{dir}/{issuerId}/{date}"
        if(not op.path_exists(completeDir)):
            op.make_dir(completeDir)

        op.to_json_file(credential, f"{completeDir}/{id}.jsonld")

            
    @staticmethod
    def loadPrivateKey(keysDir="./keys"):
        if(not op.path_exists(keysDir)):
            op.print_log(logType="CRITICAL", messageStr=f"Path [{keysDir}] not found. cryptool.loadKeys()")
            return None, None
        
        with open(f"{keysDir}/private_key.pem", "rb") as key_file:
                private_key = serialization.load_pem_private_key(
                    key_file.read(),
                    password=None,
                    backend=default_backend()
                )
        return private_key
    