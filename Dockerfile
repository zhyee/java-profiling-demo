FROM eclipse-temurin:11

COPY . /home/java
WORKDIR /home/java
RUN ./gradlew --no-daemon installDist
ENV JAVA_OPTS "-javaagent:dd-java-agent.jar -XX:FlightRecorderOptions=stackdepth=512 -Ddd.profiling.enabled=true -Ddd.profiling.allocation.enabled=true -Ddd.profiling.ddprof.cpu.enabled=true -Ddd.profiling.ddprof.wall.enabled=true -Ddd.profiling.ddprof.alloc.enabled=true -Ddd.profiling.ddprof.liveheap.enabled=true -Ddd.profiling.ddprof.memleak.enabled=true"
ENV DD_AGENT_HOST localhost
ENV DD_TRACE_AGENT_PORT 9529
ENV DD_SERVICE dk_java_profiling
ENV DD_VERSION 1.0
ENV DD_ENV testing
ENV DD_PROFILING_ENABLED true
ENV DD_PROFILING_ALLOCATION_ENABLED true
CMD ./build/install/java-profiling-demo/bin/java-profiling-demo
