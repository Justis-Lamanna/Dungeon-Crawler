/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.move;

import java.util.ArrayList;
import mysterydungeon.Controls;
import mysterydungeon.DungeonComp;
import mysterydungeon.MysteryDungeon;
import mysterydungeon.animation.Animation;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.entity.Entity;
import mysterydungeon.entity.SpeciesEntity;
import mysterydungeon.item.Item;

/**
 * The skeleton for a move.
 * @author Justis
 */
public abstract class Move implements Comparable
{

    /**
     * A constant representing a neutral move.
     */
    public static final int NEUTRAL = 0;
    
    /**
     * A constant representing a short-range move.
     */
    public static final int BRAWL = 1;

    /**
     * A constant representing a long-range move.
     */
    public static final int RANGE = 2;

    /**
     * A constant representing a room-range move.
     */
    public static final int ROOM = 3;
    
    /**
     * Determine the defender of the attack.
     * @param dungeon The dungeon the attack is performed in.
     * @param attacker The entity using the attack.
     * @return The entity receiving the attack.
     */
    public abstract ArrayList<SpeciesEntity> getDefender(Dungeon dungeon, SpeciesEntity attacker);

    /**
     * Perform the attack.
     * @param dungeon The dungeon the attack is performed in.
     * @param attacker The entity using the attack.
     * @param defender The entity receiving the attack.
     */
    public abstract void attack(Dungeon dungeon, SpeciesEntity attacker, ArrayList<SpeciesEntity> defender);

    /**
     * Returns a name for this attack.
     * @return The name of the attack.
     */
    public abstract String getName();

    /**
     * Get the type of attack this is.
     * @return An integer representing what type of attack this is (Short-range, etc)
     */
    public abstract int getType();
    
    /**
     * Get the base power of this attack.
     * @return An integer, representing the amount of HP this attack does.
     */
    public abstract int getPower();
    
    /**
     * Get the amount of stamina needed to perform this attack.
     * @return An integer, representing the amount of stamina needed to use this attack.
     */
    public abstract int getStamina();
    
    /**
     * Get the description of this move, for use in the move menu.
     */
    public abstract String getDescription();
    
    /**
     * Pause program execution by some amount of time.
     * @param millis The number of milliseconds to wait.
     */
    public static void delay(long millis)
    {
        long waitUntil = System.currentTimeMillis() + millis;
        while(System.currentTimeMillis() < waitUntil)
        {
            //Waiting...
        }
    }
    
    /**
     * Animate a certain animation.
     * Animations that are played a fix amount of time should use this method
     * to animate. This method repeatedly calls the animate() method until it
     * returns true, in which case it terminates.
     * @param anim The animation to play.
     * @param delay The amount of time to wait between frames.
     */
    public static void animate(Animation anim, long delay)
    {
        DungeonComp component = DungeonComp.getInstance();
        component.addAnimation(anim);
        do
        {
            component.repaint();
            delay(delay);
        } while (!anim.animate());
        component.removeAnimation(anim);
    }
    
    /**
     * When the player faints, this method is called.
     */
    public static void respawn()
    {
        MysteryDungeon.updateLog("Press any key...");
        while(Controls.getInstance().isAnyKeyDown())
        {
            Controls.getInstance().update();
        }
        DungeonComp.getInstance().getDungeon().startNextFloor();
    }
    
    public static void faint(SpeciesEntity fainted)
    {
        if(fainted.isPlayer())
        {
            respawn();
        }
        else if(!fainted.getItems().isEmpty())
        {
            Item heldItem = fainted.getItems().get(0);
            MysteryDungeon.updateLog(String.format("%s dropped a %s.", fainted.getName(), heldItem.getName()));
            fainted.getDungeon().spawnItem(heldItem, fainted.getCurrentNode());
        }
    }
    
    /**
     * Compare this move to another move.
     * In the hierarchy, RoomMove > RangeMove > BrawlMove.
     * Individually, higher base power > lower base power.
     * @param o The object to compare to.
     * @return 
     */
    @Override
    public int compareTo(Object o)
    {
        Move other = (Move)o;
        if(other.getType() > this.getType())
        {
            return 1;
        }
        else if(other.getType() < this.getType())
        {
            return -1;
        }
        else
        {
            if(other.getPower() > this.getPower())
            {
                return 1;
            }
            else if(other.getPower() < this.getPower())
            {
                return -1;
            }
            else
            {
                return 0;
            }
        }
    }
}
