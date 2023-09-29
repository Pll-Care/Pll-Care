# Dockerfile

FROM node:alpine as builder
WORKDIR /usr/src/app
COPY package.json ./
RUN npm install
RUN npm install --save history
COPY ./ ./
RUN npm run build

FROM nginx 
EXPOSE 3000
COPY ./default.conf /etc/nginx/conf.d/default.conf 
COPY --from=builder usr/src/app/build  /usr/share/nginx/html 