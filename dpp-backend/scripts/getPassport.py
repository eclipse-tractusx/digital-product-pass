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
import requests
from utilities.httpUtils import HttpUtils
from utilities.constants import Constants
from utilities.operators import op
from utilities.authentication import Authentication

access_token = ""
requests.packages.urllib3.disable_warnings()

def get_arguments():
    
    parser = argparse.ArgumentParser()
    try:
        parser.add_argument("-n", "--company", \
                            help="Company name required to login", required=True)
        
        parser.add_argument("-u", "--username", \
                            help="username required to login", required=True)
        
        parser.add_argument("-p", "--password", \
                            help="password required to login", required=True)

        parser.add_argument("-s", "--semanticId", \
                            help="Semantic ID of the aspect model", required=False)
        
        parser.add_argument("-it", "--idType", \
                            help="Product type attribute to lookup into digital twin registry", required=False)
        
        parser.add_argument("-id", "--id", help="The product type value to lookup into the digital twin registry", \
                            required=True)
        
        parser.add_argument("-dt", "--discoveryType", default="manufacturerPartId", \
                            help="The discovery type attribute to lookup into the discovery service", required=False)

        parser.add_argument("-di", "--discoveryId", \
                             help="The discovery type value to lookup into the discovery service", required=True)

        parser.add_argument("-c", "--getChildren", action = 'store_true' , \
                            help="A boolean value to check if the passport contains children", required=False)
        
        args = parser.parse_args()
        return args
                
    except argparse.ArgumentError as error:
        # output error, and return with an error code
        op.print_message("Argument Error: " + str(error), info_level="[ERROR]", log_enabled=is_log_enabled)

def create_process(manufacturer_part_id, session=None):
    
    data = {
        "id": manufacturer_part_id,
    }
    headers={
        "Authorization": "Bearer "+ access_token,
        "Content-Type": "application/json"
        }
    url = Constants.SERVER_URL + Constants.CREATE_API
    try:
        response = HttpUtils.do_post(url=url, session=session, headers=headers, verify=False, json=data)
        return response.json()
    
    except Exception as exception:
        op.print_message("Exception occured while creating a process -> " + exception, info_level="[ERROR]", log_enabled=is_log_enabled)

def search_contract(process_id, serialized_id, id_type=None, semantic_id=None, session=None, children=None):
    data = {
        "id": serialized_id,
        "processId": process_id
    }
    if(semantic_id is not None):
        data["semanticId"] = semantic_id

    if(id_type is not None):
        data["idType"] = id_type

    if(children is not None):
        data["children"] = children

    print(data)
    headers={
        "Authorization": "Bearer "+ access_token,
        "Content-Type": "application/json"
        }
    url = Constants.SERVER_URL + Constants.SEARCH_API
    try:
        response = HttpUtils.do_post(url=url, session=session, headers=headers, verify=False, json=data)
        return response.json()
    
    except Exception as exception:
        op.print_message("Exception occured while searching for a contract -> " + exception, info_level="[ERROR]", log_enabled=is_log_enabled)

def negotiate_contract(process_id, contract_id, token, session=None):
    data = {
        "processId": process_id,
        "contractId": contract_id,
        "token": token
    }
    headers={
        "Authorization": "Bearer "+ access_token,
        "Content-Type": "application/json"
        }
    url = Constants.SERVER_URL + Constants.AGREE_API
    try:    
        response = HttpUtils.do_post(url=url, session=session, headers=headers, verify=False, json=data)
        return response.json()
    
    except Exception as exception:
        op.print_message("Exception occured while negotiating a contract -> " + exception, info_level="[ERROR]", log_enabled=is_log_enabled)

def get_status(process_id, session=None):

    headers={
        "Authorization": "Bearer "+ access_token
        }
    url = Constants.SERVER_URL + Constants.CHECK_STATUS_API + "/{0}".format(process_id)
    try:
        response = HttpUtils.do_get(url=url, session=session, headers=headers, verify=False)
        return response.json()
        
    except Exception as exception:
        op.print_message("Exception occured while checking a contract negotiation status -> " + exception, info_level="[ERROR]", log_enabled=is_log_enabled)

def retrieve_passport(process_id, contract_id, token, session=None):
    
    data = {
        "processId": process_id,
        "contractId": contract_id,
        "token": token
    }
    headers={
        "Authorization": "Bearer "+ access_token,
        "Content-Type": "application/json"
        }
    url = Constants.SERVER_URL + Constants.RETRIEVE_PASSPORT_API   
    try: 
        response = HttpUtils.do_post(url=url, session=session, headers=headers, verify=False, json=data)
        return response.json()
    except Exception as exception:
        op.print_message("Exception occured while retrieving a passport -> " + exception, info_level="[ERROR]", log_enabled=is_log_enabled)

