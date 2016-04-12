/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import mysterydungeon.MysteryDungeon;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;
import mysterydungeon.item.Item;
import mysterydungeon.item.LightItem;

/**
 * An abstract class, for use in defining states. Many helpful functions are
 * included in this superclass, such as determining the quickest path between
 * two nodes. A state, in this game, defines how the character will behave.
 * @author Justis
 */
public abstract class EntityState
{    

    /**
     * The state logic, to be defined by concrete classes.
     * @param e The entity this state refers to.
     * @param d The dungeon this entity is in.
     */
    public abstract void doState(SpeciesEntity e, Dungeon d);

    /**
     * An arbitrary value, used to distinguish between different states.
     * @return Some integer, which should uniquely identify this state.
     */
    public abstract int isState();

    /**
     * Finds the next node the Entity should travel to.
     * This finds the shortest path from this node to another, then returns
     * the node that should be traveled to next. If that node is occupied, a
     * nearby node will be checked. If the node it is currently on is unoccupied
     * by another entity, it will return the start node. If the node it is currently on
     * is occupied, it will return an unoccupied node somewhere surrounding the
     * current node.
     * @param entity The entity this state refers to.
     * @param dungeon The dungeon the entity is in.
     * @param start The start node, where the path will begin.
     * @param end The end node, where the path will terminate.
     * @return The next valid node the Entity can travel to.
     */
    public Node nextNode(SpeciesEntity entity, Dungeon dungeon, Node start, Node end)
    {
        LinkedList<Node> path = findShortestPath(entity, dungeon, start, end);
        SpeciesEntity player = dungeon.getEntities().get(0);
        if(path.size() > 1 && !isOccupied(entity, dungeon, path.get(1)))
        {
            return path.get(1);
        } //0 is the current node, so 1 is the next node to go to.
        else
        {
            if(!isOccupied(entity, dungeon, path.get(0)))
            {
                return start;
            }
            else
            {
                for(int dir = 0; dir < 8; dir++)
                {
                    if(start.getPath(dir) != null && !isOccupied(entity, dungeon, start.getPath(dir)))
                    {
                        return start.getPath(dir);
                    }
                }
                return start; //It shouldn't come to this, I hope?
            }
        }
    }

    /**
     * Find the shortest path from one node to another.
     * @param map A hashmap linking all nodes with the nodes that are immediately closest to them.
     * @param start The node to begin the path at.
     * @param end The node to end the path at.
     * @return A linked list, in the order of path traversal.
     */
    public LinkedList<Node> findShortestPath(HashMap<Node, Node> map, Node start, Node end)
    {
        return readPath(map, start, end);
    }

    /**
     * Find the shortest path from one node to another.
     * @param entity The entity this state refers to.
     * @param dungeon The dungeon this entity is in.
     * @param start The node to begin the path at.
     * @param end The node to end the path at.
     * @return A linked list, in the order of path traversal.
     */
    public LinkedList<Node> findShortestPath(SpeciesEntity entity, Dungeon dungeon, Node start, Node end)
    {
        LinkedList<Node> path = readPath(findShortestPath(entity, dungeon, start), start, end);
        return path;
    }

    /**
     * Generate a map linking all nodes to the node closest to it.
     * @param entity The entity this state refers to.
     * @param dungeon The dungeon this entity is in.
     * @param start The node to begin the path at.
     * @return A hashmap, linking all nodes to the ones closest to them.
     */
    public HashMap<Node, Node> findShortestPath(SpeciesEntity entity, Dungeon dungeon, Node start)
    {
        HashMap<Node, Double> dist = new HashMap<>();
        HashMap<Node, Node> prev = new HashMap<>();
        LinkedList<Node> queue = new LinkedList<>();
        ArrayList<Node> graph = dungeon.getNodesList();

        for(Node node : graph)
        {
            dist.put(node, Double.MAX_VALUE);
            prev.put(node, null);
            queue.add(node);
        }

        dist.put(start, 0.0);

        while(queue.size() > 0)
        {
            Node uu = minimumDistance(queue, dist);
            queue.remove(uu);
            for(int dir = 0; dir < 8; dir++)
            {
                Node vv = uu.getPath(dir);
                if(vv == null || !isValidNode(entity, dungeon, uu, vv)){continue;}
                double weight = (dir % 2 == 0 ? 1 : 1.5);
                double alt = dist.get(uu) + weight;
                if(alt < dist.get(vv))
                {
                    dist.put(vv, alt);
                    prev.put(vv, uu);
                }
            }
        }

        return prev;
    }

    private Node minimumDistance(LinkedList<Node> queue, HashMap<Node, Double> dist)
    {
        double min = Double.MAX_VALUE;
        Node minNode = queue.get(0);
        for(Node node : queue)
        {
            double nodeDist = dist.get(node);
            if(nodeDist < min)
            {
                min = nodeDist;
                minNode = node;
            }
        }
        return minNode;
    }

