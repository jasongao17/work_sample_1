package game.elements;

import validator.SimpleVisitor;

public class Start extends DungeonElement {
    // Might want to have x and y location here for a quick way to find the start location for the player in the dungeon
    @Override
    public boolean isAccessible(InteractiveDungeonElement ide) {
        return true;
    }
    
    @Override
    public <T> T accept(SimpleVisitor<T> v) {
        return v.visit(this);
    }
}
