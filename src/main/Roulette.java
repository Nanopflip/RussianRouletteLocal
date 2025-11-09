package main;

import gui.CreateLobby;
import gui.RouletteGame;
import gui.SearchGame;
import gui.StartGame;
import utilz.itemButton;

import java.util.ArrayList;
import java.util.Random;

public class Roulette {
    public static StartGame startGame;
    public static CreateLobby createLobby;
    public static Server server;
    public static SearchGame searchGame;
    public static Client client;
    public static RouletteGame rouletteGame;
    public static String name = "Standard";
    public static int playerAmount = 0;
    public static int currentPlayer = 0;
    public static int player = 0;
    public static int bullets = 1;
    public static ArrayList<Player> players;
    public static boolean stealing = false;
    public static String[] itemNames = {"Ammunition", "Free Bullet", "Stealing-Kit", "Wheel of fortune"};
    public static int roundPlayer = 0;

    public static void removeStartGame() {
        startGame.dispose();
        startGame = null;
    }

    public static void removeServer() {
        createLobby.dispose();
        server.stopRunning();
        server = null;
        createLobby = null;
    }

    public static void removeClient() {
        searchGame.dispose();
        client.stopRunning();
        client = null;
        searchGame = null;
    }

    public static void removeLobby() {
        createLobby.dispose();
        createLobby = null;
    }

    public static void removeSearchGame() {
        searchGame.dispose();
        searchGame = null;
    }

    public static boolean shoot(){
        Random random = new Random();
        int shot = random.nextInt(7);
        if (client != null) {
            if (shot <= bullets) {
                rouletteGame.addLog("Game", name + " got shot");
                players.get(player).dead();
                sendMessage("Died|");
                if (bullets >= 2) {
                    bullets--;
                }
                return false;
            }
            rouletteGame.addLog("Game", name + " survived");
            sendMessage("Survived|");
            return true;
        } else {
            if (shot <= bullets){
                rouletteGame.addLog("Game", name + " got shot");
                sendMessage("Died|");
                players.get(0).dead();
                selectNextPlayer();
                if (bullets >= 2) {
                    bullets--;
                }
                return false;
            }
            rouletteGame.addLog("Game", name + " survived");
            sendMessage("Survived|");
            selectNextPlayer();
            return true;
        }

    }

    public static void selectNextPlayer() {
        try {
            Thread.sleep(200);
            int number = server.selectPlayer();
            if (number != -1){
                currentPlayer = number;
                if (playersAlive() != 1) {
                    server.sendMessageAll("Selec|" + number);
                    if (currentPlayer == 0) {
                        rouletteGame.enableShoot();
                    }
                    if (currentPlayer == roundPlayer) {
                        server.sendMessageAll("Round|");
                        getItem();
                        rouletteGame.addLog("Game", "New round, new items");
                    }
                } else {
                    server.sendMessageAll("Win|" + number);
                    Roulette.rouletteGame.addLog("Game", players.get(number).name + " won");
                    Roulette.rouletteGame.enableReset();
                    Roulette.rouletteGame.repaint();
                }
            }
            rouletteGame.repaint();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static int playersAlive(){
        int alive = 0;
        for (int i = 0; i < players.size(); i++){
            if (players.get(i).alive){
                alive++;
            }
        }
        return alive;
    }

    public static void reset(){
        for (Player p : players){
            p.alive();
        }
        ArrayList<itemButton> button = rouletteGame.itemButtons();
        for (itemButton i : button){
            button.remove(i);
        }
        roundPlayer = currentPlayer;
        rouletteGame.addLog("Game", "New game has begun");
        bullets = 1;
        rouletteGame.repaint();
    }

    public static void sendMessage(String message){
        if (client != null){
            client.sendMessage(message);
        } else {
            server.sendMessageAll(message);
        }
    }

    public static void steal(){
        ArrayList<itemButton> item = rouletteGame.itemButtons();
        if (item.size() > 0){
        int randomIndex = new Random().nextInt(item.size());
        sendMessage("Steal|" + player + ":" + item.get(randomIndex).getItem());
        rouletteGame.addLog("Item", "a " + itemNames[item.get(randomIndex).getItem()] + " was stolen");
        item.get(randomIndex).removeItem();
        } else {
            sendMessage("Steal|" + player + ":" + -1);
            rouletteGame.addLog("Item", "nothing was stolen");
        }
    }

    public static void getItem(){
        int randomIndex = new Random().nextInt(3);
        itemButton button = rouletteGame.getItemButton();
        if (button != null){
           button.addItem(randomIndex);
        }
    }
}
