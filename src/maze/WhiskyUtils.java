package maze;

public class WhiskyUtils {
    /**
     * Method to determine if a given string is a numeric
     * This method will return false for a negative numeric
     * @param s is the given string
     * @return if the string is a numeric
     */
    public static boolean isNumeric(String s) {
        return s.matches("[0-9]+");
    }

    /**
     * Method to generate a maze string of given size without walls, printing results
     * @param rows is the given number of rows
     * @param cols is the given number of columns
     */
    public static void generateMazeFile(int rows, int cols) {
        System.out.println(rows + "," + cols);

        for(int i = 0; i < rows; i++) {
            StringBuilder sb = new StringBuilder();
            for(int j = 0; j < cols; j++) {
                sb.append("1");
                if(j < cols-1) {
                    sb.append(",");
                }
            }

            System.out.println(sb);
        }
    }
}
