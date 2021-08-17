package game.elements;

import validator.SimpleVisitor;

public abstract class DungeonElement {
    public abstract boolean isAccessible(InteractiveDungeonElement ide);
    public abstract <T> T accept(SimpleVisitor<T> v);
}
