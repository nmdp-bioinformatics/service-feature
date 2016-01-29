#!/bin/bash

export PRODUCTION_MYSQL_HOST=productiondb.feature-service.com
export PRODUCTION_MYSQL_USER=root
export PRODUCTION_MYSQL_PASSWORD=myproductionpassword

docker-compose -p production -f docker-compose-prod.yml up -d

