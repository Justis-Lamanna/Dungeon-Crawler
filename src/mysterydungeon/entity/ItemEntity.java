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
import mysterydungeon.item.Item;

/**
 *
 * @author Justis
 */
public class ItemEntity
{
    private final Item item;
    private final Node currentNode;
    
    public ItemEntity(Item item, Dungeon dungeon)
    {
        this.item = item;
        currentNode = randomizeLocation(dungeon);
    }
    
    private Node randomizeLocation(Dungeon dungeon)
    {
        ArrayList<RoomNode> rooms = dungeon.getRooms();
        int randomRoom = Dungeon.PRNG.nextInt(rooms.size());
        RoomNode room = rooms.get(randomRoom);
        ArrayList<Node> nodes = room.getNodes();
        int randomNode = Dungeon.PRNG.nextInt(nodes.size());
        return nodes.get(randomNode);
    }
    
    public Item getItem()
    {
        return item;
    }
    
    public int getX()
    {
        return currentNode.getX();
    }
    
    public int getY()
    {
        return currentNode.getY();
    }   
}
