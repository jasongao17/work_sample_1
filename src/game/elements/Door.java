package game.elements;

import game.properties.Color;
import validator.SimpleVisitor;
import java.util.HashMap;

public class Door extends DungeonElement {
    private Color color;

    public Door(Color color) {
        this.color = color;
    }

    @Override
    public boolean isAccessible(InteractiveDungeonElement ide) {
        // monster cannot pass the door
        if (ide instanceof Monster) return false;
        // it depends on whether the player has key or not
        Player player = (Player) ide;
        HashMap<Color, Integer> keyMap = player.getKeys();
        // in the case of null, we haven't got any key of the color of the door
        // in the case of 0, we have got such a key, but we ran out of it.
        if (keyMap.get(color) == null || keyMap.get(color) == 0) {
            return false;
        }
        
        return true;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public <T> T accept(SimpleVisitor<T> v) {
        return v.visit(this);
    }
}
