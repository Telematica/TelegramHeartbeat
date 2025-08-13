# TelegramHeartbeat
Telegram based HeartBeat Utility


## Reference links:
- https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java

## Todos:
- Add Network Interfaces as console parameter
- Logging
- Escape interruption to exit
- AWS SNS for Push Notifications
- Apple Wallet Push Notifications
- Expo dev Push Notifications (React Native)
- Persists Public IPs (in memory, using telegram message as data source; or, using SQLite)
```java
    private static Set<String> publicIPHistory = new HashSet<>();
    private static Set<String> localIPHistory = new HashSet<>();
```


## Install
1) Build using Maven: `mvn install`.
2) Copy .jar file using `scripts/push-to-server.sh` to your server.

3) 3)Create envvars.sh file into `scripts` folder and add the fields:
```bash
export JAVA_TELEGRAM_API_KEY={TELEGRAM BOT API KEY}
export JAVA_TELEGRAM_USER_ID={CHAT ID FROM USER, GROUP OR CHANNEL}
export JAVA_TELEGRAM_MESSAGE_ID={SEQUENCE ID FROM THE CHAT}
export JAVA_TELEGRAM_API_URL="https://api.telegram.org/bot";
export JAVA_LOG_PATH={LOG DIRECTORY}
```

4) Use scripts/cron.sh to deploy on server.
