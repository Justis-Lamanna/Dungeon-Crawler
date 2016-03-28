/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.move;

import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.entity.Entity;

/**
 * The skeleton for a move.
 * @author Justis
 */
public interface Move
{

    /**
     * A constant representing a short-range move.
     */
    public static final int BRAWL = 0;

    /**
     * A constant representing a long-range move.
     */
    public static final int RANGE = 1;

    /**
     * A constant representing a room-range move.
     */
    public static final int ROOM = 2;
    
    /**
     * Determine the defender of the attack.
     * @param dungeon The dungeon the attack is performed in.
     * @param attacker The entity using the attack.
     * @return The entity receiving the attack.
     */
    Entity getDefender(Dungeon dungeon, Entity attacker);

    /**
     * Perform the attack.
     * @param dungeon The dungeon the attack is performed in.
     * @param attacker The entity using the attack.
     * @param defender The entity receiving the attack.
     */
    void attack(Dungeon dungeon, Entity attacker, Entity defender);

    /**
     * Returns a name for this attack.
     * @return The name of the attack.
     */
    String getName();

    /**
     * Get the type of attack this is.
     * @return An integer representing what type of attack this is (Short-range, etc)
     */
    int getType();
}
