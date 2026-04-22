FROM node:alpine
RUN apk update && apk add --no-cache bash && apk add --no-cache sudo
RUN npm install -g typescript
RUN npm install -g @angular/cli@19.2.23
WORKDIR /home/node
EXPOSE 4200
EXPOSE 3000
ENV NG_CLI_ANALYTICS="false"
ENV PS1="[frontend:\W]\$ "
CMD [ "bash" ]
