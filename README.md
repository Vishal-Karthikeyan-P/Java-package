# JVM Memory Monitoring Tool

A comprehensive Java Swing application that monitors all JVM memory partitions including heap, non-heap, memory pools, and buffer pools. The tool uses `Runtime.getRuntime()`, `System.gc()`, and various MXBeans to display detailed memory status in a Task Manager-style window.

## Documentation

- **[JVM_MEMORY_MANAGEMENT.md](JVM_MEMORY_MANAGEMENT.md)** - Comprehensive guide to JVM internals, memory management, and garbage collection concepts
- **[OBJECT_CREATION_EMULATION.md](OBJECT_CREATION_EMULATION.md)** - Step-by-step guide for using object creation simulation
- **[QUICK_START.md](QUICK_START.md)** - Quick reference for getting started with memory monitoring

## Build & Run

Compile the application:

```bash
javac -d out src/com/example/jvmmemory/MemoryMonitor.java
```

Run the tool with default heap (system dependent):

```bash
java -cp out com.example.jvmmemory.MemoryMonitor
```

**Recommended: Run with limited heap** for better memory visualization:

```bash
java -Xmx512m -cp out com.example.jvmmemory.MemoryMonitor
```

Or use the included run script:

```bash
./run.sh
```

## Features

- **Complete Memory Monitoring**:
  - Heap memory (Eden, Survivor, Old Generation)
  - Non-heap memory (Metaspace, Code Cache, etc.)
  - Direct and mapped buffer memory
  - All JVM memory pools with usage indicators

- **Real-time Updates**: Refreshes every second with live JVM memory statistics
- **Visual Indicators**: Color-coded progress bars (green < 60%, orange 60-80%, red > 80%)
- **Object Simulation**: Allocate objects on-demand to observe heap growth
- **Manual GC Control**: Trigger garbage collection to see memory reclamation
- **Detailed Pool Information**: Shows usage for all memory pools and buffer pools

## How to Use Object Creation Simulation

1. Click **Create 10 Objects** to allocate objects and watch heap usage increase
2. Observe the **Allocated Objects** and **Simulated Memory** display update
3. Click **Trigger GC** to request garbage collection after objects are created
4. Click **Clear Objects** to release the objects and see available memory increase

## Technical Details

- Each simulated object is **~10MB** (byte array allocation) - increased from 100KB to make memory changes visible
- Objects are created in a separate thread with 50ms delay between allocations
- The simulator tracks both object count and estimated memory usage
- Updates refresh every 1 second to show real-time changes
- Uses `ManagementFactory.getGarbageCollectorMXBeans()` for accurate GC statistics
- **Recommended to run with limited heap** (`-Xmx512m`) to see meaningful memory changes with fewer objects

## Memory Limiting Explained

By default, the JVM uses a large heap (often 1/4 of system RAM). This means creating even 150 objects (1.5GB total) won't visibly impact the heap. The included `run.sh` script limits the heap to 512MB:

```bash
java -Xmx512m -cp out com.example.jvmmemory.MemoryMonitor
```

This means:
- 10 objects = ~100MB (visible change)
- 20 objects = ~200MB (40% of heap)
- 50 objects = ~500MB (nearly full heap, progress bar turns orange/red)

You can adjust the heap limit by changing `-Xmx512m` to any value (e.g., `-Xmx256m` for tighter limits, `-Xmx1024m` for more space).

## Notes

- The application is built around the JVM memory model and uses the `Runtime` class for memory information
- The manual GC button calls `System.gc()` which is a request, not a guarantee
- Garbage collection may also be triggered by the JVM during object allocation
