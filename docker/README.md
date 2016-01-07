# Run the feature service in a Docker container

The `feature-dropwizard-jdbi-1.0-SNAPSHOT.jar` file is the output of `mvn package` and copied from `../dropwizard-jdbi/target/feature-dropwizard-jdbi-1.0-SNAPSHOT.jar`

The `config/database.yml` file contains the mysql connection information.

```
├── Dockerfile
├── config
│   └── database.yml
└── feature-dropwizard-jdbi-1.0-SNAPSHOT.jar
```

Build the docker image 

```sh
docker build -t service-feature:1.0-SNAPSHOT .
```

If developing locally against a mysql database, run the feature service linking to that database. See the wiki for setting up a development docker environment.
```sh
docker run -d -v $PWD/config:/config --link mysql:featuredb -p 8080:8080 -p 8081:8081 service-feature:1.0-SNAPSHOT
```


If running against a prod database eg. RDS, create a new database.yml file and run it as:
```sh
docker run -d -v $PWD/prod-config:/config -p 8080:8080 -p 8081:8081 service-feature:1.0-SNAPSHOT
```

