package utilz;

import main.Roulette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class WheelOfFortune extends JPanel {
    private static final int SIZE = 400;
    private static final int SECTORS = 8;
    private double angle = 0;
    private double speed = 0;
    private Timer timer;
    private Random random = new Random();
    private Color[] colors = {
            new Color(255, 0, 0), // Rot
            new Color(128, 0, 128),  // Lila
            new Color(0, 0, 255),    // Blau
            new Color(255, 255, 255),// Weiß
            new Color(255, 215, 0),  // Gold
            new Color(128, 0, 128),  // Lila
            new Color(0, 0, 255),    // Blau
            new Color(255, 255, 255)// Weiß
    };

    private JLabel spinLabel;

    private static final int[] price = {4, 0, 1, 2, 3, 0, 1, 2};
    private static final String[] itemNames = {"KA", "A random weapon", "Nothing", "GOLDEN GUN", "Death penalty"};

    public WheelOfFortune() {
        setSize(new Dimension(SIZE, SIZE + 100));
        setLocation(200, 200);
        setBackground(new Color(0, 0, 0, 0));
        setOpaque(true);
        setLayout(null);
        JButton spinButton = new JButton("Spin");
        LoadHandler.createButton(spinButton);
        spinButton.setLocation(128, 416);
        spinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSpin();
                spinButton.setVisible(false);
            }
        });
        spinLabel = new JLabel("");
        spinLabel.setForeground(Color.BLACK);
        spinLabel.setBounds(100, 120, 200, 30);
        spinLabel.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 14));
        spinLabel.setVisible(false);


        timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSpin();
            }
        });
        add(spinButton);
        add(spinLabel);
    }

    private void startSpin() {
        if (speed <= 0) {
            speed = random.nextDouble() * 30 + 40; // Random speed
            timer.start();
        }
    }

    private void updateSpin() {
        if (speed > 0) {
            angle += speed;
            speed *= 0.97; // Friction effect
            if (speed < 0.5) {
                speed = 0;
                timer.stop();
                determineResult();
            }
            repaint();
        }
    }

    private void determineResult() {
        int index = (int) ((angle % 360) / (360.0 / SECTORS));
        spinLabel.setText("You have drawn " + price[index]);
        spinLabel.setVisible(true);


        System.out.println(index + "|" + Roulette.player);
        String message = "dices have fallen " + itemNames[price[index]];
        Roulette.rouletteGame.addLog("Item", "The " + message);
        Roulette.sendMessage("Item|" + Roulette.player + ":3|" + Roulette.players.get(Roulette.player).name + "´s " + message);
        JButton spinButton = new JButton("Accept fate");
        LoadHandler.createButton(spinButton);
        spinButton.setLocation(128, 176);
        spinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Roulette.rouletteGame.removeWheel(getWheel());
                switch (index) {
                    case 0:
                        // KA
                        break;
                    case 1:
                        int random = (int)(Math.random() * 2);
                        Roulette.rouletteGame.getItemButton().addItem(random);
                        Roulette.rouletteGame.addLog("Item",Roulette.name + " has drawn a " + Roulette.itemNames[random]);
                        break;
                    case 2:
                        // nothing
                        break;
                    case 3:
                        // Golden weapon
                        break;
                    case 4:
                        //death of player
                        break;
                }
                Roulette.rouletteGame.enableShoot();
            }
        });
        add(spinButton);
    }

    public WheelOfFortune getWheel() {
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int centerX = SIZE / 2, centerY = SIZE / 2;
        int radius = SIZE / 2 - 10;

        for (int i = 0; i < SECTORS; i++) {
            g2d.setColor(colors[i]);
            g2d.fillArc(10, 10, radius * 2, radius * 2, (int) angle + i * (360 / SECTORS), 360 / SECTORS);
            g2d.setColor(Color.BLACK);
        }
        g2d.setColor(Color.BLACK);
        g2d.fillOval(centerX - 10, centerY - 10, 20, 20);
        g2d.fillRect(centerX - 2, 0, 4, 30);
    }
}