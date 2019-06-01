FROM anapsix/alpine-java

COPY build/libs/warlords_server.jar /home/warlords_server.jar

EXPOSE 2080

CMD java -jar /home/warlords_server.jar
