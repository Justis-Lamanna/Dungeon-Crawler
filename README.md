# Mystery-Dungeon
Algorithms project involving the creation of a Mystery Dungeon AI.

Several classes are involved in this project, and getting them straight may be confusing.
* DungeonView - The entry point of the project.
  * Responsible for creating the main frame.
  * Responsible for binding the control keys to their respective actions.
* DungeonComp - A component, used to display the dungeon.
  * Responsible for drawing the dungeon into the frame.
  * Contains the logic for toggling certain aspects of the dungeon, such as entities, nodes, paths, and rooms.
* Dungeon - The actual Dungeon.
  *Responsible for taking a basemap, and:
    1. Determining nodes
    2. Determining paths between nodes
    3. Determining rooms
    4. Determining exterior nodes and just interior nodes, which denote the transition between hallway and room.
  * Spawning enemies, keeping track of enemies, and updating them all.
* Node - Represents a node in the graph.
  * Responsible for keeping track of the node's position and neighbors.
  * Contains a type, which is currently either land or water.
* RoomNode - Represents a grouping of nodes that define a room.
  * Responsible for keeping track of all contained nodes
  * Calculates exterior nodes and just interior nodes, which denote the transition between hallway and room.
* TileOp - Currently, this class is solely used to "pretty" up the basemap, adding walls and decor.
  * Currently, I am dissatisfied with this code, and plan to modify or remove it.
* Species - Keeps track of data for each species. The data contained is identical for all members of one species.
  * Currently contains a name, as well as an image file.
* Entity - Represents a member of a certain species. The data contained may vary between members of one species.
  * Currently contains an EntityState, a current node, and a target node.
* EntityState - An abstract class, which is to be extended for each state. Includes code for determining a path.
* MoveState - A state, which causes an enemy to wander around.
* FollowState - A state, which causes an enemy to move toward the player.
* DirectionAction - A derivative of Action, which is bound to the numpad. It is responsible for moving the player a certain direction.
