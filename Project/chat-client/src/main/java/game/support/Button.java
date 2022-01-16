package game.support;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class Button extends JButton {
    private int i;
    private int j;
    
    public Button(Color color) {
        setBackground(Color.white);
        setBorder(new LineBorder(color));
        setPreferredSize(new Dimension(38, 38));
    }
    
    public Button(int i, int j, Color color) {
        this(color);
        this.i = i;
        this.j = j; 
    }
    
    public int getI() {
        return i;
    }
    
    public int getJ() {
        return j;
    }
}