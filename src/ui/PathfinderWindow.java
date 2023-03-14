package ui;

import maze.FileUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Class - Creates a Swing Window for loading maze files
 */
public class PathfinderWindow extends JFrame {
    private static final String ICON_PATH = "./resources/icons/luna.png";
    private String[] filenames;
    private JComboBox<String> filenameComboBox;

    public PathfinderWindow() {
        // Load Filenames
        updateFilenames();

        // Initialize Components
        initComponents();

        // Initialize Frame
        initFrame();
    }

    /**
     * Method to initialize the frame settings
     */
    private void initFrame() {
        setIconImage(new ImageIcon(ICON_PATH).getImage());
        setTitle("Pathfinder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 120);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    /**
     * Method to initialize the window components
     */
    private void initComponents() {
        // Create Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 1));
        mainPanel.setBorder(new EmptyBorder(0, 5, 5, 5));

        // Setup ComboBox
        filenameComboBox = new JComboBox<>();
        filenameComboBox.setModel(new DefaultComboBoxModel<>(filenames));
        filenameComboBox.setSelectedIndex(-1);
        mainPanel.add(filenameComboBox);

        // Create button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout());

        // Setup Refresh Files Button
        JButton refreshFilesButton = new JButton();
        refreshFilesButton.setText("Refresh File List");
        refreshFilesButton.addActionListener(e ->{
            updateFilenames(); // Update Filenames
            updateFileComboBox(); // Refresh ComboBox
        });
        buttonPanel.add(refreshFilesButton);

        // Setup Load Button
        JButton loadButton = new JButton();
        loadButton.setText("Load File");
        loadButton.addActionListener(e -> loadNewWindow());
        buttonPanel.add(loadButton);

        // Add ButtonPanel to mainPanel
        mainPanel.add(buttonPanel);

        // Add mainPanel to Frame
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Method to open a new window with the selected maze file
     * > Any Error Thrown during creation will fail to open new window and be discarded
     */
    private void loadNewWindow() {
        Object filename = filenameComboBox.getSelectedItem();
        if(filename != null) {
            try {
                new MazeWindow(filename.toString());
            }
            // Any Exception Thrown During Creation of New Window Can Be Discarded
            catch (Exception x) {
                showMessageDialog(null, "Failed to Load Maze File: " + filename);
            }
        }
        // No File Error
        else {
            showMessageDialog(null, "No File Selected");
        }
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
        filenameComboBox.setSelectedIndex(-1);
    }
}
