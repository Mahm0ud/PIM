# PRODUCT INFORMATION MANAGEMENT

This is a sample to show a way of merchants can have for adding/updating products in the backend. The project creates Docker containers. It has two separate microservices serving this purpose

## Services

### Importer
- The Importer microservice reads a CSV file containing all the data regarding products. This services sends messages to Kafka as JSON messages. It uses the `KafkaTemplate`.
- The service is accessible throug a web endpoint at http://localhost:8080/commercetools/index.html that accepts a CSV files and returns the number of documents within this file.

### Aggregator
- The Aggregator receives the messages. It extracts all information to create a new product if it doesn’t exist in its database, and update it if it was already there. It uses `@KafkaListener`.
- The service provides 2 end-points
  - http://localhost:8081/commercetools/products an endpoint to list all available products in its database.
  - http://localhost:8081/commercetools/status an endpoint to show daily statistics of how many products were created and how many updated. Below is a sample response
  ```{"createdProducts":0,"updatedProducts":0,"date":"2019-07-21"}```

Communication between the two services is done using a Kafka topic `catalog-items`. It has three partitions. Messages sent are keyed so that each group of products land in a separate partition. `Aggregator` has a single consumer group.

## Technologies

- Spring Boot
- Spring Kafka
- Kafka
- Zookeeper
- H2
- Mockito
- Docker Compose to link the containers.

## Installation
You can escape this section if you have docker installed and have and active internet connection esacpe the the next section.

* The example is implemented using docker, java, and Maven, hence it will need to have internet access so that it would be able to grab images, update them, and grab the needed dependencies.

* The example run in Docker Containers. You need to install Docker
  Community Edition, see https://www.docker.com/community-edition/
  . You should be able to run `docker` after the installation.

* The example starts up 3 containers (Zookeeper/Kafka, and 2 Micorservice) hence it is advised that you configure Docker to use 4 GB of RAM. Otherwise Docker containers might be killed due to lack of RAM. On Windows and macOS you can find the RAM setting in the Docker application under Preferences/ Advanced.

* After installing Docker you should also be able to run
  `docker-compose`. If this is not possible, you might need to install
  it separately. See https://docs.docker.com/compose/install/ .

## Run the containers

  First you need to build docker images. from the root directory (e.g parent of both `pim-importer` and `pim-aggregator`), run `docker-compose build`. This will download some base images, install software into Docker images and will therefore take its time (note that it skipped kafka image because I have them already).
  Note that you can still build each service separately using `docker-compose-single-service.yml`, however be aware that since both files have configured kafka instance you will need to remove the kafka service from one of the services if you plan to start them one by one, otherwise, please stick to using the `docker-compose.yml` in the services parent folder.

  ```
  $ docker-compose build
  zookeeper uses an image, skipping
  kafka uses an image, skipping
  Building pim-aggregator
  ....
  Step 10/12 : ADD pom.xml /code/pom.xml
   ---> 2726c8a58484
  Step 11/12 : ADD src /code/src
   ---> 82b5238bbc02
  Step 12/12 : CMD ["mvn", "spring-boot:run"]
  Successfully built c389a759f85b
  Successfully tagged pim-aggregator:latest

  Building pim-importer
  ...
  Step 10/12 : ADD pom.xml /code/pom.xml
   ---> fa754f52bf10
  Step 11/12 : ADD src /code/src
   ---> cb3e79da5fe3
  Step 12/12 : CMD ["mvn", "spring-boot:run"]
  Successfully built 8f9d9920082d
  Successfully tagged pim-importer:latest
  ```

  Afterwards the Docker images should have been created :

  ```
  $ docker images
  REPOSITORY               TAG                 IMAGE ID            CREATED             SIZE
  pim-aggregator           latest              9e77efc76cb2        19 minutes ago      825MB
  pim-importer             latest              23a38eaaa260        19 minutes ago      825MB
  spotify/kafka            latest              a9e0a5b8b15e        2 years ago         443MB
  ```

  Now you can start the containers using `docker-compose up -d`. The `-d` option means that the containers will be started in the background and won't output their stdout to the command line:

  ```
  $ docker-compose up -d
  Creating network "idea-projects_default" with the default driver
  Creating kafka ... done
  Creating pim-aggregator ... done
  Creating pim-importer   ... done
  ```

  Note that during the first start the Docker images for Zookeeper and Kafka will be downloaded during this step.

  Check wether all containers are running:

  ```
  [~/microservice-kafka/docker]docker ps
  CONTAINER ID        IMAGE                   COMMAND                 CREATED             STATUS              PORTS                                            NAMES
  5449fbb9b044        pim-importer:latest     "mvn spring-boot:run"   6 minutes ago       Up 6 minutes        0.0.0.0:8080->8080/tcp                           pim-importer
  903a92b4cb3e        pim-aggregator:latest   "mvn spring-boot:run"   6 minutes ago       Up 6 minutes        0.0.0.0:8081->8081/tcp                           pim-aggregator
  f7a59ee02263        spotify/kafka:latest    "supervisord -n"        6 minutes ago       Up 6 minutes        0.0.0.0:2181->2181/tcp, 0.0.0.0:9092->9092/tcp   kafka
  ```
  `docker ps -a`  also shows the terminated Docker containers. That is useful to see Docker containers that crashed right after they started.

  If one of the containers is not running, you can look at its logs using e.g.  `docker logs idea-projects_kafka_1`. The name of the container is given in the last column of the output of `docker ps`. Looking at the logs even works after the container has been terminated. If the log says that the container has been `killed`, you need to increase the RAM assigned to Docker to e.g. 4GB. On Windows and macOS you can find the RAM setting in the Docker application under Preferences/ Advanced.

  If you need to do more trouble shooting open a shell in the container using e.g. `docker exec -it idea-projects_kafka_1 /bin/sh` or execute command using `docker exec idea-projects_kafka_1 /bin/ls`.

  You can now go to http://localhost:8080/commercetools/index.html and upload products CSV list. That will create products in the other microservices.

  You can terminate all containers using `docker-compose down`.
