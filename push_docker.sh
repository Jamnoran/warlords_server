#!/usr/bin/env bash

gradle clean

gradle jar

echo "Built Jar file"

docker build -t warlords_server .

docker tag warlords_server jamnoran/warlords_server

docker push jamnoran/warlords_server

echo "Pushed server"
