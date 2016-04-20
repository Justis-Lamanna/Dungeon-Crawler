/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.move;

import java.util.ArrayList;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;
import mysterydungeon.entity.SpeciesEntity;
import mysterydungeon.MysteryDungeon;
import mysterydungeon.animation.Animation;
import mysterydungeon.animation.RangeAnimation;

/**
 * A standard long-range move.
 * This move affects entities the attacker is facing, but may be some distance
 * away. The farther the enemy is, the weaker the attack: It is at 100% power
 * at one tile away, 75% power at two tiles away, 56.25% power at three tiles away,
 * and so on. These moves have a 15% chance of missing, and no chance of doing
 * increased damage.
 * @author Justis
 */
public class RangeMove extends Move
{
    /**
     * A constant representing the fire gun attack.
     */
    public static final RangeMove FIRE_GUN = new RangeMove("Fire Gun", 15, 3, 10);
    
    /**
     * A constant representing the fire laser attack.
     */
    public static final RangeMove FIRE_LASER = new RangeMove("Fire Laser", 30, 4, 20);
    
    /**
     * A constant representing the fire rockets attack.
     */
    public static final RangeMove FIRE_ROCKET = new RangeMove("Fire Rocket", 45, 5, 30);
    
    /**
     * The value multiplied by this base power with each tile traveled.
     */
    public static final double MULTIPLIER = 0.75;
    
    private final String name;
    private final int power;
    private final int range;
    private final int stamina;
    private int currentPower;
    private int currentRange;
    
    /**
     * Creates a ranged move.
     * @param name The name of this move.
     * @param power The standard damage this attack will do, at one tile away.
     * @param range The farthest the attack will go before dying.
     * @param stamina The amount of stamina this attack will use.
     */
    public RangeMove(String name, int power, int range, int stamina)
    {
        this.name = name;
        this.power = power;
        this.stamina = stamina;
        this.currentPower = power;
        this.range = range;
    }
    
    /**
     * Get the type of move this is.
     * @return The constant Move.RANGE, defined in the Move class.
     */
    @Override
    public int getType()
    {
        return Move.RANGE;
    }
    
    /**
     * Get the name of this move. Not actually used.
     * @return The string "Blast".
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
     * Performs the actual attack.
     * @param dungeon The dungeon this attack occurs in.
     * @param attacker The entity performing the attack.
     * @param affected
     */
    @Override
    public void attack(Dungeon dungeon, SpeciesEntity attacker, ArrayList<SpeciesEntity> affected)
    {
        if(attacker.getCurrentStamina() < stamina)
        {
            MysteryDungeon.updateLog(String.format("%s doesn't have enough stamina!", attacker.getName()));
            return;
        }
        attacker.addStamina(-getStamina());
        MysteryDungeon.updateLog(String.format("%s fired off a %s!", attacker.getName(), name.split(" ")[1]));
        doAnimation(attacker.getX(), attacker.getY(), attacker.facing, currentRange);
        if(affected.isEmpty())
        {
            MysteryDungeon.updateLog("   But there was no target!");
        }
        else
        {
            SpeciesEntity defender = affected.get(0);
            if(Dungeon.PRNG.nextInt(100) < 15)
            {
                MysteryDungeon.updateLog("   Just missed!");
            }
            else
            {
                //
                int totalDamage = defender.addHP(-currentPower);
                MysteryDungeon.updateLog(String.format("   %s lost %dHP!", defender.getName(), totalDamage));
                if(defender.getCurrentHP() == 0)
                {
                    dungeon.clearEnemy(defender);
                    MysteryDungeon.updateLog(String.format("   %s was destroyed!", defender.getName()));
                    Move.faint(defender);
                }
            }
        }
    }
    
    /**
     * Gets the entity affected by this attack.
     * @param dungeon The dungeon this attack occurs in.
     * @param attacker The entity performing the attack.
     * @return The entity receiving the attack, or null if there is nobody.
     */
    @Override
    public ArrayList<SpeciesEntity> getDefender(Dungeon dungeon, SpeciesEntity attacker)
    {
        Node start = attacker.getCurrentNode();
        int facing = attacker.facing;
        currentPower = power;
        ArrayList<SpeciesEntity> affected = new ArrayList<>();
        for(int ds = 0; ds < range; ds++)
        {
            start = start.getPath(facing);
            if(start == null){break;}
            for(SpeciesEntity entity : dungeon.getEntities())
            {
                if(entity.getDestinationNode().equals(start))
                {
                    currentRange = ds;
                    affected.add(entity);
                    return affected;
                }
            }
            currentPower = (int)(currentPower * MULTIPLIER); //Reduces by 25% the farther away you go.
        }
        return new ArrayList<>();
    }
    
    @Override
    public int getStamina()
    {
        return stamina;
    }
    
    private void doAnimation(int startX, int startY, int direction, int range)
    {
        Animation anim = new RangeAnimation(startX, startY, direction, range);
        Animation.animate(anim, 5);
    }
    
    @Override
    public String getDescription()
    {
        return String.format("Power: %d; Range: %d", power, range);
    }
}
