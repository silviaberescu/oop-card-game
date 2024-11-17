package org.poo.game;

import org.poo.cards.*;
import org.poo.fileio.CardInput;

import java.util.ArrayList;

public class Player {
    private Decks decks;
    private Hero hero;
    private int front;
    private int back;
    private int mana;
    private ArrayList<Minion> cardsInHand;
    private ArrayList<Minion> currentDeck;
    boolean turn;

    public Player(Decks decks, ArrayList<Minion> deck, CardInput hero, int indexFrontRow, int indexBackRow) {
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

    public void defrost(ArrayList<Minion>[] table) {
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

    public void refreshAttackers(ArrayList<Minion>[] table) {
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

    public void setMana(int mana) {
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

    public void setCurrentDeck(ArrayList<Minion> currentDeck) {
        this.currentDeck = currentDeck;
    }

    public ArrayList<Minion> getCardsInHand() {
        return cardsInHand;
    }

    public void setCardsInHand(ArrayList<Minion> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Decks getDecks() {
        return decks;
    }

    public void setDecks(Decks decks) {
        this.decks = decks;
    }

    public boolean getTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }
}
