package gui;

import main.Roulette;
import utilz.LoadHandler;
import utilz.Taskbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class StartGame extends JFrame {
    private JTextArea name;
    private JLabel error;

    public StartGame() {
        createUI();
        createButtonsUI();
        createTextArea();
        revalidate();
        repaint();
    }

    private void createUI() {
        setUndecorated(true);
        setName("Russian roulette");
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((int) (d.getWidth() - 300) / 2, (int) (d.getHeight() - 300) / 2, 300, 300);
        this.setVisible(true);
        this.setResizable(false);
        this.setLayout(null);
        ImageIcon imageIcon = new ImageIcon("res/img.png");
        this.setIconImage(imageIcon.getImage());
        add(new Taskbar(this,300, "Start screen"));
    }

    private void createButtonsUI() {
        JButton createLobby = new JButton("Create Lobby");
        LoadHandler.createButton(createLobby);
        createLobby.setLocation(78, 152);
        createLobby.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(name.getText().equals("")) {
                    error.setVisible(true);
                } else {
                    Roulette.removeStartGame();
                    Roulette.name = name.getText();
                    Roulette.createLobby = new CreateLobby();
                }
            }
        });

        JButton joinLobby = new JButton("Join Lobby");
        LoadHandler.createButton(joinLobby);
        joinLobby.setLocation(78, 216);
        joinLobby.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(name.getText().equals("")) {
                    error.setVisible(true);
                } else {
                    Roulette.name = name.getText();
                    Roulette.searchGame = new SearchGame();
                    Roulette.removeStartGame();
                }
            }
        });
        add(createLobby);
        add(joinLobby);
    }

    private void createTextArea() {
        name = new JTextArea();
        name.setBounds(78, 84, 144, 24);
        name.setBackground(Color.BLACK);
        name.setForeground(Color.WHITE);
        name.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 12));
        name.setBorder(BorderFactory.createLineBorder(new Color(47, 0, 255), 2));
        name.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                int size = name.getFontMetrics(name.getFont()).stringWidth(name.getText());
                if (size > 130){
                    e.consume();
                }
            }
        });

        JLabel nameText = new JLabel("Username");
        nameText.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 13));
        nameText.setBounds(78, 55, 144, 24);
        nameText.setForeground(Color.BLACK);

        error = new JLabel("Please enter a username");
        error.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 13));
        error.setBounds(78, 113, 144, 24);
        error.setForeground(Color.RED);
        error.setVisible(false);

        add(name);
        add(nameText);
        add(error);
    }
}


