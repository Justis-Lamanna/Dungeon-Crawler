/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.move;

import java.util.ArrayList;
import mysterydungeon.MysteryDungeon;
import mysterydungeon.animation.AnimatedEntity;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;
import mysterydungeon.entity.SpeciesEntity;
import mysterydungeon.item.Item;

/**
 *
 * @author Justis
 */
public class NeutralMove extends Move
{

    /**
     *
     */
    public static final NeutralMove BASH = new NeutralMove("Bash", 5);
    
    private final String name;
    private final int power;
    private final int stamina;
    
    /**
     *
     * @param name
     * @param power
     */
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
    public void attack(Dungeon dungeon, SpeciesEntity attacker, ArrayList<SpeciesEntity> affected)
    {
        MysteryDungeon.updateLog(String.format("%s %sed!", attacker.getName(), name));
        if(affected.isEmpty())
        {
            MysteryDungeon.updateLog(String.format("   But there was no target!", attacker.getName()));
        }
        else
        {
            SpeciesEntity defender = affected.get(0);
            int totalDamage = defender.addHP(-power);
            MysteryDungeon.updateLog(String.format("   %s lost %dHP!", defender.getName(), totalDamage));
            if(defender.getCurrentHP() == 0)
            {
                dungeon.clearEnemy(defender);
                MysteryDungeon.updateLog(String.format(   "%s was destroyed!", defender.getName()));
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
    public String getDescription()
    {
        return String.format("Power: %d", power);
    }
}
