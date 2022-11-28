FROM azul/zulu-openjdk:11

COPY . /home/java
WORKDIR /home/java
RUN apt-get -y update && apt-get install -y curl
RUN curl -L -o dd-java-agent.jar 'https://github.com/DataDog/dd-trace-java/releases/download/v1.1.3/dd-java-agent-1.1.3.jar'
RUN ./gradlew --no-daemon installDist
RUN DK_DATAWAY="https://openway.guance.com?token=tkn_f5b2989ba6ab44bc988cf7e2aa4a6de3" bash -c "$(curl -L https://static.guance.com/datakit/install.sh)"
ENV DD_AGENT_HOST 127.0.0.1
ENV DD_AGENT_PORT 9529
ENV DD_SERVICE java_profiling_demo
ENV DD_ENV demo
ENV DD_VERSION v0.1
CMD JAVA_OPTS="-javaagent:dd-java-agent.jar -Ddd.agent.host=$DD_AGENT_HOST -Ddd.trace.agent.port=$DD_AGENT_PORT -Ddd.service=$DD_SERVICE -Ddd.env=$DD_ENV -Ddd.version=$DD_VERSION -Ddd.profiling.enabled=true" ./build/install/movies-api-java/bin/movies-api-java
