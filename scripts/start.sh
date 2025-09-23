#!/bin/bash

source /home/ec2-user/.bashrc
EnvironmentFile=/home/ec2-user/jaejudo/.env

ROOT_PATH="/home/ec2-user/jaejudo"
JAR_NAME="application.jar"
JAR_SRC="$ROOT_PATH/build/libs/$JAR_NAME"
JAR_DEST="$ROOT_PATH/$JAR_NAME"

APP_LOG="/var/log/jaejudo/application.log"
ERROR_LOG="/var/log/jaejudo/error.log"
START_LOG="/var/log/jaejudo/start.log"

NOW=$(date +%c)

# 로그 디렉토리 생성 (없을 경우)
mkdir -p /var/log/jaejudo

# JAR 파일 이동
if [ -f "$JAR_SRC" ]; then
    mv -f "$JAR_SRC" "$JAR_DEST"
    echo "[$NOW] > $JAR_NAME 이동 완료: $JAR_SRC -> $JAR_DEST" >> $START_LOG
else
    echo "[$NOW] > 이동할 JAR 파일이 존재하지 않음: $JAR_SRC" >> $START_LOG
    exit 1
fi

# Java 경로 명시
JAVA_BIN=$(which java)

# 애플리케이션 실행
echo "[$NOW] > $JAR_DEST 실행" >> $START_LOG
nohup $JAVA_BIN -jar "$JAR_DEST" > $APP_LOG 2> $ERROR_LOG &

# 실행 PID 기록
SERVICE_PID=$(pgrep -f "$JAR_NAME")
echo "[$NOW] > 서비스 PID: $SERVICE_PID" >> $START_LOG
