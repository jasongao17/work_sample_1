package game.elements;

import game.properties.Attribute;
import validator.SimpleVisitor;

public class AttributeBooster extends Item {
    private Attribute attribute;
    private int amount;
    private boolean permanent; // for heal pot to heal current hp

    public AttributeBooster(Attribute attribute, int amount, boolean permanent) {
        this.attribute = attribute;
        this.amount = amount;
        this.permanent = permanent;
    }

    public void boost(Player player) {
        // change the attribute (player's field) depending on the type of attribute
        switch (attribute) {
            case ATTACK:
                player.setAttack(player.getAttack() + amount);
                break;

            case DEFENCE:
                player.setDefence(player.getDefence() + amount);
                break;

            case HEALTH:
                if (permanent) {    // Max hp item in this case
                    player.setMaxHealth(player.getMaxHealth() + amount);
                    player.setCurrentHealth(player.getCurrentHealth() + amount);
                } else {
                    int currentHP = player.getCurrentHealth();
                    if (currentHP + amount > player.getMaxHealth()) {
                        player.setCurrentHealth(player.getMaxHealth());
                    } else {
                        player.setCurrentHealth(currentHP + amount);
                    }
                }
                break;
        }
    }

    public Attribute getAttribute() {
        return this.attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isPermanent() {
        return this.permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    @Override
    public <T> T accept(SimpleVisitor<T> v) {
        return v.visit(this);
    }
}
