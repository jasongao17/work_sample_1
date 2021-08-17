package ui;

import javax.swing.*;

import game.elements.Player;
import game.properties.PlayerMoveResult;
import game.properties.Color;

import java.awt.*;
import java.util.HashMap;

public class StatsPanel extends JPanel {
    private JPanel textPanel = new JPanel();
    private Player player;

    // Used for label strong formatting using html
    private String format_start = "<html><span style=\"font-family:Arial;font-size:14px;\">";
    private String format_mid = "</span>";
    private String format_end = "</html>";

    // Labels
    private JLabel max_hp_label;
    private JLabel current_hp_label;
    private JLabel atk_label;
    private JLabel def_label;
    private JLabel coin_label;
    private JLabel key_label;
    private JLabel green_key_label;
    private JLabel blue_key_label;
    private JLabel red_key_label;
    private JLabel kills_label;

    public StatsPanel(Player player) {
        this.player = player;
        initializeLabels();
        initializePanel();
    }

    private void initializeLabels() {
        max_hp_label = new JLabel(getMaxHpText());
        current_hp_label = new JLabel(getCurrentHpText());
        atk_label = new JLabel(getAttackText());
        def_label = new JLabel(getDefenceText());
        coin_label = new JLabel(getCoinText());
        key_label = new JLabel(getKeyText());
        green_key_label = new JLabel(getGreenKeyText());
        blue_key_label = new JLabel(getBlueKeyText());
        red_key_label = new JLabel(getRedKeyText());
        kills_label = new JLabel(getKillsText());
    }

    private String getRedKeyText() {
        HashMap<Color, Integer> keys = player.getKeys();
        int num = keys.get(Color.RED) == null ? 0 : keys.get(Color.RED);
        String small_format_start = "<html><span style=\"font-family:Arial;font-size:9px;\">";
        return small_format_start + "Red Keys: " + format_mid + num + format_end;
    }

    private String getBlueKeyText() {
        HashMap<Color, Integer> keys = player.getKeys();
        int num = keys.get(Color.BLUE) == null ? 0 : keys.get(Color.BLUE);
        String small_format_start = "<html><span style=\"font-family:Arial;font-size:9px;\">";
        return small_format_start + "Blue Keys: " + format_mid + num + format_end;
    }

    private String getGreenKeyText() {
        HashMap<Color, Integer> keys = player.getKeys();
        int num = keys.get(Color.GREEN) == null ? 0 : keys.get(Color.GREEN);
        String small_format_start = "<html><span style=\"font-family:Arial;font-size:9px;\">";
        return small_format_start + "Green Keys: " + format_mid + num + format_end;
    }

    private String getKillsText() {
        return format_start + "Monsters Killed: " + format_mid + player.getKills() + format_end;
    }

    private String getKeyText() {
        return format_start + "Keys: " + format_mid + format_end;
    }

    private String getCoinText() {
        return format_start + "Coins Collected: " + format_mid + player.getCoin() + format_end;
    }

    private String getDefenceText() {
        return format_start + "Player Defence: " + format_mid + player.getDefence() + format_end;
    }

    private String getAttackText() {
        return format_start + "Player Attack: " + format_mid + player.getAttack() + format_end;
    }

    private String getCurrentHpText() {
        return format_start + "Player Current HP: " + format_mid + player.getCurrentHealth() + format_end;
    }

    private String getMaxHpText() {
        return format_start + "Player Max HP: " + format_mid + player.getMaxHealth() + format_end;
    }

    private void initializePanel() {
        setBorder(BorderFactory.createTitledBorder("<html><span style=\"font-family:Arial;font-size:17px;\">Game Stats</span></html>"));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(GameConstants.SIDE_PANEL_WIDTH, GameConstants.SIDE_PANEL_HEIGHT));
        textPanel.setLayout(new GridLayout(0, 1));
        add(textPanel, BorderLayout.NORTH);
        textPanel.add(max_hp_label);
        textPanel.add(current_hp_label);
        textPanel.add(atk_label);
        textPanel.add(def_label);
        textPanel.add(coin_label);
        textPanel.add(key_label);
        textPanel.add(green_key_label);
        textPanel.add(blue_key_label);
        textPanel.add(red_key_label);
        textPanel.add(kills_label);
        //String giraffesays = "<html><span style=\"font-family:Arial;font-size:13px;\">Giraffe says :</span>Hi there!</html>";
    }

    // To be used for changing the text in the side panel (update stats)
    public void updatePanel(PlayerMoveResult result) {
        switch (result) {
            case DEAD:
            case HEAL:
                current_hp_label.setText(getCurrentHpText());
                break;
            case HP_MAX:
                max_hp_label.setText(getMaxHpText());
                current_hp_label.setText(getCurrentHpText());
                break;
            case DEF_MAX:
                def_label.setText(getDefenceText());
                break;
            case ATK_MAX:
                atk_label.setText(getAttackText());
                break;
            case COIN:
                coin_label.setText(getCoinText());
                break;
            case DOOR:
            case KEY:
                key_label.setText(getKeyText());
                green_key_label.setText(getGreenKeyText());
                blue_key_label.setText(getBlueKeyText());
                red_key_label.setText(getRedKeyText());
                break;
            case MONSTER:
                current_hp_label.setText(getCurrentHpText());
                kills_label.setText(getKillsText());
                break;
            default:
                break;
        }
    }


    public JLabel getMax_hp_label() {
        return this.max_hp_label;
    }

    public JLabel getCurrent_hp_label() {
        return this.current_hp_label;
    }

    public JLabel getAtk_label() {
        return this.atk_label;
    }

    public JLabel getDef_label() {
        return this.def_label;
    }

    public JLabel getCoin_label() {
        return this.coin_label;
    }

    public JLabel getKey_label() {
        return this.key_label;
    }
        
    public JLabel getGreen_key_label() {
        return this.green_key_label;
    }

    public JLabel getBlue_key_label() {
        return this.blue_key_label;
    }

    public JLabel getRed_key_label() {
        return this.red_key_label;
    }

    public JLabel getKills_label() {
        return this.kills_label;
    }
}
