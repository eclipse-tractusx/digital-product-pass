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


FROM nginx:alpine as production-build

# Copy entrypoint script as /entrypoint.sh
COPY ./entrypoint.sh /entrypoint.sh

HEALTHCHECK NONE

# make the 'app' folder the current working directory
WORKDIR /app

# Copy from the stahg 1
COPY --from=builder /app/dist /usr/share/nginx/html
COPY ./.nginx/nginx.conf /etc/nginx/nginx.conf

## add permissions for nginx user
RUN chown -R nginx:nginx /app && chmod -R 755 /app && \
        chown -R nginx:nginx /var/cache/nginx && \
        chown -R nginx:nginx /var/log/nginx && \
        chown -R nginx:nginx /etc/nginx/conf.d
RUN touch /var/run/nginx.pid && \
        chown -R nginx:nginx /var/run/nginx.pid

RUN chown -R nginx:nginx /usr/share/nginx/html && \
    chmod -R 755 /usr/share/nginx/html && \
    chmod +x /entrypoint.sh

USER nginx
EXPOSE 8080

ENTRYPOINT ["sh","/entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]
