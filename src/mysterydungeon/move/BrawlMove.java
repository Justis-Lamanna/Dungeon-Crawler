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
public class BrawlMove extends Move
{   
    /**
     * A constant representing the Weak Punch attack.
     */
    public static final BrawlMove WEAK_PUNCH = new BrawlMove("Weak Punch", 5, 5);
    
    /**
     * A constant representing the Punch attack.
     */
    public static final BrawlMove PUNCH = new BrawlMove("Punch", 15, 10);
    
    /**
     * A constant representing the Strong Punch attack.
     */
    public static final BrawlMove STRONG_PUNCH = new BrawlMove("Strong Punch", 25, 15);
    
    private final int basePower;
    private final int stamina;
    private final String name;
    
    /**
     * Creates an instance of a brawl-type move.
     * @param name The name of this move.
     * @param basepower The standard amount of damage this attack will do.
     * @param stamina The amount of stamina this attack needs.
     */
    public BrawlMove(String name, int basepower, int stamina)
    {
        this.basePower = basepower;
        this.stamina = stamina;
        this.name = name;
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
        return name;
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
        if(attacker.getCurrentStamina() < stamina)
        {
            MysteryDungeon.LOG.append(String.format("%s doesn't have enough stamina!\n", attacker.getName()));
            return;
        }
        attacker.addStamina(-getStamina());
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
                if(defender.isPlayer())
                {
                    Move.respawn();
                }
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
            if(entity.getDestinationNode().equals(facingNode))
            {
                return entity;
            }
        }
        return null;
    }
    
    @Override
    public int getStamina()
    {
        return stamina;
    }
    
    @Override
    public String getDescription()
    {
        return String.format("%s (Stamina: %d)", name, stamina);
    }
}
