#################################################################################
# Tractus-X - Digital Product Passport Application
#
# Copyright (c) 2022, 2024 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

import sys
import os
# Add the previous folder structure to the system path to import the utilities
sys.path.append("../")
from flask import Flask, request, jsonify

app = Flask(__name__)

import argparse
import requests
import logging
import traceback
from logging.config import fileConfig
from utilities.httpUtils import HttpUtils
from utilities.constants import Constants
from utilities.operators import op
from utilities.authentication import Authentication

op.make_dir("logs")

# Configure logs
logging.basicConfig(level=logging.DEBUG, format='%(asctime)s %(levelname)-8s %(message)s', datefmt='%a, %d %b %Y %H:%M:%S', filename=f"logs/{op.timestamp()}-wallet.log", filemode='a+')
fileConfig('../logging.ini')
logger = logging.getLogger()


# Set configuration for the simple http server
import http.server
import socketserver
import http.server
import socketserver

# Define the handler to use for incoming requests
handler = http.server.SimpleHTTPRequestHandler

# Set the port number for the server
port = 8080
with socketserver.TCPServer(("", port), handler) as httpd:
    logger.info(f"Serving on port {port}")
    # Run the server indefinitely
    httpd.serve_forever()



