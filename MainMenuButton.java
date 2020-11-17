import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainMenuButton extends JButton implements MouseListener {
    //constructor
    MainMenuButton() {
        super();
        super.setBackground();
    }
    MainMenuButton( String text, Color color) {
        super( text);
        super.setBackground(color);
    }

    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.5));
        super.paint(g2);
        g2.dispose();
    }

}