FROM openjdk:17
VOLUME /tmp
EXPOSE 9090
COPY target/LearnTerminology-0.0.1-SNAPSHOT.jar learn-terminology.jar
ENTRYPOINT ["java","-jar","/learn-terminology.jar"]