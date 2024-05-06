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

ROOT_DIR=/usr/share/nginx/html

echo "Replacing docker environment constants in JavaScript files"

for file in $ROOT_DIR/assets/index-*.js* $ROOT_DIR/index.html;
do
	echo "Processing $file ...";
	sed -i 's|IDENTITY_PROVIDER_URL|'${IDP_URL}'|g' $file
	sed -i 's|HOST_URL|'${SERVER_URL}'|g' $file
	sed -i 's|DATA_URL|'${BACKEND_URL}'|g' $file
	sed -i 's|APP_VERSION|'${VERSION}'|g' $file
	sed -i 's|API_SEARCH_TIMEOUT|'${API_SEARCH_TIMEOUT}'|g' $file
	sed -i 's|API_NEGOTIATE_TIMEOUT|'${API_NEGOTIATE_TIMEOUT}'|g' $file
	sed -i 's|API_DECLINE_TIMEOUT|'${API_DECLINE_TIMEOUT}'|g' $file
	sed -i 's|APP_API_DELAY|'${API_DELAY}'|g' $file
	sed -i 's|APP_API_MAX_RETRIES|'${API_MAX_RETRIES}'|g' $file
	sed -i 's|KEYCLOAK_CLIENTID|'${KEYCLOAK_CLIENTID}'|g' $file
	sed -i 's|KEYCLOAK_REALM|'${KEYCLOAK_REALM}'|g' $file
	sed -i 's|KEYCLOAK_ONLOAD|'${KEYCLOAK_ONLOAD}'|g' $file
	sed -i 's|REPO_COMMIT_ID|'${REPO_COMMIT_ID}'|g' $file
	sed -i 's|REPO_ENDPOINT_URL|'${REPO_ENDPOINT_URL}'|g' $file
	sed -i 's|AUTH_ROLE_CHECK|'${AUTH_BPN_CHECK}'|g' $file
	sed -i 's|AUTH_BPN_CHECK|'${AUTH_BPN_CHECK}'|g' $file
	sed -i 's|AUTH_BPN_NUMBER|'${AUTH_BPN_NUMBER}'|g' $file
	sed -i 's|APP_PORTAL_URL|'${APP_PORTAL_URL}'|g' $file
	sed -i 's|APP_ADMIN_EMAIL|'${APP_ADMIN_EMAIL}'|g' $file
	sed -i 's|APP_AUTO_SIGN|'${APP_AUTO_SIGN}'|g' $file

done

exec "$@"
