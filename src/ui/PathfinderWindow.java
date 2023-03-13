package ui;

import maze.FileUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Class - Creates a Swing Window for loading maze files
 */
public class PathfinderWindow extends JFrame {
    private String[] filenames;
    private JComboBox<String> filenameComboBox;


    public PathfinderWindow() {
        // Load Filenames
        updateFilenames();

        // Initialize Components
        initComponents();

        // Setup Frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 160);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void initComponents() {
        // Create Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 1));
        mainPanel.setBorder(new EmptyBorder(0, 5, 5, 5));

        // Setup Title
        JLabel titleLabel = new JLabel();
        titleLabel.setText("Pathfinder");
        int font = titleLabel.getFont().getStyle() | Font.BOLD; // Set current font as Bold
        float fontSize = titleLabel.getFont().getSize() + 10f; // Increase Font Size by 10
        titleLabel.setFont(titleLabel.getFont().deriveFont(font, fontSize));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel);

        // Setup ComboBox
        filenameComboBox = new JComboBox<>();
        filenameComboBox.setSelectedIndex(-1);
        filenameComboBox.setModel(new DefaultComboBoxModel<>(filenames));
        mainPanel.add(filenameComboBox);

        // Create button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout());

        // Setup Refresh Files Button
        JButton refreshFilesButton = new JButton();
        refreshFilesButton.setText("Refresh File List");
        refreshFilesButton.addActionListener(e ->{
            updateFilenames();
            updateFileComboBox();
        });
        buttonPanel.add(refreshFilesButton);

        // Setup Load Button
        JButton loadButton = new JButton();
        loadButton.setText("Load File");
        loadButton.addActionListener(e -> {
            // SETUP NEW WINDOW OPEN
        });
        buttonPanel.add(loadButton);

        // Add ButtonPanel to mainPanel
        mainPanel.add(buttonPanel);

        // Add mainPanel to Frame
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Method to load the current filenames for use in the combobox
     */
    private void updateFilenames() {
        filenames = FileUtils.getAllMazeFiles();
    }

    /**
     * Method to update the filename ComboBox
     */
    private void updateFileComboBox() {
        filenameComboBox.setModel(new DefaultComboBoxModel<>(filenames));
    }
}
