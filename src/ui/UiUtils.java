package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class UiUtils {
    private static final String FONT_NAME = "Segoe UI";

    /**
     * Method to get the default Title Font
     * @return Title Font
     */
    public static Font getTitleFont() {
        return new Font(FONT_NAME, Font.BOLD, 20);
    }

    /**
     * Method to get the default Normal Font
     * @return Normal Font
     */
    public static Font getNormalFont() {
        return new Font(FONT_NAME, Font.PLAIN, 14);
    }

    /**
     * Method to get a variable font size to fit a given tileSize
     * @param g2 is the graphics2D Object
     * @param tileSize is the given tileSize
     * @param str is the given String to fit
     * @return Font that fits given tileSize
     */
    public static Font getVariableFont(Graphics2D g2, int tileSize, String str) {

        Font font = getNormalFont(); // Get Default Font
        float fontSize = font.getSize();
        float fontWidth = g2.getFontMetrics(font).stringWidth(str);
        // Minimum Size
        if(fontWidth <= tileSize)
            return font;
        // Get new fontsize
        fontSize = ((float)tileSize / fontWidth) * fontSize;

        // Return new Font
        return new Font(FONT_NAME, Font.PLAIN, (int)fontSize);
    }


    /**
     * Method to get a formatted black border
     * @return formatted black line border
     */
    public static LineBorder getLineBorder() {
        return new LineBorder(MazeWindow.COLOR_TILE_BOX, 2, true);
    }

    /**
     * Method to get a padded border with given padding (top, left, bottom, right)
     * @param top is top padding
     * @param left is left padding
     * @param bottom is bottom padding
     * @param right is right padding
     * @return Padded Border with given padding
     */
    public static EmptyBorder getPaddedBorder(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }

    /**
     * Method to get a padded border with given padding (leftRight, topBottom)
     * @param leftRight is given leftRight padding
     * @param topBottom is given topBottom padding
     * @return Padded Border with given padding
     */
    public static EmptyBorder getPaddedBorder(int leftRight, int topBottom) {
        return new EmptyBorder(topBottom, leftRight, topBottom, leftRight);
    }

    /**
     * Method to get a padded line-border with given padding (leftRight, topBottom)
     * @param leftRight is the given leftRight padding
     * @param topBottom is the given topBottom padding
     * @return Padded Line-Border with given padding
     */
    public static javax.swing.border.CompoundBorder getPaddedLineBorder(int leftRight, int topBottom) {
        return BorderFactory.createCompoundBorder(getPaddedBorder(leftRight, topBottom),  getLineBorder());
    }

}
