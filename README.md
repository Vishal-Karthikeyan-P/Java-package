# JVM Memory Monitoring Tool

A simple Java Swing application that monitors the JVM heap, available memory, and garbage collection activity. The tool uses `Runtime.getRuntime()`, `System.gc()`, and JVM memory statistics to display a Task Manager-style memory status window.

## Build & Run

Compile the application:

```bash
javac -d out src/com/example/jvmmemory/MemoryMonitor.java
```

Run the tool:

```bash
java -cp out com.example.jvmmemory.MemoryMonitor
```

## Features

- Displays heap total, used, free, and max memory
- Shows available memory and GC call count
- Includes a manual `Trigger GC` button
- Updates every second with live JVM memory usage

## Notes

- The application is built around the JVM memory model and uses the `Runtime` class for memory information.
- The manual GC button calls `System.gc()` to request garbage collection.
