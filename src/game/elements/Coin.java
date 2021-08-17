package game.elements;

import validator.SimpleVisitor;

public class Coin extends Item {
    @Override
    public <T> T accept(SimpleVisitor<T> v) {
        return v.visit(this);
    }
}
