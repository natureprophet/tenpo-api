# 🚀 Tenpo API backend challenge

API interview challenge build for [Tenpo](https://www.tenpo.cl/)

This API consists of two endpoints, one to obtain the sum of two numbers, adding a percentage obtained
from an external service, and another endpoint to retrieve the history of calls made to the previous endpoint.

### Instructions to run the API

```
 1. git clone https://github.com/natureprophet/tenpo-api.git
 2. cd tenpo-api
 3. docker-compose up
```
The API container is also published on https://hub.docker.com/r/simongs89/tenpo-api, so you can run the docker compose 
using the local build or the remote one, both options are included on the docker-compose.yml file.

### Test the API

***POST /api/v1/calculator/sum***

Adds two numbers received by parameter and adds a percentage obtained from an external service
```
curl --location --request POST 'http://localhost:8080/api/v1/calculator/sum' \
--header 'Content-Type: application/json' \
--data-raw '{
    "numberOne": 5,
    "numberTwo": 6
}'
```

***GET /api/v1/calculator/journal***

Retrieve the history of calls and results made to /sum endpoint
```
curl --location --request GET 'http://localhost:8080/api/v1/calculator/journals'
```

### Postman Collection

***You can call the endpoints importing the Postman Collection:***
```
Tenpo API Challenge.postman_collection.json
```