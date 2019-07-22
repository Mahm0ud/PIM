FROM java:8

RUN apt-get install debian-archive-keyring

RUN echo "deb [check-valid-until=no] http://cdn-fastly.deb.debian.org/debian jessie main" > /etc/apt/sources.list.d/jessie.list
RUN echo "deb [check-valid-until=no] http://archive.debian.org/debian jessie-backports main" > /etc/apt/sources.list.d/jessie-backports.list
RUN sed -i '/deb http:\/\/\(deb\|httpredir\).debian.org\/debian jessie.* main/d' /etc/apt/sources.list

RUN apt-get -o Acquire::Check-Valid-Until=false update

RUN apt-get install -y maven

RUN ls -l

WORKDIR /code

ADD ./pom.xml /code/pom.xml

ADD ./src /code/src

CMD ["mvn", "spring-boot:run"]
