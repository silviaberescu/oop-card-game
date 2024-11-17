package org.poo.cards;

import org.poo.fileio.CardInput;
import org.poo.fileio.DecksInput;

import java.util.ArrayList;

public class Decks {
    private int nrCardsInDeck;
    private int nrDecks;
    private ArrayList<ArrayList<Minion>> cardsInDecks;

    public final ArrayList<ArrayList<Minion>> getCardsInDecks() {
        return cardsInDecks;
    }

    public Decks(final DecksInput deckInput) {
        this.nrCardsInDeck = deckInput.getNrCardsInDeck();
        this.nrDecks = deckInput.getNrDecks();

        this.cardsInDecks = new ArrayList<>(this.nrDecks);

        for (int i = 0; i < this.nrDecks; i++) {
            ArrayList<Minion> currentDeck = new ArrayList<>(this.nrCardsInDeck);
            this.cardsInDecks.add(currentDeck);
        }

        int deckIndex = 0;

        for (ArrayList<CardInput> currentDeckInput : deckInput.getDecks()) {

            ArrayList<Minion> currentDeck = this.cardsInDecks.get(deckIndex);

            for (CardInput currentCard : currentDeckInput) {

                if (currentCard.getName().compareTo("Miraj") == 0) {
                    Miraj miraj = new Miraj(currentCard);
                    currentDeck.add(miraj);
                } else if ((currentCard.getName().compareTo("The Ripper") == 0)) {
                    TheRipper theRipper = new TheRipper(currentCard);
                    currentDeck.add(theRipper);
                } else if (currentCard.getName().compareTo("Disciple") == 0) {
                    Disciple disciple = new Disciple(currentCard);
                    currentDeck.add(disciple);
                } else if (currentCard.getName().compareTo("The Cursed One") == 0) {
                    TheCursedOne theCursedOne = new TheCursedOne(currentCard);
                    currentDeck.add(theCursedOne);
                } else {
                    Minion minion = new Minion(currentCard);
                    currentDeck.add(minion);
                }
            }
            deckIndex++;
        }
    }
}
