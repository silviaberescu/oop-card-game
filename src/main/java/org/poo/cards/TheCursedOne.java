package org.poo.cards;

import org.poo.fileio.CardInput;

public final class TheCursedOne extends Minion {
    @Override
    public void useAbility(final Minion minion) {
        int aux = minion.getHealth();
        int aux2 = minion.getAttackDamage();
        minion.setHealth(aux2);
        minion.setAttackDamage(aux);
    }

    public TheCursedOne(final CardInput card) {
        super(card);
    }
}
