package game;

import game.elements.*;
import validator.SimpleVisitor;
import ui.GameController;

import java.awt.*;
import java.util.List;

public class Game {
    // not sure if we keep using same player, should we clean some fields (like keys) if they go to the next level
    private Player player;
    private List<Monster> monsters;
    private List<Dungeon> dungeons;

    public Game(Player p, List<Monster> ms, List<Dungeon> ds) {
        this.player = p;
        this.monsters = ms;
        this.dungeons = ds;
    }

    public Player getPlayer() {
        return this.player;
    }

    public List<Monster> getMonsters() {
        return this.monsters;
    }

    public List<Dungeon> getDungeons() {
        return this.dungeons;
    }

    public <T> T accept(SimpleVisitor<T> v) {
        return v.visit(this);
    }
  
    public void play() {
        EventQueue.invokeLater(() -> {
            var ex = new GameController(player, monsters, dungeons);
        });

    }
}
