/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.move;

import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.entity.Entity;

/**
 *
 * @author Justis
 */
public interface Move
{
    public static final int BRAWL = 0;
    public static final int RANGE = 1;
    public static final int ROOM = 2;
    
    Entity getDefender(Dungeon dungeon, Entity attacker);
    void attack(Dungeon dungeon, Entity attacker, Entity defender);
    String getName();
    int getType();
}
