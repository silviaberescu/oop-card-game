package org.poo.cards;

import org.poo.fileio.CardInput;

import java.util.ArrayList;

public final class KingMudface extends Hero {
    @Override
    public void heroAbility(final ArrayList<Minion>[] table, final int row) {
        for (Minion card : table[row]) {
            card.setHealth(card.getHealth() + 1);
        }
    }

    public KingMudface(final CardInput card) {
        super(card);
    }

    public KingMudface(final KingMudface card) {
        super(card);
    }
}
