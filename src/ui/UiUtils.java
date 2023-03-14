package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class UiUtils {
    /**
     * Method to get the default Title Font
     * @return Title Font
     */
    public static Font getTitleFont() {
        return new Font("Segoe UI", Font.BOLD, 20);
    }

    /**
     * Method to get the default Normal Font
     * @return Normal Font
     */
    public static Font getNormalFont() {
        return new Font("Segoe UI", Font.PLAIN, 14);
    }

    /**
     * Method to get a formatted black border
     * @return formated black line border
     */
    public static LineBorder getLineBorder() {
        return new LineBorder(Color.BLACK, 1);
    }


    public static EmptyBorder getPaddedBorder(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }

    public static EmptyBorder getPaddedBorder(int leftRight, int topBottom) {
        return new EmptyBorder(topBottom, leftRight, topBottom, leftRight);
    }

    public static javax.swing.border.CompoundBorder getPaddedLineBorder(int leftRight, int topBottom) {
        return BorderFactory.createCompoundBorder(getPaddedBorder(leftRight, topBottom),  getLineBorder());
    }

}
