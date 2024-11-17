package org.poo.cards;

import org.poo.fileio.CardInput;

import java.util.ArrayList;

public class Card {
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private String type;
    private boolean attacked;

    private String typeCard(final String cardName) {
        String isMinion = "Sentinel, Berserker, Goliath, Warden, The Ripper, Miraj, "
                + "The Cursed One, Disciple";
        if (isMinion.contains(cardName)) {
            return "Minion";
        } else {
            return "Hero";
        }
    }
    public Card(final CardInput card) {
        this.mana = card.getMana();
        this.description = new String(card.getDescription());
        this.colors = new ArrayList<String>(card.getColors());
        this.name = new String(card.getName());
        this.type = new String(typeCard(this.name));
        this.attacked = false;
    }
    public Card(final Card card) {
        this.mana = card.getMana();
        this.description = new String(card.getDescription());
        this.colors = new ArrayList<String>(card.getColors());
        this.name = card.getName();
        this.type = new String(card.getType());
        this.attacked = false;
    }

    public final int getMana() {
        return mana;
    }

    public final String getDescription() {
        return description;
    }

    public final ArrayList<String> getColors() {
        return colors;
    }

    public final String getName() {
        return name;
    }

    public final String getType() {
        return type;
    }

    public final void setMana(final int mana) {
        this.mana = mana;
    }

    public final void setDescription(final String description) {
        this.description = description;
    }

    public final void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final boolean getHasAttacked() {
        return attacked;
    }

    public final void setHasAttacked(final boolean hasAttacked) {
        this.attacked = hasAttacked;
    }
}
