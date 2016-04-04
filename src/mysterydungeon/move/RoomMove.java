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
import mysterydungeon.animation.Animation;
import mysterydungeon.animation.RoomAnimation;

/**
 * A standard room-range attack.
 * This move attacks all enemies in a room, and fail if done outside a room.
 * The damage done is split between all enemies affected; One enemy would
 * receive full damage, two enemies would each receive half damage, and so on.
 * These types of moves miss 25% of the time, and have no chance for increased 
 * damage.
 * @author Justis
 */
public class RoomMove extends Move
{
    /**
     * A constant representing a shock wave attack.
     */
    public static final RoomMove SHOCK_WAVE = new RoomMove("Shock Wave", 20, 25);
    
    /**
     * A constant representing a sonic boom attack.
     */
    public static final RoomMove SONIC_BOOM = new RoomMove("Sonic Boom", 40, 40);
    
    /**
     * A constant representing a nuclear blast attack.
     */
    public static final RoomMove NUCLEAR_BLAST = new RoomMove("Nuclear Blast", 60, 65);
    
    private final int power;
    private final int stamina;
    private final String name;
    
    /**
     * Creates a room-range move.
     * @param name The name of this move.
     * @param power The standard damage this attack will do, against one enemy.
     * @param stamina The amount of stamina this attack will take.
     */
    public RoomMove(String name, int power, int stamina)
    {
        this.power = power;
        this.stamina = stamina;
        this.name = name;
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
        return name;
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
     * @param affected
     */
    @Override
    public void attack(Dungeon dungeon, Entity attacker, ArrayList<Entity> affected)
    {
        if(attacker.getCurrentStamina() < stamina)
        {
            MysteryDungeon.updateLog(String.format("%s doesn't have enough stamina!", attacker.getName()));
            return;
        }
        attacker.addStamina(-getStamina());
        MysteryDungeon.updateLog(String.format("%s let off a %s", attacker.getName(), name));
        doAnimation(attacker.getPixelX(), attacker.getPixelY());
        if(affected.isEmpty())
        {
            MysteryDungeon.updateLog("   But there was no target!");
        }
        else
        {
            if(Dungeon.PRNG.nextInt(100) < 25)
            {
                MysteryDungeon.updateLog("   But there was no effect!");
            }
            else
            {
                for(Entity entity : affected)
                {
                    int newDamage = power / affected.size();
                    int totalDamage = entity.addHP(-newDamage);
                    MysteryDungeon.updateLog(String.format("   %s lost %dHP!", entity.getName(), totalDamage));
                    if(entity.getCurrentHP() == 0)
                    {
                        dungeon.clearEnemy(entity);
                        MysteryDungeon.updateLog(String.format("   %s was destroyed!", entity.getName()));
                        if(entity.isPlayer())
                        {
                            Move.respawn();
                        }
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
    public ArrayList<Entity> getDefender(Dungeon dungeon, Entity attacker)
    {
        ArrayList<Entity> affected = new ArrayList<>();
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
        return affected;
    }
    
    @Override
    public int getStamina()
    {
        return stamina;
    }
    
    private void doAnimation(int startX, int startY)
    {
        Animation anim = new RoomAnimation(startX - 16, startY - 16);
        Move.animate(anim, 5);
    }
    
    @Override
    public String getDescription()
    {
        return String.format("Power: %d; Range: Room", power);
    }
}
