#! /bin/bash

mvn clean -f ./using/pom.xml

mvn compile -f ./using/pom.xml

java -cp ./hwunit/target/hwunit-jar-with-dependencies.jar:./using/target/classes ru.otus.sokolovsky.hw5.using.App

