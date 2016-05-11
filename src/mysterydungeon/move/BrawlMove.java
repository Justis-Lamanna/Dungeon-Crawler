/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.move;

import java.util.ArrayList;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.entity.SpeciesEntity;
import mysterydungeon.MysteryDungeon;
import mysterydungeon.animation.AnimatedEntity;
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
    public static final BrawlMove WEAK_PUNCH = new BrawlMove("Weak Punch", 10, 5);
    
    /**
     * A constant representing the Punch attack.
     */
    public static final BrawlMove PUNCH = new BrawlMove("Punch", 20, 10);
    
    /**
     * A constant representing the Strong Punch attack.
     */
    public static final BrawlMove STRONG_PUNCH = new BrawlMove("Strong Punch", 30, 15);
    
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
    
    @Override
    public int getType()
    {
        return Move.BRAWL;
    }
    
    @Override
    public String getName()
    {
        return name;
    }
    
    @Override
    public int getPower()
    {
        return basePower;
    }
    
    @Override
    public void attack(Dungeon dungeon, SpeciesEntity attacker, ArrayList<SpeciesEntity> affected)
    {
        int damage = basePower;
        if(attacker.getCurrentStamina() < stamina)
        {
            MysteryDungeon.updateLog(String.format("%s doesn't have enough stamina!", attacker.getName()));
            return;
        }
        attacker.addStamina(-getStamina());
        MysteryDungeon.updateLog(String.format("%s let off a %s!", attacker.getName(), name));
        if(affected.isEmpty())
        {
            MysteryDungeon.updateLog("   But there was no target!");
        }
        else
        {
            SpeciesEntity defender = affected.get(0);
            if(Dungeon.PRNG.nextInt(100) < 10)
            {
                damage = (int)(damage * 1.5);
                MysteryDungeon.updateLog(String.format("   %s felt powered-up!", attacker.getName()));
            }
            if(Dungeon.PRNG.nextInt(100) < 5)
            {
                MysteryDungeon.updateLog("   Just missed!");
            }
            else
            {
                int totalDamage = defender.addHP(-damage);
                MysteryDungeon.updateLog(String.format("   %s lost %dHP!", defender.getName(), totalDamage));
            }
            if(defender.getCurrentHP() == 0)
            {
                dungeon.clearEnemy(defender);
                MysteryDungeon.updateLog(String.format("   %s was destroyed!", defender.getName()));
                Move.faint(defender);
            }
        }
    }
    
    @Override
    public ArrayList<SpeciesEntity> getDefender(Dungeon dungeon, SpeciesEntity attacker)
    {
        Node currentNode = attacker.getCurrentNode();
        Node facingNode = currentNode.getPath(attacker.facing);
        ArrayList<SpeciesEntity> affected = new ArrayList<>();
        if(facingNode == null)
        {
            return new ArrayList<>();
        }
        ArrayList<AnimatedEntity> entities = dungeon.getEntities();
        for(SpeciesEntity entity : entities)
        {
            if(entity.getDestinationNode().equals(facingNode))
            {
                affected.add(entity);
                return affected;
            }
        }
        return new ArrayList<>();
    }
    
    @Override
    public int getStamina()
    {
        return stamina;
    }
    
    @Override
    public String getDescription()
    {
        return String.format("Power: %d", basePower);
    }
}
