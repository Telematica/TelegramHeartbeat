#!/bin/bash

# Optimized JVM parameters for Telegram Heartbeat application
# This script provides optimal garbage collection and memory settings

# Memory settings
HEAP_SIZE="256m"
MAX_HEAP_SIZE="512m"
NEW_GEN_SIZE="64m"
MAX_NEW_GEN_SIZE="128m"

# Garbage Collection settings
# Use G1GC for better performance and lower pause times
GC_TYPE="-XX:+UseG1GC"
GC_CONCURRENT="-XX:+UseG1GC -XX:MaxGCPauseMillis=200"
GC_PARALLEL="-XX:+UseG1GC -XX:ParallelGCThreads=2 -XX:ConcGCThreads=1"

# Memory optimization
MEMORY_OPTS="-XX:+UseCompressedOops -XX:+UseStringDeduplication"
MEMORY_OPTS="$MEMORY_OPTS -XX:+OptimizeStringConcat -XX:+UseCompressedClassPointers"

# GC logging (optional - uncomment for debugging)
# GC_LOGGING="-Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintGCTimeStamps"

# Performance tuning
PERF_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
PERF_OPTS="$PERF_OPTS -XX:+UseAdaptiveSizePolicy -XX:G1HeapRegionSize=16m"

# JVM options
JVM_OPTS="-server"
JVM_OPTS="$JVM_OPTS -Xms$HEAP_SIZE -Xmx$MAX_HEAP_SIZE"
JVM_OPTS="$JVM_OPTS -XX:NewSize=$NEW_GEN_SIZE -XX:MaxNewSize=$MAX_NEW_GEN_SIZE"
JVM_OPTS="$JVM_OPTS $GC_TYPE $GC_CONCURRENT $GC_PARALLEL"
JVM_OPTS="$JVM_OPTS $MEMORY_OPTS $PERF_OPTS"
JVM_OPTS="$JVM_OPTS $GC_LOGGING"

# Check if JAR file exists
JAR_FILE="target/telegram-heartbeat-1.0-SNAPSHOT-jar-with-dependencies.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo "Building application..."
    mvn clean package
fi

# Run the application with optimized settings
echo "Starting Telegram Heartbeat with optimized JVM settings..."
echo "JVM Options: $JVM_OPTS"
echo ""

$JAVA_BIN \
-server \
-Xms$HEAP_SIZE -Xmx$MAX_HEAP_SIZE \
-XX:NewSize=$NEW_GEN_SIZE -XX:MaxNewSize=$MAX_NEW_GEN_SIZE \
-XX:+UseG1GC -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseG1GC -XX:ParallelGCThreads=2 -XX:ConcGCThreads=1 \
-XX:+UseCompressedOops -XX:+UseStringDeduplication -XX:+OptimizeStringConcat -XX:+UseCompressedClassPointers \
-XX:+TieredCompilation -XX:TieredStopAtLevel=1 -XX:+UseAdaptiveSizePolicy -XX:G1HeapRegionSize=16m \
-jar "$JAR_FILE" "$@"