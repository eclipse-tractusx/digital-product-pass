#################################################################################
# Tractus-X -  Digital Product Passport Application
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

from utilities.constants import Constants
from urllib.parse import urlparse, parse_qs
from utilities.operators import op
from utilities.httpUtils import HttpUtils, AuthenticationFailed, CompanyNotFound, UrlNotFound, CodeNotFound, TokenNotFound
import requests
import html
import re

"""
This class defines the authentication operations from the centrally managed keycloak instance
"""
class Authentication:

    _request_header_accept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"
    _request_header_accept_encoding = "gzip, deflate, br"
    _request_header_accept_language = "en-GB,en;q=0.9,de;q=0.8,en-US;q=0.7"

    _company_name = ""
    _username = ""
    _password = ""
    _central_idp = ""
    _provider = ""
    _redirect_uri = ""
    _token_uri = ""
    _client_id = ""
    _scope = ""
    _shared_idp_cookie = ""
    _central_idp_cookie = ""


    def __init__(self, company_name, username, password, appId) -> None:
        
        self._company_name = company_name
        self._username = username
        self._password = password
        self._client_id = appId
        self._redirect_uri = Constants.REDIRECT_URI
        self._token_uri = Constants.TOKEN_URI
        self._scope = Constants.SCOPE
        self._central_idp = Constants.AUTH_URI
        self._provider = Constants.PROVIDER    

    def get_access_token(self) -> str:
        try:
            shared_idp_url, self._central_idp_cookie = self.get_company_shared_url(self._central_idp)

            shared_idp_auth_url, self._shared_idp_cookie = self.get_auth_url_from_shared_idp(shared_idp_url)

            authenticate_in_central_idp_url  = self.authenticate_in_shared_idp(shared_idp_auth_url)

            auth_code = self.get_auth_code_from_central_idp(authenticate_in_central_idp_url)

            token = self.get_token_with_auth_code(auth_code)
            return token
        
        except requests.exceptions.HTTPError as err:
            print(err)

    def get_company_shared_url(self, url):
        
        params={
            "response_type": "code",
            "client_id": self._client_id,
            "scope": self._scope,
            "redirect_uri": self._redirect_uri
            }

        try:
            response = HttpUtils.do_get(url=url,params=params,allow_redirects=False)
            cookies = response.headers['Set-Cookie']
            cookie = '; '.join(c.split(';')[0] for c in cookies.split(', '))

            company_selection_page = response.text
            companies_array = re.findall(r'<pre[^>]*">([^<]+)</pre>', company_selection_page)
            companies_array = (op.json_string_to_object(op.json_string_to_object(op.to_json(companies_array[0]))))

            for company in companies_array:
                if company is not None and company["name"] == self._company_name:
                    url = company["url"]
                    break
            if url is None:
                raise CompanyNotFound
            path = self._provider + url
            return path, cookie

        except CompanyNotFound:
            print("CompanyNotFound exception occured in [get_company_shared_url]: The company was not found")
        except requests.exceptions.HTTPError as err: 
            print("HTTP exception occured in [get_company_shared_url]: " + err)
        except Exception as e:
            print("Exception occured in [get_company_shared_url]: " + e)


    def get_auth_url_from_shared_idp(self, shared_idp_url):
        
        headers={
            "Accept": self._request_header_accept,
            "Accept-Language": self._request_header_accept_language,
            "Accept-Encoding": self._request_header_accept_encoding,
            "Cookie": self._central_idp_cookie
        }
        try:
            url = shared_idp_url.replace("amp;", "")
            response = HttpUtils.do_get(url=url, headers=headers,allow_redirects=True)
            page =response.text
            cookies = response.headers['Set-Cookie']
            cookie = '; '.join(c.split(';')[0] for c in cookies.split(', '))
            form_url = html.unescape(re.search('<form\s+.*?\s+action="(.*?)"', page, re.DOTALL).group(1))
            if form_url is None:
                raise UrlNotFound
            return form_url, cookie

        except UrlNotFound:
            print("UrlNotFound exception occured in [get_auth_url_from_shared_idp]: The shared idp url is not found")
        except requests.exceptions.HTTPError as err: 
            print("HTTP exception occured in [get_auth_url_from_shared_idp]:" + err)
        except Exception as e:
            print("Exception occured in [get_auth_url_from_shared]: " + e)



    def authenticate_in_shared_idp(self, shared_idp_url):

        url = shared_idp_url.replace("amp;", "")
        headers = {
            "Accept": self._request_header_accept,
            "Accept-Language": self._request_header_accept_language,
            "Accept-Encoding": self._request_header_accept_encoding,
            "Cookie": self._shared_idp_cookie,
            "Origin": "null",
            "Content-Type": "application/x-www-form-urlencoded"
        }

        data = {
            "companyName": self._company_name,
            "username": self._username, 
            "password": self._password, 
            "credentialId": ""
        }
        try:
            response = HttpUtils.do_post(url=url, headers=headers, data=data,allow_redirects=False)

            if (not response.headers["Location"]):
                raise AuthenticationFailed
            
            auth_code_url = response.headers["Location"]
            return auth_code_url
        
        except AuthenticationFailed: 
            print("AuthenticationFailed exception occured in [authenticate_in_shared_idp]: Authentication failed, the url for getting the auth code was not found.")
        except requests.exceptions.HTTPError as err:
            print("HTTP exception occured in [authenticate_in_shared_idp]: " + err)
        except Exception as e:
            print("Exception occured in [authenticate_in_shared_idp]: " + e)
    
    def get_auth_code_from_central_idp(self, auth_code_url):
        
        headers = {
                "Accept": self._request_header_accept,
                "Accept-Language": self._request_header_accept_language,
                "Accept-Encoding": self._request_header_accept_encoding,
                "Cookie": self._central_idp_cookie
            }
        try:
            response = HttpUtils.do_get(url=auth_code_url, headers=headers,allow_redirects=False)

            if (not response.headers["Location"]):
                raise UrlNotFound
            
            url = response.headers["Location"]
            parsed_url = urlparse(url)
            auth_code = parse_qs(parsed_url.query)['code'][0]
            if auth_code is None:
                raise CodeNotFound

            return auth_code
        
        except CodeNotFound:
            print("CodeNotFound exception occured in [get_auth_code_from_central_idp]: Authorization code was not found")
        except UrlNotFound:
            print("UrlNotFound exception occured in [get_auth_code_from_central_idp]: Authentication failed, the url for getting the auth code was not found")
        except requests.exceptions.HTTPError as err: 
            print("HTTP exception occured in [get_auth_code_from_central_idp]: " + err)
        except Exception as e:
            print("Exception occured in [get_auth_code_from_central_idp]: " + e)
    
    def get_token_with_auth_code(self, auth_code):

        headers = {
                "Accept": "*/*",
                "Accept-Language": self._request_header_accept_language,
                "Accept-Encoding": self._request_header_accept_encoding,
                "Origin": self._redirect_uri,
                "Referer": self._redirect_uri,
                "Cookie": self._shared_idp_cookie,
                "Content-Type": "application/x-www-form-urlencoded"
            }
        data = {
            "code": auth_code,
            "grant_type": "authorization_code", 
            "client_id": self._client_id,
            "redirect_uri": self._redirect_uri
        }
        try:
            response = HttpUtils.do_post(url=self._token_uri, headers=headers, data=data,allow_redirects=False)
            token = response.json()
            token = op.json_string_to_object(op.to_json(token))["access_token"]
            if (token is None):
                raise TokenNotFound
            return token
        
        except TokenNotFound:
            print("TokenNotFound exception occured in [get_token_with_auth_code]: Access Token was not found")
        except requests.exceptions.HTTPError as err: 
            print("HTTP exception occured in [get_token_with_auth_code]: " + err)
        except Exception as e:
            print("Exception occured in [get_token_with_auth_code]: " + e)






