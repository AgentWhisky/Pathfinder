package ui;

import maze.Maze;
import maze.Node;
import maze.PathAlgorithms;
import maze.PathResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;

import static javax.swing.JOptionPane.showMessageDialog;

public class MazeWindow extends JFrame {
    // Tile Colors
    public static final Color COLOR_TILE_BOX = new Color(44, 44, 44);
    public static final Color COLOR_WALL = new Color(83, 83, 83);
    public static final Color COLOR_OPEN = new Color(239, 239, 239);
    public static final Color COLOR_START = new Color(95, 255, 0);
    public static final Color COLOR_GOAL = new Color(20, 122, 252);
    public static final Color COLOR_PATH = new Color(153,249,146);
    public static final Color COLOR_EXPANDED = new Color(133,16,0);
    public static final Color COLOR_ARROW = new Color(10, 63, 229);
    public static final Color COLOR_TEXT = Color.BLACK;

    // Load Icon
    private final ImageIcon LUNA_ICON = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/luna.png")));
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

    // Toggle Buttons
    JToggleButton showCost;
    JToggleButton showDirection;

    // Modify Display Speed
    JSlider speedModifier;

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

    /**
     * Constructor - Load maze from given filename and initialize GUI
     * @param parent is the load frame GUI
     * @param filename is the loaded filename
     */
    public MazeWindow(JFrame parent, String filename) {
        this.filename = filename;

        // Create Maze from filename
        maze = new Maze(filename);


        // Initialize Components
        initComponents();


        // Initialize Frame Settings
        initFrame(parent);

    }

    /**
     * Method to initialize the frame settings
     */
    private void initFrame(JFrame parent) {
        setIconImage(LUNA_ICON.getImage()); // Load Image
        setTitle(filename); // Set Title

        Dimension d = new Dimension(800, 600); // Set Frame Dimension
        setSize(d);
        setMinimumSize(d);

        setLocationRelativeTo(parent); // Open above parent frame
        setVisible(true);
    }

    /**
     * Method to initialize the window components
     */
    private void initComponents() {
        // Setup Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(UiUtils.getPaddedBorder(5, 5));

        // Setup Maze Panel
        mazePanel = new MazePanel();

        // Setup Side Panel
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.setBorder(UiUtils.getLineBorder());

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

    /**
     * Method to initialize the options panel
     * @return the options panel
     */
    private JPanel initOptionsPanel() {
        // Setup Options Panel
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(0, 1));
        optionsPanel.setBorder(UiUtils.getPaddedBorder(5, 5));

        // Add 'Options' Title Label
        JLabel optionsLabel = new JLabel("Options", SwingConstants.CENTER);
        optionsLabel.setFont(UiUtils.getTitleFont());

        // Setup Start Selection
        JPanel startPanel = new JPanel();
        startX = new JSpinner();
        startY = new JSpinner();
        startClear = new JButton("Clear");
        initTileInput(startPanel, 0);

        // Setup Goal Selection
        JPanel goalPanel = new JPanel();
        goalX = new JSpinner();
        goalY = new JSpinner();
        goalClear = new JButton("Clear");
        initTileInput(goalPanel, 1);

        // Algorithm Selection
        JPanel algorithmPanel = initAlgorithmPanel();

        // Toggle Options
        JLabel toggleTitle = new JLabel("Toggle Options", SwingConstants.CENTER);
        toggleTitle.setFont(UiUtils.getNormalFont());
        JPanel togglePanel = initTogglePanel();

        // Speed Modifier
        JLabel speedTitle = new JLabel("Speed Modifier", SwingConstants.CENTER);
        speedTitle.setFont(UiUtils.getNormalFont());
        setupSpeedModifier();

        // Run Button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout());
        runButton = new JButton("Run");
        runButton.addActionListener(e -> executePathfinder());

        // Reset Button
        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetRun());
        buttonPanel.add(runButton);
        buttonPanel.add(resetButton);

        // Add components to panel
        optionsPanel.add(optionsLabel);
        optionsPanel.add(startPanel);
        optionsPanel.add(goalPanel);
        optionsPanel.add(algorithmPanel);
        optionsPanel.add(toggleTitle);
        optionsPanel.add(togglePanel);
        optionsPanel.add(speedTitle);
        optionsPanel.add(speedModifier);
        optionsPanel.add(buttonPanel);

