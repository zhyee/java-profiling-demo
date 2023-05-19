#!/usr/bin/env bash

JAVA_OPTS="-javaagent:dd-java-agent.jar -XX:FlightRecorderOptions=stackdepth=256 -Ddd.serviccloudcare-backend -Ddd.env=prod -Ddd.version=v1.6.0 -Ddd.profiling.enabled=true -Ddd.profiling.allocation.enabled=true -Ddd.trace.agent.port=9529" ./build/install/java-profiling-demo/bin/java-profiling-demo