FROM openjdk:8-jdk-alpine
MAINTAINER christian.quindet@gmail.com
ARG JAR_FILE=target/*.jar
ARG KEY_FILE=.KeysSSL/springboot.p12
COPY ${JAR_FILE} serv-depcue-1.0.jar
COPY ${KEY_FILE} springboot.p12
ENTRYPOINT ["java","-jar","/serv-depcue-1.0.jar"]