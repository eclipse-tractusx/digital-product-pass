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

# stage1 as builder
FROM node:lts-alpine as builder

WORKDIR /app

# Copy the package.json and install dependencies
COPY package*.json ./

RUN npm install -g npm

#RUN npm install
RUN npm install --legacy-peer-deps

# Copy rest of the files
COPY . .

# Build the project
RUN npm run build


FROM nginxinc/nginx-unprivileged:alpine

ARG REPO_COMMIT_ID='REPO_COMMIT_ID'
ARG REPO_ENDPOINT_URL='REPO_ENDPOINT_URL'
ENV REPO_COMMIT_ID=${REPO_COMMIT_ID}
ENV REPO_ENDPOINT_URL=${REPO_ENDPOINT_URL}

USER root

RUN addgroup -g 3000 appgroup \
	&& adduser -u 1000 -g 3000 -h /home/nonroot -D nonroot

COPY ./entrypoint.sh /entrypoint.sh

WORKDIR /app

COPY ./.nginx/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /app/dist /usr/share/nginx/html

HEALTHCHECK NONE

# add permissions for a user
RUN chown -R 1000:3000 /app && chmod -R 775 /app/
RUN chown 1000:3000 /entrypoint.sh && chmod -R 775 /entrypoint.sh

# Install bash for env variables inject script
RUN apk update && apk add --no-cache bash
# Make nginx owner of /usr/share/nginx/html/ and change to nginx user
RUN chown -R 1000:3000 /usr/share/nginx/html/ && chmod -R 775 /usr/share/nginx/html/

USER 1000:3000

EXPOSE 8080

ENTRYPOINT ["sh","/entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]
