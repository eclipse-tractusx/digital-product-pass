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

from utilities.constants import Constants
import webbrowser
import requests


"""
This class defines the authentication operations from the centrally managed keycloak instance
"""
class Authentication:

    def __init__(self):
        auth_url = f'{Constants.AUTH_URI}?client_id={Constants.CLIENT_ID}&redirect_uri={Constants.REDIRECT_URI}&response_type=code&scope={Constants.SCOPE}'
        print("Authorization URL:", auth_url)
        webbrowser.open(auth_url)

        callback_url = input("Enter the callback URL from the browser: ")
        authorization_code = input("Enter the authorization code from the callback URL: ")


        token_data = {
            'grant_type': 'authorization_code',
            'code': authorization_code,
            'client_id': Constants.CLIENT_ID,
            'redirect_uri': 'https://materialpass.int.demo.catena-x.net/passport',
        }

        token_response = requests.post(Constants.TOKEN_URI, data=token_data)
        # token_response_data = token_response.json()
        print(token_response.json())
        # access_token = token_response_data['access_token']
        # print("Access Token:", access_token)



    # def get_token()
    #     return token

    # def get_user_info()
    #     return get_user_info

    # def isauthenticated()
    #     return False


