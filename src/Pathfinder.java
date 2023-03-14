import com.formdev.flatlaf.FlatDarculaLaf;
import ui.PathfinderWindow;

public class Pathfinder {
    public static void main(String[] args) {
        runPathfinder();
    }

    /**
     * Method to run the Pathfinder Application
     */
    public static void runPathfinder() {
        FlatDarculaLaf.setup(); // Set FlatDarcula Look and Feel
        new PathfinderWindow(); // Create Window
    }
}
