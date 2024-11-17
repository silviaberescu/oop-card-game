package org.poo.game;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.cards.Hero;
import org.poo.cards.Minion;
import org.poo.fileio.Coordinates;

import java.util.ArrayList;

public final class Actions {
    static final int MAXIDXLINES = 3;
    static final int MAXROWS = 5;

    private Actions() {
    }

    /**
     * @param playerIdx the index of the player whose deck is being retrieved
     * @param player the object representing the player
     * @param output the ArrayNode to which the JSON object will be added
     */

    public static void getPlayerDeck(final int playerIdx, final Player player,
                                     final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerDeck");
        node.put("playerIdx", playerIdx);

        ArrayNode deckArray = JsonNodeFactory.instance.arrayNode();

        for (Minion minion : player.getCurrentDeck()) {
            ObjectNode minionNode = createMinionJNode(minion);
            deckArray.add(minionNode);
        }

        node.set("output", deckArray);
        output.add(node);
    }
    /**
     * @param playerIdx the index of the player whose hero is being retrieved
     * @param player the object representing the player
     * @param output the ArrayNode to which the hero details are added
     */
    public static void getPlayerHero(final int playerIdx, final Player player,
                                     final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerHero");
        node.put("playerIdx", playerIdx);

        ObjectNode heroNode = JsonNodeFactory.instance.objectNode();
        Hero hero = player.getHero();

        heroNode.put("mana", hero.getMana());
        heroNode.put("description", hero.getDescription());
        heroNode.put("name", hero.getName());
        heroNode.put("health", hero.getHealth());

        ArrayNode colorsArray = JsonNodeFactory.instance.arrayNode();

        for (String color : hero.getColors()) {
            colorsArray.add(color);
        }
        heroNode.set("colors", colorsArray);

        node.set("output", heroNode);

        output.add(node);
    }

    /**
     * @param player the object representing the player
     * @param output the ArrayNode to which player turn is added
     */
    public static void getPlayerTurn(final Player player, final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerTurn");
        int idx = 2;
        if (player.getFront() == 2) {
            idx = 1;
        }
        if (player.getTurn()) {
            node.put("output", idx);
        } else {
            node.put("output", (MAXIDXLINES - idx));
        }
        output.add(node);
    }
    /**
     * @param playerIdx the index of the player whose mana is being retrieved
     * @param player the object representing the player
     * @param output the ArrayNode to which the mana is added
     */
    public static void getPlayerMana(final int playerIdx, final Player player,
                                     final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerMana");
        node.put("playerIdx", playerIdx);
        node.put("output", player.getMana());
        output.add(node);
    }

    /**
     * @param playerIdx the index of the player whose cardsInHand are being retrieved
     * @param player the object representing the player
     * @param output the ArrayNode to which the cardsInHand are added
     */
    public static void getCardsInHand(final int playerIdx, final Player player,
                                      final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getCardsInHand");
        node.put("playerIdx", playerIdx);

        ArrayNode handArray = JsonNodeFactory.instance.arrayNode();

        for (Minion minion : player.getCardsInHand()) {
            ObjectNode minionNode = createMinionJNode(minion);
            handArray.add(minionNode);
        }

        node.set("output", handArray);
        output.add(node);
    }

    /**
     * @param x the row index on the table
     * @param y the column index on the table
     * @param table the array representing the table where cards are placed
     * @param output the ArrayNode to which the card details or an error message is added
     */
    public static void getCardAtPosition(final int x, final int y, final ArrayList<Minion>[] table,
                                         final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getCardAtPosition");
        node.put("x", x);
        node.put("y", y);

        if (x >= 0 && x <= MAXIDXLINES && y > table[x].size() - 1) {
            node.put("output", "No card available at that position.");
        } else {
            ObjectNode minionNode = createMinionJNode(table[x].get(y));
            node.set("output", minionNode);
        }
        output.add(node);
    }


