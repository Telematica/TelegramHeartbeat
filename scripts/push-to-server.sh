#!/bin/zsh

REPO_DIR=$HOME/TelegramHeartbeat
REMOTE_DIR=/Users/telematica/TelegramHeartbeat/target

printf "Enter IP Address: " && read -r IP_ADDRESS && \
scp \
$REPO_DIR/target/telegram-heartbeat-1.0-SNAPSHOT.jar \
telematica@$IP_ADDRESS:$REMOTE_DIR/telegram-heartbeat-1.0-SNAPSHOT.jar && \
echo "Successfully pushed to server at $IP_ADDRESS"