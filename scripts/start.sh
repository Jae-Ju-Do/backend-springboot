#!/bin/bash

ROOT_PATH="/home/ec2-user/jaejudo"
JAR="$ROOT_PATH/build/libs/application.jar"

APP_LOG="/var/log/jaejudo/application.log"
ERROR_LOG="/var/log/jaejudo/error.log"
START_LOG="/var/log/jaejudo/start.log"

NOW=$(date +%c)

# 로그 디렉토리 생성 (없을 경우)
mkdir -p /var/log/jaejudo

# Java 경로 명시 (Amazon Linux에서 which java로 확인 가능)
JAVA_BIN=$(which java)

echo "[$NOW] > $JAR 실행" >> $START_LOG
nohup $JAVA_BIN -jar $JAR > $APP_LOG 2> $ERROR_LOG &

SERVICE_PID=$(pgrep -f $JAR)
echo "[$NOW] > 서비스 PID: $SERVICE_PID" >> $START_LOG
