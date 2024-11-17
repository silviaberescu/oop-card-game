package org.poo.game;

import org.poo.cards.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.Coordinates;

import java.util.ArrayList;

public class Actions {

    public static void getPlayerDeck(int playerIdx, Player player, ArrayNode output) {
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

    public static void getPlayerHero(int playerIdx, Player player, ArrayNode output){
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

    public static void getPlayerTurn(Player player, ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerTurn");
        int idx = 2;
        if(player.getFront() == 2)
            idx = 1;
        if(player.getTurn()){
            node.put("output", idx);
        } else {
            node.put("output", (3 - idx));
        }
        output.add(node);
    }

    public static void getPlayerMana(int playerIdx, Player player,
                                     final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerMana");
        node.put("playerIdx", playerIdx);
        node.put("output", player.getMana());
        output.add(node);
    }

    public static void getCardsInHand(int playerIdx, Player player, ArrayNode output) {
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

    public static void getCardAtPosition(int x, final int y, ArrayList<Minion>[] table,
                                         ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getCardAtPosition");
        node.put("x", x);
        node.put("y", y);

        if (x >= 0 && x <= 3 && y > table[x].size() - 1) {
            node.put("output", "No card available at that position.");
        } else {
            ObjectNode minionNode = createMinionJNode(table[x].get(y));
            node.set("output", minionNode);
        }
        output.add(node);
    }

    public static void getFrozenCardsOnTable(ArrayList<Minion>[] table, ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getFrozenCardsOnTable");

        ArrayNode frozenCards = JsonNodeFactory.instance.arrayNode();

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null){
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

    public static void placeCard(int handIdx, Player player, ArrayList<Minion>[] table, ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "placeCard");

        if (player.getCardsInHand().size() > handIdx && player.getCardsInHand().get(handIdx).getMana() > player.getMana()) {
            node.put("error", "Not enough mana to place card on table.");
            node.put("handIdx", handIdx);
            output.addPOJO(node);
        } else {
            if (player.getFront() == 2) {
                int row = player.getCardsInHand().get(handIdx).getFront() ? 2 : 3;
                if (table[row].size() == 5) {
                    node.put("error", "Cannot place card on table since row is full.");
                    node.put("handIdx", handIdx);
                    output.addPOJO(node);
                } else {
                    table[row].add(player.getCardsInHand().get(handIdx));
                    player.setMana(player.getMana() - player.getCardsInHand().get(handIdx).getMana());
                    player.getCardsInHand().remove(handIdx);
                }
            } else { //player front == 1
                if(player.getCardsInHand().size() > handIdx){
                    int row = player.getCardsInHand().get(handIdx).getFront() ? 1 : 0;
                    if (table[row].size() == 5) {
                        node.put("error", "Cannot place card on table since row is full.");
                        node.put("handIdx", handIdx);
                        output.addPOJO(node);
                    } else {
                        table[row].add(player.getCardsInHand().get(handIdx));
                        player.setMana(player.getMana() - player.getCardsInHand().get(handIdx).getMana());
                        player.getCardsInHand().remove(handIdx);
                    }
                }
            }
        }
    }
    public static void getCardsOnTable(ArrayList<Minion>[] table, ArrayNode output) {
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
    private static ObjectNode createMinionJNode(Minion minion) {
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

    public static void cardUsesAttack(Coordinates cardAttacker, Coordinates cardAttacked,
                                      Player attacker, Player attacked, ArrayList<Minion>[] table,
                                      ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "cardUsesAttack");
        node.putPOJO("cardAttacker", cardAttacker);
        node.putPOJO("cardAttacked", cardAttacked);

        boolean existsTank = false;
        for(Minion minion : table[attacked.getFront()]){
            if (minion.isTank()) {
                existsTank = true;
                break;
            }
        }
        if(cardAttacked != null && cardAttacker != null && cardAttacked.getX() >= 0 && cardAttacked.getX()
                < table.length && cardAttacked.getY() >= 0 && cardAttacked.getY()
                < table[cardAttacked.getX()].size()){
            if (cardAttacked.getX() == attacker.getFront() || cardAttacked.getX() == attacker.getBack()) {
                node.put("error", "Attacked card does not belong to the enemy.");
                output.addPOJO(node);
            } else if (table[cardAttacker.getX()].get(cardAttacker.getY()).getHasAttacked()) {
                node.put("error", "Attacker card has already attacked this turn.");
                output.addPOJO(node);
            } else if (table[cardAttacker.getX()].get(cardAttacker.getY()).getFrozen()) {
                node.put("error", "Attacker card is frozen.");
                output.addPOJO(node);
            } else if (existsTank && !table[cardAttacked.getX()].get(cardAttacked.getY()).isTank()) {
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

    public static void cardUsesAbility(Coordinates cardAttacker, Coordinates cardAttacked,
                                       Player attacker, Player attacked,
                                       ArrayList<Minion>[] table, ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "cardUsesAbility");
        node.putPOJO("cardAttacker", cardAttacker);
        node.putPOJO("cardAttacked", cardAttacked);

//        if (table[cardAttacker.getX()].get(cardAttacker.getY()).getFrozen()) {
//            node.put("error", "Attacker card is frozen.");
//            output.addPOJO(node);
//            return;
//        }
//        if (table[cardAttacker.getX()].get(cardAttacker.getY()).getHasAttacked()) {
//            node.put("error", "Attacker card has already attacked this turn.");
//            output.addPOJO(node);
//            return;
//        }
        if (table[cardAttacker.getX()].get(cardAttacker.getY()).getName().compareTo("Disciple") == 0) {
            if (cardAttacked.getX() != attacker.getFront() && cardAttacked.getX() != attacker.getBack()) {
                node.put("error", "Attacked card does not belong to the current player.");
                output.addPOJO(node);
                return;
            }
        } else {
            if (cardAttacked.getX() == attacker.getFront() || cardAttacked.getX() == attacker.getBack()) {
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
            for(Minion minion : table[attacked.getFront()]){
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
        if (table[cardAttacker.getX()].get(cardAttacker.getY()).getName().compareTo("The Ripper") == 0) {
            ((TheRipper)table[cardAttacker.getX()].get(cardAttacker.getY())).useAbility(table[cardAttacked.getX()].get(cardAttacked.getY()));
        } else if (table[cardAttacker.getX()].get(cardAttacker.getY()).getName().compareTo("Miraj") == 0) {
            table[cardAttacker.getX()].get(cardAttacker.getY()).useAbility(table[cardAttacked.getX()].get(cardAttacked.getY()));
        } else if (table[cardAttacker.getX()].get(cardAttacker.getY()).getName().compareTo("The Cursed One") == 0) {
            (table[cardAttacker.getX()].get(cardAttacker.getY())).useAbility(table[cardAttacked.getX()].get(cardAttacked.getY()));
            if (table[cardAttacked.getX()].get(cardAttacked.getY()).getHealth() == 0) {
                table[cardAttacked.getX()].remove(cardAttacked.getY());
            }
        } else {
            table[cardAttacker.getX()].get(cardAttacker.getY()).useAbility(table[cardAttacked.getX()].get(cardAttacked.getY()));
        }
        table[cardAttacker.getX()].get(cardAttacker.getY()).setHasAttacked(true);
    }
    public static int useAttackHero(Coordinates cardAttacker, Player attacker, Player attacked,
                                    ArrayList<Minion>[] table, ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        boolean existsTank = false;
        for(Minion minion : table[attacked.getFront()]){
            if (minion.isTank()) {
                existsTank = true;
                break;
            }
        }
        boolean hasTank = false;

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

    public static void useHeroAbility(int affectedRow, Player attacker, Player attacked,
                                      ArrayList<Minion>[] table, ArrayNode output) {
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

    public static void getPlayerOneWins(int playerOneWins, final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerOneWins");
        node.put("output", playerOneWins);
        output.addPOJO(node);
    }

    public static void getPlayerTwoWins(int playerTwoWins, final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerTwoWins");
        node.put("output", playerTwoWins);
        output.addPOJO(node);
    }
    public static void getTotalGamesPlayed(final int totalGamesPlayed, final ArrayNode output) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getTotalGamesPlayed");
        node.put("output", totalGamesPlayed);
        output.addPOJO(node);
    }
}
