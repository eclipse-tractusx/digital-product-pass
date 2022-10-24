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

# for debugging purpose
RUN apk add curl

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

USER nginx
EXPOSE 8080
ENTRYPOINT ["nginx", "-g", "daemon off;"]
