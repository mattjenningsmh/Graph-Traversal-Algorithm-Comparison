#!/bin/bash

javac -d out GraphGenerator.java Tester.java

java -cp out Tester > balls.txt
