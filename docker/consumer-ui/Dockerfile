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

RUN apk add curl

# make the 'app' folder the current working directory
WORKDIR /app

# create data directory where the battery passport is kept
RUN mkdir data

# Copy from the stahg 1
COPY --from=builder /app/dist /usr/share/nginx/html
COPY ./.nginx/nginx.conf /etc/nginx/nginx.conf

EXPOSE 80
ENTRYPOINT ["nginx", "-g", "daemon off;"]