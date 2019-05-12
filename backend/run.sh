#!/bin/bash

if ! [ -x "$(command -v docker)" ]; then
    echo "Docker is not found. Please install Docker."

elif ! [ -x "$(command -v psql)" ]; then
    echo "PostgreSQL is not found. Please install PostgreSQL."

else
    echo "Run MBMark application on port 8080."
    cp microbenchmark-web/target/MBMark_application.jar MBMark_application.jar
    java -jar -Dserver.port=8080 -Dspring.profiles.active=prod -Djava.security.egd=file:/dev/urandom MBMark_application.jar &
fi