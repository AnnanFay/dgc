#!/bin/sh

# Original Author: sw1nn

UBERJAR=*-standalone.jar

if [ -f ${UBERJAR} ]; then
     modified_files=$(find src project.clj -newer ${UBERJAR})

     if [ ${#modified_files} -eq 0 ]; then
         echo "Skipping uberjar because no files modified"
         exit 0
     fi  
fi  

lein uberjar