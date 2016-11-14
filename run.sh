#!/bin/bash

JAR="build/ds-logstreamer-1.0-SNAPSHOT-jar-with-dependencies.jar"
LOGA="log/log-partA.txt"
LOGB="log/log-partB.txt"
LOGC="log/log-partC.txt"

################################################################################
### COMPILE                                                                  ###
################################################################################

if [ "$1" == "compile" ]; then

    mvn -PbuildOutputDir clean compile assembly:single
    exit 0

fi

################################################################################
### PART A                                                                   ###
################################################################################

if [ "$1" == "partA" ]; then

    mkdir -p "log/"
    java -jar "$JAR" --serve >/dev/null &
    java -jar "$JAR" --stream-from="$2" >/dev/null &
    java -jar "$JAR" --query-every=10000 > "$LOGA"
    exit 0

fi

################################################################################
### PART B                                                                   ###
################################################################################

if [ "$1" == "partB" ]; then

    mkdir -p "log/"
    java -jar "$JAR" --serve >/dev/null &
    java -jar "$JAR" --stream-from="$2" >/dev/null &
    java -jar "$JAR" --stream-from="$3" >/dev/null &
    java -jar "$JAR" --stream-from="$4" >/dev/null &
    java -jar "$JAR" --query-every=0 > "$LOGB"
    exit 0

fi

################################################################################
### PART C                                                                   ###
################################################################################

if [ "$1" == "partC" ]; then

    mkdir -p "log/"
    java -jar "$JAR" --serve >/dev/null &
    java -jar "$JAR" --stream-from="$2" --site=".b" >/dev/null &
    java -jar "$JAR" --stream-from="$3" --site=".b" >/dev/null &
    java -jar "$JAR" --stream-from="$4" --site=".b" >/dev/null &
    java -jar "$JAR" --query-every=0 > "$LOGC"
    exit 0

fi

################################################################################
### OTHER                                                                    ###
################################################################################

echo "Unrecognized option '$1'"
