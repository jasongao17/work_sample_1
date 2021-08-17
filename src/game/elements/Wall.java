package game.elements;

import validator.SimpleVisitor;

public class Wall extends DungeonElement {

    @Override
    public boolean isAccessible(InteractiveDungeonElement ide) {
        return false;
    }

    @Override
    public <T> T accept(SimpleVisitor<T> v) {
        return v.visit(this);
    }
}
