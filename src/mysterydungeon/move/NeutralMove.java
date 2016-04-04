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
    public void attack(Dungeon dungeon, Entity attacker, ArrayList<Entity> affected)
    {
        MysteryDungeon.LOG.append(String.format("%s %sed!\n", attacker.getName(), name));
        if(affected.isEmpty())
        {
            MysteryDungeon.LOG.append(String.format("   But there was no target!\n", attacker.getName()));
        }
        else
        {
            Entity defender = affected.get(0);
            int totalDamage = defender.addHP(-power);
            MysteryDungeon.LOG.append(String.format("   %s lost %dHP!\n", defender.getName(), totalDamage));
            if(defender.getCurrentHP() == 0)
            {
                dungeon.clearEnemy(defender);
                MysteryDungeon.LOG.append(String.format(   "%s was destroyed!\n", defender.getName()));
                if(defender.isPlayer())
                {
                    Move.respawn();
                }
            }
        }
    }
    
    @Override
    public ArrayList<Entity> getDefender(Dungeon dungeon, Entity attacker)
    {
        Node currentNode = attacker.getCurrentNode();
        Node facingNode = currentNode.getPath(attacker.facing);
        ArrayList<Entity> affected = new ArrayList<>();
        if(facingNode == null)
        {
            return new ArrayList<>();
        }
        ArrayList<Entity> entities = dungeon.getEntities();
        for(Entity entity : entities)
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
    public String getDescription()
    {
        return String.format("Power: %d", power);
    }
}
