/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.dungeon;

import java.util.ArrayList;

/**
 *
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
     *
     */
    public RoomNode()
    {
        containedNodes = new ArrayList<>();
        connections = new ArrayList<>();
        justInteriorNodes = new ArrayList<>();
        exteriorNodes = new ArrayList<>();
    }

    /**
     *
     * @param node
     */
    public void addNode(Node node)
    {
        containedNodes.add(node);
    }

    /**
     *
     * @param node
     */
    public void addConnection(RoomNode node)
    {
        connections.add(node);
    }

    /**
     *
     * @param node
     */
    public void addDoubleConnection(RoomNode node)
    {
        connections.add(node);
        node.addConnection(this);
    }

    /**
     *
     * @return
     */
    public ArrayList<Node> getNodes()
    {
        return containedNodes;
    }

    /**
     *
     * @return
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
     *
     * @return
     */
    public int getX()
    {
        return centerNode.getX();
    }

    /**
     *
     * @return
     */
    public int getY()
    {
        return centerNode.getY();
    }

    /**
     *
     * @return
     */
    public Node getCenter()
    {
        return centerNode;
    }

    /**
     *
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
     *
     * @return
     */
    public ArrayList<Node> getExteriorNodes()
    {
        return exteriorNodes;
    }

    /**
     *
     * @return
     */
    public ArrayList<Node> getJustInteriorNodes()
    {
        return justInteriorNodes;
    }

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
