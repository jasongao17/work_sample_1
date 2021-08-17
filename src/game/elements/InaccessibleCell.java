package game.elements;

import validator.SimpleVisitor;

public class InaccessibleCell extends DungeonElement {

    @Override
    public boolean isAccessible(InteractiveDungeonElement ide) {
        return false;
    }

    @Override
    public <T> T accept(SimpleVisitor<T> v) {
        return v.visit(this);
    }
}