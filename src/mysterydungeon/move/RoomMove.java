/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.move;

import java.util.ArrayList;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.RoomNode;
import mysterydungeon.entity.Entity;
import mysterydungeon.MysteryDungeon;

/**
 *
 * @author Justis
 */
public class RoomMove implements Move
{
    private final int power;
    private final ArrayList<Entity> affected;
    
    /**
     *
     * @param power
     */
    public RoomMove(int power)
    {
        this.power = power;
        affected = new ArrayList<>();
    }
    
    /**
     *
     * @return
     */
    @Override
    public int getType()
    {
        return Move.ROOM;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getName()
    {
        return "Sonic Boom";
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
        if(affected.isEmpty())
        {
            MysteryDungeon.LOG.append(String.format("%s boomed and missed!\n", attacker.getName()));
        }
        else
        {
            if(Dungeon.PRNG.nextInt(100) < 25)
            {
                MysteryDungeon.LOG.append(String.format("%s boomed, but it did nothing!\n", attacker.getName()));
            }
            else
            {
                MysteryDungeon.LOG.append(String.format("%s let off a sonic boom!\n", attacker.getName()));
                for(Entity entity : affected)
                {
                    int totalDamage = entity.addHP(-power);
                    MysteryDungeon.LOG.append(String.format("%s recieved %dHP of damage!\n", entity.getName(), totalDamage));
                    if(entity.getCurrentHP() == 0)
                    {
                        dungeon.clearEnemy(entity);
                        MysteryDungeon.LOG.append(String.format("%s was destroyed!\n", entity.getName()));
                    }
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
        affected.clear();
        RoomNode room = dungeon.getRoom(attacker.getCurrentNode());
        if(room != null)
        {
            //The attacker must be in a room.
            for(Entity entity : dungeon.getEntities())
            {
                if(entity.equals(attacker)){continue;}
                if(room.equals(dungeon.getRoom(entity.getCurrentNode())))
                {
                    affected.add(entity);
                }
            }
        }
        return null;
    }
}
