package com.example.jvmmemory;

import java.util.ArrayList;
import java.util.List;

/**
 * Simulates object creation to demonstrate heap memory allocation and deallocation.
 * Used to show how the JVM manages memory during object lifecycle.
 */
public class ObjectCreationSimulator {

    private static final int OBJECT_SIZE = 1024 * 100; // ~100KB per dummy object
    private final List<byte[]> allocatedObjects = new ArrayList<>();
    private boolean isSimulating = false;

    /**
     * Creates objects to allocate heap memory
     * 
     * @param count Number of objects to create
     */
    public void createObjects(int count) {
        if (isSimulating) {
            return;
        }
        
        new Thread(() -> {
            isSimulating = true;
            for (int i = 0; i < count; i++) {
                byte[] obj = new byte[OBJECT_SIZE];
                allocatedObjects.add(obj);
                try {
                    //noinspection BusyWait
                    Thread.sleep(50); // Small delay to show gradual allocation
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            isSimulating = false;
        }).start();
    }

    /**
     * Clears allocated objects to simulate garbage collection opportunity
     */
    public void clearObjects() {
        allocatedObjects.clear();
    }

    /**
     * Returns the number of currently allocated objects
     * 
     * @return object count
     */
    public int getAllocatedObjectCount() {
        return allocatedObjects.size();
    }

    /**
     * Returns estimated memory used by allocated objects in bytes
     * 
     * @return memory in bytes
     */
    public long getEstimatedMemoryUsed() {
        return (long) allocatedObjects.size() * OBJECT_SIZE;
    }

    /**
     * Checks if simulation is running
     * 
     * @return true if actively simulating
     */
    public boolean isSimulating() {
        return isSimulating;
    }
}
