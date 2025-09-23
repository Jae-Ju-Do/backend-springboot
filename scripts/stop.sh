#!/bin/bash

ROOT_PATH="/home/ec2-user/jaejudo"
JAR="$ROOT_PATH/build/libs/application.jar"
STOP_LOG="$ROOT_PATH/stop.log"
SERVICE_PID=$(pgrep -f $JAR) # 실행 중인 Spring 서버의 PID

if [ -z "$SERVICE_PID" ]; then
  echo "$(date) : 서비스 Not Found" >> $STOP_LOG
else
  echo "$(date) : 서비스 종료 (PID: $SERVICE_PID)" >> $STOP_LOG
  kill "$SERVICE_PID"
  # 강제 종료하려면 아래 사용
  # kill -9 $SERVICE_PID
fi
