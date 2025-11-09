package gui;

import main.Client;
import main.Roulette;
import utilz.LoadHandler;
import utilz.Taskbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SearchGame extends JFrame {
    private ArrayList<JButton> joinList = new ArrayList<>();
    private ArrayList<JLabel> playerNames = new ArrayList<>();
    private JLabel name;
    private JButton cancel;
    private JLabel connected;

    public static void main(String[] args) {
        Roulette.searchGame = new SearchGame();
    }
    
    public SearchGame() {
        createUI();
        createInteract();
        revalidate();
        repaint();
        try {
            Roulette.client = new Client();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        ImageIcon imageIcon = new ImageIcon("/res/img.png");
        this.setIconImage(imageIcon.getImage());
        add(new Taskbar(this,300, "Searching Game"));
    }

    private void createInteract() {
        name = new JLabel(Roulette.name);
        LoadHandler.label(name);
        name.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 18));
        int size = name.getFontMetrics(name.getFont()).stringWidth(name.getText());
        name.setBounds((300 - size) / 2, 55, size, 24);

        cancel = new JButton("Cancel");
        LoadHandler.createButton(cancel);
        cancel.setLocation(78, 216);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Roulette.startGame = new StartGame();
                Roulette.removeClient();
            }
        });
        add(name);
        add(cancel);
    }

    public void addServer(String name, int number){
        JButton jButton = new JButton(name);
        LoadHandler.createButton(jButton);
        jButton.setLocation(78, 90 + (30 * joinList.size()));
        jButton.setIcon(new ImageIcon(LoadHandler.imageLoad(LoadHandler.buttonFrame)));
        jButton.setForeground(Color.BLACK);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Roulette.client.joinGame(number);
            }
        });
        joinList.add(jButton);
        add(jButton);
        repaint();
    }

    public void joinLobby(int number){
        for (JButton button : joinList) {
            button.setVisible(false);
        }
        name.setVisible(false);
        cancel.setVisible(false);

        String hostName = joinList.get(number).getText();
        JLabel lobbyName = new JLabel(hostName + "´s lobby");
        LoadHandler.label(lobbyName);
        lobbyName.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 18));
        int size = lobbyName.getFontMetrics(lobbyName.getFont()).stringWidth(lobbyName.getText());
        lobbyName.setBounds((300 - size) / 2, 55, size, 24);

        connected = new JLabel();
        LoadHandler.label(connected);
        connected.setBounds(55, 85, 200, 24);
        connected.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 18));

        JButton leave = new JButton("Leave");
        LoadHandler.createButton(leave);
        leave.setLocation(78, 236);
        leave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Roulette.startGame = new StartGame();
                Roulette.client.leave();
                Roulette.removeClient();
            }
        });

        addPlayer(hostName);

        add(lobbyName);
        add(connected);
        add(leave);
    }

    public void removeLobby(int number){
        remove(joinList.get(number));
    }

    public void addPlayer(String name){
        int number = playerNames.size() + 1;
        connected.setText("Connected Players: " + number + " / " + Roulette.playerAmount);
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
        connected.setText("Connected Players: " + number + " / " + Roulette.playerAmount);
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

    public void start(){
        Roulette.rouletteGame = new RouletteGame();
        for (JLabel label : playerNames) {
            Roulette.rouletteGame.addPlayer(label.getText().substring(3));
        }
        Roulette.rouletteGame.createSelect();
        Roulette.removeSearchGame();
    }
}
