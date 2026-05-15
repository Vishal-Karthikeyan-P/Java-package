# Quick Start Guide

## Memory Now Visible with 10MB Objects + 512MB Heap Limit

### Build and Run (Recommended Way)

```bash
# Compile once
javac -d out src/com/example/jvmmemory/MemoryMonitor.java src/com/example/jvmmemory/ObjectCreationSimulator.java

# Run with limited heap (BEST FOR DEMO)
java -Xmx512m -cp out com.example.jvmmemory.MemoryMonitor
```

Or use the provided script:
```bash
./run.sh
```

### What You'll See Now

The tool shows **JVM heap memory** with real-time updates:

#### Heap Memory Panel
- Total, Used, Free, Max, Available memory
- Visual progress bar with color coding
- GC count tracking

#### Object Simulation Controls
- Create 10 Objects (10MB each)
- Trigger GC manually
- Clear Objects

### What You'll See When Creating Objects

| Action | Heap Used | Progress Bar | GC Count |
|--------|-----------|--------------|----------|
| **Start** | ~0-50MB | Green (0-30%) | 0 |
| **Create 10 Objects** | ~100MB | Yellow (30-70%) | 0 |
| **Create 20 Objects** | ~200MB | Orange (70-90%) | 0 |
| **Create 30 Objects** | ~300MB | Red (90-100%) | 0 |
| **Trigger GC** | ~50MB | Green | 1+ |

### Key Features Demonstrated

- **Runtime.getRuntime()**: Total, free, max memory
- **System.gc()**: Manual garbage collection
- **Real-time monitoring**: Updates every 1 second
- **Object simulation**: 10MB objects to show memory changes
- **Heap limiting**: -Xmx512m makes changes visible

### Troubleshooting

**Memory changes not visible?**
- Use `./run.sh` (limits heap to 512MB)
- Create multiple batches of objects
- Trigger GC to see cleanup

**Tool won't start?**
- Ensure Java 8+ is installed
- Run `./build.sh` first
- Check for compilation errors

**Want to see more memory detail?**
- Read `JVM_MEMORY_MANAGEMENT.md`
- Read `OBJECT_CREATION_EMULATION.md`
| **Click "Create 10 Objects"** | +100MB used | Eden: usage ↑ | Unchanged |
| **5 clicks** | 500MB used | Eden: fills up | Unchanged |
| **Click "Trigger GC"** | May decrease | Eden clears, Survivors fill | May clean Metaspace |
| **Click "Clear Objects"** | Drops after GC | All pools decrease | Unchanged |

### Key Difference from Before

- **Before**: Only basic heap metrics
- **Now**: Complete JVM memory visibility including:
  - All heap generations (Eden, Survivor, Old)
  - Non-heap areas (Metaspace, Code Cache)
  - Direct memory buffers
  - Real-time pool-by-pool monitoring

### Heap Limit Options

```bash
# Very tight - see changes immediately
java -Xmx256m -cp out com.example.jvmmemory.MemoryMonitor

# Balanced - recommended
java -Xmx512m -cp out com.example.jvmmemory.MemoryMonitor

# Looser - more room to experiment
java -Xmx1024m -cp out com.example.jvmmemory.MemoryMonitor
```

### Observe All Memory Concepts

1. **Heap Growth** - Create objects and see Eden space fill
2. **Garbage Collection** - Trigger GC to see memory pools reorganize
3. **Memory Reclamation** - Clear objects and watch all pools decrease
4. **JVM Memory Model** - All metrics use Runtime, MemoryMXBean, MemoryPoolMXBeans
5. **Real-time Updates** - Watch changes happen every second across all partitions
