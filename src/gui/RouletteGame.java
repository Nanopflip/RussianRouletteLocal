package gui;

import main.Player;
import main.Roulette;
import utilz.*;
import utilz.Taskbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.time.LocalTime;
import java.util.ArrayList;

public class RouletteGame extends JFrame {
    private ArrayList<JLabel> playerNames = new ArrayList();
    private JPanel screen;
    private JPanel logScreen;
    private JScrollPane log;
    private BufferedImage gun;
    private BufferedImage table;
    private BufferedImage screenTV;
    private BufferedImage screenPlayerList;
    private JButton shoot;
    private JButton reset;
    private JTextArea messanger;
    private JLabel[] emotes = new JLabel[4];
    private JButton heheheha;
    private JButton grrr;
    private JButton groovy;
    private JButton opfer;
    private JButton sad;
    private JButton skull;
    private JButton takeit;
    private JButton blyat;
    private JButton oiia;
    private JButton sexy;
    private JButton score;
    private boolean cooldown = false;
    private Timer timer;
    private double height = 32.0;
    private itemButton[] itemButtons = new itemButton[3];
    private JButton[] selectButtons;

    public static void main(String[] args) {
        new RouletteGame();
    }

    public RouletteGame() {
        gun = LoadHandler.imageLoad(LoadHandler.gun);
        table = LoadHandler.imageLoad(LoadHandler.table);
        screenTV = LoadHandler.imageLoad(LoadHandler.screen);
        screenPlayerList = LoadHandler.imageLoad(LoadHandler.screenPlayer);
        Roulette.players = new ArrayList<>();

        createUI();
        createPlayerList();
        createLog();
        createInteract();
        createItems();

        revalidate();
        repaint();
    }

