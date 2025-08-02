#!/bin/bash

javac -d out GraphGenerator.java AStar.java Dijkstras.java Tester.java 

java -Xmx8g -cp out Tester 
