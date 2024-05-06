#################################################################################
# Tractus-X - Digital Product Passport Application
#
# Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
# Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
# Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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
import logging
import traceback
from logging.config import fileConfig
from utilities.httpUtils import HttpUtils
from utilities.constants import Constants
from utilities.operators import op
from utilities.authentication import Authentication

fileConfig('logging.ini')
logger = logging.getLogger()

access_token = ""
requests.packages.urllib3.disable_warnings()

def get_arguments():
    """
    Commandline argument handling. Return the populated namespace.

    Returns:
        args: :func:`parser.parse_args`
    """
    
    parser = argparse.ArgumentParser()
    parser.add_argument("-n", "--company", \
                        help="Company name required to login", required=False)
    
    parser.add_argument("-u", "--username", \
                        help="username required to login", required=False)
    
    parser.add_argument("-p", "--password", \
                        help="password required to login", required=False)
    
    parser.add_argument("-t", "--access_token", \
                        help="Access token to be used instead of username / password.", required=False)

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
    
    parser.add_argument("-a", "--appId", \
                        help="The client Id/app Id issued from the portal", required=True)

    parser.add_argument("-c", "--getChildren", action = 'store_true' , \
                        help="A boolean value to check if the passport contains children", required=False)
    
    args = parser.parse_args()
    return args

def create_process(manufacturer_part_id, session=None):
    """
    Discovery Phase.
    In the backend, the BPN and EDC Discovery services are called.

    Args:
        manufacturer_part_id (str): It identifies the manufacturer to the specific part produced. It refers to the search Id e.g., partInstanceId of the product.
        session (Session, optional): It authenticates and authorizes the APIs used to retrive the passport. Defaults to None.

    Returns:
        :func:`HttpUtils.response.json`: JSON payload of the backend.
    """

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
        logger.error(f"Exception occured while creating a process -> {str(exception)})")
        raise(exception)

def search_contract(process_id, serialized_id, id_type=None, semantic_id=None, session=None, children=None):
    """
    Search Phase.
    The backend searches for a serialized Id and retrieve its contract in a catalog.

    Args:
        process_id (str): The string identifier of the current process based on which the passport is requested.
        serialized_id (str): The serialized identifier of the part or product e.g., the value of the product partInstanceId.
        id_type (str, optional): The serialized attribute type of the product e.g., the product partInstanceId. Defaults to None.
        semantic_id (str, optional): The Id of the aspect model to be retrieved. Defaults to None.
        session (Session, optional): It authenticates and authorizes the APIs used to retrive the passport. Defaults to None.
        children (boolean, optional): A boolean value to check if requested product contains child components. Defaults to None.

    Returns:
        :func:`HttpUtils.response.json`: JSON payload of the backend.
    """

    data = {
        "id": serialized_id,
        "processId": process_id
    }
    if(semantic_id):
        data["semanticId"] = semantic_id

    if(id_type):
        data["idType"] = id_type

    if(children):
        data["children"] = children

    logger.debug(f"Searching for contract: {str(data)}")
    
    headers={
        "Authorization": "Bearer "+ access_token,
        "Content-Type": "application/json"
        }
    url = Constants.SERVER_URL + Constants.SEARCH_API
    try:
        response = HttpUtils.do_post(url=url, session=session, headers=headers, verify=False, json=data)
        return response.json()
    except Exception as exception:
        logger.error(f"Exception occured while searching for a contract -> {str(exception)})")
        raise(exception)

def negotiate_contract(process_id, contract_id, token, session=None):
    """
    This API starts the negotiation process in the backend and transfer of the passport.

    Args:
        process_id (str): The string identifier of the current process based on which the passport is requested.
        contract_id (str): A valid contract agreement Id is issued once negotiation process is done and further used in transfer process.
        token (str): The bearer token makes authentication to the backend APIs to retrieve the passport.
        session (Session, optional): It authenticates and authorizes the APIs used to retrive the passport. Defaults to None.

    Returns:
        :func:`HttpUtils.response.json`: JSON payload of the backend.
    """

    data = {
        "processId": process_id,
        "contractId": contract_id,
        "token": token
    }
    headers={
        "Authorization": "Bearer "+ access_token,
        "Content-Type": "application/json"
        }
    logger.debug(f"Negotiate contract: {str(data)}")

    url = Constants.SERVER_URL + Constants.AGREE_API
    try:    
        response = HttpUtils.do_post(url=url, session=session, headers=headers, verify=False, json=data)
        return response.json()
    except Exception as exception:
        logger.error(f"Exception occured while negotiating a contract -> {str(exception)})")
        raise(exception)

def get_status(process_id, session=None):
    """
    Get the process status once the contract is signed. This API is called until the status is ready to retrieve the passport.

    Args:
        process_id (str): The string identifier of the current process based on which the passport is requested.
        session (Session, optional): It authenticates and authorizes the APIs used to retrive the passport. Defaults to None.

    Returns:
        :func:`HttpUtils.response.json`: JSON payload of the backend.
    """

    headers={
        "Authorization": "Bearer "+ access_token
        }
    url = Constants.SERVER_URL + Constants.CHECK_STATUS_API + "/{0}".format(process_id)
    logger.debug(f"Checking contract negotiation status.")
    try:
        response = HttpUtils.do_get(url=url, session=session, headers=headers, verify=False)
        return response.json()
    except Exception as exception:
        logger.error(f"Exception occured while checking a contract negotiation status -> {str(exception)})")
        raise(exception)

