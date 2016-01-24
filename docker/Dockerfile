FROM java:7
MAINTAINER Pradeep Bashyal <pbashyal@nmdp.org>

WORKDIR /feature

EXPOSE 8080
EXPOSE 8081

VOLUME /var/log/service-feature

ENV LOGGING_LEVEL_DEFAULT=INFO \
    LOGGING_LEVEL_FEATURE=INFO \
    LOG_DIR=/var/log/service-feature \
    MYSQL_USER=root \
    MYSQL_PASSWORD=supersecretpwd \
    MYSQL_HOST=featuredb \
    MYSQL_PORT=3306

COPY config/database.yml /config/
COPY feature-dropwizard.jar /feature/feature-dropwizard.jar

ENTRYPOINT ["java", "-jar", "/feature/feature-dropwizard.jar", "server"]

CMD ["/config/database.yml"]