    /**
     * Translates a hashmap of all nodes to their closest neighbors into a path.
     * @param map The hashmap of all nodes to their closest neighbors, generated from one of the other functions.
     * @param start The node this path should begin at.
     * @param target The node this path should end at.
     * @return A linked list, containing the path that should be traversed.
     */
    public LinkedList<Node> readPath(HashMap<Node, Node> map, Node start, Node target)
    {
        LinkedList<Node> ss = new LinkedList<>();
        Node uu = target;
        while(map.get(uu) != null)
        {
            ss.push(uu);
            uu = map.get(uu);
        }
        ss.push(uu);
        //Verify the path starts at the, well, start.
        if(ss.peek().equals(start))
        {
            return ss;
        }
        else
        {
            return new LinkedList<>();
        }
    }

    /**
     * Determine if a node is a legal node to move to.
     * This does not check if the node is unoccupied, just that it is a node this entity
     * could travel on if given the opportunity.
     * @param entity The entity this state refers to.
     * @param dungeon The dungeon this entity resides in.
     * @param current The current node. Actually is unused.
     * @param next The node to check the legality of.
     * @return True if the node is valid, false if it is not.
     */
    protected boolean isValidNode(SpeciesEntity entity, Dungeon dungeon, Node current, Node next)
    {
        return next.getType() == Node.LAND;// && !isOccupied(entity, dungeon, next);
    }

    /**
     * Determines if a node is occupied.
     * @param entity The entity this state refers to.
     * @param dungeon The dungeon this entity resides in.
     * @param node The node the test the occupancy of.
     * @return True if the node is occupied, false if it isn't
     */
    protected boolean isOccupied(SpeciesEntity entity, Dungeon dungeon, Node node)
    {
        ArrayList<SpeciesEntity> enemies = dungeon.getEntities();
        for(SpeciesEntity enemy : enemies)
        {
            if(enemy.equals(entity)){continue;}
            Node enemyNode = enemy.getDestinationNode();
            if(enemyNode.equals(node))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the player is a certain number of nodes away.
     * While this could be done by generating a path and testing the length, I wanted
     * to try another method, which may involve less computations.
     * @param d The dungeon the player is in.
     * @param start The initial node.
     * @param range The "radius" of search.
     * @return
     */
    protected boolean playerNearby(Dungeon d, Node start, int range)
    {
        SpeciesEntity player = d.getEntities().get(0);
        return entityNearby(d, player, start, range);
    }
    
    protected boolean entityNearby(Dungeon d, SpeciesEntity e, Node start, int range)
    {
        Node playerNode = e.getCurrentNode();
        LinkedList<Node> queue = new LinkedList<>();
        queue.add(start);
        for(int iter = 0; iter < range; iter++)
        {
            LinkedList<Node> newqueue = new LinkedList<>();
            while(queue.size() > 0)
            {
                Node current = queue.remove();
                if(playerNode.equals(current)){return true;}
                for(int dir = 0; dir < 8; dir++)
                {
                    if(current.getPath(dir) != null)
                    {
                            newqueue.add(current.getPath(dir));
                    }
                }
            }
            queue = newqueue;
        }
        return false;
    }

    /**
     * Return the direction one would be traveling if you went from one node to another.
     * This tests specifically for a path of length one.
     * @param start The node to start at.
     * @param end The node to be traveling to.
     * @return A direction which corresponds to the directions in the Node class, or -1 if
     * there is no path of length one between the two nodes.
     */
    protected int getDirection(Node start, Node end)
    {
        for(int dir = 0; dir < 8; dir++)
        {
            if(start.getPath(dir) != null && start.getPath(dir).equals(end)){return dir;}
        }
        return -1;
    }
    
    protected boolean useItem(Dungeon d, SpeciesEntity e)
    {
        if(!e.getItems().isEmpty())
        {
            Item heldItem = e.getItems().get(0);
            double hpPercentage = (double)e.getCurrentHP() / e.getMaximumHP();
            double staminaPercentage = (double)e.getCurrentStamina() / e.getMaximumStamina();
            if(heldItem.getType() == Item.HP_HEALING && hpPercentage < 0.5)
            {
                e.useItem(heldItem);
                return true;
            }
            else if(heldItem.getType() == Item.STAMINA_HEALING && staminaPercentage < 0.25)
            {
                e.useItem(heldItem);
                return true;
            }
            if(heldItem.getClass() == LightItem.class)
            {
                e.removeItem(heldItem);
                MysteryDungeon.updateLog(String.format("%s was able to see farther.", e.getName()));
                e.setRange(SpeciesEntity.RANGE + 2);
            }
        }
        return false;
    }
}
