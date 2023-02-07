import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class PathDisplay {

    private static final int DEFAULT_TILE_SIZE = 120;

    public PathDisplay(PathResults pathResults, double scale) {
        BoardPanel bp = new BoardPanel(pathResults, scale);

        int width = (pathResults.board().getWidth()) * (int)(DEFAULT_TILE_SIZE * scale) + 16;
        int height = (pathResults.board().getHeight()) * (int)(DEFAULT_TILE_SIZE * scale) + 39;

        JFrame frame = new JFrame("Pathfinder");
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

        frame.setSize(width, height);

        frame.setResizable(false);
        frame.add(bp);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private class BoardPanel extends JPanel {

        PathResults pr;
        double scale;


        public BoardPanel(PathResults pr, double scale) {
            this.pr = pr;
            this.scale = scale;


        }

        @Override
        public void paintComponent(final Graphics g) {

            super.paintComponent(g);

            BufferedImage tileImg = null;
            BufferedImage expandedImg = null;
            BufferedImage wallImg = null;
            BufferedImage pathImg = null;
            BufferedImage startImg = null;
            BufferedImage finishImg = null;


            try {
                tileImg = ImageIO.read(new File("Resources/images/default_tile.png"));
                expandedImg = ImageIO.read(new File("Resources/images/expanded.png"));
                wallImg = ImageIO.read(new File("Resources/images/wall.png"));
                pathImg = ImageIO.read(new File("Resources/images/path.png"));
                startImg = ImageIO.read(new File("Resources/images/start.png"));
                finishImg = ImageIO.read(new File("Resources/images/finish.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }


            Board b = pr.board();

            int tileSize = (int) (DEFAULT_TILE_SIZE * scale);

            for(int i = 0; i < b.getWidth(); i++) {
                for(int j = 0; j < b.getHeight(); j++) {
                    int x = i * tileSize;
                    int y = j * tileSize;

                    Node curNode = new Node(j, i);
                    String tileStr = b.getTile(curNode);

                    Node[] path = pr.path();
                    HashSet<Node> expanded = pr.expanded();

                    BufferedImage curImage;
                    String displayText = null;

                    // Start
                    if(curNode.equals(path[0])) {
                        curImage = startImg;
                        displayText = "Start";
                    }
                    // Finish
                    else if(curNode.equals(path[path.length-1])) {
                        curImage = finishImg;
                        displayText = "Goal";
                    }
                    // Path
                    else if(Arrays.asList(path).contains(curNode)) {
                        curImage = pathImg;
                        displayText = tileStr;
                    }
                    // Expanded
                    else if(expanded.contains(curNode)) {
                        curImage = expandedImg;
                        displayText = tileStr;
                    }
                    else if(b.isWall(curNode)) {
                        curImage = wallImg;
                    }
                    else {
                        curImage = tileImg;
                        displayText = tileStr;
                    }


                    g.drawImage(curImage, x, y, tileSize, tileSize, null);
                    
                    if(displayText != null) {
                        g.drawString(displayText, x + (tileSize/2), y + (tileSize/2));
                    }
                    


                }
            }


            System.out.println(getWidth() + "," + getHeight());

        }
    }



}