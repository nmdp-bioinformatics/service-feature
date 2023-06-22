FROM --platform=linux/amd64 adoptopenjdk/openjdk8:debian-slim
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
COPY run.sh /feature/run.sh

# Run command
CMD ["/feature/run.sh"]