if __name__ == "__main__":

    try:
        # Extract the command line parameters
        args = get_arguments()
        company = args.company
        username = args.username 
        password = args.password
        semantic_id = args.semanticId
        id_type = args.idType
        serialized_id = args.id
        discovery_type = args.discoveryType
        discovery_id = args.discoveryId
        children = args.getChildren

        auth = Authentication(company, username, password)
        access_token = auth.get_access_token()
        retries = 0
        max_retries = Constants.API_MAX_RETRIES
        waiting_time = Constants.API_DELAY
        status_response = None
        passport_response = None
        is_log_enabled = Constants.IS_LOG_ENABLED

        # create a user session
        session = requests.Session()
        
        # create a process
        process_response = create_process(discovery_id, session)
        op.print_message("Create a process against manufacturerPartId: " + discovery_id, log_enabled=is_log_enabled)
        
        status = op.get_attribute(process_response, "status")
        if ((status and status != 200) or
            op.get_attribute(process_response, "data.processId") is None ):
            op.print_message("The contract request was not valid, no process id exists", info_level="[ERROR]", log_enabled=is_log_enabled)
            raise Exception("[ERROR] - The contract request was not valid, no process id exists")

        process_id = op.get_attribute(process_response, "data.processId")
        status = op.get_attribute(process_response, "status")
        op.print_message("Process created with ID " + process_id, log_enabled=is_log_enabled)

        # search for a contract
        negotiation_response = search_contract(process_id, serialized_id, id_type, semantic_id, session, children)
        if ((status and status != 200) or not negotiation_response["data"]):
            op.print_message("The contract was not available", info_level="[ERROR]", log_enabled=is_log_enabled)
            raise Exception("[ERROR] - The contract was not available")

        negotiation = negotiation_response["data"]
        token = op.get_attribute(negotiation, "token")
        contracts = op.get_attribute(negotiation, "contracts")

        #Get first contract key from dictionary
        firstContract = list(contracts.keys())[0]
        contract_id = op.get_attribute(contracts[firstContract], "@id")
        
        # If token or contract id does not exist -> error is returned
        if (not token or not contract_id):
            op.print_message("The contract request was not valid", info_level="[ERROR]", log_enabled=is_log_enabled)
            raise Exception("[ERROR] - The contract request was not valid")
        
        op.print_message("Recieved a valid contract from the provider's registry", log_enabled=is_log_enabled)

        # Negotiate and sign the contract request
        status_response = negotiate_contract(process_id, contract_id, token, session)
        status = op.get_attribute(status_response, "status")

        if ((status and status != 200) or not status_response["data"]):
            op.print_message("Error while getting the negotiation status", info_level="[ERROR]", log_enabled=is_log_enabled)
            raise Exception("[ERROR] - Error while getting the negotiation status")

        if (status == "FAILED"):
            op.print_message("The negotiation process has failed", info_level="[ERROR]", log_enabled=is_log_enabled)
            raise Exception("[ERROR] - The negotiation process has failed")
        
        while retries < max_retries:
            status_response = get_status(process_id, session)
            status = op.get_attribute(status_response, "status")
            negotiation_status = op.get_attribute(status_response, "data.status")
            if (status is None):
                op.print_message("It was not possible to retrieve the negotiation status", info_level="[ERROR]", log_enabled=is_log_enabled)
                raise Exception("[ERROR] - It was not possible to retrieve the negotiation status")

            elif (status == "FAILED"):
                op.print_message("Failed to retrieve passport", info_level="[ERROR]", log_enabled=is_log_enabled)
                raise Exception("[ERROR] - Failed to retrieve passport")
            
            op.print_message("Checking for a negotiation status: " + negotiation_status, log_enabled=is_log_enabled)

            op.wait(waiting_time)
            retries += 1

        history = op.get_attribute(status_response, "data.history")
        if (op.get_attribute(history, "transfer-completed") and op.get_attribute(history, "data-received")):
            op.print_message("The contract negotiation has been completed successfully", log_enabled=is_log_enabled)
            
            # retrieve the passport data
            passport_response = retrieve_passport(process_id, contract_id, token, session)
            
            status = op.get_attribute(passport_response, "status")
            if ((status and status != 200) or not passport_response["data"]):
                op.print_message("Failed to retrieve passport", info_level="[ERROR]", log_enabled=is_log_enabled)
                raise Exception("[ERROR] - Failed to retrieve passport")

        passport = op.get_attribute(passport_response, "data.aspect")
        op.print_message("Passport retrieved successfully", log_enabled=is_log_enabled)

        # display the pasport data to console
        data = op.to_json(passport, indent=4)
        print("Passport: \n", data)

        if (Constants.EXPORT_TO_FILE):
            print("Export passport data to a file: passport.json")
            op.write_to_file(data=data, openMode='w', filePath="passport.json")

    except Exception as exception:
        op.print_message(exception, info_level="[ERROR]", log_enabled=is_log_enabled)









