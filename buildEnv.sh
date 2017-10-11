#!/usr/bin/env bash
mvn clean package -DskipTests
docker-compose up --build