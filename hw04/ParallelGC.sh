#!/usr/bin/env bash

MEMORY="-Xms300m -Xmx300m"
GC="-XX:+UseParallelGC -XX:+UseParallelOldGC -XX:MaxMetaspaceSize=128m"

java $MEMORY $GC -jar ./target/hw04-1.0-SNAPSHOT.jar Parallel.log