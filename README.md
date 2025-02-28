#OOP project - Card game

The implementation is done using two main packages:

1. cards - which contains the classes that represent all card types:
- Card is the most generic card type class that defines common attributes and methods for all card types. It serves as 
a starting point for other card classes like Minions and Heroes.
- Minion is a subclass of class Card, and stores more information about the card, like its health, attack damage, if it 
is frozen, of type tank, if it has to be placed on the front or back row.
- Decks manages information about multiple decks of cards and is used when needed to stock the decks for each player 
given at the beginning of games.
- Hero is a subclass of type Card that has information about the health of a hero and a method which is later 
implemented for each type of hero.
-Disciple, Miraj, TheCursedOne, TheRipper are a special type of Minion card which implement a unique ability, specific 
to each card.
-EmpressThorina, GeneralKocioraw, LordRoyce, KingMudface are classes which implement specific abilities for each hero.

  
2. game - which contains classes related to game logic and flow.
- Actions is a class that implements static methods for all the actions that can be received at input.
- InitGame is responsible for initializing the game. It sets up the players, their decks, the game seed value and 
determines which player will start. It also initializes the game table, where each list corresponds to a row on the 
table.
- Game manages the simulation of multiple games, based on a series of input actions. It initializes the games and 
creates a new InitGame object for each game, shuffles the decks of both players, and then starts the game. The 
startGame method simulates the gameplay of each game based on a list of actions. When a player's turn ends, it 
refreshes the hasAttacked and frozen status and updates the player's mana, drawing cards after a round. The class 
tracks the number of games played, as well as how many wins each player has.
- Player manages the data for each player in the game, such as their hero, decks, current deck, cards in hand, mana, and 
turn status. It also handles specific actions like unfreezing minions and resetting attack states at the end of a 
turn.

The game begins by reading the information needed for a series of games from an input file. When an instance of Games 
is created, it initializes win counters and starts tracking the number of games. It processes a list of games from the 
input data and initializes each game by calling InitGame. For each game, it shuffles the players' decks and starts the game. The startGame() method is where the actual game logic happens. It handles the
game's main loop, processing each action in the sequence provided by the input. The game proceeds with alternating turns
between the two players. The corresponding logic for each action is handled by the Actions class, which applies the
appropriate changes to the game state. Players alternate turns, based on the actions received. 
The game continues until a winner is determined, by eliminating the opponent's hero. It keeps track of the number of 
games played and the wins for each player.
  
