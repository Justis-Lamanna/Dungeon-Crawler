/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.dungeon;

import java.util.ArrayList;

/**
 * A class that represents a room. Contains all nodes in the room, as well as
 * exits outside of the room.
 * @author Justis
 */
public class RoomNode
{
    private final ArrayList<Node> containedNodes;
    private final ArrayList<RoomNode> connections;
    private final ArrayList<Node> justInteriorNodes;
    private final ArrayList<Node> exteriorNodes;

    private Node centerNode;

    /**
     * Creates an empty room.
     */
    public RoomNode()
    {
        containedNodes = new ArrayList<>();
        connections = new ArrayList<>();
        justInteriorNodes = new ArrayList<>();
        exteriorNodes = new ArrayList<>();
    }

    /**
     * Add a node to this room.
     * @param node The node to add to this room.
     */
    public void addNode(Node node)
    {
        containedNodes.add(node);
    }

    /**
     * Adds a directed connection between this room and another.
     * @param node The room to connect to.
     * @deprecated This method of joining rooms is no longer used.
     */
    public void addConnection(RoomNode node)
    {
        connections.add(node);
    }

    /**
     * Adds an undirected connection between this room and another.
     * @param node The room to connect to.
     * @deprecated This method of joining rooms is no longer used.
     */
    public void addDoubleConnection(RoomNode node)
    {
        connections.add(node);
        node.addConnection(this);
    }

    /**
     * Get all nodes contained in this room.
     * @return A list of all nodes in the room.
     */
    public ArrayList<Node> getNodes()
    {
        return containedNodes;
    }

    /**
     * Finds the center of a room.
     * @return True if a center was calculated, or false if there was no viable center.
     */
    public boolean calculateCenter()
    {
        for(Node node : containedNodes)
        {
            if(
                containedNodes.contains(node.getPath(Node.NORTH)) &&
                containedNodes.contains(node.getPath(Node.EAST)) &&
                containedNodes.contains(node.getPath(Node.SOUTH)) &&
                containedNodes.contains(node.getPath(Node.WEST)))
            {
                centerNode = node;
                return true;
            }
        }
        //Nothing matched the criteria.
        return false;
    }

    /**
     * Gets the X value of the center of the room.
     * @return
     */
    public int getX()
    {
        return centerNode.getX();
    }

    /**
     * Gets the Y value of the center of the room.
     * @return
     */
    public int getY()
    {
        return centerNode.getY();
    }

    /**
     * Gets the node representing the center of the room.
     * @return
     */
    public Node getCenter()
    {
        return centerNode;
    }

    /**
     * Determines the nodes signifying the exits out of the dungeon.
     * More specifically, this determines exterior nodes, which are the true
     * exits, and are not contained in the room. This also determines "just
     * interior" nodes, which are directly connected to the exterior nodes, and
     * are contained in the room.
     */
    public void calculateExteriorNodes()
    {
        for(Node node : containedNodes)
        {
            for(int dir = 0; dir < 8; dir++)
            {
                Node connection = node.getPath(dir);
                if(connection != null && !containedNodes.contains(connection) && !exteriorNodes.contains(connection))
                {
                    exteriorNodes.add(connection);
                    justInteriorNodes.add(node);
                }
            }
        }
    }

    /**
     * Get the list of exterior nodes.
     * @return The list of exterior nodes, with indices that correspond to their just interior nodes.
     */
    public ArrayList<Node> getExteriorNodes()
    {
        return exteriorNodes;
    }

    /**
     * Get a list of just interior nodes.
     * @return The list of just interior nodes, with indices that correspond to their exterior nodes.
     */
    public ArrayList<Node> getJustInteriorNodes()
    {
        return justInteriorNodes;
    }

    /**
     * Returns a String representation of this Room.
     * @return A string, listing all nodes in the form of X-Y pairs.
     */
    @Override
    public String toString()
    {
        String ret = "";
        for(Node node : containedNodes)
        {
            ret += String.format("(%d, %d)\t", node.getX(), node.getY());
        }
        return ret;
    }
}
