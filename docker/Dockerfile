FROM java:7
MAINTAINER Pradeep Bashyal <pbashyal@nmdp.org>

WORKDIR /feature

COPY feature-dropwizard-jdbi-1.0-SNAPSHOT.jar /feature/feature-dropwizard.jar

VOLUME ["/config"]

ENTRYPOINT ["java", "-jar", "/feature/feature-dropwizard.jar"]

CMD ["server", "/config/database.yml"]
