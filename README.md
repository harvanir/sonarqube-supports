# Sonar Qube Supports
Utilities for Sonar Qube covering:
* Project
    * Housekeeping
    * TBD

## Build
```shell script
./mvnw clean install -Dmaven.test.skip=true
```

## Run command
```shell script
java -server -Xmx30m -XX:+UseG1GC -XX:+UseStringDeduplication -XX:+CrashOnOutOfMemoryError -XX:MaxMetaspaceSize=60m -XX:CompressedClassSpaceSize=10m -XX:ReservedCodeCacheSize=30m -jar target/sonarqube-supports-0.0.1-SNAPSHOT.jar
```