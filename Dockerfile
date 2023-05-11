FROM eclipse-temurin:11

COPY . /home/java
WORKDIR /home/java
RUN ./gradlew --no-daemon installDist
ENV JAVA_OPTS "-javaagent:dd-java-agent.jar -Ddd.agent.host=datakit -Ddd.trace.agent.port=9529 -Ddd.service=dd-java-profiling -Ddd.env=test -Ddd.version=v1.0 -Ddd.profiling.enabled=true -Ddd.profiling.allocation.enabled=true"
CMD ./build/install/java-profiling-demo/bin/java-profiling-demo
