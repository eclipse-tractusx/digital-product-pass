#################################################################################
# Catena-X - Product Passport Consumer Frontend
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

# stage1 as builder
FROM node:lts-alpine as builder

WORKDIR /app

# Copy the package.json and install dependencies
COPY package*.json ./

#RUN npm install
RUN npm install --legacy-peer-deps

# Copy rest of the files
COPY . .

# Build the project
RUN npm run build


FROM nginxinc/nginx-unprivileged:stable-alpine

COPY ./entrypoint.sh /entrypoint.sh

HEALTHCHECK NONE

WORKDIR /app

COPY ./.nginx/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /app/dist /usr/share/nginx/html

USER root

RUN chmod +x /entrypoint.sh


# Install bash for env variables inject script
RUN apk update && apk add --no-cache bash
# Make nginx owner of /usr/share/nginx/html/ and change to nginx user
RUN chown -R 1001:1001 /usr/share/nginx/html/
USER 1001

EXPOSE 8080

ENTRYPOINT ["sh","/entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]