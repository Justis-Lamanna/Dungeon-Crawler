/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.dungeon;

import mysterydungeon.DungeonComp;

/**
 * The class representing one node.
 * @author Justis
 */
public class Node
{
    private final int x;
    private final int y;
    private final int type;
    private final Node[] connections = new Node[8];

    /**
     * Constant representing the north connection.
     */
    public static final int NORTH = 0;

    /**
     * Constant representing the northeast connection.
     */
    public static final int NORTHEAST = 1;

    /**
     * Constant representing the east connection.
     */
    public static final int EAST = 2;

    /**
     * Constant representing the southeast connection.
     */
    public static final int SOUTHEAST = 3;

    /**
     * Constant representing the south connection.
     */
    public static final int SOUTH = 4;

    /**
     * Constant representing the southwest connection.
     */
    public static final int SOUTHWEST = 5;

    /**
     * Constant representing the west connection.
     */
    public static final int WEST = 6;

    /**
     * Constant representing the northwest connection.
     */
    public static final int NORTHWEST = 7;

    /**
     * Constant representing an obstacle-type Node.
     */
    public static final int OBSTACLE = 0;

    /**
     * Constant representing a land-type Node.
     */
    public static final int LAND = 1;

    /**
     * Constant representing a water-type Node.
     */
    public static final int WATER = 2;

    /**
     * Create a node of the specified type and position.
     * @param type The type of node this is.
     * @param x The x position of this node, in tiles.
     * @param y The y position of this node, in tiles.
     */
    public Node(int type, int x, int y)
    {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /**
     * Get the x position of this node.
     * @return The x position of this node, in tiles.
     */
    public int getX()
    {
        return x;
    }

    /**
     * Get the y position of this node.
     * @return The y position of this node, in tiles.
     */
    public int getY()
    {
        return y;
    }

    /**
     * Get the type of this tile.
     * @return The type of this tile.
     */
    public int getType()
    {
        return type;
    }

    /**
     * Create a directed path from this node to another, along a certain direction.
     * @param connect The node to connect to.
     * @param direction The direction between this node and the connected node.
     */
    public void setPath(Node connect, int direction)
    {
        connections[direction] = connect;
    }

    /**
     * Creates an undirected path from this node to another, along a certain direction.
     * @param connect The node to connect to.
     * @param direction The direction from this node to the new node.
     */
    public void setDoublePath(Node connect, int direction)
    {
        connections[direction] = connect;
        int oppositeDirection = (direction + 4) % 8;
        if(connect != null)
        {
            connect.setPath(this, oppositeDirection);
        }
    }

    /**
     * Gets the node that's connected to this one in a specified direction
     * @param direction The direction of the node to check.
     * @return The node that's connected to this one in the specified direction, or null if there is none.
     */
    public Node getPath(int direction)
    {
        if(direction >= 0 && direction < 8){return connections[direction];}
        else{return null;}
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == null){return false;}
        Node node = (Node)o;
        return (x == node.getX() && y == node.getY());
    }
    
    /**
     * Checks if the specified pixel coordinates correspond to this node.
     * @param ox The x coordinate to check, in pixels.
     * @param oy The y coordinate to check, in pixels.
     * @return True if this tile as at (ox, oy), false if not.
     */
    public boolean equals(int ox, int oy)
    {
        return ((this.x * DungeonComp.TILE_SIZE) == ox) &&
                ((this.y * DungeonComp.TILE_SIZE) == oy);
    }
    
    /**
     * Returns a hash code for this node. Guaranteed to be unique in maps smaller than 99x99 tiles.
     * @return a hash code.
     */
    @Override
    public int hashCode() {
        return this.x * 100 + this.y;
    }

    /**
     * Returns a string representation of this node.
     * @return A string of the format "Node @ ([x value], [y value], [type])
     */
    @Override
    public String toString()
    {
        return String.format("Node @ (%d, %d, %d)", x, y, type);
    }
}
