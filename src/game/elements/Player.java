package game.elements;

import java.util.HashMap;

import game.Dungeon;
import game.properties.Attribute;
import game.properties.Color;
import game.properties.Direction;
import game.properties.PlayerMoveResult;
import validator.SimpleVisitor;

public class Player extends InteractiveDungeonElement {
    private int maxHp = 0;
    private int currentHp = 0;
    private int def = 0;
    private int atk = 0;
    private int coin = 0;
    private int monstersKilled = 0;
    private HashMap<Color, Integer> keys = new HashMap<>();

    public Player(Dungeon parent, int xpos, int ypos) {
        super(parent, xpos, ypos);
    }

    public Player setMaxHealth(int hp) {
        this.maxHp = hp;
        return this;
    }

    public Player setCurrentHealth(int hp) {
        this.currentHp = hp;
        return this;
    }

    public Player setAttack(int atk) {
        this.atk = atk;
        return this;
    }

    public Player setDefence(int def) {
        this.def = def;
        return this;
    }

    public PlayerMoveResult move(Direction direction) {
        // TODO: get a MoveMutex
        PlayerMoveResult result = PlayerMoveResult.NONE;
        if (isMovable(direction)) {
            int[] newPos = getNextPosition(direction, xpos, ypos);
            int newXpos = newPos[0];
            int newYpos = newPos[1];

            if (parent.getElement(newXpos, newYpos) != null) {
                // do some works with overlapped, accessible element
                result = replace(parent.getElement(newXpos, newYpos));
                if (result == PlayerMoveResult.GOAL) {
                    return result;
                }
            }

            parent.replaceElement(null, xpos, ypos);
            this.xpos = newXpos;
            this.ypos = newYpos;
            parent.replaceElement(this, xpos, ypos);
        }
        return result;
    }

    private PlayerMoveResult replace(DungeonElement replacedElement) {
        if (replacedElement instanceof Key) {
            this.addKey((Key) replacedElement);
            return PlayerMoveResult.KEY;
        } else if (replacedElement instanceof Monster) {
            // not sure if there is probably double attack  --- probably can be solved by mutex lock
            return ((Monster) replacedElement).attack(this);
        } else if (replacedElement instanceof Door) {
            Door door = (Door) replacedElement;
            this.keys.put(door.getColor(), this.keys.get(door.getColor()) - 1);
            return PlayerMoveResult.DOOR;
        } else if (replacedElement instanceof Goal) {
            // TODO: if we have win condition added to the grammar this might need more logic checking
            return PlayerMoveResult.GOAL;
        } else if (replacedElement instanceof AttributeBooster) {
            ((AttributeBooster) replacedElement).boost(this);
            Attribute type = ((AttributeBooster) replacedElement).getAttribute();
            switch (type) {
                case HEALTH:
                    if (((AttributeBooster) replacedElement).isPermanent()) {
                        return PlayerMoveResult.HP_MAX;
                    } else {
                        return PlayerMoveResult.HEAL;
                    }
                case DEFENCE:
                    return PlayerMoveResult.DEF_MAX;
                case ATTACK:
                    return PlayerMoveResult.ATK_MAX;
                default:
                    return PlayerMoveResult.NONE;
            }
        } else if (replacedElement instanceof Coin) {
            this.coin += 1;
            return PlayerMoveResult.COIN;
        } else {
            System.out.println("Unknown reachable element encountered, ensure isAccessible is false for the element");
            return PlayerMoveResult.NONE;
        }
    }

    @Override
    public boolean isAccessible(InteractiveDungeonElement ide) {
        return ide instanceof Monster;
    }

    public void addKey(Key key) {
        this.keys.put(key.getColor(), (this.keys.get(key.getColor()) == null ? 1 : this.keys.get(key.getColor()) + 1));
    }

    public void addKills() {
        this.monstersKilled++;
    }

    public int getMaxHealth() {
        return this.maxHp;
    }

    public int getCurrentHealth() {
        return this.currentHp;
    }

    public int getAttack() {
        return this.atk;
    }

    public int getDefence() {
        return this.def;
    }

    public int getCoin() {
        return this.coin;
    }

    public int getKills() {
        return this.monstersKilled;
    }

    public HashMap<Color, Integer> getKeys() {
        return this.keys;
    }

    @Override
    public <T> T accept(SimpleVisitor<T> v) {
        return v.visit(this);
    }
}
