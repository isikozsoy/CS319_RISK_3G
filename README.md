# CS319-03 Group G
As a CS 319 Term Project , we will implement the RISK boardgame.

<h3>Group Members: </h3>
<p> • Işık Özsoy 21703160 </p>
<p> • Mustafa Hakan Kara 21703317 </p>
<p> • Defne Betül Çiftci 21802635 </p>
<p> • Alp Üneri 21802481 </p>
<p> • Burak Yetiştiren 21802608 </p>

***************************************************************************************************************************************************
***************************************************************************************************************************************************
***************************************************************************************************************************************************

Risk is a strategy board game aimed for two to six players of diplomacy, conflict, and conquest.
The standard version of the game consists of a board in which a map of the world is depicted. The
map is divided into forty-two territories which are grouped into six continents. Turns rotate among
players in control of armies with which they attempt to capture territories from other players via battles
whose results are determined by dice rolls. The number of dice a player is able to attack or defend with is
dependent on the number of soldiers that player controls in the applicable territories. The classic goal of
the game is to occupy every territory on the board and achieve total global domination. Both formal and
informal alliances may be formed during the course of the game.

<p> Our project is based on RISK Global Domination, however some certain features will be added/removed, such as: </p>
<p> • We plan on allowing players to be able to leave territories in their control unguarded should they choose to. </p>
<p> • We plan on allowing players to set the goal of capturing a continent before the game starts and receive rewards should they be able to do so. </p>
<p> • We plan on implementing a rock-paper-scissors based combat system where more skill is involved as opposed to dice rolls. </p>
<p> • We plan on implementing an alliance mechanism where the two players allied will be allowed to go through their ally's territories to attack an opponent's territory. The allies will not be able to attack each other for the duration of their alliance. This implementation will not be available if only 2 players are playing. </p>
<p> • We plan on implementing an airplane mechanism. A player will be able to build an airport on his territories by paying a cost. </p>

<b>Explanations</b>

• In the original game, players are required to leave at least one unit in any territory that they are in control of. We
plan on allowing players the ability to leave territories they currently control unguarded so as to move the soldiers on 
that territory to any other connected territory that they control. This would make it so that the territory previously controlled
by the player is left empty, such that any other player with a connected territory can capture the empty one without any 
battle required.

• We plan on having the players be able to declare a target continent that they will aim to capture before territories
are dealt and the game begins. This would make it so should that player be able to actually conquer that continent they
will receive some additional rewards in addition to the rewards they would have received for simply conquering that continent.


• We are planning on implementing a combat system whose results are determined by something more skill based such as rock-paper-scissors
as opposed to just dice rolls. This could serve to eliminate the frustration caused by unlucky dice rolls and add another layer
of complexity/depth to the game.

• In the original game, there is no official system of alliances but the player can form alliances nonetheless. One plan of ours is to implement an official system of alliances, which will not be available when only 2 players play, which will benefit the user in terms of reaching territories of their allies and performing an attack on an opponent through their ally's territory, potentially creating an environment where more strategies are supposed to be formed. The alliances will not be able to attack each other (as they are able to in the original game) nor will they be able to stay as allies for the entire duration of the game (as this would make the game end with no winners). This new implementation could add more complexity and intrigue to the game as well as make the players compose more strategies with more possible scenarios and interact with the other players more throughout this whole process.

• We also plan on having the players be able to attack non-neighboring territories by building an airport on their territories by a cost of 5 soldiers (could be changed later on). By attacking through a plane, a player will be able to attack a territory which is up to 3 territories away (this detail could be changed as well).
