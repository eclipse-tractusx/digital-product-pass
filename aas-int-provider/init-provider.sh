#!/bin/bash
# Copyright 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#     http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


set -o errexit
set -o errtrace
set -o pipefail
set -o nounset

digitalTwinId='urn:uuid:365e6fbe-bb34-11ec-8422-0242ac120001'
digitalTwinSubmodelId='urn:uuid:61125dc3-5e6f-4f4b-838d-447432b97918'


# Create dummy data
curl -X POST -H 'Content-Type: application/json' --data "@resources/data.json" http://localhost:8194/data/$digitalTwinSubmodelId

# Create a asset
curl -X POST -H 'Content-Type: application/json' --data "@resources/asset.json" http://localhost:8187/api/v1/data/assets

# Create a general policy
curl -X POST -H 'Content-Type: application/json' --data "@resources/policy.json" http://localhost:8187/api/v1/data/policies

# Create a contract definition
curl -X POST -H 'Content-Type: application/json' --data "@resources/contractdefinition.json" http://localhost:8187/api/v1/data/contractdefinitions

# Create a digital twin and register inside the registry
curl -X POST -H 'Content-Type: application/json' --data "@resources/digitaltwin.json" http://localhost:4243/registry/shell-descriptors

