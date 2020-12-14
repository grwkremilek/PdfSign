# Stage 1 - build the app

FROM maven:3.6-jdk-11

# COPY ./PdfSignature /build
COPY . /build

WORKDIR /build

RUN mvn clean install

#Stage 2 - Final image

FROM tomcat:9-jdk11

# RUN rm -r /usr/local/tomcat/webapps/*

COPY --from=0 /build/target/*.war /usr/local/tomcat/webapps/ROOT.war
