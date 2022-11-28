#!/usr/bin/env bash

nohup /usr/local/datakit/datakit >/dev/null 2>&1 &
if [ $? -ne 0 ]; then
  echo "start datakit failed :("
  exit 1
fi

JAVA_OPTS="-javaagent:dd-java-agent.jar -Ddd.agent.host=$DD_AGENT_HOST -Ddd.trace.agent.port=$DD_AGENT_PORT -Ddd.service=$DD_SERVICE -Ddd.env=$DD_ENV -Ddd.version=$DD_VERSION -Ddd.profiling.enabled=true" ./build/install/java-profiling-demo/bin/java-profiling-demo