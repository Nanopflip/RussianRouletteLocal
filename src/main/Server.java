package main;

import gui.CreateLobby;
import utilz.LoadHandler;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

public class Server {
    private Thread gameThread;
    private CreateLobby createLobby;
    private DatagramSocket socket;
    private InetAddress group;
    private int port = 4446;
    private int maxPlayers;
    private String state = "creating";
    private ArrayList<Player> players = new ArrayList<>();

    public Server(int maxPlayers) throws Exception {
        this.maxPlayers = maxPlayers;
        socket = new DatagramSocket();
        group = InetAddress.getByName("230.0.0.0");
        this.createLobby = Roulette.createLobby;
        gameThread = new Thread(this::run);
        gameThread.start();
    }

    public void run() {
        while (true) {
            switch (state) {
                case "creating":
                    stateCreating();
                    break;
                case "starting":
                    break;
                case "Game":
                    stateGame();
                    break;

            }
        }
    }

    public void stopRunning() {
        gameThread.interrupt();
        socket.close();
    }

    public void startGame() {
        try {
            byte[] joinMessageBuffer = ("Start|").getBytes();
            for (Player p : players) {
                DatagramPacket joinPacket = new DatagramPacket(joinMessageBuffer, joinMessageBuffer.length, InetAddress.getByName(p.ipAddress), p.ipPort);
                socket.send(joinPacket);
            }
            state = "Game";
            Thread.sleep(200);
            Random random = new Random();
            int player = random.nextInt(players.size() + 1);
            byte[] selecMessageBuffer = ("Selec|" + player).getBytes();
            for (Player p : players) {
                DatagramPacket selecPacket = new DatagramPacket(selecMessageBuffer, selecMessageBuffer.length, InetAddress.getByName(p.ipAddress), p.ipPort);
                socket.send(selecPacket);
            }
            Roulette.currentPlayer = player;
            Roulette.roundPlayer = player;
            Roulette.createLobby.start();
            if (player == 0){
                Roulette.rouletteGame.enableShoot();
            }
            Roulette.getItem();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void stateCreating() {
        if (createLobby.updatePlayer()) {
            state = "starting";
        }
        try {
            String message = "Serv|" + Roulette.name;
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] receiveBuffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        try {
            socket.setSoTimeout(500);
            socket.receive(receivePacket);
            String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            String command = receivedMessage.substring(0, receivedMessage.indexOf("|"));
            String name = receivedMessage.substring(receivedMessage.indexOf("|") + 1);
            switch (command) {
                case "Join":
                    if (players.size() < maxPlayers){
                        Player player = new Player(name, receivePacket.getPort(), receivePacket.getAddress().getHostAddress());
                        Roulette.createLobby.addPlayer(receivedMessage.substring(receivedMessage.indexOf("|") + 1));
                        String ack = ("ack|" + maxPlayers);
                        for (Player players : players) {
                            ack = ack + ":" + players.name;
                        }
                        byte[] ackBuffer = ack.getBytes();
                        DatagramPacket ackPacket = new DatagramPacket(ackBuffer, ackBuffer.length, receivePacket.getAddress(), receivePacket.getPort());
                        socket.send(ackPacket);
                        byte[] joinMessageBuffer = ("Joined|" + name).getBytes();
                        for (Player p : players) {
                            DatagramPacket joinPacket = new DatagramPacket(joinMessageBuffer, joinMessageBuffer.length, InetAddress.getByName(p.ipAddress), p.ipPort);
                            socket.send(joinPacket);
                        }
                        players.add(player);
                        break;
                    } else {
                        Roulette.createLobby.addPlayer(receivedMessage.substring(receivedMessage.indexOf("|") + 1));
                        String full = ("Full|");
                        byte[] fullBuffer = full.getBytes();
                        DatagramPacket fullPacket = new DatagramPacket(fullBuffer, fullBuffer.length, receivePacket.getAddress(), receivePacket.getPort());
                        socket.send(fullPacket);
                    }
                case "Leave":
                    for (Player p : players) {
                        if (p.name.equals(name)) {
                            players.remove(p);
                            break;
                        }
                    }
                    Roulette.playerAmount--;
                    Roulette.createLobby.removePlayer(name);
                    byte[] leaveMessageBuffer = ("Leaved|" + name).getBytes();
                    for (Player p : players) {
                        DatagramPacket joinPacket = new DatagramPacket(leaveMessageBuffer, leaveMessageBuffer.length, InetAddress.getByName(p.ipAddress), p.ipPort);
                        socket.send(joinPacket);
                    }
                    break;
            }
        } catch (SocketTimeoutException e) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void stateGame() {
        byte[] receiveBuffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        try {
            socket.setSoTimeout(0);
            socket.receive(receivePacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
        if (receivedMessage.contains("|")) {
            String command = receivedMessage.substring(0, receivedMessage.indexOf("|"));
            switch (command) {
                case "Died":
                    if (Roulette.currentPlayer == 0) {
                        sendMessageAll("Died|" + Roulette.currentPlayer);
                    } else {
                        for (Player p : players) {
                            if (players.get(Roulette.currentPlayer - 1) != p) {
                                sendMessage("Died|" + Roulette.currentPlayer, p);
                            }
                        }
                    }
                    Roulette.rouletteGame.addLog("Game", Roulette.players.get(Roulette.currentPlayer).name + " got shot");
                    Roulette.players.get(Roulette.currentPlayer).dead();
                    Roulette.selectNextPlayer();
                    LoadHandler.loadSound(LoadHandler.soundGun, 1f);
                    if (Roulette.bullets >= 2){
                        Roulette.bullets--;
                    }
                    break;
                case "Survived":
                    if (Roulette.currentPlayer == 0) {
                        sendMessageAll("Survived|" + Roulette.currentPlayer);
                    } else {
                        for (Player p : players) {
                            if (players.get(Roulette.currentPlayer - 1) != p) {
                                sendMessage("Survived|" + Roulette.currentPlayer, p);
                            }
                        }
                    }
                    Roulette.rouletteGame.addLog("Game", Roulette.players.get(Roulette.currentPlayer).name + " survived");
                    Roulette.selectNextPlayer();
                    LoadHandler.loadSound(LoadHandler.soundGunClick, 1f);
                    break;
                case "Send":
                    String message = receivedMessage.substring(receivedMessage.indexOf("|") + 1);
                    String messageText = message.substring(message.indexOf(":") + 1);
                    int messagePlayer = Integer.parseInt(message.substring(0, 1));
                    Roulette.rouletteGame.addLog(Roulette.players.get(messagePlayer).name, messageText);
                    for (int i = 0; i < players.size(); i++) {
                        if (i != messagePlayer - 1) {
                            sendMessage("Send|" + messagePlayer + ":" + messageText, players.get(i));
                        }
                    }
                    break;
                case "Emote":
                    String messageEmote = receivedMessage.substring(receivedMessage.indexOf("|") + 1);
                    int emotePlayer = Integer.parseInt(messageEmote.substring(0, messageEmote.indexOf(":")));
                    int emote = Integer.parseInt(messageEmote.substring(messageEmote.indexOf(":") + 1));
                    Roulette.rouletteGame.playEmote(emote, emotePlayer);
                    for (int i = 0; i < players.size(); i++) {
                        if (i != emotePlayer - 1) {
                            sendMessage("Emote|" + emotePlayer + ":" + emote, players.get(i));
                        }
                    }
                    break;
                case "Item":
                    String messageItem = receivedMessage.substring(receivedMessage.indexOf("|") + 1);
                    int item;
                    if (messageItem.contains("|")) {
                        item = Integer.parseInt(messageItem.substring(messageItem.indexOf(":") + 1, messageItem.indexOf("|")));
                    } else {
                        item = Integer.parseInt(messageItem.substring(messageItem.indexOf(":") + 1));
                    }
                    int itemPlayer = Integer.parseInt(messageItem.substring(0, messageItem.indexOf(":")));
                    Roulette.bullets = 3;
                    switch (item) {
                        case 0:
                            Roulette.rouletteGame.addLog("Item", Roulette.players.get(itemPlayer).name + " added some extra bullets");
                            Roulette.bullets = 3;
                            for (int i = 0; i < players.size(); i++) {
                                if (i != itemPlayer - 1) {
                                    sendMessage("Item|" + itemPlayer + ":" + item, players.get(i));
                                }
                            }
                            break;
                        case 1:
                            Roulette.rouletteGame.addLog("Item", Roulette.players.get(itemPlayer).name + " skipped his turn");
                            Roulette.selectNextPlayer();
                            for (int i = 0; i < players.size(); i++) {
                                if (i != itemPlayer - 1) {
                                    sendMessage("Item|" + itemPlayer + ":" + item, players.get(i));
                                }
                            }
                            break;
                            case 2:
                                int number = Integer.parseInt(messageItem.substring(messageItem.indexOf("|") + 1));
                                Roulette.rouletteGame.addLog("Item", Roulette.players.get(itemPlayer).name + " is stealing from" + Roulette.players.get(number).name);
                                for (int i = 0; i < players.size(); i++) {
                                    if (i != itemPlayer - 1) {
                                        sendMessage("Item|" + itemPlayer + ":2:" + number , players.get(i));
                                    }
                                }
                                if (number == Roulette.player) {
                                    Roulette.steal();
                                }
                                break;
                        case 3:
                                String wheelMessage = messageItem.substring(messageItem.indexOf("|") + 1);
                                Roulette.rouletteGame.addLog("Item", wheelMessage);
                            for (int i = 0; i < players.size(); i++) {
                                if (players.get(i).ipAddress == receivePacket.getAddress().getHostAddress()) {
                                    sendMessage(receivedMessage, players.get(i));
                                }
                            }
                            break;


                    }
                    break;
                case "Steal":
                    String messageSteal = receivedMessage.substring(receivedMessage.indexOf("|") + 1);
                    int stealPlayer = Integer.parseInt(messageSteal.substring(0, messageSteal.indexOf(":")));
                    int itemStolen = Integer.parseInt(messageSteal.substring(messageSteal.indexOf(":") + 1));
                    if (itemStolen != -1) {
                        Roulette.rouletteGame.addLog("Item", "a " + Roulette.itemNames[itemStolen] + " was stolen");
                        if (Roulette.stealing){
                            Roulette.stealing = false;
                            Roulette.rouletteGame.getItemButton().addItem(itemStolen);
                        }
                    } else {
                        Roulette.rouletteGame.addLog("Item", "nothing was stolen");
                    }
                    for (int i = 0; i < players.size(); i++) {
                        if (i != stealPlayer - 1) {
                            sendMessage("Steal|:" + itemStolen, players.get(i));
                        }
                    }
                    break;

            }
            Roulette.rouletteGame.repaint();
        }
    }

    public int selectPlayer() {
        int start = Roulette.currentPlayer;
        do {
            start++;
            if (start >= Roulette.players.size()) {
                start = 0;
            } else {
                if (start == Roulette.currentPlayer) {
                    return -1;
                }
            }
        } while (!Roulette.players.get(start).alive);
        Roulette.currentPlayer = start;
        return start;
    }

    public void sendMessage(String message, Player p) {
        byte[] sendBuffer = message.getBytes();
        try {
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getByName(p.ipAddress), p.ipPort);
            socket.send(sendPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageAll(String message) {
        byte[] sendBuffer = message.getBytes();
        for (Player p : players) {
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getByName(p.ipAddress), p.ipPort);
                socket.send(sendPacket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
