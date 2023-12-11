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
from urllib.parse import urlparse, parse_qs
from urllib.parse import urlencode
from utilities.operators import op
import webbrowser
import json
import requests
import html
import re
import urllib.parse




"""
This class defines the authentication operations from the centrally managed keycloak instance
"""
class Authentication:

    auth_url = (
        f'{Constants.AUTH_URI}?'
        f'client_id={Constants.CLIENT_ID}&'
        f'response_type=code&'
        f'redirect_uri={Constants.REDIRECT_URI}&'
        f'scope={Constants.SCOPE}'
    )
    request_header_accept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"
    request_header_accept_encoding = "gzip, deflate, br"
    request_header_accept_language = "en-GB,en;q=0.9,de;q=0.8,en-US;q=0.7"

    _company_name = ""

    def __init__(self, company_name) -> None:
        
        _company_name = company_name

        central_idp = Constants.AUTH_URI
        state = "fooobarfoobar"
        try:
            shared_idp_url, cookie = self.get_company_shared_url(central_idp)
            op.wait(3)
            shared_idp_auth_url = self.get_auth_url_from_shared_idp(shared_idp_url, cookie)
            op.wait(3)
            self.authenticate_in_shared_idp(shared_idp_auth_url,"company 2 user","changeme", cookie)
            #op.wait(3)





            # response = requests.get(url=path,
            #             headers={
            #                     "Cookie": cookie, 
            #                     "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
            #                     "Accept-Encoding": "gzip, deflate, br",
            #                     "Accept-Language": "en-US",
            #                     'User-Agent': "Mozilla/5.0"

            #                     },
            #             allow_redirects=False
            #         )
            # print(response.raise_for_status())
        
        except requests.exceptions.HTTPError as errh: 
            print("HTTP Error") 
            print(errh.args) 
        # Prints the response code 
        # print(response) 

        #result = re.search('<form\s+.*?\s+action="(.*?)"', page, re.DOTALL)
        # xx = "guru99,education is fun"
        # r1 = re.findall(r"^\w+",xx)

        # response = requests.get("https://sharedidp.int.demo.catena-x.net/auth/realms/CX-Test-Access/protocol/openid-connect/auth?scope=openid&state=fooobarfoobar&response_type=code&client_id=Central-IdP&redirect_uri=https://centralidp.int.demo.catena-x.net/auth/realms/CX-Central/broker/CX-Test-Access/endpoint")
        # print(response.text)
        
        # response = requests.get(url="https://centralidp.int.demo.catena-x.net/auth/realms/CX-Central/broker/CX-Test-Access/login?client_id=Cl13-CX-Battery&amp;tab_id=fsGMt1AP4dY&amp;session_code=I1vhOK7wVEKSKbNRPw_0yVrDhV53d7Om3kutczwC44c")
        # print(response)
        # response = requests.get(url="https://sharedidp.int.demo.catena-x.net/auth/realms/CX-Test-Access/protocol/openid-connect/auth",
        #              params={
        #                 "response_type": "code",
        #                 "client_id": "Central-IdP",
        #                 "scope": Constants.SCOPE,
        #                 "redirect_uri": "https://centralidp.int.demo.catena-x.net/auth/realms/CX-Central/broker/CX-Test-Access/endpoint",
        #                 "state": state
        #              },
        #              allow_redirects=True
        #         )
        # print(response.text)
        # print(response.url)

        # page = response.text
        # url = re.findall(r'(https?://\S+)', page)
        # tt = re.search('<form\s+.*?\s+action="(.*?)"', page, re.DOTALL)
        # form_action = html.unescape(re.search('<form\s+.*?\s+action="(.*?)"', page, re.DOTALL).group(1))
        # # print(form_action)

        # resp = requests.post(
        # url=form_action, 
        # data={
        #     "username": "company 2 user",
        #     "password": "changeme",
        #     "credentialId": ""
        # }, 
        # headers={"Cookie": cookie},
        # allow_redirects=True
        # )
        # print(resp.status_code)
        # print(resp.text)




        # not working code
        # # Use requests or curl to perform the HTTP request programmatically
        # response = requests.get(self.auth_url)
        # print(response.url)
        # print(response.headers)
        # # Extract the authorization code from the response URL or headers
        # authorization_code = self.extract_code_from_response(response.url)
        # print("code: ",authorization_code)

        # Keycloak configuration
        # keycloak_url = 'https://centralidp.int.demo.catena-x.net/auth/realms/CX-Central'
        # client_id = 'your-client-id'
        # redirect_uri = 'your-redirect-uri'

        # # Step 1: Get authorization code
        # auth_params = {
        #     'client_id': Constants.CLIENT_ID,
        #     'redirect_uri': Constants.REDIRECT_URI,
        #     'response_type': 'code',
        #     'scope': 'openid',  # Add additional scopes as needed
        # }
        # auth_url = f'{keycloak_url}/protocol/openid-connect/auth?{urlencode(auth_params)}'
        # print(f'Open the following URL in your browser and authorize the application:\n{auth_url}')

        # # Retrieve the authorization code from the user after they authorize the application
        # authorization_code = input('Enter the authorization code: ')

        # # Step 2: Exchange authorization code for tokens
        # token_params = {
        #     'client_id': Constants.CLIENT_ID,
        #     'client_secret': '',
        #     'redirect_uri': Constants.REDIRECT_URI,
        #     'code': authorization_code,
        #     'grant_type': 'authorization_code',
        # }
        # token_url = f'{keycloak_url}/protocol/openid-connect/token'
        # token_response = requests.post(token_url, data=token_params)

        # # Extract tokens from the response
        # tokens = token_response.json()
        # access_token = tokens['access_token']
        # refresh_token = tokens.get('refresh_token')
        # id_token = tokens['id_token']

        # # Now you can use the access token to make authorized requests

        # # Example: Get user info using the access token
        # user_info_url = f'{keycloak_url}/protocol/openid-connect/userinfo'
        # headers = {'Authorization': f'Bearer {access_token}'}
        # user_info_response = requests.get(user_info_url, headers=headers)

        # print(f'User Info:\n{user_info_response.json()}')
        

    # def extract_code_from_response(self, response_url):
    #     parsed_url = urlparse(response_url)
    #     query_params = parse_qs(parsed_url.query)
    #     print(query_params)
    #     # Assuming the authorization code is returned as 'code' parameter
    #     authorization_code = query_params.get('code')
    #     return authorization_code
    
    # # Example usage
    # response_url = 'https://your-redirect-uri/?code=your-authorization-code&state=your-state'
    # authorization_code = extract_code_from_response(response_url)
    # print("Authorization Code:", authorization_code)

    # def __init__(self):
    #     auth_url = f'{Constants.AUTH_URI}?client_id={Constants.CLIENT_ID}&redirect_uri={Constants.REDIRECT_URI}&response_type=code&scope={Constants.SCOPE}'
    #     print("Authorization URL:", auth_url)
    #     webbrowser.open(auth_url)

        # callback_url = input("Enter the callback URL from the browser: ")
        # authorization_code = input("Enter the authorization code from the callback URL: ")


        # token_data = {
        #     'grant_type': 'authorization_code',
        #     'code': authorization_code,
        #     'client_id': Constants.CLIENT_ID,
        #     'redirect_uri': 'https://materialpass.int.demo.catena-x.net/passport',
        # }

        # token_response = requests.post(Constants.TOKEN_URI, data=token_data)
        # # token_response_data = token_response.json()
        # print(token_response.json())
        # access_token = token_response_data['access_token']
        # print("Access Token:", access_token)



    # def get_token()
    #     return token

    # def get_user_info()
    #     return get_user_info

    # def isauthenticated()
    #     return False

    # def get_access_token(username, password):

        #companies = StartCentralIdp();
        #sharedIdpCompanyUrl = GetCompanySharedIdpUrl(companies);
        #auth_url_from_shared_idp = get_auth_url_from_shared_idp(sharedIdpCompanyUrl);
        #authenticateInCentralIdpUrl =
        #     await AuthenticateInSharedIdp(auth_url_from_shared_idp, "company 2 user", "changeme");
        # authCode = await GetAuthCodeFromCentralIdp(authenticateInCentralIdpUrl);
        # return await GetTokenWithAuthCode(authCode);


    def get_company_shared_url(self, url):
        state = "fooobarfoobar"

        try:
            response = requests.get(
                url=url,
                params={
                    "response_type": "code",
                    "client_id": Constants.CLIENT_ID,
                    "scope": Constants.SCOPE,
                    "redirect_uri": Constants.REDIRECT_URI,
                    "state": state
                },
                allow_redirects=False
            )

            cookies = response.headers['Set-Cookie']
            cookie = '; '.join(c.split(';')[0] for c in cookies.split(', '))

            company_selection_page = response.text
            companies_array = re.findall(r'<pre[^>]*">([^<]+)</pre>', company_selection_page)
            companies_array = (op.json_string_to_object(op.json_string_to_object(op.to_json(companies_array[0]))))

            for company in companies_array:
                if company is not None and company["name"] == Constants.COMPANY:
                    url = company["url"]
                    break
            

            path = Constants.PROVIDER + url
            # print(path)
            # print(cookie)
            return path, cookie

        
        except requests.exceptions.HTTPError as errh: 
            print("HTTP Error") 
            print(errh.args)


    def get_auth_url_from_shared_idp(self, shared_idp_url, cookie):
        
        try:
            central_idp_url = Constants.PROVIDER.replace("https://", "")
            headers = {
                "Accept": self.request_header_accept,
                "Accept-Language": self.request_header_accept_language,
                "Accept-Encoding": self.request_header_accept_encoding,
                "Cookie": cookie
            }
            # headers = {
            #             "Cookie": cookie, 
            #             "Accept": "*/*",
            #             "Accept-Encoding": "gzip, deflate, br",
            #             "Host": central_idp_url
            #         }
            
            url = shared_idp_url.replace("amp;", "")
            print(url)
            response = requests.get(
                url=url,
                headers=headers
            )
            page =response.text
            form_url = html.unescape(re.search('<form\s+.*?\s+action="(.*?)"', page, re.DOTALL).group(1))
            if form_url is None:
                return None
            print(form_url)
            return form_url

            # response = requests.get(
            #     url=shared_idp_url,
            #     headers={"Accept": self.request_header_accept,
            #              "Accept-Language": self.request_header_accept_language,
            #              "Accept-Encoding": self.request_header_accept_encoding
            #              }

            # )
            # page = response.text
            # auth_url = html.unescape(re.search('<form\s+.*?\s+action="(.*?)"', page, re.DOTALL).group(1))

            # if (auth_url is None):
            #     print("Authentication failed: The auth url was not found")

            # return auth_url

        except requests.exceptions.HTTPError as errh: 
            print("HTTP Error") 
            print(errh.args)



    def authenticate_in_shared_idp(self, shared_idp_url, username, password, cookie):

        print(shared_idp_url)

        url = shared_idp_url.replace("amp;", "")
        headers = {
            "Accept": self.request_header_accept,
            "Accept-Language": self.request_header_accept_language,
            "Accept-Encoding": self.request_header_accept_encoding,
            "Origin": "null",
            "Cookie": cookie
        }
        data = {
            "companyName": "CX-Test-Access",
            "username": username, 
            "password": password,
            "credentialId": ""
        }
        print(data)
        print(headers)
        response = requests.post(
            url=url,
            headers=headers,
            data=data
        )

        print(response.text)
        


        # var authCodeOrUpdatePasswordUrl = response.Headers.Location?.AbsoluteUri;
        # if (authCodeOrUpdatePasswordUrl is null)
        # {
        #     throw new Exception("Authentication failed: the url for getting the auth code or updating the password was not found.");
        # }




