package org.poo.cards;

import org.poo.fileio.CardInput;

import java.util.ArrayList;

public class Hero extends Card{
    public static final int HERO_HEALTH = 30;
    private int health;
    public Hero(CardInput card) {
        super(card);
        this.health = HERO_HEALTH;
    }

    public Hero(final Hero card) {
        super(card);
        this.health = card.health;
    }
    public final int getHealth() {
        return health;
    }

    public final void setHealth(final int health) {
        this.health = health;
    }
    public void heroAbility(ArrayList<Minion>[] table, int row){
        System.out.println("Hero does not have ability to use.");
    }
}