def retrieve_passport(process_id, contract_id, token, session=None):
    """
    Decrypts the passport file and retrieves it.
    The passport is returned just once to the user and cannot be accessed anymore.
    A new process is initiated to retrieve passport again.

    Args:
        process_id (str): The string identifier of the current process based on which the passport is requested.
        contract_id (str): A valid contract agreement Id is issued once negotiation process is done and further used in transfer process. 
        token (str): The bearer token makes authentication to the backend APIs to retrieve the passport.
        session (Session, optional): It authenticates and authorizes the APIs used to retrive the passport. Defaults to None.

    Returns:
        :func:`HttpUtils.response.json`: JSON payload of the backend.
    """
    
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
    logger.debug(f"Retrieve passport: {str(data)}")
    try: 
        response = HttpUtils.do_post(url=url, session=session, headers=headers, verify=False, json=data)
        return response.json()
    except Exception as exception:
        logger.error(f"Exception occured while retrieving a passport -> {str(exception)})")
        raise(exception)

if __name__ == "__main__":

    try:
        # Extract the command line parameters
        args = get_arguments()
        company = args.company
        access_token = args.access_token
        username = args.username 
        password = args.password
        semantic_id = args.semanticId
        id_type = args.idType
        serialized_id = args.id
        discovery_type = args.discoveryType
        discovery_id = args.discoveryId
        app_id = args.appId
        children = args.getChildren

        retries = 0
        max_retries = Constants.API_MAX_RETRIES
        waiting_time = Constants.API_DELAY
        status_response = None
        passport_response = None

        logger.info(f"Retrieving passport: {discovery_id}:{serialized_id}")

        if(access_token):
            logger.debug(f"Access token found.")
            pass
        elif(username and password):
            logger.debug(f"Username/Password found.")
            if company is None:
                raise("Company is required along with user and password") 
            auth = Authentication(company, username, password, app_id)
            access_token = auth.get_access_token()
        else:
            raise("Either username+password or access_token must be specified.")

        # create a user session
        session = requests.Session()
        
        # create a process
        logger.debug(f"Create a process against manufacturerPartId: discovery_id")
        process_response = create_process(discovery_id, session)
        
        status = op.get_attribute(process_response, "status")
        if ((status and status != 200) or
            op.get_attribute(process_response, "data.processId") is None ):
            msg = "The contract request was not valid, no process id exists"
            logger.error(msg)
            raise Exception(msg)

        process_id = op.get_attribute(process_response, "data.processId")
        status = op.get_attribute(process_response, "status")
        logger.debug(f"Process created with ID {process_id}")

        # search for a contract
        negotiation_response = search_contract(process_id, serialized_id, id_type, semantic_id, session, children)
        if ((status and status != 200) or not negotiation_response["data"]):
            msg = "The contract was not available"
            logger.error(msg)
            raise Exception(msg)

        negotiation = negotiation_response["data"]
        token = op.get_attribute(negotiation, "token")
        contracts = op.get_attribute(negotiation, "contracts")

        #Get first contract key from dictionary
        firstContract = list(contracts.keys())[0]
        contract_id = op.get_attribute(contracts[firstContract], "@id")
        
        # If token or contract id does not exist -> error is returned
        if (not token or not contract_id):
            msg = "The contract request was not valid"
            logger.error(msg)
            raise Exception(msg)
        
        logger.info("Recieved a valid contract from the provider's registry")

        # Negotiate and sign the contract request
        status_response = negotiate_contract(process_id, contract_id, token, session)
        status = op.get_attribute(status_response, "status")

        if ((status and status != 200) or "data" not in negotiation_response.keys()):
            msg = "Error while getting the negotiation status"
            logger.error(msg)
            raise Exception(msg)

        if (status == "FAILED"):
            msg = "The negotiation process has failed"
            logger.error(msg)
            raise Exception(msg)
        
        passport = None
        while retries < max_retries: 
            status_response = get_status(process_id, session)
            status = op.get_attribute(status_response, "status")
            negotiation_status = op.get_attribute(status_response, "data.status")
            if (status is None):
                msg = "It was not possible to retrieve the negotiation status"
                logger.error(msg)
                raise Exception(msg)

            elif (status == "FAILED"):
                msg = "Failed to retrieve passport"
                logger.error(msg)
                raise Exception(msg)
            
            logger.info(f"Checked for a negotiation status: {negotiation_status}")

            op.wait(waiting_time)

            history = op.get_attribute(status_response, "data.history")
            if (op.get_attribute(history, "transfer-completed") and op.get_attribute(history, "data-received")):
                logger.info("The contract negotiation has been completed successfully")
                
                # retrieve the passport data
                passport_response = retrieve_passport(process_id, contract_id, token, session)
                
                status = op.get_attribute(passport_response, "status")
                if ((status and status != 200) or not passport_response["data"]):
                    msg = "Failed to retrieve passport"
                    logger.error(msg)
                    raise Exception(msg)

                passport = op.get_attribute(passport_response, "data.aspect")
                if(passport is not None):
                    semantic_id_passport = op.get_attribute(passport_response, "data.semanticId")
                    logger.info(f"Passport with semanticId {semantic_id_passport} retrieved successfully!")
                    break
            retries += 1
            
        if passport is None:
            msg = "Failed to retrieve passport, passport is empty"
            logger.error(msg)
            raise Exception(msg)

        # display the pasport data to console
        data = op.to_json(passport, indent=4)
        print("Passport: \n", data)

        if (Constants.EXPORT_TO_FILE):
            print("Export passport data to a file: passport.json")
            op.write_to_file(data=data, openMode='w', filePath="passport.json")

    except Exception as exception:
        logger.error(f"Exception: {str(exception)}")
        print(traceback.format_exc())
