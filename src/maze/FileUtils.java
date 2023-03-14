package maze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {
    private static final String MAZE_DIR_PATH = "Resources/mazes/"; // Directory for Maze Files
    private static final String WALL_CHAR = "_"; // Character used for a wall character

    /**
     * Method to import maze files with given filename and output a 2D String Array
     * @param filename is the given filename for a file in the maze file directory
     * @return 2D String Array representation of the maze
     */
    public static String[][] importMazeFile(String filename) {

        String[][] maze;
        try {
            BufferedReader br = new BufferedReader(new FileReader(MAZE_DIR_PATH + filename));
            String line;

            // Create maze.Maze
            line = br.readLine();
            String[] size = line.split(",");
            int height = Integer.parseInt(size[0]);
            int width = Integer.parseInt(size[1]);
            maze = new String[height][width];

            // Read maze.Maze
            for(int i = 0; i < height; i++) {
                line = br.readLine();
                String[] row = line.split(",");

                for(int j = 0; j < width; j++) {
                    String nodeInfo = row[j];

                    // Check for invalid data in imported file
                    if(!WhiskyUtils.isNumeric(nodeInfo) && !nodeInfo.equalsIgnoreCase(WALL_CHAR)) {
                        throw new InvalidMazeException("Error: maze.Maze File Contains Invalid Data");
                    }
                    maze[i][j] = row[j];
                }
            }

            br.close();

            return maze;
        }
        catch (IOException e) {
            System.err.println("Failed to import file with given filename: " + filename);
        }

        return null;
    }

    /**
     * Method to get all filenames from the maze file directory
     * @return all filenames in maze file directory
     */
    public static String[] getAllMazeFiles() {
        File mazeFolder = new File(MAZE_DIR_PATH);

        File[] files = mazeFolder.listFiles();

        if(files != null) {
            String[] filenames = new String[files.length];

            for(int i = 0; i < filenames.length; i++) {
                filenames[i] = files[i].getName(); // Add to Array
            }

            return filenames;
        }
        return new String[]{"No Files Found"};
    }

    /**
     * Invalid Maze File Exception
     */
    public static class InvalidMazeException extends RuntimeException {
        public InvalidMazeException(String msg) {
            super(msg);
        }
    }
}