        return optionsPanel;
    }

    /**
     * Method to initialize the tile_input panel of given type
     * @param panel is the tile_input panel
     * @param type is the given type (0 for Start, 1 for Goal)
     */
    private void initTileInput(JPanel panel, int type) {
        // Setup Start Input
        if(type == 0) {
            JLabel startLabel = new JLabel("Start:");
            startLabel.setFont(UiUtils.getNormalFont());

            // Start X-Coordinate
            startX.addChangeListener(e -> {
                limitSpinner(startX, 0);
                updateStartTile();
            });

            // Start Y-Coordinate
            startY.addChangeListener(e -> {
                limitSpinner(startY, 1);
                updateStartTile();
            });

            // Start Clear Button
            startClear.addActionListener(e -> clearStart());

            // Add components to panel
            panel.add(startLabel);
            panel.add(startX);
            panel.add(startY);
            panel.add(startClear);
        }

        // Setup Goal Inputs
        else {
            JLabel goalLabel = new JLabel("Goal:");
            goalLabel.setFont(UiUtils.getNormalFont());

            // Goal X-Coordinate
            goalX.addChangeListener(e -> {
                limitSpinner(goalX, 0);
                updateGoalTile();
            });

            // Goal Y-Coordinate
            goalY.addChangeListener(e -> {
                limitSpinner(goalY, 1);
                updateGoalTile();
            });

            // Goal Clear Button
            goalClear.addActionListener(e -> clearGoal());

            // Add components to panel
            panel.add(goalLabel);
            panel.add(goalX);
            panel.add(goalY);
            panel.add(goalClear);
        }
    }

    /**
     * Method to initialize the results panel
     * @return result panel
     */
    private JPanel initResultsPanel() {
        // Create Results Panel
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new GridLayout(0,1));
        resultsPanel.setBorder(UiUtils.getPaddedBorder(5, 5));

        // Add 'Results' Title Label
        JLabel resultsLabel = new JLabel("Results Info", SwingConstants.CENTER);
        resultsLabel.setFont(UiUtils.getTitleFont());

        // Create Labels
        numExpanded = new JLabel("Number Expanded:");
        pathCost = new JLabel("Path Cost:");
        pathLength = new JLabel("Path Length:");

        // Update Font
        numExpanded.setFont(UiUtils.getNormalFont());
        pathCost.setFont(UiUtils.getNormalFont());
        pathLength.setFont(UiUtils.getNormalFont());

        // Add Components to Panel
        resultsPanel.add(resultsLabel);
        resultsPanel.add(numExpanded);
        resultsPanel.add(pathLength);
        resultsPanel.add(pathCost);

        return resultsPanel;
    }

    /**
     * Method to initialize the algorithm selection panel
     * @return algorithm selection panel
     */
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
     * Method to initialize the toggle button panel
     * @return the toggle butotn panel
     */
    public JPanel initTogglePanel() {
        JPanel togglePanel = new JPanel();
        togglePanel.setLayout(new GridLayout());

        // Setup Show Cost Button
        showCost = new JToggleButton("Show Cost");
        showCost.setSelected(true);
        showCost.addActionListener(e -> repaintMazePanel());

        // Setup Show Direction Button
        showDirection = new JToggleButton("Show Direction");
        showDirection.setSelected(true);
        showDirection.addActionListener(e -> repaintMazePanel());

        // Add components to panel
        togglePanel.add(showCost);
        togglePanel.add(showDirection);

        return togglePanel;
    }

    /**
     * Method to set up the display speed modifier slider
     */
    public void setupSpeedModifier() {
        speedModifier = new JSlider(0, 5, 0);
        speedModifier.addChangeListener(e -> mazePanel.updateTimer(speedModifier.getValue()));
        speedModifier.setPaintTicks(true);
        speedModifier.setMajorTickSpacing(1);
        speedModifier.setPaintLabels(true);
        speedModifier.setSnapToTicks(true);
    }

    /**
     * Class - Used to handle Maze Display of PathResults
     */
    private class MazePanel extends JPanel implements ActionListener {
        public static final int DEFAULT_TIMER_DELAY = 100;

        public HashSet<Node> expanded;
        public int index;
        public Timer timer;
        public boolean showPath;

        public MazePanel() {
            //setBorder(new LineBorder(Color.BLACK, 1));
            timer = new Timer(DEFAULT_TIMER_DELAY, this); // Setup Timer (Default 100ms)
            showPath = false;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // Set rendering hints for text anti-aliasing
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Set rendering hints for drawing anti-aliasing
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            final int MIN_SQUARE_SIZE = 10; // Set Minimum Tile Size

            // Tile IDs
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
                        // Tile Type - Expanded
                        if(expanded.contains(curNode)) {
                            tileType = TILE_EXPANDED;
                        }

                        // Tile Type - Path
                        if(pathResult.path().contains(curNode) && showPath) {
                            // Override curNode with the pathNode to include Direction
                            curNode = pathResult.path().get(pathResult.path().indexOf(curNode));

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
                        // Draw Open Tile
                        case TILE_OPEN -> {
                            g2.setColor(COLOR_OPEN);
                            g2.fillRect(x, y, tileSize, tileSize);
                            drawCost(g2, x, y, tileSize, maze.getTileStr(curNode));
                        }
                        // Draw Wall Tile
                        case TILE_WALL -> {
                            g2.setColor(COLOR_WALL);
                            g2.fillRect(x, y, tileSize, tileSize);
                        }
                        // Draw Start Tile
                        case TILE_START -> {
                            g2.setColor(COLOR_START);
                            g2.fillRect(x, y, tileSize, tileSize);

                            if(showPath && pathResult != null) {
                                drawArrow(g2, curNode.getDirection(), x, y, tileSize);
                            }

                            drawText(g2, x, y, tileSize, "S");
                        }
                        // Draw Goal Tile
                        case TILE_GOAL -> {
                            g2.setColor(COLOR_GOAL);
                            g2.fillRect(x, y, tileSize, tileSize);

                            if(showCost.isSelected()) {
                                drawCost(g2, x, y, tileSize, "G-" + maze.getTileStr(curNode));
                            }
                            else {
                                drawText(g2, x, y, tileSize, "G");
                            }

                        }
                        // Draw Path Tile w/ Directional Arrow
                        case TILE_PATH -> {
                            g2.setColor(COLOR_PATH);
                            g2.fillRect(x, y, tileSize, tileSize);

                            drawArrow(g2, curNode.getDirection(), x, y, tileSize);

                            drawCost(g2, x, y, tileSize, maze.getTileStr(curNode));
                        }
                        // Draw Expanded Tile
                        case TILE_EXPANDED -> {
                            g2.setColor(COLOR_EXPANDED);
                            g2.fillRect(x, y, tileSize, tileSize);
                            // Show Cost
                            drawCost(g2, x, y, tileSize, maze.getTileStr(curNode));

                        }
                    }

                    // At Black Box Around Tile
                    g2.setColor(COLOR_TILE_BOX);
                    Stroke oldStroke = g2.getStroke();
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRect(x, y, tileSize, tileSize);
                    g2.setStroke(oldStroke);

                }
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            LinkedList<Node> expandedOrder = pathResult.expandedOrder();

            // Add next expanded node to display
            if(index < expandedOrder.size()) {
                expanded.add(expandedOrder.get(index));
                index++;
            }
            // Stop Timer as Display is Done
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
         * @param multiplier is used to modify the timer
         */
        public void updateTimer(int multiplier) {
            if(multiplier <= 0) {
                return;
            }
            // New Delay = DEFAULT / 2^multiplier
            int newDelay = DEFAULT_TIMER_DELAY / (int)(Math.pow(2, multiplier));
            System.out.println(newDelay);

            timer.setDelay(newDelay);
        }

        /**
         * Method to stop the timer
         */
        public void killTimer() {
            timer.stop();
        }

        /**
         * Method to draw costHeuristic when enabled
         * @param g2 is the Graphics2D object
         * @param x is the x coordinate
         * @param y is the y coordinate
         * @param tileSize is the tileSize to fit
         * @param text is the text to draw
         */
        private void drawCost(Graphics2D g2, int x, int y, int tileSize, String text) {
            if(!showCost.isSelected()) {
                return;
            }

            drawText(g2, x, y, tileSize, text);
        }



        /**
         * Method to draw the directional arrow for a tile at x,y of given size
         * @param g2 is the Graphics2D Object
         * @param direction is the direction
         * @param x is the given x coordinate
         * @param y is the given y coordinate
         * @param size is the given size
         */
        private void drawArrow(Graphics2D g2, int direction, int x, int y, int size) {
            // Only Draw Arrow if Toggled
            if(!showDirection.isSelected()) {
                return;
            }

            // Set Arrow Color
            g2.setColor(COLOR_ARROW);

            // Draw Arrow in given direction
            switch (direction) {
                case Node.NORTH -> g2.fillPolygon(
                        new int[]{x + size/4, x + size/2, x + 3*(size/4)},
                        new int[]{y + size/4, y, y + size/4},
                        3
                );
                case Node.SOUTH -> g2.fillPolygon(
                        new int[]{x + size/4, x + size/2, x + 3*(size/4)},
                        new int[]{y + 3*(size/4), y + size, y + 3*(size/4)},
                        3);
                case Node.EAST -> g2.fillPolygon(
                        new int[]{x + 3*(size/4), x+size, x+3*(size/4)},
                        new int[]{y+size/4, y+size/2, y+3*(size/4)},
                        3);
                case Node.WEST -> g2.fillPolygon(
                        new int[]{x+size/4, x, x+size/4},
                        new int[]{y+size/4, y+size/2, y+3*(size/4)},
                        3);
            }

        }
    }

    // *** Utility Methods ***

    /**
     * Method to force repaint the mazePanel
     */
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

            if(pathResult == null) {
                showMessageDialog(null, "No Path Found");
                return;
            }

            // Start Display of Expanded Nodes
            mazePanel.startExpandedDisplay();

            // Update Text in Results Panel
            numExpanded.setText(String.format("Number Expanded: %d Nodes", pathResult.expanded().size()));
            pathLength.setText(String.format("Path Length: %d Nodes", pathResult.pathLength()));
            pathCost.setText(String.format("Path Cost: %d Units", pathResult.pathCost()));
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

        g2.setFont(UiUtils.getVariableFont(g2, tileSize, str));
        g2.setColor(COLOR_TEXT);
        g2.drawString(str, textX, textY);
    }

}
