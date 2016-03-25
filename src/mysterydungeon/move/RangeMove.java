/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.move;

import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;
import mysterydungeon.entity.Entity;
import mysterydungeon.MysteryDungeon;

/**
 *
 * @author Justis
 */
public class RangeMove implements Move
{

    /**
     *
     */
    public static final double MULTIPLIER = 0.75;
    
    private final int power;
    private final int range;
    private int currentPower;
    
    /**
     *
     * @param power
     * @param range
     */
    public RangeMove(int power, int range)
    {
        this.power = power;
        this.currentPower = power;
        this.range = range;
    }
    
    /**
     *
     * @return
     */
    @Override
    public int getType()
    {
        return Move.RANGE;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getName()
    {
        return "Blast";
    }
    
    /**
     *
     * @param dungeon
     * @param attacker
     * @param defender
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
                int totalDamage = defender.addHP(-currentPower);
                MysteryDungeon.LOG.append(String.format("%s attacked %s for %dHP of damage!\n", attacker.getName(), defender.getName(), totalDamage));
                if(defender.getCurrentHP() == 0)
                {
                    dungeon.clearEnemy(defender);
                    MysteryDungeon.LOG.append(String.format("%s was destroyed!\n", defender.getName()));
                }
            }
        }
    }
    
    /**
     *
     * @param dungeon
     * @param attacker
     * @return
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
                if(entity.getCurrentNode().equals(start))
                {
                    return entity;
                }
            }
            currentPower = (int)(currentPower * MULTIPLIER); //Reduces by 25% the farther away you go.
        }
        return null;
    }
}
