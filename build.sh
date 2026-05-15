#!/bin/bash

# JVM Memory Monitoring Tool - Build and Run Script

echo "=== JVM Memory Monitoring Tool ==="
echo ""
echo "Building..."

# Create output directory
mkdir -p out

# Compile the Java files
javac -d out \
  src/com/example/jvmmemory/MemoryMonitor.java \
  src/com/example/jvmmemory/ObjectCreationSimulator.java

if [ $? -eq 0 ]; then
    echo "Build successful!"
    echo ""
    echo "To run the application, execute:"
    echo "  java -cp out com.example.jvmmemory.MemoryMonitor"
    echo ""
else
    echo "Build failed!"
    exit 1
fi
