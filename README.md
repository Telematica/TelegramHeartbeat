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
