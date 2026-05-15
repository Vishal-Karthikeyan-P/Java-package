package com.example.jvmmemory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.management.BufferPoolMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

public final class MemoryMonitor extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final DecimalFormat MB_FORMAT = new DecimalFormat("#,##0.0");
    private final Runtime runtime = Runtime.getRuntime();
    private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    private final List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
    private final List<MemoryPoolMXBean> memoryPoolBeans = ManagementFactory.getMemoryPoolMXBeans();
    private final List<BufferPoolMXBean> bufferPoolBeans = ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class);
    private final ObjectCreationSimulator simulator = new ObjectCreationSimulator();

    // Heap memory labels
    private final JLabel heapTotalLabel = new JLabel();
    private final JLabel heapUsedLabel = new JLabel();
    private final JLabel heapFreeLabel = new JLabel();
    private final JLabel heapMaxLabel = new JLabel();
    private final JLabel availableLabel = new JLabel();

    // Non-heap memory labels
    private final JLabel nonHeapUsedLabel = new JLabel();
    private final JLabel nonHeapCommittedLabel = new JLabel();
    private final JLabel nonHeapMaxLabel = new JLabel();

    // Direct memory labels
    private final JLabel directMemoryUsedLabel = new JLabel();
    private final JLabel mappedMemoryUsedLabel = new JLabel();

    // GC and simulation labels
    private final JLabel gcCountLabel = new JLabel();
    private final JLabel allocatedObjectsLabel = new JLabel();
    private final JLabel simulatedMemoryLabel = new JLabel();

    // Progress bars
    private final JProgressBar heapUsageBar = new JProgressBar(0, 100);
    private final JProgressBar nonHeapUsageBar = new JProgressBar(0, 100);

    // Memory pools display
    private final JTextArea memoryPoolsArea = new JTextArea(8, 50);

    private Timer refreshTimer;

    public MemoryMonitor() {
        setTitle("JVM Memory Monitor - All Partitions");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);

        startRefreshTimer();
        updateStatus();
    }

    private JPanel createHeader() {
        JLabel title = new JLabel("JVM Memory Monitoring Tool");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        title.setHorizontalAlignment(JLabel.CENTER);

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(16, 16, 0, 16));
        header.add(title, BorderLayout.CENTER);
        return header;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        // Heap Memory Panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.4;
        mainPanel.add(createHeapMemoryPanel(), gbc);

        // Non-Heap Memory Panel
        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(createNonHeapMemoryPanel(), gbc);

        // Memory Pools Panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 0.6;
        mainPanel.add(createMemoryPoolsPanel(), gbc);

        return mainPanel;
    }

    private JPanel createHeapMemoryPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Heap Memory"));
        panel.setPreferredSize(new Dimension(350, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        panel.add(new JLabel("Total:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        panel.add(heapTotalLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.4;
        panel.add(new JLabel("Used:"), gbc);

        gbc.gridx = 1;
        panel.add(heapUsedLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Free:"), gbc);

        gbc.gridx = 1;
        panel.add(heapFreeLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Max:"), gbc);

        gbc.gridx = 1;
        panel.add(heapMaxLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Available:"), gbc);

        gbc.gridx = 1;
        panel.add(availableLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 8, 5, 8);
        heapUsageBar.setStringPainted(true);
        heapUsageBar.setPreferredSize(new Dimension(320, 25));
        panel.add(heapUsageBar, gbc);

        return panel;
    }

    private JPanel createNonHeapMemoryPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Non-Heap Memory"));
        panel.setPreferredSize(new Dimension(350, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        panel.add(new JLabel("Used:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        panel.add(nonHeapUsedLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.4;
        panel.add(new JLabel("Committed:"), gbc);

        gbc.gridx = 1;
        panel.add(nonHeapCommittedLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Max:"), gbc);

        gbc.gridx = 1;
        panel.add(nonHeapMaxLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Direct Memory:"), gbc);

        gbc.gridx = 1;
        panel.add(directMemoryUsedLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Mapped Memory:"), gbc);

        gbc.gridx = 1;
        panel.add(mappedMemoryUsedLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("GC Calls:"), gbc);

        gbc.gridx = 1;
        panel.add(gcCountLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 8, 5, 8);
        nonHeapUsageBar.setStringPainted(true);
        nonHeapUsageBar.setPreferredSize(new Dimension(320, 25));
        panel.add(nonHeapUsageBar, gbc);

        return panel;
    }

    private JPanel createMemoryPoolsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Memory Pools & Simulation"));

        // Memory pools text area
        memoryPoolsArea.setEditable(false);
        memoryPoolsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        memoryPoolsArea.setBackground(new Color(248, 248, 248));
        JScrollPane scrollPane = new JScrollPane(memoryPoolsArea);
        scrollPane.setPreferredSize(new Dimension(700, 200));

        // Simulation info panel
        JPanel simPanel = new JPanel(new GridBagLayout());
        simPanel.setBorder(BorderFactory.createTitledBorder("Object Simulation"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        simPanel.add(new JLabel("Allocated Objects:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        simPanel.add(allocatedObjectsLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        simPanel.add(new JLabel("Simulated Memory:"), gbc);

        gbc.gridx = 1;
        simPanel.add(simulatedMemoryLabel, gbc);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(simPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void startRefreshTimer() {
        refreshTimer = new Timer(1000, e -> updateStatus());
        refreshTimer.start();
    }

    private void updateStatus() {
        // Heap memory from Runtime
        long totalHeap = runtime.totalMemory();
        long freeHeap = runtime.freeMemory();
        long maxHeap = runtime.maxMemory();
        long usedHeap = totalHeap - freeHeap;
        long availableHeap = maxHeap - usedHeap;
        int heapPercentUsed = (int) ((usedHeap * 100) / maxHeap);

        // Non-heap memory from MemoryMXBean
        MemoryUsage nonHeapUsage = memoryMXBean.getNonHeapMemoryUsage();
        long nonHeapUsed = nonHeapUsage.getUsed();
        long nonHeapCommitted = nonHeapUsage.getCommitted();
        long nonHeapMax = nonHeapUsage.getMax();
        int nonHeapPercentUsed = nonHeapMax > 0 ? (int) ((nonHeapUsed * 100) / nonHeapMax) : 0;

        // Direct and mapped memory from BufferPoolMXBeans
        long directMemoryUsed = 0;
        long mappedMemoryUsed = 0;
        for (BufferPoolMXBean bufferPool : bufferPoolBeans) {
            if ("direct".equals(bufferPool.getName())) {
                directMemoryUsed = bufferPool.getMemoryUsed();
            } else if ("mapped".equals(bufferPool.getName())) {
                mappedMemoryUsed = bufferPool.getMemoryUsed();
            }
        }

        // Update heap labels
        heapTotalLabel.setText(formatMB(totalHeap));
        heapUsedLabel.setText(formatMB(usedHeap));
        heapFreeLabel.setText(formatMB(freeHeap));
        heapMaxLabel.setText(formatMB(maxHeap));
        availableLabel.setText(formatMB(availableHeap));

        // Update non-heap labels
        nonHeapUsedLabel.setText(formatMB(nonHeapUsed));
        nonHeapCommittedLabel.setText(formatMB(nonHeapCommitted));
        nonHeapMaxLabel.setText(nonHeapMax == -1 ? "Unlimited" : formatMB(nonHeapMax));
        directMemoryUsedLabel.setText(formatMB(directMemoryUsed));
        mappedMemoryUsedLabel.setText(formatMB(mappedMemoryUsed));

        // Update GC and simulation labels
        gcCountLabel.setText(String.valueOf(getTotalGcCount()));
        int objCount = simulator.getAllocatedObjectCount();
        allocatedObjectsLabel.setText(objCount + " objects");
        simulatedMemoryLabel.setText(formatMB(simulator.getEstimatedMemoryUsed()));

        // Update progress bars
        heapUsageBar.setValue(heapPercentUsed);
        heapUsageBar.setString(heapPercentUsed + "% used");
        heapUsageBar.setForeground(getBarColor(heapPercentUsed));

        nonHeapUsageBar.setValue(nonHeapPercentUsed);
        nonHeapUsageBar.setString(nonHeapPercentUsed + "% used");
        nonHeapUsageBar.setForeground(getBarColor(nonHeapPercentUsed));

        // Update memory pools display
        updateMemoryPoolsDisplay();
    }

    private long getTotalGcCount() {
        long total = 0;
        for (GarbageCollectorMXBean bean : gcBeans) {
            long count = bean.getCollectionCount();
            if (count >= 0) {
                total += count;
            }
        }
        return total;
    }

    private void updateMemoryPoolsDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("Memory Pools Details:\n");
        sb.append("=".repeat(60)).append("\n");

        for (MemoryPoolMXBean pool : memoryPoolBeans) {
            MemoryUsage usage = pool.getUsage();
            MemoryType type = pool.getType();

            sb.append(String.format("%-20s %-8s Used: %-8s Committed: %-8s Max: %-8s\n",
                pool.getName(),
                type == MemoryType.HEAP ? "HEAP" : "NON-HEAP",
                formatMB(usage.getUsed()),
                formatMB(usage.getCommitted()),
                usage.getMax() == -1 ? "Unlimited" : formatMB(usage.getMax())
            ));

            // Show usage percentage if max is defined
            if (usage.getMax() > 0) {
                int percent = (int) ((usage.getUsed() * 100) / usage.getMax());
                sb.append(String.format("                     Usage: %d%% %s\n",
                    percent, getUsageIndicator(percent)));
            }
        }

        sb.append("\nBuffer Pools:\n");
        sb.append("=".repeat(30)).append("\n");
        for (BufferPoolMXBean bufferPool : bufferPoolBeans) {
            sb.append(String.format("%-15s Count: %-6d Memory: %s\n",
                bufferPool.getName(),
                bufferPool.getCount(),
                formatMB(bufferPool.getMemoryUsed())
            ));
        }

        memoryPoolsArea.setText(sb.toString());
    }

    private String getUsageIndicator(int percent) {
        if (percent < 30) return "[LOW]";
        if (percent < 70) return "[MODERATE]";
        if (percent < 90) return "[HIGH]";
        return "[CRITICAL]";
    }

    private JPanel createControlPanel() {
        JButton triggerGcButton = new JButton("Trigger GC");
        triggerGcButton.addActionListener(e -> {
            System.gc();
            updateStatus();
        });

        JButton createObjectsButton = new JButton("Create 10 Objects");
        createObjectsButton.addActionListener(e -> {
            simulator.createObjects(10);
        });

        JButton clearObjectsButton = new JButton("Clear Objects");
        clearObjectsButton.addActionListener(e -> {
            simulator.clearObjects();
            updateStatus();
        });

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        panel.add(createObjectsButton);
        panel.add(triggerGcButton);
        panel.add(clearObjectsButton);
        return panel;
    }

    private Color getBarColor(int percent) {
        if (percent < 60) {
            return new Color(0, 153, 0);
        }
        if (percent < 80) {
            return new Color(255, 153, 0);
        }
        return new Color(204, 0, 0);
    }

    private static String formatMB(long bytes) {
        double mb = bytes / 1024.0 / 1024.0;
        return MB_FORMAT.format(mb) + " MB";
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            MemoryMonitor monitor = new MemoryMonitor();
            monitor.setVisible(true);
        });
    }
}
