package game.elements;

public abstract class Item extends DungeonElement {
    @Override
    public boolean isAccessible(InteractiveDungeonElement ide) {
        // monster cannot pass the block with item
        return ide instanceof Player;
    }
}
