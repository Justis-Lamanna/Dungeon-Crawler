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
public class BounceAnimation implements Animation
{    
    private final AnimatedEntity entity;
    private final int speed;
    private int counter = 0;
    
    /**
     *
     * @param entity
     * @param speed
     */
    public BounceAnimation(AnimatedEntity entity, int speed)
    {
        this.entity = entity;
        this.speed = speed;
    }
    
    @Override
    public boolean animate()
    {
        counter = (counter + 1) % (speed * 2);
        return false;
    }

    @Override
    public BufferedImage getImage()
    {
        return entity.getImage();
    }

    @Override
    public int getX()
    {
        return entity.getX();
    }

    @Override
    public int getY()
    {
        if(counter < speed)
        {
            return entity.getY();
        }
        else
        {
            return entity.getY() - 1;
        }
    }

    @Override
    public Entity getContained()
    {
        return entity;
    }
    
}
