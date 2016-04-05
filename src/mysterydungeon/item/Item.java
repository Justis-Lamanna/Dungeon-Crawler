/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.item;

import java.awt.image.BufferedImage;
import mysterydungeon.entity.Entity;

/**
 * The interface for a usable item in this game.
 * @author Justis
 */
public interface Item
{
    public static final boolean REMOVE = true;
    public static final boolean KEEP = false;
    
    /**
     * Dictate what the item will do when used.
     * @param user The user of the item.
     * @return True if the item should be consumed, false if not.
     */
    boolean useItem(Entity user);
    
    /**
     * Get the name of this item.
     * @return This item's name.
     */
    String getName();
    
    /**
     * Get the image of this item.
     * @return The sprite containing this image.
     */
    BufferedImage getImage();
    
    /**
     * Get the description of this item.
     * @return A description of this item.
     */
    String getDescription();
    
    
}
