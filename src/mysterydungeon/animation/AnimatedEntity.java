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
 * Couples an entity with an animation.
 * This used to be a decorator for Entity, allowing animations to be "stacked".
 * However, that code caused alot of awkward casts; this makes the code
 * so much nicer. Now, this couples a SpeciesEntity with an associated
 * animation.
 * <br><br>
 * Animations function the same as before.
 * @author jlamanna
 */
public class AnimatedEntity extends SpeciesEntity implements Animation
{
    private Animation animation = null;
    
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
     * Sets the associated animation.
     * If this animation is set to null, it acts as a non-animation.
     * @param newAnimation The new animation to use.
     */
    public void setAnimation(Animation newAnimation)
    {
        animation = newAnimation;
    }
    
    /**
     * Gets the associated animation.
     * @return The animation this AnimatedEntity uses.
     */
    public Animation getAnimation()
    {
        return animation;
    }
    
    /**
     * Gets the image, after the animation has been applied.
     * If the animation is null, this functions as getImage().
     * @return The image, after animation.
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
     * Gets the X position of this sprite, after the animation has been applied.
     * If the animation is null, this functions as getX().
     * @return The X position, after animation.
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
     * Gets the Y position of this sprite, after the animation has been applied.
     * If the animation is null, this functions as getY().
     * @return The Y position, after animation.
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
