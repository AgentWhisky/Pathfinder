public class WhiskyUtils {
    /**
     * Method to determine if a given string is a numeric
     * @param s is the given string
     * @return if the string is a numeric
     */
    public static boolean isNumeric(String s) {
        return s.matches("[0-9]+");
    }
}
