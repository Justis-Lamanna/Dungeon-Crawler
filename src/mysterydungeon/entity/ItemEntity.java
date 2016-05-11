/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import mysterydungeon.MysteryDungeon;
import mysterydungeon.animation.AnimatedEntity;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;
import mysterydungeon.item.Item;

/**
 *
 * @author Justis
 */
public class ItemEntity implements ActionEntity
{
    private final Item item;
    private final Dungeon dungeon;
    private final Node currentNode;
    
    /**
     *
     * @param item
     * @param dungeon
     */
    public ItemEntity(Item item, Dungeon dungeon)
    {
        this.item = item;
        this.dungeon = dungeon;
        currentNode = Entity.generateRandomLocation(dungeon);
    }
    
    /**
     *
     * @param item
     * @param dungeon
     * @param node
     */
    public ItemEntity(Item item, Dungeon dungeon, Node node)
    {
        this.item = item;
        this.dungeon = dungeon;
        currentNode = node;
    }
    
    /**
     *
     * @return
     */
    @Override
    public Entity getContained()
    {
        return this;
    }
    
    @Override
    public int getX()
    {
        return currentNode.getX();
    }
    
    @Override
    public int getY()
    {
        return currentNode.getY();
    }   
    
    @Override
    public BufferedImage getImage()
    {
        return item.getImage();
    }
    
    @Override
    public void onTurn()
    {
        ArrayList<AnimatedEntity> entities = dungeon.getEntities();
        for(SpeciesEntity entity : entities)
        {
            if(entity.getDestinationNode().getX() == this.getX() && entity.getDestinationNode().getY() == this.getY())
            {
                if(entity.isPlayer() || entity.getItems().isEmpty())
                {
                    MysteryDungeon.updateLog(String.format("%s picked up %s.", entity.getName(), item.getName()));
                    entity.addItem(item);
                    dungeon.clearItem(this);
                    break;
                }
            }
        }
    }
}
