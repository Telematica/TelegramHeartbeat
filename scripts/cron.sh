#!/bin/zsh

export ENV_VAR_FILE_PATH=$HOME/TelegramHeartBeat/scripts/envvars.sh

function writepidtoenvfile {
    sed -i.bak -E "s/PID=[0-9]+/PID=$PID_HEARTBEAT/" $ENV_VAR_FILE_PATH
}

function runjar {
    . $HOME/TelegramHeartBeat/scripts/run-optimized.sh --quiet & PID_HEARTBEAT=$!
}

if [ ! -f $ENV_VAR_FILE_PATH ]; then
    echo "<envvars.sh> file not found! Please create it."
    return 1;
fi

source $ENV_VAR_FILE_PATH

if ps -p $PID_HEARTBEAT > /dev/null
    then
        echo "$PID_HEARTBEAT is running, skipping."
        return 1;
    else
        runjar
        writepidtoenvfile
        echo "PID created: $PID_HEARTBEAT, starting new JAR app instance."
        return 0;
fi
