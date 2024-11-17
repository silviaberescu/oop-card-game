package org.poo.cards;

import org.poo.fileio.CardInput;

import java.util.ArrayList;

public final class LordRoyce extends Hero {
    @Override
    public void heroAbility(final ArrayList<Minion>[] table, final int row) {
        for (Minion card : table[row]) {
            card.setFrozen(true);
        }
    }

    public LordRoyce(final CardInput card) {
        super(card);
    }

    public LordRoyce(final LordRoyce card) {
        super(card);
    }
}
