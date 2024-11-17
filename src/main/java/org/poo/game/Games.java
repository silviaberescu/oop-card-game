package org.poo.game;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.ActionsInput;
import org.poo.fileio.GameInput;
import org.poo.fileio.Input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class Games {
    static final int MAX_MANA = 10;
    private int player1Wins;
    private int player2Wins;
    private int nrGames;
    public Games(final Input input, final ArrayNode output) {
        this.setPlayer1Wins(0);
        this.setPlayer2Wins(0);
        this.setNrGames(0);
        for (GameInput game : input.getGames()) {
            InitGame newGame = new InitGame(game, input.getPlayerOneDecks(),
                    input.getPlayerTwoDecks());
            shuffle(newGame);
            startGame(newGame, input.getGames().get(input.getGames().indexOf(game)).
                    getActions(), output);
        }
    }

    /**
     * @param newGame the game for which the shuffle is made
     */
    public void shuffle(final InitGame newGame) {
        Collections.shuffle(newGame.getPlayer1().getCurrentDeck(), new Random(newGame.getSeed()));
        Collections.shuffle(newGame.getPlayer2().getCurrentDeck(), new Random(newGame.getSeed()));
    }

    /**
     * @param newGame the game about to start
     * @param actions a list of actions to be done
     * @param output the ArrayNode to output game data
     */
    public void startGame(final InitGame newGame, final ArrayList<ActionsInput> actions,
                          final ArrayNode output) {
        int winner = 0;
        int turns = 0;
        int mana = 1;

        Player player1 = newGame.getPlayer1();
        Player player2 = newGame.getPlayer2();

        if (newGame.getStartingPlayer() == 1) {
            player1.setTurn(true);
        } else {
            player2.setTurn(true);
        }

        if (!player1.getCurrentDeck().isEmpty() && !player2.getCurrentDeck().isEmpty()) {
            player1.getCardsInHand().add(player1.getCurrentDeck().get(0));
            player1.getCurrentDeck().remove(0);
            player2.getCardsInHand().add(player2.getCurrentDeck().get(0));
            player2.getCurrentDeck().remove(0);
        }

        for (ActionsInput action : actions) {
            String command = action.getCommand();
            if ("endPlayerTurn".equals(command)) {
                if (winner == 0) {
                    if (player1.getTurn()) {
                        player1.defrost(newGame.getTable());
                        player1.refreshAttackers(newGame.getTable());
                        player1.getHero().setHasAttacked(false);
                        player2.setTurn(true);
                        player1.setTurn(false);
                        turns++;
                    } else {
                        player2.defrost(newGame.getTable());
                        player2.refreshAttackers(newGame.getTable());
                        player2.getHero().setHasAttacked(false);
                        player1.setTurn(true);
                        player2.setTurn(false);
                        turns++;
                    }
                    if (turns == 2) { // New round
                        turns = 0;
                        if (mana < MAX_MANA) {
                            mana++;
                        }
                        player1.setMana(player1.getMana() + mana);
                        player2.setMana(player2.getMana() + mana);

                        if (!player1.getCurrentDeck().isEmpty() && !player2.getCurrentDeck().
                                isEmpty()) {
                            player1.getCardsInHand().add(player1.getCurrentDeck().get(0));
                            player1.getCurrentDeck().remove(0);
                            player2.getCardsInHand().add(player2.getCurrentDeck().get(0));
                            player2.getCurrentDeck().remove(0);
                        }
                    }
                }
            } else if ("getPlayerDeck".equals(command)) {
                if (action.getPlayerIdx() == 1) {
                    Actions.getPlayerDeck(action.getPlayerIdx(), player1, output);
                } else {
                    Actions.getPlayerDeck(action.getPlayerIdx(), player2, output);
                }
            } else if ("getPlayerHero".equals(command)) {
                if (action.getPlayerIdx() == 1) {
                    Actions.getPlayerHero(action.getPlayerIdx(), player1, output);
                } else {
                    Actions.getPlayerHero(action.getPlayerIdx(), player2, output);
                }
            } else if ("getPlayerTurn".equals(command)) {
                Actions.getPlayerTurn(player1, output);
            } else if ("getCardsInHand".equals(command)) {
                if (action.getPlayerIdx() == 1) {
                    Actions.getCardsInHand(action.getPlayerIdx(), player1, output);
                } else {
                    Actions.getCardsInHand(action.getPlayerIdx(), player2, output);
                }
            } else if ("getPlayerMana".equals(command)) {
                if (action.getPlayerIdx() == 1) {
                    Actions.getPlayerMana(action.getPlayerIdx(), player1, output);
                } else {
                    Actions.getPlayerMana(action.getPlayerIdx(), player2, output);
                }
            } else if ("getCardAtPosition".equals(command)) {
                Actions.getCardAtPosition(action.getX(), action.getY(), newGame.getTable(), output);
            } else if ("getFrozenCardsOnTable".equals(command)) {
                Actions.getFrozenCardsOnTable(newGame.getTable(), output);
            } else if ("placeCard".equals(command)) {
                if (winner == 0) {
                    if (player1.getTurn()) {
                        Actions.placeCard(action.getHandIdx(), player1, newGame.getTable(), output);
                    } else {
                        Actions.placeCard(action.getHandIdx(), player2, newGame.getTable(), output);
                    }
                }
            } else if ("getCardsOnTable".equals(command)) {
                Actions.getCardsOnTable(newGame.getTable(), output);
            } else if ("cardUsesAttack".equals(command)) {
                if (winner == 0) {
                    Player attacker, attacked;
                    if (player1.getTurn()) {
                        attacker = newGame.getPlayer1();
                        attacked = newGame.getPlayer2();
                    } else {
                        attacker = newGame.getPlayer2();
                        attacked = newGame.getPlayer1();
                    }
                    Actions.cardUsesAttack(action.getCardAttacker(), action.getCardAttacked(),
                            attacker, attacked, newGame.getTable(), output);
                }
            } else if ("cardUsesAbility".equals(command)) {
                if (winner == 0) {
                    Player attacker, attacked;
                    if (player1.getTurn()) {
                        attacker = newGame.getPlayer1();
                        attacked = newGame.getPlayer2();
                    } else {
                        attacker = newGame.getPlayer2();
                        attacked = newGame.getPlayer1();
                    }
                    Actions.cardUsesAbility(action.getCardAttacker(), action.getCardAttacked(),
                            attacker, attacked, newGame.getTable(), output);
                }
            } else if ("useAttackHero".equals(command)) {
                if (winner == 0) {
                    Player attacker, attacked;
                    if (player1.getTurn()) {
                        attacker = newGame.getPlayer1();
                        attacked = newGame.getPlayer2();
                    } else {
                        attacker = newGame.getPlayer2();
                        attacked = newGame.getPlayer1();
                    }
                    winner = Actions.useAttackHero(action.getCardAttacker(),
                            attacker, attacked, newGame.getTable(), output);
                }
                if (winner == 1) {
                    this.setPlayer1Wins(this.getPlayer1Wins() + 1);
                }
                if (winner == 2) {
                    this.setPlayer2Wins(this.getPlayer2Wins() + 1);
                }
                this.setNrGames(this.getPlayer1Wins() + this.getPlayer2Wins());
            } else if ("useHeroAbility".equals(command)) {
                if (winner == 0) {
                    Player attacker, attacked;
                    if (player1.getTurn()) {
                        attacker = newGame.getPlayer1();
                        attacked = newGame.getPlayer2();
                    } else {
                        attacker = newGame.getPlayer2();
                        attacked = newGame.getPlayer1();
                    }
                    Actions.useHeroAbility(action.getAffectedRow(),
                            attacker, attacked, newGame.getTable(), output);
                }
            } else if ("getPlayerOneWins".equals(command)) {
                Actions.getPlayerOneWins(this.getPlayer1Wins(), output);
            } else if ("getPlayerTwoWins".equals(command)) {
                Actions.getPlayerTwoWins(this.getPlayer2Wins(), output);
            } else if ("getTotalGamesPlayed".equals(command)) {
                Actions.getTotalGamesPlayed(this.getNrGames(), output);
            }
        }
    }

    public int getPlayer1Wins() {
        return player1Wins;
    }

    public int getPlayer2Wins() {
        return player2Wins;
    }

    public int getNrGames() {
        return nrGames;
    }

    public void setPlayer1Wins(final int player1Wins) {
        this.player1Wins = player1Wins;
    }

    public void setNrGames(final int nrGames) {
        this.nrGames = nrGames;
    }

    public void setPlayer2Wins(final int player2Wins) {
        this.player2Wins = player2Wins;
    }
}
