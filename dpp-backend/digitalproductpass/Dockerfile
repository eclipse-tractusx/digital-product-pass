#################################################################################
# Catena-X - Product Passport Consumer Backend
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


FROM eclipse-temurin:19-alpine

RUN addgroup -g 3000 appgroup \
	&& adduser -u 10000 -g 3000 -h /home/appuser -D appuser
    
WORKDIR /app

VOLUME data log

RUN mkdir -p ./log
RUN mkdir -p ./data

COPY ./target/digitalproductpass*.jar digitalproductpass.jar

#HEALTHCHECK --interval=60s --timeout=4s CMD curl -f http://localhost:8080/health || exit 1
HEALTHCHECK NONE

# add permissions for a user
RUN chown -R 10000:3000 /app && chmod -R 775 /app/

USER 10000:3000
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./digitalproductpass.jar"]