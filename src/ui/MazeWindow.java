package ui;

import maze.Maze;
import maze.Node;
import maze.PathAlgorithms;
import maze.PathResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.LinkedList;

public class MazeWindow extends JFrame {
    private static final String ICON_PATH = "./resources/icons/luna.png";
    private final String filename;

    private final Maze maze;

    // *** Components ***

    // Start
    JSpinner startX;
    JSpinner startY;
    JButton startClear;

    // Goal
    JSpinner goalX;
    JSpinner goalY;
    JButton goalClear;

    // Algorithm
    JComboBox<String> algorithmCombobox;

    // Reset/Run Buttons
    JButton runButton;
    JButton resetButton;


    // *** Results Panel ***
    JLabel numExpanded;
    JLabel pathCost;
    JLabel pathLength;


    // *** Pathfinder ***
    MazePanel mazePanel;
    PathResult pathResult;
    Node start;
    Node goal;

    public MazeWindow(String filename) {
        this.filename = filename;

        // Create Maze from filename
        maze = new Maze(filename);


        // Initialize Components
        initComponents();


        // Initialize Frame Settings
        initFrame();

    }

    /**
     * Method to initialize the frame settings
     */
    private void initFrame() {
        setIconImage(new ImageIcon(ICON_PATH).getImage());
        setTitle(filename);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Method to initialize the window components
     */
    private void initComponents() {
        // Setup Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Setup Maze Panel
        mazePanel = new MazePanel();

        // Setup Side Panel
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.setBorder(UiUtils.getPaddedBorder(5, 0));

        // Setup Options Panel
        JPanel optionsPanel = initOptionsPanel();

        // Setup Results Panel
        JPanel resultsPanel = initResultsPanel();

        // Add side panel components to side panel
        sidePanel.add(optionsPanel, BorderLayout.NORTH);
        sidePanel.add(resultsPanel, BorderLayout.SOUTH);

        // Add panels to main panel
        mainPanel.add(mazePanel, BorderLayout.CENTER);
        mainPanel.add(sidePanel, BorderLayout.EAST);

        // Add Panel to Frame
        add(mainPanel, BorderLayout.CENTER);

    }


    private JPanel initOptionsPanel() {
        // Setup Options Panel
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(0, 1));

        // Add 'Options' Title Label
        JLabel optionsLabel = new JLabel("Options", SwingConstants.CENTER);
        optionsLabel.setFont(UiUtils.getTitleFont());
        //optionsLabel.setBorder(new LineBorder(Color.BLACK, 1));

        // Setup Start and Goal Selection
        JPanel startPanel = new JPanel();
        startX = new JSpinner();
        startY = new JSpinner();
        startClear = new JButton("Clear");
        initTileInput(startPanel, 0);

        JPanel goalPanel = new JPanel();
        goalX = new JSpinner();
        goalY = new JSpinner();
        goalClear = new JButton("Clear");
        initTileInput(goalPanel, 1);


        // Algorithm Selection
        JPanel algorithmPanel = initAlgorithmPanel();


        // Run Button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout());
        runButton = new JButton("Run");
        runButton.addActionListener(e ->{
            executePathfinder();
        });

        // Reset Button
        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            resetRun();
        });
        buttonPanel.add(runButton);
        buttonPanel.add(resetButton);

        // Add components to panel
        optionsPanel.add(optionsLabel);
        optionsPanel.add(startPanel);
        optionsPanel.add(goalPanel);
        optionsPanel.add(algorithmPanel);
        optionsPanel.add(new JSeparator());
        optionsPanel.add(buttonPanel);

        return optionsPanel;
    }

    private void initTileInput(JPanel panel, int type) {
        if(type == 0) {
            JLabel startLabel = new JLabel("Start:");
            startLabel.setFont(UiUtils.getNormalFont());


            // Setup Start Inputs
            startX.addChangeListener(e -> {
                limitSpinner(startX, 0);
                updateStartTile();
            });

            startY.addChangeListener(e -> {
                limitSpinner(startY, 1);
                updateStartTile();
            });

            startClear.addActionListener(e -> {
                clearStart();
            });


            panel.add(startLabel);
            panel.add(startX);
            panel.add(startY);
            panel.add(startClear);
        }
        else {
            JLabel goalLabel = new JLabel("Goal:");
            goalLabel.setFont(UiUtils.getNormalFont());

            // Setup Goal Inputs
            goalX.addChangeListener(e -> {
                limitSpinner(goalX, 0);
                updateGoalTile();
            });

            goalY.addChangeListener(e -> {
                limitSpinner(goalY, 1);
                updateGoalTile();
            });

            goalClear.addActionListener(e -> {
                clearGoal();
            });

            panel.add(goalLabel);
            panel.add(goalX);
            panel.add(goalY);
            panel.add(goalClear);
        }
    }

    private JPanel initResultsPanel() {
        // Create Results Panel
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new GridLayout(2, 2));
        resultsPanel.setBorder(UiUtils.getPaddedBorder(0, 5));

        // Create Labels
        numExpanded = new JLabel("Number Expanded:");
        pathCost = new JLabel("Path Cost:");
        pathLength = new JLabel("Path Length:");

        // Update Font
        numExpanded.setFont(UiUtils.getNormalFont());
        pathCost.setFont(UiUtils.getNormalFont());
        pathLength.setFont(UiUtils.getNormalFont());

        // Add Components to Panel
        resultsPanel.add(numExpanded);
        resultsPanel.add(pathCost);
        resultsPanel.add(pathLength);

        return resultsPanel;
    }

    private JPanel initAlgorithmPanel() {
        // Algorithm Selection
        JPanel algorithmPanel = new JPanel();
        algorithmPanel.setLayout(new BorderLayout());
        algorithmPanel.setBorder(UiUtils.getPaddedBorder(5, 5));

        // Setup Label
        JLabel algoLabel = new JLabel("Algorithm:");
        algoLabel.setBorder(UiUtils.getPaddedBorder(0, 0, 0, 5));
        algoLabel.setFont(UiUtils.getNormalFont());

        // Setup algorithm ComboBox
        algorithmCombobox = new JComboBox<>();
        algorithmCombobox.setModel(new DefaultComboBoxModel<>(PathAlgorithms.getAlgorithmList()));
        algorithmCombobox.setSelectedIndex(-1);
        algorithmPanel.add(algoLabel, BorderLayout.WEST);
        algorithmPanel.add(algorithmCombobox, BorderLayout.CENTER);

        return algorithmPanel;
    }

    /**
     * Class - Used to handle Maze Display of PathResults
     */
    private class MazePanel extends JPanel implements ActionListener {
        HashSet<Node> expanded;
        int index;
        Timer timer;
        boolean showPath;

        public MazePanel() {
            //setBorder(new LineBorder(Color.BLACK, 1));
            timer = new Timer(100, this); // Setup Timer (Default 100ms)
            showPath = false;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;


            final int MIN_SQUARE_SIZE = 10; // Set Minimum Tile Size

            final int TILE_OPEN = 0;
            final int TILE_WALL = 1;
            final int TILE_START = 2;
            final int TILE_GOAL = 3;
            final int TILE_PATH = 4;
            final int TILE_EXPANDED = 5;


            // Get Size from Maze
            int cols = maze.getWidth();
            int rows = maze.getHeight();
            
            // Calculate Tile Size
            int tileSize = Math.max(MIN_SQUARE_SIZE, Math.min(getWidth() / cols, getHeight() / rows));

            // Calculate Offsets
            int xOffset = (getWidth() - (tileSize * cols)) / 2;
            int yOffset = (getHeight() - (tileSize * rows)) / 2;

            // Draw Tiles
            for(int i = 0; i < cols; i++) {
                for(int j = 0; j < rows; j++) {
                    int x = xOffset + i * tileSize;
                    int y = yOffset + j * tileSize;

                    // Get Current Node
                    Node curNode = new Node(j, i);

                    // Open Tile
                    int tileType = TILE_OPEN;

                    // Expanded and Path
                    if(pathResult != null) {
                        if(expanded.contains(curNode)) {
                            tileType = TILE_EXPANDED;
                        }

                        if(pathResult.path().contains(curNode) && showPath) {
                            tileType = TILE_PATH;
                        }
                    }


                    // Start Tile
                    if(start != null) {
                        if(curNode.equals(start)) {
                            tileType = TILE_START;
                        }
                    }

                    // Goal Tile
                    if(goal != null) {
                        if(curNode.equals(goal)) {
                            tileType = TILE_GOAL;
                        }
                    }

                    // Wall Tile
                    if(maze.isWall(curNode)) {
                        tileType = TILE_WALL;
                    }

                    // Switch between tile types
                    switch (tileType) {
                        case TILE_OPEN -> {
                            g2.setColor(Color.WHITE);
                            g2.fillRect(x, y, tileSize, tileSize);
                            drawText(g2, x, y, tileSize, maze.getTileStr(curNode));
                        }
                        case TILE_WALL -> {
                            g2.setColor(Color.GRAY);
                            g2.fillRect(x, y, tileSize, tileSize);
                        }
                        case TILE_START -> {
                            g2.setColor(Color.CYAN);
                            g2.fillRect(x, y, tileSize, tileSize);
                            drawText(g2, x, y, tileSize, "S");
                        }
                        case TILE_GOAL -> {
                            g2.setColor(Color.ORANGE);
                            g2.fillRect(x, y, tileSize, tileSize);
                            drawText(g2, x, y, tileSize, "G");
                        }
                        case TILE_PATH -> {
                            g2.setColor(Color.GREEN);
                            g2.fillRect(x, y, tileSize, tileSize);

                            drawArrow(g2, 0, x, y, tileSize);

                            drawText(g2, x, y, tileSize, maze.getTileStr(curNode));
                        }
                        case TILE_EXPANDED -> {
                            g2.setColor(Color.RED);
                            g2.fillRect(x, y, tileSize, tileSize);
                            drawText(g2, x, y, tileSize, maze.getTileStr(curNode));
                        }

                    }

                    // At Black Box Around Tile
                    g2.setColor(Color.BLACK);
                    g2.drawRect(x, y, tileSize, tileSize);

                }
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            LinkedList<Node> expandedOrder = pathResult.expandedOrder();

            if(index < expandedOrder.size()) {
                expanded.add(expandedOrder.get(index));

                index++;
            }
            else {
                showPath = true;
                timer.stop();
            }

            repaint();
        }

        /**
         * Method to begin display of expanding nodes
         */
        public void startExpandedDisplay() {
            showPath = false;
            expanded = new HashSet<>();
            index = 0;
            timer.start();
        }

        /**
         * Method to update the timer
         * @param delay is the new timer delay
         */
        public void updateTimer(int delay) {
            timer.setDelay(delay);
        }

        /**
         * Method to stop the timer
         */
        public void killTimer() {
            timer.stop();
        }

        private void drawArrow(Graphics2D g2, int direction, int x, int y, int size) {

            System.out.println(x + "," + y + "," + size);
            g2.setColor(Color.BLACK);

            // X Values
            int left = x;
            int right = x + size;
            int middleX = x + size/2;
            // Y Values
            int top = y;
            int bottomY = y + size;
            int middleY = y + size/2;


            switch (direction) {
                case 0 -> {
                    g2.drawPolygon(new int[]{left, middleX, right}, new int[]{middleY, top, middleY}, 3);
                }
            }

        }
    }

    // *** Utility Methods ***
    public void repaintMazePanel() {
        mazePanel.repaint();
    }

    /**
     * Method to begin execution of the current algorithm on the maze
     */
    public void executePathfinder() {
        // Return if No Algorithm Selected
        if(algorithmCombobox.getSelectedIndex() == -1) {
            return;
        }

        // Return if Start or Goal has not been set
        if(start == null || goal == null) {
            return;
        }

        // Return if start = goal
        if(start.equals(goal)) {
            return;
        }

        // Execute Path Algorithm
        Object algo = algorithmCombobox.getSelectedItem();
        if (algo != null) {
            String algoName = algo.toString();
            pathResult = maze.runAlgorithm(algoName, start, goal);
            mazePanel.startExpandedDisplay();

            numExpanded.setText("Number Expanded: " + pathResult.expanded().size());
            pathLength.setText("Path Length: " + pathResult.pathLength());
            pathCost.setText("Path Cost: " + pathResult.pathCost());
        }

    }

    /**
     * Method to reset the current run
     * >Deletes the previous pathResult
     * >Sets the Algorithm to Null
     * >Clear Start and Goal Nodes
     * >Kills Panel Timer
     * >Resets all Labels
     */
    public void resetRun() {
        pathResult = null;
        algorithmCombobox.setSelectedIndex(-1);
        clearStart();
        clearGoal();
        mazePanel.killTimer();

        // Reset Labels
        numExpanded.setText("Number Expanded:");
        pathLength.setText("Path Length:");
        pathCost.setText("Path Cost:");
    }

    /**
     * Method to clear the start node
     */
    public void clearStart() {
        startX.setValue(0);
        startY.setValue(0);
        start = null;
        repaintMazePanel();
    }

    /**
     * Method to clear the goal node
     */
    public void clearGoal() {
        goalX.setValue(0);
        goalY.setValue(0);
        goal = null;
        repaintMazePanel();
    }

    /**
     * Method to limit the spinners to only valid values
     * @param s is the given spinner
     * @param type is the spinner type (X or Y)
     */
    public void limitSpinner(JSpinner s, int type) {
        int val = (int)s.getValue();
        if(val < 0) {
            s.setValue(0);
        }
        // X
        if(type == 0) {
            int y = maze.getHeight();
            if(val > y-1) {
                s.setValue(y-1);
            }
        }
        // Y
        if(type == 1) {
            int x = maze.getWidth();
            if(val > x-1) {
                s.setValue(x-1);
            }
        }
    }

    /**
     * Method to update the start node on edit
     */
    public void updateStartTile() {
        Node newStart = new Node((int)startX.getValue(), (int)startY.getValue());

        if(!newStart.equals(goal) && maze.isValidNode(newStart) && maze.isOpen(newStart)) {
            start = newStart;
        }
        else {
            start = null;
        }
        repaintMazePanel();
    }

    /**
     * Method to update the goal node on edit
     */
    public void updateGoalTile() {
        Node newGoal = new Node((int)goalX.getValue(), (int)goalY.getValue());

        if(!newGoal.equals(start) && maze.isValidNode(newGoal) && maze.isOpen(newGoal)) {
            goal = newGoal;
        }
        else {
            goal = null;
        }
        repaintMazePanel();

    }

    /**
     * Method to draw text at the center of a tile of given tileSize at x,y
     * @param g2 is the graphics object
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @param tileSize is the tileSize
     * @param str is the text to draw
     */
    public static void drawText(Graphics2D g2, int x, int y, int tileSize, String str) {
        FontMetrics metrics = g2.getFontMetrics(UiUtils.getNormalFont());
        int textWidth = metrics.stringWidth(str);
        int textHeight = metrics.getHeight();
        int textX = x + (tileSize - textWidth) / 2;
        int textY = y + (tileSize - textHeight) / 2 + metrics.getAscent();
        g2.setColor(Color.BLACK);
        g2.drawString(str, textX, textY);
    }

}
