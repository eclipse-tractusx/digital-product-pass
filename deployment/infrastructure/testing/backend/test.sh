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

## this command in Python is recommended to run in UNBUFFERED mode, and to print standard output (stdout/stderr)
export PYTHONUNBUFFERED=TRUE;

## execute the python script
python ./getPassport.py --id BAT-XYZ789  \
  --discoveryId XYZ78901 \
  --company CX-Test-Access \
  --username "<your username>" \
  --password "<your password>"
