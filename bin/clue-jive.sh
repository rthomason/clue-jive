#!/bin/bash

bin=`dirname "$0"`
bin=`cd "$bin"; pwd`

JAVA=/usr/local/jive/java/bin/java
HEAP_OPTS="-Xms512m -Xmx1024m -XX:NewSize=256m"
JAVA_OPTS="-server -d64"

MAIN_CLASS="com.senseidb.clue.ClueApplication"
CLASSPATH=$CLASSPATH:$resources/:$bin/*:$1/ext/*

(cd $bin/..; $JAVA $JAVA_OPTS $JMX_OPTS $HEAP_OPTS -classpath $CLASSPATH $MAIN_CLASS $@)
