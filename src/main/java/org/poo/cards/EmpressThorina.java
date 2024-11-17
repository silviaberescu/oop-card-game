package org.poo.cards;

import org.poo.fileio.CardInput;

import java.util.ArrayList;

public final class EmpressThorina extends Hero {
    @Override
    public void heroAbility(final ArrayList<Minion>[] table, final int row) {
        int index = 0;
        int maxHealth = 0;
        for (Minion card : table[row]) {
            if (card.getHealth() > maxHealth) {
                maxHealth = card.getHealth();
                index = table[row].indexOf(card);
            }
        }
        (table[row]).remove(index);
    }

    public EmpressThorina(final CardInput card) {
        super(card);
    }

    public EmpressThorina(final EmpressThorina card) {
        super(card);
    }
}
