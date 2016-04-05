/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.animation;

import java.awt.image.BufferedImage;
import mysterydungeon.entity.Entity;

/**
 * A wrapper class, which wraps an Entity inside an Animation.
 * This class adds an animation to an entity, to make it more visually interesting.
 * Currently, this causes the entity to bounce up and down every so often.
 * While this could conceivably handle the sliding between tiles, it doesn't because
 * reasons.
 * @author jlamanna
 */
public class AnimatedEntity implements Animation
{
    private final Entity entity;
    private int counter;
    
    private static final int FRAMES_BETWEEN_STEP = 24;
    
    /**
     * Create an animated entity.
     * @param entity The base entity to use.
     */
    public AnimatedEntity(Entity entity)
    {
        this.entity = entity;
        counter = 0;
    }
    
    @Override
    public int getAnimationX()
    {
        return entity.getPixelX();
    }
    
    @Override
    public int getAnimationY()
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
    
    /**
     * Get the entity associated with this animated entity.
     * @return The entity contained in this animated entity.
     */
    public Entity getEntity()
    {
        return entity;
    }
}
