package utilz;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class LoadHandler {
    // button
    public static final String button = "/res/images/button.png";
    public static final String buttonSmall = "/res/images/buttonSmall.png";
    public static final String buttonSmallFocused = "/res/images/buttonSmallFocused.png";
    public static final String buttonFrame = "/res/images/buttonFrame.png";
    public static final String buttonUp = "/res/images/buttonUp.png";
    public static final String buttonDown = "/res/images/buttonDown.png";
    public static final String buttonEnter = "/res/images/buttonEnter.png";
    public static final String buttonEmote = "/res/images/buttonEmote.png";
    public static final String buttonItems = "/res/images/buttonItems.png";

    // emote
    public static final String heheheha = "res/animation/heheheha.gif";
    public static final String grrr = "res/animation/grrr.gif";
    public static final String groovy = "res/animation/groovy.gif";
    public static final String opfer = "res/animation/opfer.gif";
    public static final String sad = "res/animation/sad.gif";
    public static final String skull = "res/animation/skull.gif";
    public static final String takeit = "res/animation/takeit.gif";
    public static final String blyat  = "res/animation/blyat.gif";
    public static final String oiia  = "res/animation/oiia.gif";
    public static final String score  = "res/animation/score.gif";
    public static final String sexy  = "res/animation/sexy.gif";

    // items
    public static final String itemBackground = "/res/images/items/background.png";
    public static final String itemAmmunition = "/res/images/items/ammunition.png";
    public static final String itemFreeBullet = "/res/images/items/freeBullet.png";
    public static final String itemStealinKit = "/res/images/items/stealingKit.png";
    public static final String itemWheelOfFortune = "/res/images/items/wheelOfFortune.png";


    // taskbar
    public static final String closeProgramm = "/res/images/taskbar/closeProgram.png";
    public static final String closeWindow = "/res/images/taskbar/closeWindow.png";

    // game
    public static final String gun = "/res/images/game/pistel.png";
    public static final String table = "/res/images/game/table.png";
    public static final String screen = "/res/images/game/screen.png";
    public static final String screenPlayer = "/res/images/game/screen2.png";
    public static final String player = "/res/images/game/player";
    public static final String ghost = "/res/images/game/ghost";

    // font
    public static final String fontOxygen = "res/fonts/Oxygen-Regular.ttf";

    //sound
    public static final String soundGun = "res/sound/gun.wav";
    public static final String soundGunClick = "res/sound/gunclick.wav";
    public static final String soundHeheheha = "res/sound/heheheha.wav";
    public static final String soundGrrr = "res/sound/grrr.wav";
    public static final String soundGroovy = "res/sound/groovy.wav";
    public static final String soundOpfer = "res/sound/opfer.wav";
    public static final String soundSad = "res/sound/sad.wav";
    public static final String soundSkull = "res/sound/skull.wav";
    public static final String soundTakeit = "res/sound/takeit.wav";
    public static final String soundBlyat  = "res/sound/blyat.wav";
    public static final String soundOiia  = "res/sound/oiia.wav";
    public static final String soundScore  = "res/sound/score.wav";
    public static final String soundSexy  = "res/sound/sexy.wav";




    public static BufferedImage imageLoad(String filename) {
        BufferedImage img = null;
        InputStream is = LoadHandler.class.getResourceAsStream(filename);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return img;
    }

    public static Font getFont(String filename, int size){
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(filename));
            font = font.deriveFont((float)size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return font;
    }

    public static void loadSound(String filePath, float volume) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    setVolume(clip, volume);
                    clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

    }


    private static void setVolume(Clip clip, float volume) {
        if (clip.isOpen()) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }

    public static void createButton(JButton pButton) {
        pButton.setIcon(new ImageIcon(LoadHandler.imageLoad(LoadHandler.button)));
        pButton.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 15));
        pButton.setSize(144, 32);
        pButton.setBackground(new Color(0, 0, 0, 0));
        pButton.setForeground(Color.white);
        pButton.setFocusable(false);
        pButton.setFocusPainted(false);
        pButton.setContentAreaFilled(false);
        pButton.setBorderPainted(false);
        pButton.setVerticalTextPosition(JButton.TOP);
        pButton.setHorizontalTextPosition(JButton.CENTER);
        pButton.setIconTextGap(-25);
    }

    public static void createEmote(JButton pButton) {
        pButton.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 12));
        pButton.setSize(100, 32);
        pButton.setForeground(Color.white);
        pButton.setFocusable(false);
        pButton.setFocusPainted(false);
        pButton.setVisible(false);
        pButton.setBorder(BorderFactory.createLineBorder(new Color(70,0, 255), 2));
    }

    public static void label(JLabel jLabel) {
        jLabel.setFont(LoadHandler.getFont(LoadHandler.fontOxygen, 15));
        jLabel.setForeground(Color.BLACK);
        jLabel.setFocusable(false);
        jLabel.setVerticalTextPosition(JButton.TOP);
        jLabel.setHorizontalTextPosition(JButton.CENTER);
    }
}
