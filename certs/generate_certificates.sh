#!/usr/bin/env sh

# Creds, passwords for the local generated certs
export KEYSSL_PATH=springboot.p12
export KEYSSL_STR_PASSWORD=keyStorePassword
export KEYSSL_STR_PROVIDER=SunJSSE
export KEYSSL_STR_TYPE=pkcs12
export KEYSSL_STR_ALIAS=localhost
export KEYSSL_PASSWORD=keyPassword


# Pick which OpenSSL to use - system default or one installed with Homebrew
export OPENSSL=openssl
#export OPENSSL=/usr/local/opt/openssl/bin/openssl

# Clean any previously-generated keys or certs
rm -rf ./generated
mkdir ./generated

# Generate Root CA Certificate

$OPENSSL genrsa -aes256 \
                -out ./generated/ca.key.pem -passout env:KEYSSL_PASSWORD \
                4096

$OPENSSL req -config openssl.ca.cnf -batch \
             -new -x509 -days 3650 \
             -key ./generated/ca.key.pem -passin env:KEYSSL_PASSWORD \
             -out ./generated/ca.cert.pem -passout env:KEYSSL_PASSWORD

# Generate Server Certificate signed by Root CA

$OPENSSL genrsa -aes256 \
                -out ./generated/depcue.key.pem -passout env:KEYSSL_PASSWORD \
                4096

$OPENSSL req -config openssl.server.cnf -extensions server_cert -batch \
             -new -days 3650 \
             -key ./generated/depcue.key.pem -passin env:KEYSSL_PASSWORD \
             -out ./generated/depcue.cert.csr -passout env:KEYSSL_PASSWORD

$OPENSSL x509 -extfile openssl.server.cnf -extensions server_cert \
              -req -sha512 -days 3650 \
              -in ./generated/depcue.cert.csr -passin env:KEYSSL_PASSWORD \
              -CAkey ./generated/ca.key.pem -CA ./generated/ca.cert.pem -set_serial 1000 \
              -out ./generated/depcue.cert.pem

# Convert Server Key and Certificate to proper format for java trust store

$OPENSSL pkcs12 -export -name depcue -caname "DepCue CA" \
                -inkey ./generated/depcue.key.pem -passin env:KEYSSL_PASSWORD \
                -in ./generated/depcue.cert.pem \
                -certfile ./generated/ca.cert.pem \
                -out ./generated/depcue.p12 -passout env:KEYSSL_STR_PASSWORD