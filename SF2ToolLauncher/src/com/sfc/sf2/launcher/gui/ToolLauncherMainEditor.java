/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.launcher.gui;

import com.sfc.sf2.core.gui.AbstractMainEditor;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.gui.controls.DirectoryButton;
import com.sfc.sf2.helpers.PathHelpers;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.stream.Stream;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author TiMMy
 */
public class ToolLauncherMainEditor extends AbstractMainEditor {

    private Console console1;
    private DirectoryButton directoryButtonTools;
    private JList<ToolEntry> toolList;
    private DefaultListModel<ToolEntry> listModel;
    private JLabel statusLabel;

    public ToolLauncherMainEditor() {
        super();
        initComponents();
        initCore(console1);
    }

    @Override
    protected void initEditor() {
        super.initEditor();
        scanTools();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SF2ToolLauncher");

        console1 = new Console();
        directoryButtonTools = new DirectoryButton();
        directoryButtonTools.setLabelText("Tools folder :");
        directoryButtonTools.setDirectoryPath(".");
        directoryButtonTools.setUseRelativeDirectory(false);
        directoryButtonTools.setInfoMessage("Folder to scan for SF2*.jar files. store/ fat jars are preferred over dist/ jars.");

        listModel = new DefaultListModel<>();
        toolList = new JList<>(listModel);
        toolList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        toolList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    launchSelected();
                }
            }
        });

        JButton scanButton = new JButton("Scan");
        scanButton.addActionListener(e -> scanTools());
        JButton launchButton = new JButton("Launch");
        launchButton.addActionListener(e -> launchSelected());

        statusLabel = new JLabel("Using Java " + System.getProperty("java.version") + " from " + System.getProperty("java.home"));

        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        top.add(directoryButtonTools, BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        buttons.add(scanButton);
        buttons.add(launchButton);
        top.add(buttons, BorderLayout.EAST);

        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.setBorder(BorderFactory.createTitledBorder("SF2 tools"));
        center.add(new JScrollPane(toolList), BorderLayout.CENTER);
        center.add(statusLabel, BorderLayout.SOUTH);

        JPanel main = new JPanel(new BorderLayout());
        main.add(top, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, main, console1);
        split.setDividerLocation(480);
        split.setResizeWeight(0.8);
        split.setOneTouchExpandable(true);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(split, BorderLayout.CENTER);
        setSize(new Dimension(900, 700));
        setLocationRelativeTo(null);
    }

    private void scanTools() {
        listModel.clear();
        Path root = Path.of(directoryButtonTools.getDirectoryPath());
        if (!root.isAbsolute()) {
            root = PathHelpers.getApplicationpath().resolve(root).normalize();
        }
        if (!Files.isDirectory(root)) {
            Console.logger().severe("Tools folder does not exist : " + root);
            statusLabel.setText("Folder not found: " + root);
            return;
        }
        Map<String, Path> chosen = new LinkedHashMap<>();
        try (Stream<Path> stream = Files.walk(root, 4)) {
            stream.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toLowerCase().endsWith(".jar"))
                    .filter(path -> {
                        String name = path.getFileName().toString();
                        return name.startsWith("SF2") && !name.contains("CoreLibrary") && !name.contains("ToolLauncher");
                    })
                    .filter(path -> {
                        String normalized = path.toString().replace('\\', '/');
                        return !normalized.contains("/lib/") && !normalized.contains("/nblib/");
                    })
                    .forEach(path -> {
                        String name = path.getFileName().toString();
                        Path existing = chosen.get(name);
                        if (existing == null || isPreferred(path, existing)) {
                            chosen.put(name, path);
                        }
                    });
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Scanning tools folder : " + root);
            return;
        }
        List<ToolEntry> entries = new ArrayList<>();
        for (Path jar : chosen.values()) {
            entries.add(new ToolEntry(jar));
        }
        entries.sort(Comparator.comparing(ToolEntry::displayName));
        for (ToolEntry entry : entries) {
            listModel.addElement(entry);
        }
        Console.logger().info("Found " + entries.size() + " tools in " + root);
        statusLabel.setText(entries.size() + " tools found. Java " + System.getProperty("java.version"));
    }

    private static boolean isPreferred(Path candidate, Path existing) {
        boolean candidateStore = candidate.toString().replace('\\', '/').contains("/store/");
        boolean existingStore = existing.toString().replace('\\', '/').contains("/store/");
        if (candidateStore != existingStore) {
            return candidateStore;
        }
        return candidate.toString().length() < existing.toString().length();
    }

    private void launchSelected() {
        ToolEntry selected = toolList.getSelectedValue();
        if (selected == null) {
            Console.logger().warning("No tool selected.");
            return;
        }
        File javaBin = new File(System.getProperty("java.home"), "bin/java.exe");
        if (!javaBin.exists()) {
            javaBin = new File(System.getProperty("java.home"), "bin/java");
        }
        try {
            ProcessBuilder builder = new ProcessBuilder(javaBin.getAbsolutePath(), "-jar", selected.path.toString());
            builder.directory(selected.path.getParent().toFile());
            builder.start();
            Console.logger().info("Launched " + selected.path);
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Could not launch : " + selected.path);
        }
    }

    public static void main(String args[]) {
        AbstractMainEditor.programSetup();
        java.awt.EventQueue.invokeLater(() -> new ToolLauncherMainEditor().setVisible(true));
    }

    private static final class ToolEntry {
        private final Path path;
        private final String display;

        private ToolEntry(Path path) {
            this.path = path;
            this.display = buildDisplay(path);
        }

        private static String buildDisplay(Path path) {
            String name = path.getFileName().toString();
            String version = readVersion(path);
            String location = path.getParent().toString();
            if (version != null && !version.isEmpty()) {
                return name + "  v" + version + "  (" + location + ")";
            }
            return name + "  (" + location + ")";
        }

        private static String readVersion(Path path) {
            try (JarFile jar = new JarFile(path.toFile())) {
                Manifest manifest = jar.getManifest();
                if (manifest == null) {
                    return null;
                }
                Attributes attributes = manifest.getMainAttributes();
                String version = attributes.getValue("Implementation-Version");
                if (version == null || version.isEmpty()) {
                    version = attributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
                }
                return version;
            } catch (Exception ex) {
                return null;
            }
        }

        private String displayName() {
            return path.getFileName().toString();
        }

        @Override
        public String toString() {
            return display;
        }
    }
}
