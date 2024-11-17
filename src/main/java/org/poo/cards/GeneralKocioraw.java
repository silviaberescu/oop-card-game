package org.poo.cards;

import org.poo.fileio.CardInput;

import java.util.ArrayList;

public final class GeneralKocioraw extends Hero {
    @Override
    public void heroAbility(final ArrayList<Minion>[] table, final int row) {
        for (Minion card : table[row]) {
            card.setAttackDamage(card.getAttackDamage() + 1);
        }
    }

    public GeneralKocioraw(final CardInput card) {
        super(card);
    }

}
