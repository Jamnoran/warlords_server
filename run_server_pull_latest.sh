#! /bin/sh

git pull

gradle clean

gradle jar

java -jar build/libs/warlords_server.jar
