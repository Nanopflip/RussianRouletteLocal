package main;

import utilz.LoadHandler;
import gui.*;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class Client {
    private Thread gameThread;
    private MulticastSocket socket;
    private InetAddress group;
    private int port = 4446;
    private String state = "searching";
    private ArrayList<String> ipHost = new ArrayList<>();
    private ArrayList<String> ipPort = new ArrayList<>();
    private ArrayList<String> hostName = new ArrayList<>();
    private int currentIP = 0;

    public Client() throws Exception {
        socket = new MulticastSocket(port);
        group = InetAddress.getByName("230.0.0.0");
        socket.joinGroup(group);
        gameThread = new Thread(this::run);
        gameThread.start();
    }

    public void run() {
        while(true) {
            switch(state) {
                case "searching":
                   stateSearching();
                    break;
                case "joining":
                    stateJoining();
                    break;
                case "joined":
                    stateJoined();
                    break;
                case "Game":
                    stateGame();
                    break;
            }
        }
    }

    public void stopRunning() {
        gameThread.interrupt();
        gameThread = null;
        try {
            socket.leaveGroup(group);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        socket.close();
    }

    private void stateSearching() {
        byte[] receiveBuffer = new byte[256];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        try {
            socket.receive(receivePacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String hostAdress = receivePacket.getAddress().getHostAddress();
        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
        boolean exist = false;
        if (message.contains("Serv|")) {
            String name = message.substring(message.indexOf("|") + 1);
            for (String hostName : hostName) {
                if (hostName.equals(name)) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                ipHost.add(hostAdress);
                hostName.add(name);
                ipPort.add(String.valueOf(receivePacket.getPort()));
                int index = hostName.size() - 1;
                Roulette.searchGame.addServer(hostName.get(index), index);
            }
        }
    }

    private void stateJoining(){
        try {
            String joinMessage = "Join|" + Roulette.name;
            byte[] sendBuffer = joinMessage.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getByName(ipHost.get(currentIP)), Integer.parseInt(ipPort.get(currentIP)));            socket.send(sendPacket);
            byte[] recieveBuffer = new byte[256];
            DatagramPacket ackknowledgment = new DatagramPacket(recieveBuffer, recieveBuffer.length);
            try {
                socket.setSoTimeout(1000);
                socket.receive(ackknowledgment);
                socket.setSoTimeout(0);
                String message = new String(ackknowledgment.getData(), 0, ackknowledgment.getLength());
                String command = message.split("\\|")[0];
                if (command.equals("ack")) {
                    state = "joined";
                    int number = 1;
                    Roulette.playerAmount = Integer.parseInt(message.substring(message.indexOf("|") + 1, message.indexOf("|") + 2));
                    Roulette.searchGame.joinLobby(currentIP);
                    if (message.length() > 5){
                        String name;
                        message = message.substring(message.indexOf(":") + 1);
                        while(message.contains(":")) {
                            name = message.substring(0, message.indexOf(":"));
                            message = message.substring(message.indexOf(":") + 1);
                            Roulette.searchGame.addPlayer(name);
                            number++;
                        }
                        Roulette.searchGame.addPlayer(message);
                        number++;
                    }
                    Roulette.searchGame.addPlayer(Roulette.name);
                    Roulette.player = number;
                } else if (command.equals("Full")) {
                    ipHost.remove(currentIP);
                    ipPort.remove(currentIP);
                    hostName.remove(currentIP);
                    Roulette.searchGame.remove(currentIP);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void stateJoined(){
        try {
            byte[] recieveBuffer = new byte[256];
            DatagramPacket recievePacket = new DatagramPacket(recieveBuffer, recieveBuffer.length);
            socket.setSoTimeout(0);
            socket.receive(recievePacket);
            String messagePacket = new String(recievePacket.getData(), 0, recievePacket.getLength());
            if (messagePacket.contains("|")) {
                String command = messagePacket.substring(0, messagePacket.indexOf("|"));
                String message = messagePacket.substring(messagePacket.indexOf("|") + 1);
                switch (command) {
                    case "Joined":
                        System.out.println(message);
                        Roulette.searchGame.addPlayer(message);
                        break;
                    case "Leaved":
                        Roulette.searchGame.removePlayer(message);
                        break;
                    case "Start":
                        Roulette.searchGame.start();
                        Roulette.getItem();
                        state = "Game";
                        break;

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void stateGame(){
        try {
            byte[] recieveBuffer = new byte[256];
            DatagramPacket recievePacket = new DatagramPacket(recieveBuffer, recieveBuffer.length);
            socket.setSoTimeout(0);
            socket.receive(recievePacket);
            String messagePacket = new String(recievePacket.getData(), 0, recievePacket.getLength());
            if (messagePacket.contains("|")) {
                String command = messagePacket.substring(0, messagePacket.indexOf("|"));
                String message = messagePacket.substring(messagePacket.indexOf("|") + 1);
                switch (command) {
                    case "Selec":
                        Roulette.currentPlayer = Integer.parseInt(message);
                        if (Roulette.currentPlayer == Roulette.player) {
                            Roulette.rouletteGame.enableShoot();
                        }
                        Roulette.rouletteGame.repaint();
                        break;
                    case "Died":
                        Player p = Roulette.players.get(Roulette.currentPlayer);
                        p.dead();
                        Roulette.rouletteGame.addLog("Game", p.name + " got shot");
                        LoadHandler.loadSound(LoadHandler.soundGun, 1f);
                        if (Roulette.bullets >= 2){
                            Roulette.bullets--;
                        }
                        break;
                    case "Survived":
                        Player pTwo = Roulette.players.get(Roulette.currentPlayer);
                        Roulette.rouletteGame.addLog("Game", pTwo.name + " survived");
                        LoadHandler.loadSound(LoadHandler.soundGunClick, 1f);
                        break;
                    case "Send":
                        String messageText = message.substring(message.indexOf(":") + 1);
                        int messagePlayer = Integer.parseInt(messagePacket.substring(messagePacket.indexOf("|") + 1, messagePacket.indexOf(":")));
                        Roulette.rouletteGame.addLog(Roulette.players.get(messagePlayer).name, messageText);
                        break;
                    case "Win":
                        int player = Integer.parseInt(message);
                        Roulette.rouletteGame.addLog("Game", Roulette.players.get(player).name + " won");
                        break;
                    case "Emote":
                        int emotePlayer = Integer.parseInt(message.substring(0, message.indexOf(":")));
                        int emote = Integer.parseInt(message.substring(message.indexOf(":") + 1));
                        Roulette.rouletteGame.playEmote(emote, emotePlayer);
                        break;
                    case "Item":
                        int itemPlayer = Integer.parseInt(message.substring(0, message.indexOf(":")));
                        int item;
                        if (message.contains("|")) {
                            item = Integer.parseInt(message.substring(message.indexOf(":") + 1, message.indexOf("|")));
                        } else{
                            item = Integer.parseInt(message.substring(message.indexOf(":") + 1));
                        }
                        switch (item) {
                            case 0:
                                Roulette.rouletteGame.addLog("Item", Roulette.players.get(itemPlayer).name + " added some extra bullets");
                                Roulette.bullets = 3;
                                break;
                                case 1:
                                    Roulette.rouletteGame.addLog("Item", Roulette.players.get(itemPlayer).name + " skipped his turn");
                                    break;
                                    case 2:
                                        int number = Integer.parseInt(message.substring(message.indexOf("|") + 1));
                                        Roulette.rouletteGame.addLog("Item", Roulette.players.get(itemPlayer).name + " is stealing from" + Roulette.players.get(number).name);
                                        if (number == Roulette.player) {
                                            Roulette.steal();
                                        }
                                        break;
                            case 3:
                                int wheelPlayer = Integer.parseInt(message.substring(0, message.indexOf(":")));
                                String wheelMessage = message.substring(message.indexOf("|") + 1);
                                Roulette.rouletteGame.addLog("Item", Roulette.players.get(wheelPlayer) + "´s " + wheelMessage);
                                break;
                        }
                        break;
                    case "Round":
                        Roulette.getItem();
                        Roulette.rouletteGame.addLog("Game", "New round, new items");
                        break;
                    case "Steal":
                        int itemStolen = Integer.parseInt(message.substring(message.indexOf(":") + 1));
                        if (itemStolen != -1) {
                            Roulette.rouletteGame.addLog("Item", "a " + Roulette.itemNames[itemStolen] + " was stolen");
                            if (Roulette.stealing){
                                Roulette.stealing = false;
                                Roulette.rouletteGame.getItemButton().addItem(itemStolen);
                            }
                        } else {
                            Roulette.rouletteGame.addLog("Item", "nothing was stolen");
                        }
                        break;
                    case "Reset":
                        Roulette.reset();
                        break;
                }
                Roulette.rouletteGame.repaint();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void leave() {
        byte[] sendBuffer = ("Leave|" + Roulette.name).getBytes();
        try {
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getByName(ipHost.get(currentIP)), Integer.parseInt(ipPort.get(currentIP)));
            socket.send(sendPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinGame(int number){
        currentIP = number;
        state = "joining";
    }

    public void sendMessage(String message){
        try {
            byte[] sendBuffer = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getByName(ipHost.get(currentIP)), Integer.parseInt(ipPort.get(currentIP)));
            socket.send(sendPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
