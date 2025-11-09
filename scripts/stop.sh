#!/bin/bash

ROOT_PATH="/home/ec2-user/jaejudo"
STOP_LOG="$ROOT_PATH/stop.log"
SERVICE_PID=$(sudo lsof -t -i:8080) # 실행 중인 Spring 서버의 PID

if [ -z "$SERVICE_PID" ]; then
  echo "$(date) : 8080 점유 프로세스 없음" >> $STOP_LOG
else
  echo "$(date) : 서비스 종료 시도 (PID: $SERVICE_PID)" >> $STOP_LOG
  # 1. 부드럽게 종료 시도 (SIGTERM)
  sudo kill $SERVICE_PID
  sleep 5

  # 2. 여전히 살아있다면 강제 종료 (SIGKILL)
  if ps -p $SERVICE_PID > /dev/null; then
    echo "$(date) : 서비스 강제 종료 (kill -9 $SERVICE_PID)" >> $STOP_LOG
    sudo kill -9 $SERVICE_PID
  fi
fi
