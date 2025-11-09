package gui;

import main.Server;
import main.Roulette;
import utilz.LoadHandler;
import utilz.Taskbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CreateLobby extends JFrame {
    private int animation = 0;
    private int currentButton = 2;
    private ArrayList<JLabel> playerNames = new ArrayList<>();
    private JLabel amount;
    private JButton two;
    private JButton three;
    private JButton four;
    private JLabel connected;
    private JLabel searching;
    private JButton start;

    public CreateLobby() {
        createUI();
        createInteract();
        createSearch();
        revalidate();
        repaint();
    }

    private void createUI() {
        setUndecorated(true);
        setName("Lobby");
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((int) (d.getWidth() - 300) / 2, (int) (d.getHeight() - 300) / 2, 300, 300);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        ImageIcon imageIcon = new ImageIcon("res/img.png");
        this.setIconImage(imageIcon.getImage());
        add(new Taskbar(this,300, "Creating Lobby"));
    }

    private void createInteract() {
        JLabel lobbyName = new JLabel(Roulette.name + "´s lobby");
        LoadHandler.label(lobbyName);
        lobbyName.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 18));
        int size = lobbyName.getFontMetrics(lobbyName.getFont()).stringWidth(lobbyName.getText());
        lobbyName.setBounds((300 - size) / 2, 55, size, 24);

        amount = new JLabel("Player amount");
        LoadHandler.label(amount);
        amount.setBounds(100,115, 144, 24);


        two = new JButton("2");
        LoadHandler.createButton(two);
        two.setIcon(new ImageIcon(LoadHandler.imageLoad(LoadHandler.buttonSmallFocused)));
        two.setBounds(44, 144, 64, 32);
        two.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setButton(2);
            }
        });

        three = new JButton("3");
        LoadHandler.createButton(three);
        three.setIcon(new ImageIcon(LoadHandler.imageLoad(LoadHandler.buttonSmall)));
        three.setBounds(118, 144, 64, 32);
        three.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setButton(3);
            }
        });

        four = new JButton("4");
        LoadHandler.createButton(four);
        four.setIcon(new ImageIcon(LoadHandler.imageLoad(LoadHandler.buttonSmall)));
        four.setBounds(192, 144, 64, 32);
        four.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setButton(4);
            }
        });

        JButton play = new JButton("Play");
        LoadHandler.createButton(play);
        play.setLocation(78, 216);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                amount.setVisible(false);
                two.setVisible(false);
                three.setVisible(false);
                four.setVisible(false);
                play.setVisible(false);
                connected.setVisible(true);
                searching.setVisible(true);
                addPlayer(Roulette.name);
                repaint();
                try {
                    Roulette.server = new Server(currentButton);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        add(lobbyName);
        add(amount);
        add(two);
        add(three);
        add(four);
        add(play);
    }

    private void createSearch(){
        connected = new JLabel();
        LoadHandler.label(connected);
        connected.setBounds(55, 85, 200, 24);
        connected.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 18));
        connected.setVisible(false);

        searching = new JLabel("Searching");
        LoadHandler.label(searching);
        searching.setBounds(115, 225, 144, 24);
        searching.setVisible(false);

        start = new JButton("Start");
        LoadHandler.createButton(start);
        start.setLocation(78,256);
        start.setVisible(false);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Roulette.server.startGame();
            }
        });

        add(connected);
        add(searching);
        add(start);
    }

    private void setButton(int button) {
        if (currentButton != button) {
            switch (currentButton) {
                case 2:
                    two.setIcon(new ImageIcon(LoadHandler.imageLoad(LoadHandler.buttonSmall)));
                    break;
                case 3:
                    three.setIcon(new ImageIcon(LoadHandler.imageLoad(LoadHandler.buttonSmall)));
                    break;
                case 4:
                    four.setIcon(new ImageIcon(LoadHandler.imageLoad(LoadHandler.buttonSmall)));
                    break;
            }
            switch (button) {
                case 2:
                    two.setIcon(new ImageIcon(LoadHandler.imageLoad(LoadHandler.buttonSmallFocused)));
                    break;
                case 3:
                    three.setIcon(new ImageIcon(LoadHandler.imageLoad(LoadHandler.buttonSmallFocused)));
                    break;
                case 4:
                    four.setIcon(new ImageIcon(LoadHandler.imageLoad(LoadHandler.buttonSmallFocused)));
                    break;
            }
            currentButton = button;
        }
    }

    public void addPlayer(String name){
        int number = playerNames.size() + 1;
        if (number > 1){
            start.setVisible(true);
        }
        connected.setText("Connected Players: " + number + " / " + currentButton);
        JLabel player = new JLabel(number + ". " +name);
        LoadHandler.label(player);
        int size = player.getFontMetrics(player.getFont()).stringWidth(player.getText());
        player.setBounds((300 - size) / 2, 85 + (30 * number), size, 24);
        add(player);
        playerNames.add(player);
        repaint();
    }

    public void removePlayer(String name){
        int number = playerNames.size() - 1;
        if (number < 2){
            start.setVisible(false);
        }
        connected.setText("Connected Players: " + number + " / " + currentButton);
        System.out.println(name);
        for (int i = 0; i < playerNames.size(); i++) {
            String currentName = (playerNames.get(i).getText()).substring(3);
            if (currentName.equals(name)) {
                remove(playerNames.get(i));
                playerNames.remove(i);
                break;
            }
        }
        for (int i = 0; i < playerNames.size(); i++) {
            JLabel player = playerNames.get(i);
            int size = player.getFontMetrics(player.getFont()).stringWidth(player.getText());
            player.setBounds((300 - size) / 2, 115 + (30 * i), size, 24);
        }
        revalidate();
        repaint();
    }

    public boolean updatePlayer(){
        if (playerNames.size() == currentButton){
            searching.setVisible(false);
            return true;
        }
        animation++;
        if (animation > 3){
            animation = 0;
        }
        switch (animation) {
            case 0:
                searching.setText("Searching");
                searching.setLocation(115, 225);
                break;
            case 1:
                searching.setText("Searching.");
                searching.setLocation(113, 225);
                break;
            case 2:
                searching.setText("Searching..");
                searching.setLocation(111, 225);
                break;
            case 3:
                searching.setText("Searching...");
                searching.setLocation(109, 225);
                break;
        }
        repaint();
        return false;
    }

    public void start(){
        Roulette.rouletteGame = new RouletteGame();
        for (JLabel label : playerNames) {
            Roulette.rouletteGame.addPlayer(label.getText().substring(3));
        }
        Roulette.rouletteGame.createSelect();
        Roulette.removeLobby();
    }
}
