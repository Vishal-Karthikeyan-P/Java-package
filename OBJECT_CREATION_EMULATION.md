# Object Creation Emulation Guide

This guide demonstrates how to use the JVM Memory Monitoring Tool to simulate object creation and observe real-time memory changes.

## Scenario 1: Observe Heap Growth with Object Creation

### Steps:
1. Launch the application: `java -cp out com.example.jvmmemory.MemoryMonitor`
2. Note the initial **Heap Used** and **Heap Max** values
3. Click **Create 10 Objects** - watch the heap usage increase in real-time
4. Repeat clicking several times to allocate more objects
5. Observe the progress bar color changing from green → orange → red as heap fills

### Expected Results:
- **Allocated Objects** counter increments by 10 each click
- **Simulated Memory** shows cumulative size (~1 MB per 10 objects)
- **Heap Used** increases as objects are allocated
- Progress bar fills up, changing color based on usage percentage

---

## Scenario 2: Trigger Garbage Collection

### Steps:
1. Create several object batches (5-10 clicks of "Create 10 Objects")
2. Click **Trigger GC** to request garbage collection
3. Watch the **GC Calls** counter increment

### Expected Results:
- **GC Calls** increases by 1-2 (depending on how many GC threads run)
- **Heap Used** may decrease as garbage is collected
- Progress bar may shrink, changing from red/orange back to green
- If you created many objects, you'll see significant heap reclamation

---

## Scenario 3: Clear Objects to Reclaim Memory

### Steps:
1. Create several object batches to fill the heap
2. Observe high memory usage and progress bar in red
3. Click **Clear Objects** to release all allocated objects
4. Watch the memory status update in real-time

### Expected Results:
- **Allocated Objects** drops to 0 immediately
- **Simulated Memory** drops to 0 MB
- **Heap Used** eventually decreases (may need manual GC trigger)
- Progress bar returns to green
- **Available Memory** increases

---

## Scenario 4: Complete Memory Lifecycle

### Steps (Full Demonstration):
1. Start with a clean state - note initial memory values
2. Click **Create 10 Objects** multiple times (observe gradual heap growth)
3. **Trigger GC** several times to compact the heap
4. Click **Create 10 Objects** again to see dynamic reallocation
5. **Clear Objects** when done
6. **Trigger GC** one final time to see memory reclamation

### What This Demonstrates:
- **JVM Memory Model**: Shows heap total, used, and available portions
- **Object Allocation**: Objects consume heap memory when created
- **Garbage Collection**: Manual GC requests attempt to reclaim unused objects
- **Memory Reclamation**: Unused objects are freed for reuse
- **Heap Pressure**: Real-time visualization of how full the heap becomes

---

## Key Metrics Explained

| Metric | Meaning |
|--------|---------|
| **Heap Total** | Current size of the heap (can grow up to Heap Max) |
| **Heap Used** | Memory occupied by active objects and data |
| **Heap Free** | Available space within current heap allocation |
| **Heap Max** | Maximum heap size (set by JVM startup flags) |
| **Available Memory** | Free space available for allocation (Heap Max - Heap Used) |
| **GC Calls** | Total number of garbage collection cycles executed |
| **Allocated Objects** | Count of test objects created by the simulator |
| **Simulated Memory** | Estimated memory used by simulated objects |

---

## Performance Observations

- Each simulated object is approximately **100KB**
- Objects are created with a **50ms delay** to allow real-time observation
- Heap updates occur **every 1 second**
- The progress bar color indicates memory pressure:
  - **Green**: < 60% used (healthy)
  - **Orange**: 60-80% used (caution)
  - **Red**: > 80% used (critical)

---

## Advanced Experiments

### Experiment A: GC Efficiency
- Create 50 objects, note the heap size
- Manually trigger GC multiple times
- Observe whether heap shrinks after GC

### Experiment B: Heap Fragmentation
- Create 30 objects, clear, create 20 objects, clear, etc.
- Watch how the heap reorganizes
- Trigger GC to see compaction

### Experiment C: Out of Memory Simulation
- Create objects continuously until the progress bar is maxed out
- Note: This won't actually crash due to the simulator's bounded object size
- Demonstrates what happens under memory pressure

---

## Building and Running

```bash
# Build the project
./build.sh

# Run the monitor
java -cp out com.example.jvmmemory.MemoryMonitor
```

Or manually:
```bash
javac -d out src/com/example/jvmmemory/*.java
java -cp out com.example.jvmmemory.MemoryMonitor
```
