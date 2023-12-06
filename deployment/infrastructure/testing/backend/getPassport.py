#################################################################################
# Catena-X - Product Passport Consumer Application
#
# Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

import argparse
import json
import math
import requests
import asyncio
import logging
import time
import uuid
import getopt, sys
from copy import copy
from requests.adapters import HTTPAdapter, Retry
from utilities.httpUtils import HttpUtils
from utilities.constants import Constants
from utilities.operators import op
from utilities.authentication import Authentication

access_token = ""
requests.packages.urllib3.disable_warnings()



def get_arguments():
    
    parser = argparse.ArgumentParser()
    try:
        parser.add_argument("-s", "--semanticId", \
                            default="urn:bamm:io.catenax.generic.digital_product_passport:1.0.0#DigitalProductPassport", \
                            help="Semantic ID of the aspect model", required=False)
        
        parser.add_argument("-it", "--idType", default="partInstanceId", \
                            help="Product type attribute to lookup into digital twin registry", required=False)
        
        parser.add_argument("-id", "--id", help="The product type value to lookup into the digital twin registry", \
                            required=True)
        
        parser.add_argument("-dt", "--discoveryType", default="manufacturerPartId", \
                            help="The discovery type attribute to lookup into the discovery service", required=False)

        parser.add_argument("-di", "--discoveryId", \
                             help="The discovery type value to lookup into the discovery service", required=True)

        parser.add_argument("-c", "--getChildren", action = 'store_true' , \
                            help="A boolean value to check if the passport contains children", required=False)
        
        parser.add_argument("-H", "--Help", help="To display the help menu", required=False)
        # parser.add_argument('--flag', dest = 'flag', action = 'store_true', help = 'Set the flag value to True.')
        # parser.add_argument('--no-flag', dest = 'flag', action = 'store_false', help = 'Set the flag value to False.')
        args = parser.parse_args()
        # print('flag: ', args.flag)
        return args
                
    except argparse.ArgumentError as error:
        # output error, and return with an error code
        print (str(error))

def create_process(manufacturer_part_id, session=None):
    
    data = {
        "id": manufacturer_part_id,
    }
    headers={
        "Authorization": access_token,
        "Content-Type": "application/json"
        }
    url = Constants.SERVER_URL + Constants.CREATE_API
    try:
        response = HttpUtils.do_post(url=url, session=session, headers=headers, verify=False, json=data)
        return response.json()
    
    except Exception as exception:
        if Constants.IS_LOG_ENABLED:
            op.print_log("Exception occurs while creating a process -> ", "ERROR", exception)
        print("[ERROR] - Exception occurs while creating a process -> ", exception)

def search_contract(process_id, serialized_id, id_type, semantic_id, session=None):
    data = {
        "id": serialized_id,
        "processId": process_id,
        "idType": id_type,
        "semanticId": semantic_id
    }
    headers={
        "Authorization": access_token,
        "Content-Type": "application/json"
        }
    url = Constants.SERVER_URL + Constants.SEARCH_API
    try:
        response = HttpUtils.do_post(url=url, session=session, headers=headers, verify=False, json=data)
        return response.json()
    
    except Exception as exception:
        if Constants.IS_LOG_ENABLED:
            op.print_log("Exception occurs while searching for a contract -> ", "ERROR", exception)
        print("[ERROR] - Exception occurs while searching for a contract -> " , exception)

def negotiate_contract(process_id, contract_id, token, session=None):
    data = {
        "processId": process_id,
        "contractId": contract_id,
        "token": token
    }
    headers={
        "Authorization": access_token,
        "Content-Type": "application/json"
        }
    url = Constants.SERVER_URL + Constants.SIGN_API
    try:    
        response = HttpUtils.do_post(url=url, session=session, headers=headers, verify=False, json=data)
        return response.json()
    except Exception as exception:
        if Constants.IS_LOG_ENABLED:
            op.print_log("Exception occurs while negotiating a contract -> ", "ERROR", exception)
        print("[ERROR] - Exception occurs while negotiating  a contract -> " , exception)

def get_status(process_id, session=None):

    headers={
        "Authorization": access_token
        }
    url = Constants.SERVER_URL + Constants.CHECK_STATUS_API + "/{0}".format(process_id)
    try:
        response = HttpUtils.do_get(url=url, session=session, headers=headers, verify=False)
        return response.json()
    except Exception as exception:
        if Constants.IS_LOG_ENABLED:
            op.print_log("Exception occurs while checking a contract negotiation status -> ", "ERROR", exception)
        print("[ERROR] - Exception occurs while checking  a contract negotiation status -> " , exception)

def retrieve_passport(process_id, contract_id, token, session=None):
    
    data = {
        "processId": process_id,
        "contractId": contract_id,
        "token": token
    }
    headers={
        "Authorization": access_token,
        "Content-Type": "application/json"
        }
    url = Constants.SERVER_URL + Constants.RETRIEVE_PASSPORT_API   
    try: 
        response = HttpUtils.do_post(url=url, session=session, headers=headers, verify=False, json=data)
        return response.json()
    except Exception as exception:
        if Constants.IS_LOG_ENABLED:
            op.print_log("Exception occurs while retrieving a passport -> ", "ERROR", exception)
        print("[ERROR] - Exception occurs while retrieving a passport -> " , exception)


