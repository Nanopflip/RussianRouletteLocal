package utilz;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CustomScrollBarUI extends BasicScrollBarUI {
    // Use your custom images here
    private BufferedImage decreaseButtonImage;
    private BufferedImage increaseButtonImage;

    public CustomScrollBarUI() {
        super();
        decreaseButtonImage = LoadHandler.imageLoad(LoadHandler.buttonUp);
        increaseButtonImage = LoadHandler.imageLoad(LoadHandler.buttonDown);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.drawImage(decreaseButtonImage, 0, 0, getWidth(), getHeight(), null);
                g2.dispose();
            }
        };
        button.setPreferredSize(new Dimension(16, 16)); // Set the size of the button
        button.setBorder(BorderFactory.createEmptyBorder()); // Remove default borders
        return button;
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.drawImage(increaseButtonImage, 0, 0, getWidth(), getHeight(), null);
                g2.dispose();
            }
        };
        button.setPreferredSize(new Dimension(16, 16)); // Set the size of the button
        button.setBorder(BorderFactory.createEmptyBorder()); // Remove default borders
        return button;
    }


    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(70, 0, 255));
        g2.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
        g2.dispose();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(new Color(184, 158, 255));
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }


}
