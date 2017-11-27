#!/bin/bash

java -Xmx512m -Xms512m -XX:-UseTLAB -javaagent:instrumentation.jar -jar execution.jar
