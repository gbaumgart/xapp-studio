#!/bin/sh

cp ./WEB-INF/build.xml ./WEB-INF/classes/
cd ./WEB-INF/classes/
ant


cp pmedia.jar /opt/local/share/java/groovy/lib/

