package org.poo.game;

public class Statistics {
    private int player1Wins = 0,  player2Wins = 0;
    private int nrGames = 0;

    private static Statistics instance = null;
    private Statistics() { }

    public static Statistics getInstance() {
        if (instance == null) {
            instance = new Statistics();
        }
        return instance;
    }

    public int getNrGames() {
        return nrGames;
    }

    public void setNrGames(int nrGames) {
        this.nrGames = nrGames;
    }

    public int getPlayer2Wins() {
        return player2Wins;
    }

    public void setPlayer2Wins(int player2Wins) {
        this.player2Wins = player2Wins;
    }

    public int getPlayer1Wins() {
        return player1Wins;
    }

    public void setPlayer1Wins(int player1Wins) {
        this.player1Wins = player1Wins;
    }

    public void reset() {
        this.nrGames = 0;
        this.player1Wins = 0;
        this.player2Wins = 0;
    }
}
