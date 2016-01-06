# Run the feature service in a Docker container

The feature-dropwizard....jar file is the output of 'mvn package' and copied from ../dropwizard/target/feature-dropwizard-1.0-SNAPSHOT.jar
The config/database.yml file contains the mysql connection information.

├── Dockerfile
├── config
│   └── database.yml
└── feature-dropwizard-1.0-SNAPSHOT.jar

Build the docker image 

```sh
docker build -t service-feature:1.0-SNAPSHOT .
```

