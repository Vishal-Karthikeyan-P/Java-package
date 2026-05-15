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

- **Memory Display**: Shows heap total, used, free, and max memory in real-time
- **GC Monitoring**: Displays garbage collection call count from all GC beans
- **Object Simulation**: Allocate objects on-demand to observe heap growth
- **Allocated Objects Tracking**: Shows number of simulated objects and their memory footprint
- **Live Heap Progress Bar**: Visual indicator (green < 60%, orange 60-80%, red > 80%)
- **Manual Controls**:
  - **Create 10 Objects**: Allocates ~100KB objects per click to simulate heap growth
  - **Trigger GC**: Manually calls `System.gc()` to request garbage collection
  - **Clear Objects**: Releases all allocated objects to show memory reclamation

## How to Use Object Creation Simulation

1. Click **Create 10 Objects** to allocate objects and watch heap usage increase
2. Observe the **Allocated Objects** and **Simulated Memory** display update
3. Click **Trigger GC** to request garbage collection after objects are created
4. Click **Clear Objects** to release the objects and see available memory increase

## Technical Details

- Each simulated object is ~100KB (byte array allocation)
- Objects are created in a separate thread with 50ms delay between allocations
- The simulator tracks both object count and estimated memory usage
- Updates refresh every 1 second to show real-time changes
- Uses `ManagementFactory.getGarbageCollectorMXBeans()` for accurate GC statistics

## Notes

- The application is built around the JVM memory model and uses the `Runtime` class for memory information
- The manual GC button calls `System.gc()` which is a request, not a guarantee
- Garbage collection may also be triggered by the JVM during object allocation
