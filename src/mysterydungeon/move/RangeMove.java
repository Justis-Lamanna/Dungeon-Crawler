/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.move;

import mysterydungeon.Controls;
import mysterydungeon.DungeonComp;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;
import mysterydungeon.entity.Entity;
import mysterydungeon.MysteryDungeon;
import mysterydungeon.animation.Animation;
import mysterydungeon.animation.RangeAnimation;

/**
 * A standard long-range move.
 * This move affects entities the attacker is facing, but may be some distance
 * away. The farther the enemy is, the weaker the attack: It is at 100% power
 * at one tile away, 75% power at two tiles away, 56.25% power at three tiles away,
 * and so on. These moves have a 15% chance of missing, and no chance of doing
 * increased damage.
 * @author Justis
 */
public class RangeMove implements Move, Comparable
{

    /**
     * The value multiplied by this base power with each tile traveled.
     */
    public static final double MULTIPLIER = 0.75;
    
    private final int power;
    private final int range;
    private int currentPower;
    private int currentRange;
    
    /**
     * Creates a ranged move.
     * @param power The standard damage this attack will do, at one tile away.
     * @param range The farthest the attack will go before dying.
     */
    public RangeMove(int power, int range)
    {
        this.power = power;
        this.currentPower = power;
        this.range = range;
    }
    
    /**
     * Get the type of move this is.
     * @return The constant Move.RANGE, defined in the Move class.
     */
    @Override
    public int getType()
    {
        return Move.RANGE;
    }
    
    /**
     * Get the name of this move. Not actually used.
     * @return The string "Blast".
     */
    @Override
    public String getName()
    {
        return "Blast";
    }
    
    /**
     * Get the base power of this attack.
     * @return An integer, representing the amount of HP this attack does.
     */
    @Override
    public int getPower()
    {
        return power;
    }
    
    
    /**
     * Performs the actual attack.
     * @param dungeon The dungeon this attack occurs in.
     * @param attacker The entity performing the attack.
     * @param defender The entity being attacked.
     */
    @Override
    public void attack(Dungeon dungeon, Entity attacker, Entity defender)
    {
        if(defender == null)
        {
            MysteryDungeon.LOG.append(String.format("%s fired and missed!\n", attacker.getName()));
        }
        else
        {
            if(Dungeon.PRNG.nextInt(100) < 15)
            {
                MysteryDungeon.LOG.append(String.format("%s fired at %s and missed!\n", attacker.getName(), defender.getName()));
            }
            else
            {
                doAnimation(dungeon.getComponent(), attacker.getPixelX(), attacker.getPixelY(), attacker.facing, currentRange);
                int totalDamage = defender.addHP(-currentPower);
                MysteryDungeon.LOG.append(String.format("%s attacked %s for %dHP of damage!\n", attacker.getName(), defender.getName(), totalDamage));
                if(defender.getCurrentHP() == 0)
                {
                    dungeon.clearEnemy(defender);
                    MysteryDungeon.LOG.append(String.format("%s was destroyed!\n", defender.getName()));
                    if(defender.isPlayer())
                    {
                        MysteryDungeon.LOG.append("Press any key...");
                        while(!Controls.getInstance().isAnyKeyDown())
                        {
                            
                        }
                        dungeon.startDungeon();
                    }
                }
            }
        }
    }
    
    /**
     * Gets the entity affected by this attack.
     * @param dungeon The dungeon this attack occurs in.
     * @param attacker The entity performing the attack.
     * @return The entity receiving the attack, or null if there is nobody.
     */
    @Override
    public Entity getDefender(Dungeon dungeon, Entity attacker)
    {
        Node start = attacker.getCurrentNode();
        int facing = attacker.facing;
        currentPower = power;
        for(int ds = 0; ds < range; ds++)
        {
            start = start.getPath(facing);
            if(start == null){break;}
            for(Entity entity : dungeon.getEntities())
            {
                if(entity.getDestinationNode().equals(start))
                {
                    currentRange = ds;
                    return entity;
                }
            }
            currentPower = (int)(currentPower * MULTIPLIER); //Reduces by 25% the farther away you go.
        }
        return null;
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
    
    private void doAnimation(DungeonComp component, int startX, int startY, int direction, int range)
    {
        Animation anim = new RangeAnimation(startX, startY, direction, range);
        component.addAnimation(anim);
        int windowX = startX - (range * 24);
        int windowY = startY - (range * 24);
        do
        {
            component.paintImmediately(windowX, windowY, range*48, range*48);
        } while (!anim.animate());
        component.removeAnimation(anim);
    }
}
