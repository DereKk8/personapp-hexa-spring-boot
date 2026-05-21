#!/bin/sh
# Optimized CLI runner: only rebuilds if source changed, otherwise runs JAR directly

set -e
JAR=$(ls cli-input-adapter/target/cli-input-adapter-*.jar 2>/dev/null | head -1)

rebuild() {
  echo "[run-cli] Rebuilding CLI module..."
  mvn package -pl cli-input-adapter -am -DskipTests -q -T 1C
  JAR=$(ls cli-input-adapter/target/cli-input-adapter-*.jar | head -1)
}

if [ -z "$JAR" ]; then
  rebuild
elif find cli-input-adapter/src common/src domain/src application/src \
       maria-output-adapter/src mongo-output-adapter/src \
       -newer "$JAR" -print -quit | grep -q .; then
  rebuild
else
  echo "[run-cli] JAR is up-to-date, skipping build"
fi

java -jar "$JAR"
