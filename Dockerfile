FROM openjdk:8
COPY target/atm-machine-0.0.1-SNAPSHOT.jar atm-machine-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","atm-machine-0.0.1-SNAPSHOT.jar"]
