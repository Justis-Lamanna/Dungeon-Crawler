/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.animation;

import java.awt.image.BufferedImage;
import mysterydungeon.entity.Entity;

/**
 *
 * @author jlamanna
 */
public class AnimatedEntity implements Animation
{
    private final Entity entity;
    private int counter;
    
    private static final int FRAMES_BETWEEN_STEP = 16;
    
    public AnimatedEntity(Entity entity)
    {
        this.entity = entity;
        counter = 0;
    }
    
    @Override
    public int getX()
    {
        return entity.getPixelX();
    }
    
    @Override
    public int getY()
    {
        if(counter < FRAMES_BETWEEN_STEP)
        {
            return entity.getPixelY();
        }
        else
        {
            return entity.getPixelY() - 1;
        }
    }
    
    @Override
    public BufferedImage getImage()
    {
        return entity.getSpecies().getImage();
    }
    
    @Override
    public boolean animate()
    {
        if(counter == FRAMES_BETWEEN_STEP * 2)
        {
            counter = 0;
        }
        else
        {
            counter++;
        }
        return false;
    }
    
    public Entity getEntity()
    {
        return entity;
    }
}
