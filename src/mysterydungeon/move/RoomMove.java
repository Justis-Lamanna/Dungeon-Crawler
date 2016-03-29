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
 * A standard room-range attack.
 * This move attacks all enemies in a room, and fail if done outside a room.
 * The damage done is split between all enemies affected; One enemy would
 * receive full damage, two enemies would each receive half damage, and so on.
 * These types of moves miss 25% of the time, and have no chance for increased 
 * damage.
 * @author Justis
 */
public class RoomMove implements Move, Comparable
{
    private final int power;
    private final ArrayList<Entity> affected;
    
    /**
     * Creates a room-range move.
     * @param power The standard damage this attack will do, against one enemy.
     */
    public RoomMove(int power)
    {
        this.power = power;
        affected = new ArrayList<>();
    }
    
    /**
     * Get the type of move this is.
     * @return The constant Move.ROOM, defined in the Move class.
     */
    @Override
    public int getType()
    {
        return Move.ROOM;
    }
    
    /**
     * Get the name of this move. Not actually used.
     * @return The string "Sonic Boom".
     */
    @Override
    public String getName()
    {
        return "Sonic Boom";
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
     * Performs the attack.
     * Due to the method signature, this attack relies on an internally
     * stored list of affected entities. The defender entity is ignored.
     * @param dungeon The dungeon the attack occurs in.
     * @param attacker The entity performing the attack.
     * @param defender Ignored for the purposes of this attack.
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
                    int newDamage = power / affected.size();
                    int totalDamage = entity.addHP(-newDamage);
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
     * Gets the entities affected by this attack.
     * Due to the method signature, this class stores the affected entities
     * internally.
     * @param dungeon The dungeon this attack occurs in.
     * @param attacker The entity performing the attack.
     * @return Null. The affected entities are stored internally.
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
                if(room.equals(dungeon.getRoom(entity.getDestinationNode())))
                {
                    affected.add(entity);
                }
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
