#!/bin/bash

JAR="build/ds-logstreamer-1.0-SNAPSHOT-jar-with-dependencies.jar"
OUTA="log/log-partA.txt"
OUTB="log/log-partB.txt"
OUTC="log/log-partC.txt"

function getlog() {
    if [ ! -f "build/$1" ]; then
        wget "https://dumps.wikimedia.org/other/pageviews/2016/2016-10/$1" -P build/
    fi
}

if [ "$1" == "compile" ]; then

    mvn -PbuildOutputDir clean compile assembly:single

fi

if [ "$1" == "partA" ]; then

    mkdir -p "log/"
    java -jar "$JAR" --serve >/dev/null &
    java -jar "$JAR" --stream-from="$2" >/dev/null &
    java -jar "$JAR" --query-every=10000 > "$OUTA"

fi

if [ "$1" == "partB" ]; then

    mkdir -p "log/"
    java -jar "$JAR" --serve >/dev/null &
    java -jar "$JAR" --stream-from="$2" >/dev/null &
    java -jar "$JAR" --stream-from="$3" >/dev/null &
    java -jar "$JAR" --stream-from="$4" >/dev/null &
    java -jar "$JAR" --query-every=0 > "$OUTB"

fi
