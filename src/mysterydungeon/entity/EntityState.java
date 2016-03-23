/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;

/**
 *
 * @author Justis
 */
public abstract class EntityState
{
    public abstract void doState(Entity e, Dungeon d);

    public abstract int isState();

    public Node nextNode(Entity entity, Dungeon dungeon, Node start, Node end)
    {
            LinkedList<Node> path = findShortestPath(entity, dungeon, start, end);
            Entity player = dungeon.getEntities().get(0);
            //System.out.println(path);
            if(path.size() > 1 && !isOccupied(entity, dungeon, path.get(1)))
            {
                return path.get(1);
            } //0 is the current node, so 1 is the next node to go to.
            Node newNode;
            do
            {
                    int randomDirection = (int)(dungeon.PRNG.nextInt(8));
                    newNode = start.getPath(randomDirection);
                    entity.facing = randomDirection;
            } while(newNode == null || !isValidNode(entity, dungeon, start, newNode) || isOccupied(entity, dungeon, newNode));
            return newNode;
    }

    public LinkedList<Node> findShortestPath(HashMap<Node, Node> map, Node start, Node end)
    {
            return readPath(map, start, end);
    }

    public LinkedList<Node> findShortestPath(Entity entity, Dungeon dungeon, Node start, Node end)
    {
            LinkedList<Node> path = readPath(findShortestPath(entity, dungeon, start), start, end);
            return path;
    }

    public HashMap<Node, Node> findShortestPath(Entity entity, Dungeon dungeon, Node start)
    {
            HashMap<Node, Integer> dist = new HashMap<>();
            HashMap<Node, Node> prev = new HashMap<>();
            LinkedList<Node> queue = new LinkedList<>();
            ArrayList<Node> graph = dungeon.getNodesList();

            for(Node node : graph)
            {
                    dist.put(node, Integer.MAX_VALUE);
                    prev.put(node, null);
                    queue.add(node);
            }

            dist.put(start, 0);

            while(queue.size() > 0)
            {
                    Node uu = minimumDistance(queue, dist);
                    queue.remove(uu);
                    for(int dir = 0; dir < 8; dir++)
                    {
                            Node vv = uu.getPath(dir);
                            if(vv == null || !isValidNode(entity, dungeon, uu, vv)){continue;}
                            int alt = dist.get(uu) + 1;
                            if(alt < dist.get(vv))
                            {
                                    dist.put(vv, alt);
                                    prev.put(vv, uu);
                            }
                    }
            }

            return prev;
    }

    private Node minimumDistance(LinkedList<Node> queue, HashMap<Node, Integer> dist)
    {
            int min = Integer.MAX_VALUE;
            Node minNode = queue.get(0);
            for(Node node : queue)
            {
                    int nodeDist = dist.get(node);
                    if(nodeDist < min)
                    {
                            min = nodeDist;
                            minNode = node;
                    }
            }
            return minNode;
    }

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

    protected boolean isValidNode(Entity entity, Dungeon dungeon, Node current, Node next)
    {
        return next.getType() == Node.LAND;// && !isOccupied(entity, dungeon, next);
    }

    protected boolean isOccupied(Entity entity, Dungeon dungeon, Node node)
    {
            ArrayList<Entity> enemies = dungeon.getEntities();
            for(Entity enemy : enemies)
            {
                    Node enemyNode = enemy.getDestinationNode();
                    if(enemyNode.equals(node))
                    {
                        return true;
                    }
            }
            return false;
    }

    protected boolean playerNearby(Dungeon d, Node start, int range)
    {
            Entity player = d.getEntities().get(0);
            Node playerNode = player.getCurrentNode();
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

    protected int getDirection(Node start, Node end)
    {
            for(int dir = 0; dir < 8; dir++)
            {
                    if(start.getPath(dir) != null && start.getPath(dir).equals(end)){return dir;}
            }
            return -1;
    }
}
