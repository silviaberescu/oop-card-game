package org.poo.game;

import org.poo.cards.Decks;
import org.poo.cards.Minion;
import org.poo.fileio.DecksInput;
import org.poo.fileio.GameInput;

import java.util.ArrayList;

public final class InitGame {
    public static final int IDXMAXROWS = 3;
    private Player player1, player2;
    private int seed;
    private int startingPlayer;

    private ArrayList<Minion>[] table = new ArrayList[IDXMAXROWS + 1];

    public InitGame(final GameInput gameInput, final DecksInput decksForPlayer1Input,
                    final DecksInput decksForPlayer2Input) {
        Decks decksForPlayer1 = new Decks(decksForPlayer1Input);
        Decks decksForPlayer2 = new Decks(decksForPlayer2Input);

        int playerOneDeckIndex = gameInput.getStartGame().getPlayerOneDeckIdx();
        ArrayList<Minion> playerOneDeck = decksForPlayer1.getCardsInDecks().get(playerOneDeckIndex);

        int playerTwoDeckIndex = gameInput.getStartGame().getPlayerTwoDeckIdx();
        ArrayList<Minion> playerTwoDeck = decksForPlayer2.getCardsInDecks().get(playerTwoDeckIndex);

        player1 = new Player(decksForPlayer1, playerOneDeck, gameInput.getStartGame().
                getPlayerOneHero(), 2, IDXMAXROWS);
        player2 = new Player(decksForPlayer2, playerTwoDeck, gameInput.getStartGame().
                getPlayerTwoHero(), 1, 0);

        this.seed = gameInput.getStartGame().getShuffleSeed();
        this.startingPlayer = gameInput.getStartGame().getStartingPlayer();


        for (int i = 0; i <= IDXMAXROWS; i++) {
            table[i] = new ArrayList<Minion>();
        }
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public int getSeed() {
        return seed;
    }

    public int getStartingPlayer() {
        return startingPlayer;
    }

    public ArrayList<Minion>[] getTable() {
        return table;
    }
}
