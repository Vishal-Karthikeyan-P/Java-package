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

| Action | Result |
|--------|--------|
| **Start** | ~0-50MB used out of 512MB (green bar) |
| **Click "Create 10 Objects"** | +100MB used (5 clicks = nearly full heap) |
| **Progress Bar** | Quickly turns orange (~60%), then red (>80%) |
| **Click "Trigger GC"** | Heap stays the same (objects still held) |
| **Click "Clear Objects"** | Allocated Objects → 0, Simulated Memory → 0 MB |
| **After Clear + GC** | Heap used drops significantly (green bar returns) |

### Key Difference from Before

- **Before**: Each object = 100KB → 150 objects = 15MB (barely visible in multi-GB default heap)
- **Now**: Each object = 10MB → 50 objects = 500MB (fills 512MB heap, very visible)

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

1. **Heap Growth** - Create objects and see memory climb
2. **Garbage Collection** - Trigger GC to attempt reclamation
3. **Memory Reclamation** - Clear objects and watch heap shrink
4. **JVM Memory Model** - All metrics use `Runtime.getRuntime()` and GarbageCollectorMXBean
5. **Real-time Updates** - Watch changes happen every second
