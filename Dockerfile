FROM openjdk

WORKDIR /APP

COPY ./build/libs/authservice-0.0.1-SNAPSHOT.jar /APP

EXPOSE 8081

CMD ["java", "-jar", "authservice-0.0.1-SNAPSHOT.jar"]