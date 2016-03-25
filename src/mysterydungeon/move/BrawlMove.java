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
 *
 * @author Justis
 */
public class BrawlMove implements Move
{   
    private final int basePower;
    
    /**
     *
     * @param basepower
     */
    public BrawlMove(int basepower)
    {
        this.basePower = basepower;
    }
    
    /**
     *
     * @return
     */
    @Override
    public int getType()
    {
        return Move.BRAWL;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getName()
    {
        return "Punch";
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
     *
     * @param dungeon
     * @param attacker
     * @return
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
}
