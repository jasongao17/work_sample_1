package game.elements;

import java.util.ArrayList;
import java.util.List;

import game.Dungeon;
import game.properties.Direction;
import game.properties.MonsterType;
import game.properties.PlayerMoveResult;
import validator.SimpleVisitor;

public class Monster extends InteractiveDungeonElement {
    private MonsterType monsterType;
    private String name;
    private int atk = 0;
    private int def = 0;
    private int hp = 0;
    private int curHp = 0;
    private List<Direction> path = new ArrayList<Direction>();
    private int cursor = 0;
    private boolean dead = false;

    public Monster(MonsterType monsterType, String name, Dungeon parent, int xpos, int ypos) {
        super(parent, xpos, ypos);
        this.monsterType = monsterType;
        this.name = name;
    }

    // every time when the frame is refreshed, it should call this function so that monster can move to next postion
    // current decision is to keep moving on the same path instead of walking backwards, in the future if we do that we need a flag and reverse path to handle it
    public PlayerMoveResult next() {
        // TODO: get a MoveMutex
        PlayerMoveResult result = PlayerMoveResult.NONE;
        if (path.size() != 0) {
            Direction direction = path.get(cursor);
            if (isMovable(direction)) {
                int[] nextPos = getNextPosition(direction, xpos, ypos);
                int newX = nextPos[0];
                int newY = nextPos[1];

                cursor++;
                if (cursor >= path.size()) {
                    cursor = 0;
                }
                DungeonElement replacedElement = parent.getElement(newX, newY);
                // if the overlap between monster and some accessible element is player, the monster will attack the player
                if (replacedElement instanceof Player) {
                    return attack((Player) replacedElement);
                }

                parent.replaceElement(null, xpos, ypos);
                this.xpos = newX;
                this.ypos = newY;
                parent.replaceElement(this, xpos, ypos);
            }
        }
        return result;
    }

    // When the monster attacks the player both side will keep fighting until one dies
    public PlayerMoveResult attack(Player player) {
        // not sure the hp calculation (especially how should we use def)
        int remainingHP = player.getCurrentHealth();
        while (remainingHP > 0 && this.curHp > 0) {
            int damageToPlayer = this.atk - player.getDefence();
            if (damageToPlayer > 0) {   // this is to ensure negative damage will not heal
                remainingHP -= damageToPlayer;
            } else {    // this is to prevent an infinite fight
                remainingHP -= 1;
            }
            int damageToMonster = player.getAttack() - this.def; 
            if (damageToMonster > 0) {
                this.curHp -= damageToMonster;
            } else {
                this.curHp -= 1;
            }
        }
        if (remainingHP <= 0) {
            // TODO: should change this to return enum of states
            player.setCurrentHealth(remainingHP);
            return PlayerMoveResult.DEAD;
        } else {
            setDead();
            player.setCurrentHealth(remainingHP);
            player.addKills();
            // we need clean the moster from the playground, just simply set it to null
            parent.replaceElement(null, xpos, ypos);
            return PlayerMoveResult.MONSTER;
        }
    }

    @Override
    public boolean isAccessible(InteractiveDungeonElement ide) {
        return ide instanceof Player;
    }

    public Monster setHealth(int hp) {
        this.hp = hp;
        this.curHp = hp;
        return this;
    }

    public Monster setAttack(int atk) {
        this.atk = atk;
        return this;
    }

    public Monster setDefence(int def) {
        this.def = def;
        return this;
    }

    public Monster setPath(List<Direction> path) {
        this.path = path;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public MonsterType getType() {
        return this.monsterType;
    }

    public int getMaxHealth() {
        return this.hp;
    }

    public int getCurrentHealth() {
        return this.curHp;
    }

    public int getAttack() {
        return this.atk;
    }

    public int getDefence() {
        return this.def;
    }

    private void setDead() {
        this.dead = true;
        this.curHp = 0;
    }

    public boolean isDead() {
        return this.dead;
    }

    @Override
    public <T> T accept(SimpleVisitor<T> v) {
        return v.visit(this);
    }

    public void reset() {
        this.curHp = this.hp;
        this.cursor = 0;
        this.dead = false;
    }
}

