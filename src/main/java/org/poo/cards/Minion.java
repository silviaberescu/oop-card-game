package org.poo.cards;

import org.poo.fileio.CardInput;

public class Minion extends Card {
    private boolean frozen;
    private boolean tank;
    private boolean front;
    private int attackDamage;
    private int health;

    public final int getHealth() {
        return health;
    }
    public final void setHealth(final int health) {
        this.health = health;
    }
    public final int getAttackDamage() {
        return attackDamage;
    }

    public final void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public final boolean getFrozen() {
        return frozen;
    }

    public final void setFrozen(final boolean frozen) {
        this.frozen = frozen;
    }

    public final boolean getFront() {
        return front;
    }

    public final boolean isTank() {
        return tank;
    }

    private boolean isFront(final String name) {
        String frontRow = "The Ripper, Miraj, Goliath, Warden";
        return frontRow.contains(name);
    }
    private boolean isTank(final String name) {
        String frontRow = "Goliath, Warden";
        return frontRow.contains(name);
    }
    public Minion(final CardInput card) {
        super(card);
        this.health = card.getHealth();
        this.attackDamage = card.getAttackDamage();
        this.frozen = false;
        this.front = isFront(this.getName());
        this.tank = isTank(this.getName());
    }
    public Minion(final Minion card) {
        super(card);
        this.health = card.getHealth();
        this.attackDamage = card.getAttackDamage();
        this.frozen = false;
        this.front = card.getFront();
        this.tank = isTank(card.getName());
    }
    /**
     * @param minion the minion whose ability is to be used
    */
    public void useAbility(final Minion minion) {
        System.out.println("Minion does not have a special ability");
    }
}