    private void createUI() {
        setUndecorated(true);
        setName("Game of main.Roulette");
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((int) (d.getWidth() - 800) / 2, (int) (d.getHeight() - 800) / 2, 800, 800);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        ImageIcon imageIcon = new ImageIcon("res/img.png");
        this.setIconImage(imageIcon.getImage());
        add(new Taskbar(this, getWidth(), "Knall dir die Rübe weg"));
        screen = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.setColor(new Color(0, 154, 21));
                g.fillRect(25, 25, 190, 190);

                g.setColor(new Color(136, 255, 46, 77));
                g.fillRect(25, 25, 80, 15);

                g.setColor(new Color(187, 255, 70, 77));
                g.fillRect(25, 25, 120, 20);

                g.setColor(Color.WHITE);
                g.fillRect(37, 65, 100, 2);

                g.drawImage(screenTV, 280, 20, 300, 200, null);

                g.drawImage(screenPlayerList, 20, 20, 200, 200, null);

                for (int i = Roulette.players.size() - 1; i >= 0; i--) {
                    g.setColor(new Color(136, 255, 46, 77));
                    g.fillRect(32, 50 + 30 * (i + 1), 100, 18);
                    g.setColor(new Color(187, 255, 70, 77));
                    g.fillRect(32, 50 + 30 * (i + 1), 140, 23);
                    g.setColor(new Color(200, 255, 203, 179));
                    g.fillRect(32, 49 + 30 * (i + 1), 160, 1);
                    if (i == 0) {
                        g.drawImage(table, 172, 350, 256, 136, null);
                    }
                    Roulette.players.get(i).draw(g);
                }
                if (playerNames.size() - 1 >= Roulette.currentPlayer * 2) {
                    JLabel player = playerNames.get(Roulette.currentPlayer * 2);
                    int size = player.getFontMetrics(player.getFont()).stringWidth(player.getText());
                    g.drawImage(gun, size + 50, 85 + 30 * Roulette.currentPlayer, 30, 16, null);
                }
            }
        };
        screen.setLayout(null);
        screen.setBackground(Color.LIGHT_GRAY);
        screen.setBounds(0, 20, 800, 780);
        add(screen);

    }

    private void createPlayerList() {
        JLabel playerList = new JLabel("Player List");
        LoadHandler.label(playerList);
        playerList.setBounds(40, 40, 200, 24);
        playerList.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 20));
        playerList.setForeground(Color.WHITE);
        screen.add(playerList);
    }

    private void createLog() {
        logScreen = new JPanel();
        logScreen.setLayout(new GridLayout(10, 1, 0, 0));
        logScreen.setBackground(new Color(0, 154, 21, 255));
        logScreen.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        log = new JScrollPane(logScreen);
        log.setBackground(new Color(0, 0, 0, 0));
        log.setBounds(290, 30, 280, 180);
        log.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        log.getVerticalScrollBar().setUnitIncrement(4);
        log.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        screen.add(log);
    }

    public void addLog(String player, String message) {
        String date = LocalTime.now().getHour() + ":" + LocalTime.now().getMinute();
        JLabel log = new JLabel(date + "   " + player + ":  " + message) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(136, 255, 46, 77));
                g.fillRect(0, 0, 240, 16);
                g.setColor(new Color(187, 255, 70, 77));
                g.fillRect(0, 0, 200, 12);
            }
        };
        LoadHandler.label(log);
        log.setBounds(0, 0, 200, 24);
        log.setForeground(new Color(255, 255, 255, 255));
        log.setBorder(BorderFactory.createLineBorder(new Color(200, 255, 203, 179), 1));
        log.setPreferredSize(new Dimension(200, 24));
        log.setMaximumSize(new Dimension(200, 24));
        log.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 12));
        logScreen.add(log);
        if (logScreen.getComponentCount() < 10) {
            logScreen.setLayout(new GridLayout(7, 0, 0, 0));
        } else {
            logScreen.setLayout(new GridLayout(logScreen.getComponentCount(), 0, 0, 0));
            SwingUtilities.invokeLater(() -> {
                JScrollBar vertical = this.log.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            });
        }
        revalidate();
        repaint();
    }

    private void createInteract() {
        shoot = new JButton("Shoot");
        LoadHandler.createButton(shoot);
        shoot.setLocation(228, 540);
        shoot.setVisible(false);
        shoot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Roulette.shoot();
                shoot.setVisible(false);
            }
        });

        reset = new JButton("Reset");
        LoadHandler.createButton(reset);
        reset.setLocation(228, 540);
        reset.setVisible(false);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Roulette.reset();
                reset.setVisible(false);
                Roulette.sendMessage("Reset|");
                try {
                    Thread.sleep(200);
                    Roulette.selectNextPlayer();
                } catch (InterruptedException et) {
                    throw new RuntimeException(et);
                }
            }
        });

        messanger = new JTextArea();
        messanger.setBounds(290, 220, 260, 24);
        messanger.setBorder(BorderFactory.createLineBorder(new Color(200, 255, 203, 179), 2));
        messanger.setBackground(new Color(0, 154, 21));
        messanger.setForeground(Color.WHITE);
        messanger.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 12));
        messanger.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (messanger.getText().isEmpty()) {
                        messanger.setText("");
                    } else {
                        sendMessage();
                    }
                    e.consume();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (messanger.getText().length() >= 30) {
                    if (e.getKeyChar() != KeyEvent.VK_BACK_SPACE && e.getKeyChar() != KeyEvent.VK_DELETE) {
                        e.consume();
                    }
                }
            }
        });

        JButton enter = new JButton();
        LoadHandler.createButton(enter);
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!messanger.getText().isEmpty()) {
                    sendMessage();
                }
            }
        });
        enter.setBounds(550, 220, 30, 24);
        enter.setIcon(new ImageIcon(LoadHandler.imageLoad(LoadHandler.buttonEnter)));

        JButton emote = new JButton("Emote");
        LoadHandler.createButton(emote);
        emote.setBounds(714, 25, 60, 40);
        emote.setIcon(new ImageIcon(LoadHandler.imageLoad(LoadHandler.buttonEmote)));
        emote.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean setter = true;
                if (heheheha.isVisible()) {
                    setter = false;
                }
                heheheha.setVisible(setter);
                grrr.setVisible(setter);
                groovy.setVisible(setter);
                opfer.setVisible(setter);
                sad.setVisible(setter);
                skull.setVisible(setter);
                takeit.setVisible(setter);
                blyat.setVisible(setter);
                oiia.setVisible(setter);
                sexy.setVisible(setter);
                score.setVisible(setter);
            }
        });

        heheheha = new JButton() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 140, 255));
                g.fillRect(0, 0, getWidth(), (int) height);
                g.setColor(new Color(255, 255, 255));
                g.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 12));
                g.drawString("Heheheha", 10, 20);
            }
        };
        LoadHandler.createEmote(heheheha);
        heheheha.setLocation(604, 25);
        heheheha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendEmote(0);
            }
        });
        grrr = new JButton() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 140, 255));
                g.fillRect(0, 0, getWidth(), (int) height);
                g.setColor(new Color(255, 255, 255));
                g.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 12));
                g.drawString("Grrr", 10, 20);
            }
        };
        LoadHandler.createEmote(grrr);
        grrr.setLocation(604, 62);
        grrr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendEmote(1);
            }
        });

        groovy = new JButton() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 140, 255));
                g.fillRect(0, 0, getWidth(), (int) height);
                g.setColor(new Color(255, 255, 255));
                g.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 12));
                g.drawString("Groovy", 10, 20);
            }
        };
        LoadHandler.createEmote(groovy);
        groovy.setLocation(604, 97);
        groovy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendEmote(2);
            }
        });

        opfer = new JButton() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 140, 255));
                g.fillRect(0, 0, getWidth(), (int) height);
                g.setColor(new Color(255, 255, 255));
                g.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 12));
                g.drawString("Opfer", 10, 20);
            }
        };
        LoadHandler.createEmote(opfer);
        opfer.setLocation(604, 132);
        opfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendEmote(3);
            }
        });

        sad = new JButton() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 140, 255));
                g.fillRect(0, 0, getWidth(), (int) height);
                g.setColor(new Color(255, 255, 255));
                g.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 12));
                g.drawString("Sad", 10, 20);
            }
        };
        LoadHandler.createEmote(sad);
        sad.setLocation(604, 167);
        sad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendEmote(4);
            }
        });

        skull = new JButton() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 140, 255));
                g.fillRect(0, 0, getWidth(), (int) height);
                g.setColor(new Color(255, 255, 255));
                g.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 12));
                g.drawString("Skull", 10, 20);
            }
        };
        LoadHandler.createEmote(skull);
        skull.setLocation(604, 202);
        skull.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendEmote(5);
            }
        });

        takeit = new JButton() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 140, 255));
                g.fillRect(0, 0, getWidth(), (int) height);
                g.setColor(new Color(255, 255, 255));
                g.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 12));
                g.drawString("Take it", 10, 20);
            }
        };
        LoadHandler.createEmote(takeit);
        takeit.setLocation(604, 237);
        takeit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendEmote(6);
            }
        });

        blyat = new JButton() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 140, 255));
                g.fillRect(0, 0, getWidth(), (int) height);
                g.setColor(new Color(255, 255, 255));
                g.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 12));
                g.drawString("Blyat", 10, 20);
            }
        };
        LoadHandler.createEmote(blyat);
        blyat.setLocation(604, 272);
        blyat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendEmote(7);
            }
        });

        oiia = new JButton() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 140, 255));
                g.fillRect(0, 0, getWidth(), (int) height);
                g.setColor(new Color(255, 255, 255));
                g.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 12));
                g.drawString("Oiia-gato", 10, 20);
            }
        };
        LoadHandler.createEmote(oiia);
        oiia.setLocation(604, 307);
        oiia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendEmote(8);
            }
        });

        sexy = new JButton() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 140, 255));
                g.fillRect(0, 0, getWidth(), (int) height);
                g.setColor(new Color(255, 255, 255));
                g.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 12));
                g.drawString("Sexy", 10, 20);
            }
        };
        LoadHandler.createEmote(sexy);
        sexy.setLocation(604, 342);
        sexy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendEmote(9);
            }
        });

        score = new JButton() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 140, 255));
                g.fillRect(0, 0, getWidth(), (int) height);
                g.setColor(new Color(255, 255, 255));
                g.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 12));
                g.drawString("Score", 10, 20);
            }
        };
        LoadHandler.createEmote(score);
        score.setLocation(604, 377);
        score.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendEmote(10);
            }
        });


        for (int i = 0; i < emotes.length; i++) {
            Image icon = new ImageIcon(LoadHandler.heheheha).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
            emotes[i] = new JLabel(new ImageIcon(icon));
            emotes[i].setOpaque(true);
            emotes[i].setVisible(false);
            emotes[i].setBackground(new Color(0, 0, 0, 0));
            switch (i) {
                case 0:
                    emotes[i].setBounds(260, 350, 80, 80);
                    break;
                case 1:
                    emotes[i].setBounds(102, 286, 80, 80);
                    break;
                case 2:
                    emotes[i].setBounds(260, 208, 80, 80);
                    break;
                case 3:
                    emotes[i].setBounds(418, 286, 80, 80);
                    break;
            }
            screen.add(emotes[i]);
        }

        timer = new Timer(100, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (height < 32.0) {
                    height += 0.5;
                    repaint();
                } else {
                    cooldown = false;
                    timer.stop();
                }
            }
        });
        timer.setRepeats(true);

        screen.add(shoot);
        screen.add(reset);
        screen.add(messanger);
        screen.add(enter);
        screen.add(emote);
        screen.add(heheheha);
        screen.add(grrr);
        screen.add(groovy);
        screen.add(opfer);
        screen.add(sad);
        screen.add(skull);
        screen.add(takeit);
        screen.add(blyat);
        screen.add(oiia);
        screen.add(sexy);
        screen.add(score);
    }

    private void createItems() {
        JButton items = new JButton();
        LoadHandler.createButton(items);
        items.setBounds(714, 715, 60, 40);
        items.setIcon(new ImageIcon(LoadHandler.imageLoad(LoadHandler.buttonItems)));
        items.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean setter = !itemButtons[0].isVisible();
                for (int i = 0; i < itemButtons.length; i++) {
                    itemButtons[i].setVisible(setter);
                }
            }
        });
        for (int i = 0; i < itemButtons.length; i++) {
            itemButtons[i] = new itemButton();
            itemButtons[i].setLocation(634 - 70 * i, 705);
            screen.add(itemButtons[i]);
        }

        screen.add(items);
    }

    private void sendMessage() {
        String text = messanger.getText();
        messanger.setText("");
        messanger.setCaretPosition(0);
        addLog(Roulette.players.get(Roulette.player).name, text);
        Roulette.sendMessage("Send|" + Roulette.player + ":" + text);
    }

    private void sendEmote(int emote) {
        if (!cooldown) {
            timer.start();
            cooldown = true;
            height = 0;
            playEmote(emote, Roulette.player);
            this.timer.start();
            Roulette.sendMessage("Emote|" + Roulette.player + ":" + emote);
            repaint();
        }
    }

    public void playEmote(int emote, int player) {
        Image icon = null;
        int time = 0;
        switch (emote) {
            case 0:
                icon = new ImageIcon(LoadHandler.heheheha).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
                time = 1500;
                LoadHandler.loadSound(LoadHandler.soundHeheheha, 1f);
                break;
            case 1:
                icon = new ImageIcon(LoadHandler.grrr).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
                time = 1500;
                LoadHandler.loadSound(LoadHandler.soundGrrr, 1f);
                break;
            case 2:
                icon = new ImageIcon(LoadHandler.groovy).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
                time = 5000;
                LoadHandler.loadSound(LoadHandler.soundGroovy, 1f);
                break;
            case 3:
                icon = new ImageIcon(LoadHandler.opfer).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
                time = 3200;
                LoadHandler.loadSound(LoadHandler.soundOpfer, 1f);
                break;
            case 4:
                icon = new ImageIcon(LoadHandler.sad).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
                time = 3000;
                LoadHandler.loadSound(LoadHandler.soundSad, 1f);
                break;
            case 5:
                icon = new ImageIcon(LoadHandler.skull).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
                time = 4000;
                LoadHandler.loadSound(LoadHandler.soundSkull, 1f);
                break;
            case 6:
                icon = new ImageIcon(LoadHandler.takeit).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
                time = 5200;
                LoadHandler.loadSound(LoadHandler.soundTakeit, 1f);
                break;
            case 7:
                icon = new ImageIcon(LoadHandler.blyat).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
                time = 7000;
                LoadHandler.loadSound(LoadHandler.soundBlyat, 1f);
                break;
            case 8:
                icon = new ImageIcon(LoadHandler.oiia).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
                time = 1600;
                LoadHandler.loadSound(LoadHandler.soundOiia, 1f);
                break;
            case 9:
                icon = new ImageIcon(LoadHandler.sexy).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
                time = 1500;
                LoadHandler.loadSound(LoadHandler.soundSexy, 1f);
                break;
            case 10:
                icon = new ImageIcon(LoadHandler.score).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
                time = 3800;
                LoadHandler.loadSound(LoadHandler.soundScore, 1f);
                break;
        }
        emotes[player].setIcon(new ImageIcon(icon));
        emotes[player].setVisible(true);
        Timer timer = new Timer(time, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emotes[player].setVisible(false);
                emotes[player].setIcon(null);
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    public void addPlayer(String playerName) {
        int number = (playerNames.size() / 2) + 1;
        JLabel player = new JLabel(number + ". " + playerName);
        LoadHandler.label(player);
        int size = player.getFontMetrics(player.getFont()).stringWidth(player.getText());
        player.setBounds(40, 50 + 30 * number, size, 24);
        switch (number) {
            case 1:
                player.setForeground(new Color(0, 255, 159));
                break;
            case 2:
                player.setForeground(new Color(228, 255, 0));
                break;
            case 3:
                player.setForeground(new Color(0, 217, 255));
                break;
            case 4:
                player.setForeground(new Color(255, 0, 242));
                break;
        }
        JLabel shadow = new JLabel(number + ". " + playerName);
        LoadHandler.label(shadow);
        shadow.setBounds(38, 50 + 30 * number, size, 24);
        shadow.setForeground(Color.WHITE);
        shadow.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 15));

        Roulette.players.add(new Player(playerName, Roulette.players.size()));

        screen.add(shadow);
        screen.add(player);
        playerNames.add(shadow);
        playerNames.add(player);
        repaint();
    }

    public void enableShoot() {
        shoot.setVisible(true);
    }

    public void disableShoot(){shoot.setVisible(false);}

    public void enableReset(){
        reset.setVisible(true);
    }

    public void createSelect(){
        selectButtons = new JButton[Roulette.players.size()];
        for (int i = 0; i < selectButtons.length; i++) {
            JButton button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(0, 0, 0, 0));
            switch(i){
                case 0:
                    button.setBounds(270, 404, 60, 24);
                    button.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 159)));
                    break;
                case 1:
                    button.setBounds(112, 336, 60, 24);
                    button.setBorder(BorderFactory.createLineBorder(new Color(228, 255, 0)));
                    break;
                case 2:
                    button.setBounds(270, 258, 60, 24);
                    button.setBorder(BorderFactory.createLineBorder(new Color(0, 217, 255)));
                    break;
                case 3:
                    button.setBounds(428, 336, 60, 24);
                    button.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 242)));
                    break;
            }
            button.setVisible(false);
            selectButtons[i] = button;
            screen.add(button);
        }
    }

    public void robbing(){
        for (int i = 0; i < selectButtons.length; i++) {
            if (i != Roulette.player) {
                selectButtons[i].setVisible(true);
                if (selectButtons[i].getActionListeners().length > 0) {
                    selectButtons[i].removeActionListener(selectButtons[i].getActionListeners()[0]);
                }
                int finalI = i;
                selectButtons[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int number = finalI;
                        deselect();
                        Roulette.sendMessage("Item|" + Roulette.player + ":2|" + number);
                        addLog("Item", Roulette.players.get(Roulette.player).name + " is stealing from" + Roulette.players.get(number).name);
                        enableShoot();
                    }
                });
            }
        }
    }

    public void deselect(){
        for (int i = 0; i < selectButtons.length; i++) {
            selectButtons[i].setVisible(false);
        }
    }

    public ArrayList itemButtons() {
        ArrayList resp = new ArrayList();
        for (int i = 0; i < itemButtons.length; i++) {
            if (itemButtons[i].getItem() != -1) {
                resp.add(itemButtons[i]);
            }
        }
        return resp;
    }

    public itemButton getItemButton(){
        for (int i = 0; i < itemButtons.length; i++) {
            if (itemButtons[i].getItem() == -1) {
                return itemButtons[i];
            }
        }
        return null;
    }

    public void addWheel(WheelOfFortune wheel){
        screen.add(wheel);
        screen.revalidate();
        screen.repaint();
    }

    public void removeWheel(WheelOfFortune wheel){
        screen.remove(wheel);
        screen.revalidate();
        screen.repaint();
    }
}
