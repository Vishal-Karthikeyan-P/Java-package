#!/bin/bash

# JVM Memory Monitoring Tool - Run Script with Limited Heap

echo "=== JVM Memory Monitoring Tool (Limited Heap) ==="
echo ""
echo "Running with heap limit: 512MB"
echo "This allows memory changes to be more visible when creating objects"
echo ""

# Run with limited heap (-Xmx512m) for demonstration purposes
java -Xmx512m -cp out com.example.jvmmemory.MemoryMonitor

