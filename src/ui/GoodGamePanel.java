package ui;

import game.elements.Player;

import javax.swing.*;
import java.awt.*;

import static ui.GameConstants.*;

public class GoodGamePanel extends JPanel {
    private Image gg, opt, aud;
    private Player p;

    private boolean win;
    public GoodGamePanel(Player player, boolean win){
        this.p = player;
        this.win = win;

        if (win) {
            this.gg = new ImageIcon("src/images/misc/wellplayed.jpg").getImage();
            this.opt = new ImageIcon("src/images/misc/wp.jpg").getImage();
            this.aud = new ImageIcon("src/images/misc/koukou.jpg").getImage();
        } else {
            this.gg = new ImageIcon("src/images/misc/gg.png").getImage();
            this.opt = new ImageIcon("src/images/misc/dead.png").getImage();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, MAX_FRAME_WIDTH, MAX_FRAME_HEIGHT);
        g2d.setFont(new Font("TimesRoman", Font.BOLD, 50));
        g2d.setColor(Color.YELLOW);
        g2d.drawString("Game Score",280,345);
        g2d.setFont(new Font("TimesRoman", Font.BOLD, 20));
        g2d.setColor(Color.YELLOW);
        String s = "Coins Collected:       "+String.valueOf(p.getCoin());
        g2d.drawString(s,320,380);
        s = "Max Health:           "+String.valueOf(p.getMaxHealth());
        g2d.drawString(s,320,405);
        s = "Attack Power:         "+String.valueOf(p.getAttack());
        g2d.drawString(s,320,430);
        s = "Defence Power:      "+String.valueOf(p.getDefence());
        g2d.drawString(s,320,455);
        s = "Monsters Killed:       "+String.valueOf(p.getKills());
        g2d.drawString(s,320,480);

        if (win) {
            g2d.drawImage(gg, 300, 460, this);
            g2d.drawImage(opt, 200, 160, this);
            for (int i = 0; i < 9; i++) {
                g2d.drawImage(aud, 2+i*99, 800, this);
            }
        } else {
            g2d.drawImage(gg, 180, 180, this);
        }
    }

}
