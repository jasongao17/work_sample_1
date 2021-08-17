package validator;

import game.elements.*;
import game.Dungeon;
import game.Game;

public interface SimpleVisitor<T> {
    T visit(Game g);
    T visit(Dungeon d);
    T visit(AttributeBooster ab);
    T visit(Coin c);
    T visit(Door d);
    T visit(Goal n);
    T visit(InaccessibleCell ic);
    T visit(Key k);
    T visit(Monster m);
    T visit(Player p);
    T visit(Start s);
    T visit(Wall w);
}
