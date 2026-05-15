package com.example.jvmmemory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

public final class MemoryMonitor extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final DecimalFormat MB_FORMAT = new DecimalFormat("#,##0.0");
    private final Runtime runtime = Runtime.getRuntime();
    private final List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
    private final ObjectCreationSimulator simulator = new ObjectCreationSimulator();

    private final JLabel heapTotalLabel = new JLabel();
    private final JLabel heapUsedLabel = new JLabel();
    private final JLabel heapFreeLabel = new JLabel();
    private final JLabel heapMaxLabel = new JLabel();
    private final JLabel availableLabel = new JLabel();
    private final JLabel gcCountLabel = new JLabel();
    private final JLabel allocatedObjectsLabel = new JLabel();
    private final JLabel simulatedMemoryLabel = new JLabel();
    private final JProgressBar heapUsageBar = new JProgressBar(0, 100);

    private Timer refreshTimer;

    public MemoryMonitor() {
        setTitle("JVM Memory Monitor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 360);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(createHeader(), BorderLayout.NORTH);
        add(createStatusPanel(), BorderLayout.CENTER);
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

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Memory and Garbage Collection Status"));
        panel.setPreferredSize(new Dimension(500, 230));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        panel.add(new JLabel("Heap Total:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        panel.add(heapTotalLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.2;
        panel.add(new JLabel("Heap Used:"), gbc);

        gbc.gridx = 1;
        panel.add(heapUsedLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Heap Free:"), gbc);

        gbc.gridx = 1;
        panel.add(heapFreeLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Heap Max:"), gbc);

        gbc.gridx = 1;
        panel.add(heapMaxLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Available Memory:"), gbc);

        gbc.gridx = 1;
        panel.add(availableLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("GC Calls:"), gbc);

        gbc.gridx = 1;
        panel.add(gcCountLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Allocated Objects:"), gbc);

        gbc.gridx = 1;
        panel.add(allocatedObjectsLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Simulated Memory:"), gbc);

        gbc.gridx = 1;
        panel.add(simulatedMemoryLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(16, 12, 12, 12);
        heapUsageBar.setStringPainted(true);
        heapUsageBar.setPreferredSize(new Dimension(450, 28));
        panel.add(heapUsageBar, gbc);

        return panel;
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

    private void startRefreshTimer() {
        refreshTimer = new Timer(1000, e -> updateStatus());
        refreshTimer.start();
    }

    private void updateStatus() {
        long totalHeap = runtime.totalMemory();
        long freeHeap = runtime.freeMemory();
        long maxHeap = runtime.maxMemory();
        long usedHeap = totalHeap - freeHeap;
        long availableHeap = maxHeap - usedHeap;
        int percentUsed = (int) ((usedHeap * 100) / maxHeap);

        heapTotalLabel.setText(formatMB(totalHeap));
        heapUsedLabel.setText(formatMB(usedHeap));
        heapFreeLabel.setText(formatMB(freeHeap));
        heapMaxLabel.setText(formatMB(maxHeap));
        availableLabel.setText(formatMB(availableHeap));
        gcCountLabel.setText(String.valueOf(getTotalGcCount()));

        int objCount = simulator.getAllocatedObjectCount();
        allocatedObjectsLabel.setText(objCount + " objects");
        simulatedMemoryLabel.setText(formatMB(simulator.getEstimatedMemoryUsed()));

        heapUsageBar.setValue(percentUsed);
        heapUsageBar.setString(percentUsed + "% used");
        heapUsageBar.setForeground(getBarColor(percentUsed));
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
