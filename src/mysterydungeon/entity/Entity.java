/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;
import mysterydungeon.dungeon.RoomNode;

/**
 * Represents a basic Entity.
 * This class is used to bridge the gap between a class, and its appearance
 * in the dungeon.
 * @author jlamanna
 */
public interface Entity
{
    /**
     * Get the X value of the entity.
     * @return The X value of the entity, in pixels.
     */
    int getX();
    
    /**
     * Get the Y value of the entity.
     * @return The Y value of the entity, in pixels.
     */
    int getY();
 
    /**
     * Get how this entity should be drawn.
     * @return The image of this entity.
     */
    BufferedImage getImage();
    
    /**
     * Generates a random node.
     * More specifically, this picks a random room, then a random
     * number inside that. It makes no distinction on whether the
     * node is occupied or not.
     * @param dungeon The dungeon the entity is in.
     * @return A random node of a random room.
     */
    static Node generateRandomLocation(Dungeon dungeon)
    {
        ArrayList<RoomNode> rooms = dungeon.getRooms();
        int randomRoom = Dungeon.PRNG.nextInt(rooms.size());
        RoomNode room = rooms.get(randomRoom);
        ArrayList<Node> nodes = room.getNodes();
        int randomNode = Dungeon.PRNG.nextInt(nodes.size());
        return nodes.get(randomNode);
    }
}
