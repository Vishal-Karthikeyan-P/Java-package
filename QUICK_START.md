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

### What You'll See Now (Expanded Monitoring)

The tool now shows **ALL JVM memory partitions**:

#### Heap Memory Panel
- Total, Used, Free, Max, Available memory
- Visual progress bar with color coding

#### Non-Heap Memory Panel
- Used, Committed, Max memory
- Direct Memory (NIO buffers)
- Mapped Memory (memory-mapped files)
- Visual progress bar

#### Memory Pools Display
- **Eden Space**: New object allocation
- **Survivor Spaces**: Objects surviving GC
- **Old Generation**: Long-lived objects
- **Metaspace**: Class metadata
- **Code Cache**: Compiled native code
- **Compressed Class Space**: Class pointers

#### Buffer Pools
- Direct buffers count and memory usage
- Mapped buffers count and memory usage

### What You'll See When Creating Objects

| Action | Heap Panel | Memory Pools | Non-Heap |
|--------|------------|--------------|----------|
| **Start** | ~0-50MB used | Eden: low usage | Metaspace: class data |
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
