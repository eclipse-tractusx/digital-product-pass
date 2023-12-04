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

import requests

class HttpUtils:

    # do post request without session
    def do_get(url,verify=False,headers=None,timeout=None):
        return requests.get(url=url,verify=verify,timeout=timeout,headers=headers)
    
    # do get request with session
    def do_get(url,session=None,verify=False,headers=None,timeout=None):
        if session is None:
            session = requests.Session()
        return session.get(url=url,verify=verify,timeout=timeout,headers=headers)
    
    # do post request without session
    def do_post(url,data=None,verify=False,headers=None,timeout=None,json=None):
        return requests.post(url=url,verify=verify,timeout=timeout,headers=headers,data=data,json=json)
    
    # do post request with session
    def do_post(url,session=None,data=None,verify=False,headers=None,timeout=None,json=None):
        if session is None:
            session = requests.Session()
        return session.post(url=url,verify=verify,timeout=timeout,headers=headers,data=data,json=json)
