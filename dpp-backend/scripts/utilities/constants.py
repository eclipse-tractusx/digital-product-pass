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

class Constants:

    PROVIDER = "https://centralidp.int.demo.catena-x.net"
    TOKEN_URI = "https://centralidp.int.demo.catena-x.net/auth/realms/CX-Central/protocol/openid-connect/token"
    AUTH_URI = "https://centralidp.int.demo.catena-x.net/auth/realms/CX-Central/protocol/openid-connect/auth"
    REDIRECT_URI = "https://dpp.int.demo.catena-x.net"
    REALM = "CX-Central"
    SCOPE = "openid"
    SERVER_URL = "https://dpp.int.demo.catena-x.net"
    CREATE_API = "/api/contract/create"
    SEARCH_API = "/api/contract/search"
    AGREE_API = "/api/contract/agree"
    CHECK_STATUS_API = "/api/contract/status"
    RETRIEVE_PASSPORT_API = "/api/data"
    API_MAX_RETRIES = 3
    API_DELAY = 3
    EXPORT_TO_FILE = False

