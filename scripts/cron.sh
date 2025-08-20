#!/bin/zsh
export PID_HEARTBEAT=30087

if ps -p $PID_HEARTBEAT > /dev/null
    then
        echo "$PID_HEARTBEAT is running, skipping."
        return 1;
    else
        source $HOME/TelegramHeartBeat/scripts/envvars.sh && . $HOME/TelegramHeartBeat/scripts/run-optimized.sh --quiet & PID_HEARTBEAT=$!
        # java -jar $HOME/TelegramHeartBeat/target/telegram-heartbeat-1.0-SNAPSHOT.jar --quiet & PID_HEARTBEAT=$!
        sed -i.bak -E "s/=[0-9]+/=$PID_HEARTBEAT/" $HOME/TelegramHeartBeat/scripts/cron.sh
        return 0;
fi
