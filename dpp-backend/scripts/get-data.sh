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

## this command in Python is recommended to run in UNBUFFERED mode, and to print standard output (stdout/stderr)
export PYTHONUNBUFFERED=TRUE;
PYTHON=""

## step 1: check if the python/python3 executable exists
PYEXEC=$(which python)

if [ -f ${PYEXEC} ]; then
  PYTHON=${PYEXEC};
fi

PYEXEC=$(which python3)

if [ -f ${PYEXEC} ]; then
  PYTHON=${PYEXEC};
fi

if [ ! -f ${PYTHON} ]; then
  echo "Python executable not found";
  exit 1
fi

## step 2: execute the python script
${PYTHON} ./getPassport.py --id NCM-6789  \
  --discoveryId MAT7814 \
  --company CX-Test-Access \
  --username "<username>" \
  --password "<password>" \
  --appId "<appId>"