    /**
     * @param table the array representing the table where cards are placed
     * @param output the ArrayNode to which the frozen card details are added
     */
    public static void getFrozenCardsOnTable(final ArrayList<Minion>[] table,
                                             final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getFrozenCardsOnTable");

        ArrayNode frozenCards = JsonNodeFactory.instance.arrayNode();

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                for (Minion minion : table[i]) {
                    if (minion.getFrozen()) {
                        ObjectNode minionNode = createMinionJNode(minion);
                        frozenCards.add(minionNode);
                    }
                }
            }
        }
        node.set("output", frozenCards);
        output.add(node);
    }


    /**
     * @param handIdx the index of the card in the player's hand to be placed on the table
     * @param player the player placing the card
     * @param table the array representing the table
     * @param output the ArrayNode to which errors are added
     */
    public static void placeCard(final int handIdx, final Player player,
                                 final ArrayList<Minion>[] table, final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "placeCard");

        if (player.getCardsInHand().size() > handIdx
                && player.getCardsInHand().get(handIdx).getMana() > player.getMana()) {
            node.put("error", "Not enough mana to place card on table.");
            node.put("handIdx", handIdx);
            output.addPOJO(node);
        } else {
            if (player.getFront() == 2) {
                int row = player.getCardsInHand().get(handIdx).getFront() ? 2 : MAXIDXLINES;
                if (table[row].size() == MAXROWS) {
                    node.put("error", "Cannot place card on table since row is full.");
                    node.put("handIdx", handIdx);
                    output.addPOJO(node);
                } else {
                    table[row].add(player.getCardsInHand().get(handIdx));
                    player.setMana(player.getMana() - player.getCardsInHand().
                            get(handIdx).getMana());
                    player.getCardsInHand().remove(handIdx);
                }
            } else { //player front == 1
                if (player.getCardsInHand().size() > handIdx) {
                    int row = player.getCardsInHand().get(handIdx).getFront() ? 1 : 0;
                    if (table[row].size() == MAXROWS) {
                        node.put("error", "Cannot place card on table since row is full.");
                        node.put("handIdx", handIdx);
                        output.addPOJO(node);
                    } else {
                        table[row].add(player.getCardsInHand().get(handIdx));
                        player.setMana(player.getMana() - player.getCardsInHand().
                                get(handIdx).getMana());
                        player.getCardsInHand().remove(handIdx);
                    }
                }
            }
        }
    }
    /**
     * @param table the array representing the game table
     * @param output the ArrayNode to which the resulting cards on the table are added
     */
    public static void getCardsOnTable(final ArrayList<Minion>[] table, final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getCardsOnTable");

        ArrayNode cardsOnTable = JsonNodeFactory.instance.arrayNode();

        for (ArrayList<Minion> row : table) {
            ArrayNode cardsOnCurrentRow = JsonNodeFactory.instance.arrayNode();

            for (Minion minion : row) {
                ObjectNode minionNode = createMinionJNode(minion);
                cardsOnCurrentRow.add(minionNode);
            }

            cardsOnTable.add(cardsOnCurrentRow);
        }
        node.set("output", cardsOnTable);
        output.add(node);
    }

    /**
     * @param minion the object to be converted into a JSON node
     */
    private static ObjectNode createMinionJNode(final Minion minion) {
        ObjectNode minionNode = JsonNodeFactory.instance.objectNode();
        minionNode.put("mana", minion.getMana());
        minionNode.put("attackDamage", minion.getAttackDamage());
        minionNode.put("health", minion.getHealth());
        minionNode.put("description", minion.getDescription());

        ArrayNode colorsArray = JsonNodeFactory.instance.arrayNode();
        minion.getColors().forEach(colorsArray::add);
        minionNode.set("colors", colorsArray);

        minionNode.put("name", minion.getName());
        return minionNode;
    }


    /**
     * @param cardAttacker the coordinates of the attacking card
     * @param cardAttacked the coordinates of the attacked card
     * @param attacker the player that attacks
     * @param attacked player being attacked
     * @param table the game table
     * @param output an ArrayNode to store the result of the attack
     */
    public static void cardUsesAttack(final Coordinates cardAttacker, final Coordinates
            cardAttacked, final Player attacker, final Player attacked, final
            ArrayList<Minion>[] table, final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "cardUsesAttack");
        node.putPOJO("cardAttacker", cardAttacker);
        node.putPOJO("cardAttacked", cardAttacked);

        boolean existsTank = false;
        for (Minion minion : table[attacked.getFront()]) {
            if (minion.isTank()) {
                existsTank = true;
                break;
            }
        }
        if (cardAttacked != null && cardAttacker != null && cardAttacked.getX() >= 0
                && cardAttacked.getX() < table.length && cardAttacked.getY() >= 0
                && cardAttacked.getY() < table[cardAttacked.getX()].size()) {
            if (cardAttacked.getX() == attacker.getFront() || cardAttacked.getX()
                    == attacker.getBack()) {
                node.put("error", "Attacked card does not belong to the enemy.");
                output.addPOJO(node);
            } else if (table[cardAttacker.getX()].get(cardAttacker.getY()).getHasAttacked()) {
                node.put("error", "Attacker card has already attacked this turn.");
                output.addPOJO(node);
            } else if (table[cardAttacker.getX()].get(cardAttacker.getY()).getFrozen()) {
                node.put("error", "Attacker card is frozen.");
                output.addPOJO(node);
            } else if (existsTank && !table[cardAttacked.getX()].
                    get(cardAttacked.getY()).isTank()) {
                node.put("error", "Attacked card is not of type 'Tank'.");
                output.addPOJO(node);
            } else {
                table[cardAttacker.getX()].get(cardAttacker.getY()).setHasAttacked(true);
                table[cardAttacked.getX()].get(cardAttacked.getY()).setHealth(table[cardAttacked.
                        getX()].get(cardAttacked.getY()).getHealth() - table[cardAttacker.getX()].
                        get(cardAttacker.getY()).getAttackDamage());
                if (table[cardAttacked.getX()].get(cardAttacked.getY()).getHealth() <= 0) {
                    table[cardAttacked.getX()].remove(cardAttacked.getY());
                }
            }
        }
    }

    /**
     * @param cardAttacker the coordinates of the attacking card
     * @param cardAttacked the coordinates of the attacked card
     * @param attacker the player that attacks
     * @param attacked player being attacked
     * @param table the game table
     * @param output an ArrayNode to store the result of the attack
     */
    public static void cardUsesAbility(final Coordinates cardAttacker, final Coordinates
            cardAttacked, final Player attacker, final Player attacked, final
            ArrayList<Minion>[] table, final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "cardUsesAbility");
        node.putPOJO("cardAttacker", cardAttacker);
        node.putPOJO("cardAttacked", cardAttacked);

        if (table[cardAttacker.getX()].get(cardAttacker.getY()).getName().
                compareTo("Disciple") == 0) {
            if (cardAttacked.getX() != attacker.getFront() && cardAttacked.getX()
                    != attacker.getBack()) {
                node.put("error", "Attacked card does not belong to the current player.");
                output.addPOJO(node);
                return;
            }
        } else {
            if (cardAttacked.getX() == attacker.getFront() || cardAttacked.getX()
                    == attacker.getBack()) {
                node.put("error", "Attacked card does not belong to the enemy.");
                output.addPOJO(node);
                return;
            }
        }
        if (table[cardAttacker.getX()].get(cardAttacker.getY()).getHasAttacked()) {
            node.put("error", "Attacker card has already attacked this turn.");
            output.addPOJO(node);
            return;
        }
        if (table[cardAttacker.getX()].get(cardAttacker.getY()).getFrozen()) {
            node.put("error", "Attacker card is frozen.");
            output.addPOJO(node);
            return;
        }

        if (table[cardAttacker.getX()].get(cardAttacker.getY()).getName().compareTo("Disciple")
                != 0) {
            boolean existsTank = false;
            for (Minion minion : table[attacked.getFront()]) {
                if (minion.isTank()) {
                    existsTank = true;
                    break;
                }
            }

            if (existsTank && !table[cardAttacked.getX()].get(cardAttacked.getY()).isTank()) {
                node.put("error", "Attacked card is not of type 'Tank'.");
                output.addPOJO(node);
                return;
            }
        }
        if (table[cardAttacker.getX()].get(cardAttacker.getY()).getName().
                compareTo("The Ripper") == 0) {
            table[cardAttacker.getX()].get(cardAttacker.getY()).
                    useAbility(table[cardAttacked.getX()].get(cardAttacked.getY()));
        } else if (table[cardAttacker.getX()].get(cardAttacker.getY()).getName().
                compareTo("Miraj") == 0) {
            table[cardAttacker.getX()].get(cardAttacker.getY()).
                    useAbility(table[cardAttacked.getX()].get(cardAttacked.getY()));
        } else if (table[cardAttacker.getX()].get(cardAttacker.getY()).getName().
                compareTo("The Cursed One") == 0) {
            (table[cardAttacker.getX()].get(cardAttacker.getY())).
                    useAbility(table[cardAttacked.getX()].get(cardAttacked.getY()));
            if (table[cardAttacked.getX()].get(cardAttacked.getY()).getHealth() == 0) {
                table[cardAttacked.getX()].remove(cardAttacked.getY());
            }
        } else {
            table[cardAttacker.getX()].get(cardAttacker.getY()).
                    useAbility(table[cardAttacked.getX()].get(cardAttacked.getY()));
        }
        table[cardAttacker.getX()].get(cardAttacker.getY()).setHasAttacked(true);
    }

    /**
     * @param cardAttacker the coordinates of the attacking card
     * @param attacker the player attacker
     * @param attacked the player being attacked
     * @param table the game table
     * @param output an ArrayNode to record the results of the attack
     */
    public static int useAttackHero(final Coordinates cardAttacker, final Player attacker,
                                    final Player attacked, final ArrayList<Minion>[] table,
                                    final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        boolean existsTank = false;
        for (Minion minion : table[attacked.getFront()]) {
            if (minion.isTank()) {
                existsTank = true;
                break;
            }
        }

        int winner = 0;

        if (table[cardAttacker.getX()].get(cardAttacker.getY()).getFrozen()) {
            node.put("command", "useAttackHero");
            node.putPOJO("cardAttacker", cardAttacker);
            node.put("error", "Attacker card is frozen.");
            output.addPOJO(node);
        } else if (table[cardAttacker.getX()].get(cardAttacker.getY()).getHasAttacked()) {
            node.put("command", "useAttackHero");
            node.putPOJO("cardAttacker", cardAttacker);
            node.put("error", "Attacker card has already attacked this turn.");
            output.addPOJO(node);
        } else if (existsTank) {
            node.put("command", "useAttackHero");
            node.putPOJO("cardAttacker", cardAttacker);
            node.put("error", "Attacked card is not of type 'Tank'.");
            output.addPOJO(node);
        } else {
            attacked.getHero().setHealth(attacked.getHero().getHealth() - table[cardAttacker.
                    getX()].get(cardAttacker.getY()).getAttackDamage());
            table[cardAttacker.getX()].get(cardAttacker.getY()).setHasAttacked(true);

            if (attacked.getHero().getHealth() <= 0) {
                if (attacker.getFront() == 2) {
                    node.put("gameEnded", "Player one killed the enemy hero.");
                    winner = 1;
                } else {
                    node.put("gameEnded", "Player two killed the enemy hero.");
                    winner = 2;
                }
                output.addPOJO(node);
            }
        }
        return winner;
    }

    /**
     * @param affectedRow the coordinate of the affected row
     * @param attacker the player attacker
     * @param attacked the player being attacked
     * @param table the game table
     * @param output an ArrayNode to record the results of the attack
     */
    public static void useHeroAbility(final int affectedRow, final Player attacker,
                                      final Player attacked, final ArrayList<Minion>[] table,
                                      final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        if (attacker.getMana() < attacker.getHero().getMana()) {
            node.put("command", "useHeroAbility");
            node.put("affectedRow", affectedRow);
            node.put("error", "Not enough mana to use hero's ability.");
            output.addPOJO(node);
        } else if (attacker.getHero().getHasAttacked()) {
            node.put("command", "useHeroAbility");
            node.put("affectedRow", affectedRow);
            node.put("error", "Hero has already attacked this turn.");
            output.addPOJO(node);
        } else if ((attacker.getHero().getName().compareTo("Lord Royce") == 0 || attacker.
                getHero().getName().compareTo("Empress Thorina") == 0) && (affectedRow
                != attacked.getFront() && affectedRow != attacked.getBack())) {
            node.put("command", "useHeroAbility");
            node.put("affectedRow", affectedRow);
            node.put("error", "Selected row does not belong to the enemy.");
            output.addPOJO(node);
        } else if ((attacker.getHero().getName().compareTo("General Kocioraw") == 0
                || attacker.getHero().getName().compareTo("King Mudface") == 0)
                && (affectedRow != attacker.getFront() && affectedRow != attacker.getBack())) {
            node.put("command", "useHeroAbility");
            node.put("affectedRow", affectedRow);
            node.put("error", "Selected row does not belong to the current player.");
            output.addPOJO(node);
        } else {
            (attacker.getHero()).heroAbility(table, affectedRow);
            attacker.setMana(attacker.getMana() - attacker.getHero().getMana());
            attacker.getHero().setHasAttacked(true);
        }
    }

    /**
     * @param playerOneWins the number of wins for player one
     * @param output an ArrayNode to store the results
     */
    public static void getPlayerOneWins(final int playerOneWins, final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerOneWins");
        node.put("output", playerOneWins);
        output.addPOJO(node);
    }
    /**
     * @param playerTwoWins the number of wins for player two
     * @param output an ArrayNode to store the results
     */
    public static void getPlayerTwoWins(final int playerTwoWins, final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerTwoWins");
        node.put("output", playerTwoWins);
        output.addPOJO(node);
    }
    /**
     * @param totalGamesPlayed the number of games
     * @param output an ArrayNode to store the results
     */
    public static void getTotalGamesPlayed(final int totalGamesPlayed, final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getTotalGamesPlayed");
        node.put("output", totalGamesPlayed);
        output.addPOJO(node);
    }
}
