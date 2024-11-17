package org.poo.cards;

import org.poo.fileio.CardInput;

public final class Disciple extends Minion {
    @Override
    public void useAbility(final Minion minion) {
        minion.setHealth(minion.getHealth() + 2);
    }

    public Disciple(final CardInput card) {
        super(card);
    }

    public Disciple(final Disciple card) {
        super(card);
    }
}
