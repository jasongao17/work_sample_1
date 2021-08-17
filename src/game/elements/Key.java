package game.elements;

import game.properties.Color;
import validator.SimpleVisitor;

public class Key extends Item {
    private Color color;

    public Key(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public <T> T accept(SimpleVisitor<T> v) {
        return v.visit(this);
    }
}
