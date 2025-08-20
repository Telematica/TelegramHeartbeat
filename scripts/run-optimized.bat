@echo off
REM Optimized JVM parameters for Telegram Heartbeat application
REM This script provides optimal garbage collection and memory settings

REM Memory settings
set HEAP_SIZE=256m
set MAX_HEAP_SIZE=512m
set NEW_GEN_SIZE=64m
set MAX_NEW_GEN_SIZE=128m

REM Garbage Collection settings
REM Use G1GC for better performance and lower pause times
set GC_TYPE=-XX:+UseG1GC
set GC_CONCURRENT=-XX:+UseG1GC -XX:MaxGCPauseMillis=200
set GC_PARALLEL=-XX:+UseG1GC -XX:ParallelGCThreads=2 -XX:ConcGCThreads=1

REM Memory optimization
set MEMORY_OPTS=-XX:+UseCompressedOops -XX:+UseStringDeduplication
set MEMORY_OPTS=%MEMORY_OPTS% -XX:+OptimizeStringConcat -XX:+UseCompressedClassPointers

REM GC logging (optional - uncomment for debugging)
REM set GC_LOGGING=-Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintGCTimeStamps

REM Performance tuning
set PERF_OPTS=-XX:+TieredCompilation -XX:TieredStopAtLevel=1
set PERF_OPTS=%PERF_OPTS% -XX:+UseAdaptiveSizePolicy -XX:G1HeapRegionSize=16m

REM JVM options
set JVM_OPTS=-server
set JVM_OPTS=%JVM_OPTS% -Xms%HEAP_SIZE% -Xmx%MAX_HEAP_SIZE%
set JVM_OPTS=%JVM_OPTS% -XX:NewSize=%NEW_GEN_SIZE% -XX:MaxNewSize=%MAX_NEW_GEN_SIZE%
set JVM_OPTS=%JVM_OPTS% %GC_TYPE% %GC_CONCURRENT% %GC_PARALLEL%
set JVM_OPTS=%JVM_OPTS% %MEMORY_OPTS% %PERF_OPTS%
set JVM_OPTS=%JVM_OPTS% %GC_LOGGING%

REM Check if JAR file exists
set JAR_FILE=target\telegram-heartbeat-1.0-SNAPSHOT-jar-with-dependencies.jar
if not exist "%JAR_FILE%" (
    echo Building application...
    call mvn clean package
)

REM Run the application with optimized settings
echo Starting Telegram Heartbeat with optimized JVM settings...
echo JVM Options: %JVM_OPTS%
echo.

java %JVM_OPTS% -jar "%JAR_FILE%" %* 