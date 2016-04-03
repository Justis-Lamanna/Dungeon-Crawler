/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.move;

import java.util.ArrayList;
import mysterydungeon.MysteryDungeon;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;
import mysterydungeon.entity.Entity;

/**
 *
 * @author Justis
 */
public class NeutralMove extends Move
{
    public static final NeutralMove BASH = new NeutralMove("Bash", 2);
    
    private final String name;
    private final int power;
    private final int stamina;
    
    public NeutralMove(String name, int power)
    {
        this.name = name;
        this.power = power;
        this.stamina = 0;
    }
    
    @Override
    public int getStamina()
    {
        return stamina;
    }
    
    @Override
    public int getPower()
    {
        return power;
    }
    
    @Override
    public int getType()
    {
        return Move.NEUTRAL;
    }
    
    @Override
    public String getName()
    {
        return name;
    }
    
    @Override
    public void attack(Dungeon dungeon, Entity attacker, Entity defender)
    {
        if(defender == null)
        {
            MysteryDungeon.LOG.append(String.format("%s attacked and missed!\n", attacker.getName()));
        }
        else
        {
            int totalDamage = defender.addHP(-power);
            MysteryDungeon.LOG.append(String.format("%s attacked %s for %dHP of damage!\n", attacker.getName(), defender.getName(), totalDamage));
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
    public String getDescription()
    {
        return String.format("%s (No Stamina)", name);
    }
}
