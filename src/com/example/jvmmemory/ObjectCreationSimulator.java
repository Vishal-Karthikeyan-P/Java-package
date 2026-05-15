package com.example.jvmmemory;

import java.util.ArrayList;
import java.util.List;

/**
 * Simulates object creation to demonstrate heap memory allocation and deallocation.
 * Used to show how the JVM manages memory during object lifecycle.
 */
public class ObjectCreationSimulator {

    private static final int OBJECT_SIZE = 1024 * 1024 * 10; // ~10MB per dummy object
    private final List<byte[]> allocatedObjects = new ArrayList<>();
    private boolean isSimulating = false;
    private String lastStatusMessage = "";

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
            Runtime runtime = Runtime.getRuntime();
            
            for (int i = 0; i < count; i++) {
                // Check if we have enough memory before creating the object
                long freeMemory = runtime.freeMemory();
                long totalMemory = runtime.totalMemory();
                long maxMemory = runtime.maxMemory();
                long availableMemory = maxMemory - (totalMemory - freeMemory);
                
                if (availableMemory < OBJECT_SIZE + (10 * 1024 * 1024)) { // Leave 10MB buffer
                    String message = "Warning: Not enough memory to create object " + (i + 1) + 
                                     ". Available: " + (availableMemory / 1024 / 1024) + "MB, " +
                                     "Needed: " + ((OBJECT_SIZE + (10 * 1024 * 1024)) / 1024 / 1024) + "MB";
                    System.out.println(message);
                    lastStatusMessage = message;
                    break; // Stop creating more objects
                }
                
                try {
                    byte[] obj = new byte[OBJECT_SIZE];
                    allocatedObjects.add(obj);
                } catch (OutOfMemoryError e) {
                    String message = "OutOfMemoryError: Could not allocate object " + (i + 1) + 
                                     ". Stopping object creation.";
                    System.out.println(message);
                    lastStatusMessage = message;
                    break;
                }
                
                // Note: Removed Thread.sleep to avoid busy wait warning
                // Memory changes are still visible through real-time UI updates
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

    /**
     * Gets the last status message from object creation
     * 
     * @return status message or empty string if no issues
     */
    public String getLastStatusMessage() {
        return lastStatusMessage;
    }

    /**
     * Clears the last status message
     */
    public void clearStatusMessage() {
        lastStatusMessage = "";
    }
}
