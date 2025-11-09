package utilz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Taskbar extends JPanel {
    private JFrame frame;
    private JButton closeWindow;
    private JButton closeProgram;
    private JLabel text;
    private int positionX = 0, positionY = 0;
    private boolean inside = false;


    public Taskbar (JFrame frame, int width, String text){
        this.frame = frame;
        instantiate(width);
        this.text.setText(text);
    }


    private void instantiate (int width){
        Dimension dimension = new Dimension(width, 25);
        setLocation(0,0);
        setPreferredSize(dimension);
        setSize(dimension);
        setLayout(null);
        setFocusable(false);
        setBackground(Color.darkGray);
        createCloseWindow(width);
        createCloseProgram(width);
        createText();
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (inside) {
                    positionX = e.getX();
                    positionY = e.getY();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                frame.setLocation(e.getXOnScreen() - positionX, e.getYOnScreen() - positionY);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                inside = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                inside = false;
            }
        };

        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }

    private void createCloseWindow(int width){
        closeWindow = new JButton();
        closeWindow.setBounds(width - 50, 0, 25, 25);
        closeWindow.setContentAreaFilled(false);
        closeWindow.setBorderPainted(false);
        closeWindow.setFocusPainted(false);
        closeWindow.setBackground(Color.lightGray);
        closeWindow.setFocusable(false);
        closeWindow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                frame.setExtendedState(JFrame.ICONIFIED);
                closeWindow.setContentAreaFilled(false);
            }
        });
        closeWindow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeWindow.setContentAreaFilled(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeWindow.setContentAreaFilled(false);
            }
        });
        Icon icon = new ImageIcon (LoadHandler.imageLoad(LoadHandler.closeWindow));
        closeWindow.setIcon(icon);
        add(closeWindow);
    }

    private void createCloseProgram(int width){
        closeProgram = new JButton();
        closeProgram.setBounds(width - 25, 0, 25, 25);
        closeProgram.setContentAreaFilled(false);
        closeProgram.setBorderPainted(false);
        closeProgram.setFocusPainted(false);
        closeProgram.setBackground(Color.red);
        closeProgram.setFocusable(false);
        closeProgram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                frame.dispose();
            }
        });
        closeProgram.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeProgram.setContentAreaFilled(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeProgram.setContentAreaFilled(false);
            }
        });
        Icon icon = new ImageIcon (LoadHandler.imageLoad(LoadHandler.closeProgramm));
        closeProgram.setIcon(icon);
        add(closeProgram);
    }

    private void createText(){
        text = new JLabel();
        add(text);
        text.setBounds(5, 0, 150, 25);
        text.setForeground(Color.WHITE);
    }
}
