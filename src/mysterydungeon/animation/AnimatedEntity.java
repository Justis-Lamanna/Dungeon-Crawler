/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.animation;

import java.awt.image.BufferedImage;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.entity.EntityState;
import mysterydungeon.entity.Species;
import mysterydungeon.entity.SpeciesEntity;

/**
 * A wrapper class, which wraps an Entity inside an Animation.
 * This class adds an animation to an entity, to make it more visually interesting.
 * Currently, this causes the entity to bounce up and down every so often.
 * While this could conceivably handle the sliding between tiles, it doesn't because
 * reasons.
 * @author jlamanna
 */
public class AnimatedEntity extends SpeciesEntity implements Animation
{
    Animation animation = null;
    
    /**
     * Creates an entity.
     * Remember, the entity will only be drawn when it's added to the dungeon!
     * @param dungeon The dungeon this entity resides in.
     * @param species The species this entity is.
     * @param startState The initial state of this entity.
     * @param player True if this entity is the player, false if not.
     */
    public AnimatedEntity(Dungeon dungeon, Species species, EntityState startState, boolean player)
    {
        super(dungeon, species, startState, player);
    }

    /**
     * Creates an entity.
     * This entity will be initialized with a default state of MoveState, causing
     * it to wander the dungeons until it sees the player. Remember, the entity will
     * only be drawn when it's added to the dungeon!
     * @param dungeon The dungeon this entity resides in.
     * @param species The species this entity is.
     * @param player True if this entity is the player, false if not.
     */
    public AnimatedEntity(Dungeon dungeon, Species species, boolean player)
    {
        super(dungeon, species, player);
    }

    /**
     * Creates an entity.
     * This entity will be initialized with a default state of MoveState, causing
     * it to wander the dungeons until it sees the player. It is also, by default,
     * not the player. Remember, the entity will only be drawn when it's added to
     * the dungeon!
     * @param dungeon The dungeon this entity resides in.
     * @param species The species this entity is.
     */
    public AnimatedEntity(Dungeon dungeon, Species species)
    {
        super(dungeon, species);
    }
    
    /**
     *
     * @param newAnimation
     */
    public void setAnimation(Animation newAnimation)
    {
        animation = newAnimation;
    }
    
    /**
     *
     * @return
     */
    public Animation getAnimation()
    {
        return animation;
    }
    
    /**
     *
     * @return
     */
    public BufferedImage getAnimatedImage()
    {
        if(animation == null)
        {
            return getImage();
        }
        else
        {
            return animation.getImage();
        }
    }
    
    /**
     *
     * @return
     */
    public int getAnimatedX()
    {
        if(animation == null)
        {
            return getX();
        }
        else
        {
            return animation.getX();
        }
    }
    
    /**
     *
     * @return
     */
    public int getAnimatedY()
    {
        if(animation == null)
        {
            return getY();
        }
        else
        {
            return animation.getY();
        }
    }
    
    @Override
    public boolean animate()
    {
        if(animation == null)
        {
            return false;
        }
        else
        {
            return animation.animate();
        }
    }
}
