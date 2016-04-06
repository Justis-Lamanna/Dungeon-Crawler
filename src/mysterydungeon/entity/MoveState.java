/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.entity;

import java.util.ArrayList;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;
import mysterydungeon.dungeon.RoomNode;

/**
 * A state which meanders around the dungeon until the player is found.
 * @author Justis
 */
public class MoveState extends EntityState
{
    private Node targetNode = null;

    /**
     * A constant representing the range at which this entity should begin to attack.
     */
    public static final int RANGE = 5; //If the player is this many nodes away, switch to the attack.

    /**
     * Generates the behavior of this entity.
     * More specifically, it follows hallways, and turns at random on junctions.
     * In rooms, it travels to a randomly-chosen exit.
     * @param e The entity this state is attached to.
     * @param d The dungeon this entity resides in.
     */
    @Override
    public void doState(SpeciesEntity e, Dungeon d)
    {
        Node current = e.getCurrentNode();
        if(d.getRoom(current) != null)
        {
            if(targetNode == null)
            {
                //Pick a target exit, not the same as what we just left.
                RoomNode currentRoom = d.getRoom(current);
                Node randomExteriorNode;
                Node randomInteriorNode;
                if(currentRoom.getExteriorNodes().size() > 1)
                {
                    while(true)
                    {
                        int randomPosition = (int)(Math.random() * currentRoom.getExteriorNodes().size());
                        randomExteriorNode = currentRoom.getExteriorNodes().get(randomPosition);
                        randomInteriorNode = currentRoom.getJustInteriorNodes().get(randomPosition);
                        if(!randomInteriorNode.equals(current)){break;}
                    }
                    targetNode = randomExteriorNode;
                }
                else
                {
                    targetNode = currentRoom.getExteriorNodes().get(0);
                }
            }
            Node nextNode = nextNode(e, d, current, targetNode);
            e.facing = getDirection(current, nextNode);
            e.setDestinationNode(nextNode);
        }
        else
        {
            //You're on a pathway. Keep going the same way. If you're at an intersection, pick one at random.
            targetNode = null;
            ArrayList<Node> candidateNodes = new ArrayList<>();
            for(int dir = 0; dir < 8; dir++)
            {
                if(current.getPath(dir) != null && !isOccupied(e, d, current.getPath(dir)))
                {
                        candidateNodes.add(current.getPath(dir));
                }
            }
            if(candidateNodes.size() > 1)
            {
                Node exclude = current.getPath((e.facing + 4) % 8); //The opposite direction.
                Node randomNode;
                while(true)
                {
                        int randomPosition = (int)(Math.random() * candidateNodes.size());
                        randomNode = candidateNodes.get(randomPosition);
                        if(!randomNode.equals(exclude)){break;}
                }
                e.facing = getDirection(current, randomNode);
                e.setDestinationNode(randomNode);
            }
            else if(candidateNodes.size() == 1)
            {
                Node next = candidateNodes.get(0);
                e.facing = getDirection(current, next);
                e.setDestinationNode(next);
            }
        }
        pickupItem(d, e);
        if(playerNearby(d, e.getCurrentNode(), RANGE))
        {
            e.setState(new FollowState());
        }
    }

    /**
     * Returns an integer representing this state.
     * @return The constant integer 0.
     */
    @Override
    public int isState()
    {
        return 0;
    }
}
