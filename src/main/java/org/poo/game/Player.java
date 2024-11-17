package org.poo.game;


import org.poo.cards.*;
import org.poo.fileio.CardInput;

import java.util.ArrayList;

public final class Player {
    private Decks decks;
    private Hero hero;
    private int front;
    private int back;
    private int mana;
    private ArrayList<Minion> cardsInHand;
    private ArrayList<Minion> currentDeck;
    private boolean turn;

    public Player(final Decks decks, final ArrayList<Minion> deck, final CardInput hero,
                  final int indexFrontRow, final int indexBackRow) {
        this.currentDeck = deck;
        this.decks = decks;
        switch (hero.getName()) {
            case "Lord Royce" -> this.hero = new LordRoyce(hero);
            case "Empress Thorina" -> this.hero = new EmpressThorina(hero);
            case "King Mudface" -> this.hero = new KingMudface(hero);
            case "General Kocioraw" -> this.hero = new GeneralKocioraw(hero);
            default -> this.hero = null;
        }
        this.mana = 1;
        this.front = indexFrontRow;
        this.back = indexBackRow;
        this.cardsInHand = new ArrayList<>();
        this.turn = false;
    }

    /**
     * @param table the game table
     */
    public void defrost(final ArrayList<Minion>[] table) {
        for (Minion minion : table[this.front]) {
            if (minion.getFrozen()) {
                minion.setFrozen(false);
            }
        }

        for (Minion minion : table[this.back]) {
            if (minion.getFrozen()) {
                minion.setFrozen(false);
            }
        }
    }

    /**
     * @param table the game table
     */
    public void refreshAttackers(final ArrayList<Minion>[] table) {
        for (Minion minion : table[this.front]) {
            if (minion.getHasAttacked()) {
                minion.setHasAttacked(false);
            }
        }

        for (Minion minion : table[this.back]) {
            if (minion.getHasAttacked()) {
                minion.setHasAttacked(false);
            }
        }
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public int getBack() {
        return back;
    }

    public int getFront() {
        return front;
    }

    public ArrayList<Minion> getCurrentDeck() {
        return currentDeck;
    }

    public ArrayList<Minion> getCardsInHand() {
        return cardsInHand;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(final Hero hero) {
        this.hero = hero;
    }

    public Decks getDecks() {
        return decks;
    }

    public void setDecks(final Decks decks) {
        this.decks = decks;
    }

    public boolean getTurn() {
        return turn;
    }

    public void setTurn(final boolean turn) {
        this.turn = turn;
    }

    public boolean isTurn() {
        return turn;
    }
}