if __name__ == "__main__":


    # auth = Authentication()
    retries = 0
    max_retries = Constants.API_MAX_RETRIES
    waiting_time = Constants.API_DELAY
    status_response = None
    passport_response = None
    is_log_enabled = Constants.IS_LOG_ENABLED

    try: 
        args = get_arguments()
        semantic_id = args.semanticId
        id_type = args.idType
        serialized_id = args.id
        discovery_type = args.discoveryType
        discovery_id = args.discoveryId
        children = args.getChildren

        # if children:
        #     print("Displaying Output as: % s" % children)

        # create a user session
        session = requests.Session()
        
        # create a process
        process_response = create_process(discovery_id, session)
        if is_log_enabled:
            op.print_log("Create a process against manufacturerPartId: ", discovery_id)
        print("[INFO] - Create a process against manufacturerPartId: ", discovery_id)
        
        status = op.get_attribute(process_response, "status")
        if ((status and status != 200) or
            op.get_attribute(process_response, "data.processId") is None ):
            if is_log_enabled:
                op.print_log("The contract request was not valid, no process id exists", "ERROR")
            raise Exception("[ERROR] - The contract request was not valid, no process id exists")

        process_id = op.get_attribute(process_response, "data.processId")
        status = op.get_attribute(process_response, "status")
        
        if is_log_enabled:
            op.print_log("Process created with ID ", process_id)
        print("[INFO] - Process created with ID ", process_id)

        
        # search for a contract
        negotiation_response = search_contract(process_id, serialized_id, id_type, semantic_id, session)
        if ((status and status != 200) or not negotiation_response["data"]):
            if is_log_enabled:
                op.print_log("The contract was not available", "ERROR")
            raise Exception("[ERROR] - The contract was not available")


        negotiation = negotiation_response["data"]
        token = op.get_attribute(negotiation, "token")
        contract_id = op.get_attribute(negotiation, "contract.@id")
        
        # If token or contract id does not exist -> error is returned
        if (not token or not contract_id):
            if is_log_enabled:
                op.print_log("The contract request was not valid", "ERROR")
            raise Exception("[ERROR] - The contract request was not valid")
        
        if is_log_enabled:
            op.print_log("Recieved a valid contract from the provider's registry", "INFO")
        print("[INFO] - Recieved a valid contract from the provider's registry")

        # Negotiate and sign the contract request
        status_response = negotiate_contract(process_id, contract_id, token, session)
        status = op.get_attribute(status_response, "status")

        if ((status and status != 200) or not status_response["data"]):
            if is_log_enabled:
                op.print_log("Error while getting the negotiation status", "ERROR")
            raise Exception("[ERROR] - Error while getting the negotiation status")


        if (status == "FAILED"):
            if is_log_enabled:
                op.print_log("The negotiation process has failed", "ERROR")
            raise Exception("[ERROR] - The negotiation process has failed")
        
        while retries < max_retries:
            status_response = get_status(process_id, session)
            status = op.get_attribute(status_response, "status")
            negotiation_status = op.get_attribute(status_response, "data.status")
            if (status is None):
                if is_log_enabled:
                    op.print_log("It was not possible to retrieve the negotiation status", "ERROR")
                raise Exception("[ERROR] - It was not possible to retrieve the negotiation status")

            elif (status == "FAILED"):
                if is_log_enabled:
                    op.print_log("Failed to retrieve passport", "ERROR")
                raise Exception("[ERROR] - Failed to retrieve passport")

            if is_log_enabled:
                op.print_log("Checking for a negotiation status...", "INFO")
            print("[INFO] - Checking for a negotiation status: ", negotiation_status)

            op.wait(waiting_time)
            retries += 1

        history = op.get_attribute(status_response, "data.history")
        if (op.get_attribute(history, "transfer-completed") and op.get_attribute(history, "data-received")):
            if is_log_enabled:
                op.print_log("The contract negotiation has been completed successfully", "INFO")
            print("[INFO] - The contract negotiation has been completed successfully")
            
            # retrieve the passport data
            passport_response = retrieve_passport(process_id, contract_id, token, session)
            
            status = op.get_attribute(passport_response, "status")
            if ((status and status != 200) or not passport_response["data"]):
                if is_log_enabled:
                    op.print_log("Failed to retrieve passport", "ERROR")
                raise Exception("[ERROR] - Failed to retrieve passport")

        passport = op.get_attribute(passport_response, "data.aspect")
        if is_log_enabled:
            op.print_log("Passport retrieved successfully", "INFO")
        print("[INFO] - Passport retrieved successfully")

        # display the pasport data to console
        data = op.to_json(passport, indent=4)
        print("Passport: \n", data)

        if (Constants.EXPORT_TO_FILE):
            print("Export passport data to a file: passport.json")
            op.write_to_file(data=data, openMode='w', filePath="passport.json")

    except Exception as exception:
        if is_log_enabled:
            op.print_log(exception)
        print(exception)









