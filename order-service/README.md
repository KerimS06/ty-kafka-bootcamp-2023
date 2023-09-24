# Order Service
=========================

- With this application, when an order is created or its status is updated, we will be able to see the data we have sent to the database and Kafka.

## Built With
- Docker
- Jdk17
- Spring Boot 2.6.1
- Postgresql
- Kafka
- Akhq (Optional)

Environment Info
---------------------
Environment | Local
--- | ---
Kafka | localhost:29092
Order PostgreSql DB | jdbc:postgresql://localhost:5432/order <br /> DbUser: order_db_appuser <br /> DBPass: order_db_apppswd
Akhq | localhost:8090

Required Apps
---------------------

In order to run application we need postgresql and kafka.

Running Containers
---------------------
To run couchbase and kafka locally

    .build/docker-compose up -d

To stop

    .build/docker-compose down
