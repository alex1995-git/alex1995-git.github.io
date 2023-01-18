# !/bin/bash
#!/usr/bin/env sh

export SERVER_PORT=443

# Creds, passwords for the local generated certs
export KEYSSL_ENABLED=true
export KEYSSL_PATH=./certs/generated/depcue.p12
export KEYSSL_STR_PASSWORD=keyStorePassword
export KEYSSL_STR_PROVIDER=SunJSSE
export KEYSSL_STR_TYPE=PKCS12
export KEYSSL_STR_ALIAS=depcue
export KEYSSL_PASSWORD=keyStorePassword

export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/dbdepcue
export SPRING_JPA_HIBERNATE_DDL_AUTO=update
export SPRING_JPA_SHOW_SQL=true
export SPRING_DATASOURCE_HOST=localhost
export SPRING_DATASOURCE_PORT=5432
export SPRING_DATASOURCE_DATABASENAME=dbdepcue
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=DCadmin*
export TIME_SCHEDULER=15
export MIN_UNLOCK=240
export MIN_CADUCATE_TOKEN=600

kill $(ps aux | grep 'java -jar target/apidepcue-0.0.1-SNAPSHOT.jar' | awk '{print $2}')

nohup java -jar target/apidepcue-0.0.1-SNAPSHOT.jar &
