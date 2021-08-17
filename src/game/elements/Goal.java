package game.elements;

import validator.SimpleVisitor;

public class Goal extends DungeonElement {
    // Might want to have x and y location here for a quick way to find the goal in the dungeon
    // we probably need to check # of coins on the map
    // warning: I assume we need to collect all coins in order to go to the next level
    
    // private Player player;

    // public Goal(Player player) {
    //     this.player = player;
    // }

    @Override
    public boolean isAccessible(InteractiveDungeonElement ide) {
        /*
        if (checkAllCoins()) {
            return true;
        }
        */

        // monster cannot pass the block with goal
        return ide instanceof Player;
    }

    @Override
    public <T> T accept(SimpleVisitor<T> v) {
        return v.visit(this);
    }

    // private boolean checkAllCoins() {
    //     DungeonElement[][] playground = player.parent.getPlayground();
    //     for (int i = 0; i < playground.length; i++) {
    //         for (int j = 0; j < playground[i].length; j++) {
    //             if (playground[i][j] instanceof Coin) {
    //                 return false;
    //             }
    //         }
    //     }

    //     return true;
    // }

}
