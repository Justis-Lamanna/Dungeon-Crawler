/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.entity;

import java.awt.image.BufferedImage;

/**
 * Represents a basic Entity.
 * This class is used to bridge the gap between a class, and its appearance
 * in the dungeon.
 * @author jlamanna
 * @param <E> The contained item.
 */
public interface Entity<E>
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
     * Get the item contained inside this entity.
     * @return The item wrapped.
     */
    E getContained();
}
