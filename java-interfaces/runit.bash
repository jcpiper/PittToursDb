#!/bin/bash
echo setting up environment variables
source /afs/pitt.edu/home/p/a/panos/1555/bash.env.unixs
echo compiling customer ui
javac pittTours.java
echo starting program
java pittTours
