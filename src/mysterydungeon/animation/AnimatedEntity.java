/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.animation;

import java.awt.image.BufferedImage;
import mysterydungeon.entity.Entity;
import mysterydungeon.entity.SpeciesEntity;

/**
 * A wrapper class, which wraps an Entity inside an Animation.
 * This class adds an animation to an entity, to make it more visually interesting.
 * Currently, this causes the entity to bounce up and down every so often.
 * While this could conceivably handle the sliding between tiles, it doesn't because
 * reasons.
 * @author jlamanna
 */
public class AnimatedEntity implements Animation, Entity
{
    private final SpeciesEntity entity;
    private int counter;
    
    private static final int FRAMES_BETWEEN_STEP = 24;
    
    /**
     * Create an animated entity.
     * @param entity The base entity to use.
     */
    public AnimatedEntity(SpeciesEntity entity)
    {
        this.entity = entity;
        counter = 0;
    }
    
    @Override
    public int getX()
    {
        return entity.getX();
    }
    
    @Override
    public int getY()
    {
        if(counter < FRAMES_BETWEEN_STEP)
        {
            return entity.getY();
        }
        else
        {
            return entity.getY() - 1;
        }
    }
    
    @Override
    public BufferedImage getImage()
    {
        return ((Entity)entity.getContained()).getImage();
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
    @Override
    public SpeciesEntity getContained()
    {
        return entity;
    }
}
