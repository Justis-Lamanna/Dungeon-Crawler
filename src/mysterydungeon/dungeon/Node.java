/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.dungeon;

import mysterydungeon.DungeonComp;

/**
 *
 * @author Justis
 */
public class Node
{
    private final int x;
    private final int y;
    private final int type;
    private final Node[] connections = new Node[8];

    /**
     *
     */
    public static final int NORTH = 0;

    /**
     *
     */
    public static final int NORTHEAST = 1;

    /**
     *
     */
    public static final int EAST = 2;

    /**
     *
     */
    public static final int SOUTHEAST = 3;

    /**
     *
     */
    public static final int SOUTH = 4;

    /**
     *
     */
    public static final int SOUTHWEST = 5;

    /**
     *
     */
    public static final int WEST = 6;

    /**
     *
     */
    public static final int NORTHWEST = 7;

    /**
     *
     */
    public static final int OBSTACLE = 0;

    /**
     *
     */
    public static final int LAND = 1;

    /**
     *
     */
    public static final int WATER = 2;

    /**
     *
     * @param type
     * @param x
     * @param y
     */
    public Node(int type, int x, int y)
    {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /**
     *
     * @return
     */
    public int getX()
    {
        return x;
    }

    /**
     *
     * @return
     */
    public int getY()
    {
        return y;
    }

    /**
     *
     * @return
     */
    public int getType()
    {
        return type;
    }

    /**
     *
     * @param connect
     * @param direction
     */
    public void setPath(Node connect, int direction)
    {
        connections[direction] = connect;
    }

    /**
     *
     * @param connect
     * @param direction
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
     *
     * @param direction
     * @return
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
     *
     * @param ox
     * @param oy
     * @return
     */
    public boolean equals(int ox, int oy)
    {
        return ((this.x * DungeonComp.TILE_SIZE) == ox) &&
                ((this.y * DungeonComp.TILE_SIZE) == oy);
    }

    @Override
    public int hashCode() {
        return this.x * 100 + this.y;
    }

    @Override
    public String toString()
    {
        return String.format("(%d, %d, %d)", x, y, type);
    }
}
