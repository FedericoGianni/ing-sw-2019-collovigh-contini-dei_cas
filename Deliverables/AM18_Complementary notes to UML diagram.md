# Complementary notes to UML diagram

## Model

In the MVC design pattern, the model package represents the current state of the game and it should save every information regarding players like their current position in the map, damage taken, ammo, weapons, etc.
We designed a MVC pattern implementing classes to store current game data. Here's a brief explanation of every class and its function:

##### -CurrentGame

This is the main class, and it stores every information regarding the current game. It stores a list of the current Players and their stats like damage taken, weapons, etc. and the actual state of the map like the map type, which depends on the number of players and  the actual position of the players in the map

  1. constructor has to call the getIstance() method of the singleton
  2. the class is a singleton itself

##### -Player

This class stores information regarding the stats of every player, like their damage taken, the number of deaths, their current weapons and ammo.

  1. getCurrentPosition() return the cell where the player is
  2. setPlayerPosition() modify the currentPosition atttribute, requires a Cell type
  3. getPlayerId() returns the player id as an integer
  4. getPlayerName() return the string that identify the player as is name in the current game
  5. getWeapon()/addWeapon()/delWeapon() returns a list of weapon type/add a weapon to the list/ removes one, not more than 3 elements for each player
  6.  getPowerUps()/addPowerUp()/delPowerUp()returns a list of power Ups/add a power up to the list/ removes one, not more than 3 elements for each player
  6. canTraget()/canBeTargeted() returns the list of the players that can be targeted/can target  by/- the current player.
  7. addCube() requires an AmmoCube type and add it to the his ammo type attribute.
  8. blue/red/yellowCubes() returns the number of ammos for each type
  9. numDeaths() returns how many times the current player is died
  10. incrDeaths() increase by 1 the number of deaths of the current player
  10. setDmg() give damages to the player
  11. addMarks() give marks to the player
  12. addScore give points to the player

##### -Map

Represents the game map, it can be of different types, based on the number of players in the game. The actual Map is implemented as a group of Cells, which can be normal or Spawn Cells (the only difference is that spawn cells will contain 3 weapons which can be bought by Players inside them).

  1. is a Singleton
  2. getPlacedWeapons() return a COPY of the list in the specified SpawnCell ( NB: the use of SpawnCell instead of the upper class Cell is preferred as it eliminates the need for a check )
  3. discardPowerUp() takes as an intake the discarded card and add it to the thrashPowerUpDeck  --> discardPowerUp action need to be forwarded to here
  4. if getPowerUp() fins out the deck is empty -> swap the powerUpDeck w/ the thrashPowerUpDeck (it changes the pointers ) and shuffles

##### -Cell

  1. getAmmoPlaced() -> just returns a COPY of the AmmoCard stored inside the Cell
  2. pickAmmoPlaced() -> return the link to the stored AmmoCard and generate a new one
  3. getColor() return the enum equivalence of the color
  4. getNorth/South/East/West() returns the reference of the closest Cells, is null if it's a wall

##### -SpawnCell

  1. getWeapons() returns a COPY of the local weapon list
  2. buy() returns false if the weapon can not be bought ( can be turned in an exception) chooses between the local list of (max 3) weapons and buy one(if )

##### -AmmoCard

  1. getRandCard() Represents the ammo cards which are placed within the game map, they can be 3 ammo cubes of different colors, or 2 ammo cubes and one powerup.
  We aim to implement a function that generates this cards randomly, based on the probability of color combinations and the presence of powerups, by looking at the real ammo deck of the board game.

##### -AmmoCube

  1. Ammocubes is an ammo type. Each one represents a different type/color of ammunition.

##### -PowerUp

  1. sell() give in this powerUp and get the equal amount of AmmoCubes
  2. canBeUsedNow() return true if the player can use this powerUp at moment of the call

##### -PowerUpDeck

  1. reinsert() requires a powerUp and push it back in the current deck

##### -Deck

 1. interface of every card type
 2. getRandomCard() returns a random card from the requested type deck

##### -WeaponDeck

  1. list of weapons , randomly ordered every match

##### -Weapon

  1. isLoaded() returns true if the weapon is loaded and ready
  2. canBeReloaded() returns true if the weapon can be reloaded
  3. reload() reaload the weapon, spendes the prefixed ammocubes and set true the isLoaded attribute
  4. getCost() returns the total cost of the weapon

##### -FireMode

  1. This class represents our idea of handling the differences between weapons and their effects
  2. We don't enhance this part because it could be heavily changed during the implementation
  3. we tought that a Gson file could work perfectly for saving the weapons stats and get loaded at the beginning of every match, this class handles the eventually Gson file.

##### -Model (Class)

  1. This class will handle all the communications with the others packages
  2. We tought methods useful for communications but pratically we will work on these parts of the project for the next deadline
