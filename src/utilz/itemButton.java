package utilz;

import main.Roulette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class itemButton extends JButton {
    private int item = -1;

    public itemButton() {
        super();
        LoadHandler.createButton(this);
        setSize(60,60);
        setIcon(new ImageIcon(LoadHandler.imageLoad(LoadHandler.itemBackground).getScaledInstance(60,60, Image.SCALE_DEFAULT)));
        setVisible(false);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (item != -1) {
                    if (Roulette.currentPlayer == Roulette.player) {
                        switch (item) {
                            case 0:
                                if (Roulette.server != null) {
                                    Roulette.bullets = 3;
                                }
                                Roulette.rouletteGame.addLog("Item", Roulette.players.get(Roulette.player).name + " added some extra bullets");
                                Roulette.sendMessage("Item|" + Roulette.currentPlayer + ":" + item);
                                break;
                                case 1:
                                    Roulette.rouletteGame.disableShoot();
                                    Roulette.rouletteGame.addLog("Item", Roulette.players.get(Roulette.player).name + " skipped his turn");
                                    Roulette.sendMessage("Item|" + Roulette.currentPlayer + ":" + item);
                                    if (Roulette.server != null) {
                                        try {
                                            Thread.sleep(20);
                                        } catch (InterruptedException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                        Roulette.selectNextPlayer();
                                    }
                                    break;
                            case 2:
                                Roulette.rouletteGame.disableShoot();
                                Roulette.stealing = true;
                                Roulette.rouletteGame.robbing();
                                break;
                                case 3:
                                    Roulette.rouletteGame.disableShoot();
                                    Roulette.rouletteGame.addWheel(new WheelOfFortune());
                                    break;
                        }
                        item = -1;
                        setIcon(new ImageIcon(LoadHandler.imageLoad(LoadHandler.itemBackground).getScaledInstance(60, 60, Image.SCALE_DEFAULT)));
                    }
                }
            }
        });
    }

    public void addItem(int number){
        item = number;
        BufferedImage img = null;
       Image back = LoadHandler.imageLoad(LoadHandler.itemBackground).getScaledInstance(60,60, Image.SCALE_DEFAULT);
        switch (item){
            case 0:
                img = LoadHandler.imageLoad(LoadHandler.itemAmmunition);
                break;
            case 1:
                img = LoadHandler.imageLoad(LoadHandler.itemFreeBullet);
                break;
            case 2:
                img = LoadHandler.imageLoad(LoadHandler.itemStealinKit);
                break;
            case 3:
                img = LoadHandler.imageLoad(LoadHandler.itemWheelOfFortune);
                break;
        }
        BufferedImage icon = new BufferedImage(60, 60, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = icon.getGraphics();
        g.drawImage(back, 0, 0,60, 60, null);
        g.drawImage(img, 6, 6,48,48, null);
        g.dispose();
        setIcon(new ImageIcon(icon));
    }

    public void removeItem(){
        item = -1;
        setIcon(new ImageIcon(LoadHandler.imageLoad(LoadHandler.itemBackground).getScaledInstance(60, 60, Image.SCALE_DEFAULT)));
    }

    public int getItem(){
        return item;
    }
}
