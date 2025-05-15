#!/bin/bash

check_ret() {
    if [ $1 -ne 0 ]; then
        echo ""
        echo "!!! FAIL: $2"
        echo "********************************************************************************"
        echo ""
        exit $1
    else
        echo ""
        echo "*** SUCCESS: $2"
        echo "********************************************************************************"
        echo ""
    fi
}

rm -rf bin

javac -d bin ./src/*
check_ret $? "Compile"

jar -cmf manifest.mf road2dgenerator.jar  -C bin .
check_ret $? "Build road2dgenerator.jar"

java -jar road2dgenerator.jar
check_ret $? "Run road2dgenerator.jar"
