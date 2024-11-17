package org.poo.cards;

import org.poo.fileio.CardInput;

public final class Miraj extends Minion {
    @Override
    public void useAbility(final Minion minion) {
        int aux = this.getHealth();
        this.setHealth(minion.getHealth());
        minion.setHealth(aux);
    }

    public Miraj(final CardInput card) {
        super(card);
    }

    public Miraj(final Miraj card) {
        super(card);
    }
}
