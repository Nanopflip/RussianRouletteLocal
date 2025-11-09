package main;

import utilz.LoadHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player {
    public String name;
    public int ipPort;
    public String ipAddress;
    public boolean alive = true;
    public int number = 0;
    public BufferedImage playerModel;

    public Player(String name, int ipPort, String ipAddress) {
        this.name = name;
        this.ipPort = ipPort;
        this.ipAddress = ipAddress;
    }

    public Player(String name, int number){
        this.name = name;
        this.number = number;
        playerModel = LoadHandler.imageLoad(LoadHandler.player + number + ".png");
    }

    public void draw(Graphics g){
        g.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        int size = g.getFontMetrics(g.getFont()).stringWidth(name);
        switch(number){
            case 0:
                g.setColor(new Color(0, 255, 159));
                g.drawString(name, 300 - (size / 2), 444);
                g.drawImage(playerModel, 270, 454, 60, 64, null);
                break;
            case 1:
                g.setColor(new Color(228, 255, 0));
                g.drawString(name, 122 - (size / 2), 376);
                g.drawImage(playerModel, 112, 386, 60, 64, null);
                break;
            case 2:
                g.setColor(new Color(0, 217, 255));
                g.drawString(name, 300 - (size / 2), 298);
                g.drawImage(playerModel, 270, 308, 60, 64, null);
                break;
            case 3:
                g.setColor(new Color(255, 0, 242));
                g.drawString(name, 478 - (size / 2), 376);
                g.drawImage(playerModel, 428, 386, 60, 64, null);
                break;
        }
    }

    public void dead(){
        alive = false;
        playerModel = LoadHandler.imageLoad(LoadHandler.ghost + number + ".png");
    }

    public void alive(){
        alive = true;
        playerModel = LoadHandler.imageLoad(LoadHandler.player + number + ".png");
    }
}
