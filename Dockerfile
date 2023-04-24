# FROM azul/zulu-openjdk:11
FROM eclipse-temurin:11.0.17_8-jdk

COPY . /home/java
WORKDIR /home/java
RUN ./gradlew --no-daemon installDist
ENV JAVA_OPTS "-javaagent:dd-java-agent.jar -Ddd.agent.host=datakit -Ddd.trace.agent.port=9529 -Ddd.service=dd-java-profiling -Ddd.env=test -Ddd.version=v1.0 -Ddd.profiling.enabled=true -Ddd.profiling.allocation.enabled=true"
# CMD JAVA_OPTS="-javaagent:dd-java-agent.jar -Ddd.agent.host=$DD_AGENT_HOST -Ddd.trace.agent.port=$DD_AGENT_PORT -Ddd.service=$DD_SERVICE -Ddd.env=$DD_ENV -Ddd.version=$DD_VERSION -Ddd.profiling.enabled=true" ./build/install/java-profiling-demo/bin/java-profiling-demo
CMD ./build/install/java-profiling-demo/bin/java-profiling-demo
