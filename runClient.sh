#!/bin/bash
#

export SVR_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

JAVA_MAIN=' gash.grpc.route.client.RouteClient'
JAVA_ARGS="$1 $2"
JAVA_TUNE='-client -Xms96m -Xmx512m'

#java ${JAVA_TUNE} -cp .:${SVR_HOME}/lib/'*':${SVR_HOME}/classes ${JAVA_MAIN} ${JAVA_ARGS}
java ${JAVA_TUNE} -cp .:'/Users/Student/Google Drive/CMPE-275/grpc-project/grpc-route/lib/*':'/Users/Student/Google Drive/CMPE-275/grpc-project/grpc-route/classes' ${JAVA_MAIN} ${JAVA_ARGS}
