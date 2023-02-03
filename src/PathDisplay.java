import javax.swing.*;
import java.awt.*;

public class PathDisplay extends JPanel {
    private JFrame  frame;

    public PathDisplay() {
        setPreferredSize(new Dimension(100, 100));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillOval(25, 25, 50, 50);
    }

    private void showGui() {
        frame = new JFrame("Drawing");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(this, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new PathDisplay().showGui();
    }
}