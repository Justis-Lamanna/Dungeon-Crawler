/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.move;

import java.util.ArrayList;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.entity.Entity;
import mysterydungeon.MysteryDungeon;
import mysterydungeon.dungeon.Node;

/**
 * A standard close-range move.
 * This type of move affects enemies the player is facing toward, and who are
 * one tile away. This class of moves has a 5% chance of missing, and a 10%
 * chance of doing 50% more damage.
 * @author Justis
 */
public class BrawlMove implements Move, Comparable
{   
    private final int basePower;
    
    /**
     * Creates an instance of a brawl-type move.
     * @param basepower The standard amount of damage this attack will do.
     */
    public BrawlMove(int basepower)
    {
        this.basePower = basepower;
    }
    
    /**
     * Get the type of move this is
     * @return The constant Move.BRAWL, defined in the Move class.
     */
    @Override
    public int getType()
    {
        return Move.BRAWL;
    }
    
    /**
     * Get the name of this attack. Not actually used.
     * @return The string "Punch".
     */
    @Override
    public String getName()
    {
        return "Punch";
    }
    
    /**
     * Get the base power of this attack.
     * @return An integer, representing the amount of HP this attack does.
     */
    @Override
    public int getPower()
    {
        return basePower;
    }
    
    /**
     * Performs the actual attack.
     * @param dungeon The dungeon this attack is happening in.
     * @param attacker The entity doing the attack.
     * @param defender The entity receiving the attack.
     */
    @Override
    public void attack(Dungeon dungeon, Entity attacker, Entity defender)
    {
        int damage = basePower;
        if(defender == null)
        {
            MysteryDungeon.LOG.append(String.format("%s attacked and missed!\n", attacker.getName()));
        }
        else
        {
            if(Dungeon.PRNG.nextInt(100) < 10)
            {
                damage = (int)(damage * 1.5);
                MysteryDungeon.LOG.append(String.format("%s felt powered-up!\n", attacker.getName()));
            }
            if(Dungeon.PRNG.nextInt(100) < 5)
            {
                MysteryDungeon.LOG.append(String.format("%s attacked %s and missed!\n", attacker.getName(), defender.getName()));
            }
            else
            {
                int totalDamage = defender.addHP(-damage);
                MysteryDungeon.LOG.append(String.format("%s attacked %s for %dHP of damage!\n", attacker.getName(), defender.getName(), totalDamage));
            }
            if(defender.getCurrentHP() == 0)
            {
                dungeon.clearEnemy(defender);
                MysteryDungeon.LOG.append(String.format("%s was destroyed!\n", defender.getName()));
            }
        }
    }
    
    /**
     * Gets the entity affected by this attack.
     * @param dungeon The dungeon this attack happens in.
     * @param attacker The entity using the attack.
     * @return The entity who will receive the attack, or null if there is nobody.
     */
    @Override
    public Entity getDefender(Dungeon dungeon, Entity attacker)
    {
        Node currentNode = attacker.getCurrentNode();
        Node facingNode = currentNode.getPath(attacker.facing);
        if(facingNode == null)
        {
            return null;
        }
        ArrayList<Entity> entities = dungeon.getEntities();
        for(Entity entity : entities)
        {
            if(entity.getCurrentNode().equals(facingNode))
            {
                return entity;
            }
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
}
