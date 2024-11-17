package org.poo.cards;

import org.poo.fileio.CardInput;

public final class TheRipper extends Minion {
    @Override
    public void useAbility(final Minion minion) {
        minion.setAttackDamage(minion.getAttackDamage() - 2);
        if (minion.getAttackDamage() < 0) {
            minion.setAttackDamage(0);
        }
    }
    public TheRipper(final CardInput card) {
        super(card);
    }
    public TheRipper(final TheRipper card) {
        super(card);
    }
}
