/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.move;

import mysterydungeon.entity.Entity;

/**
 *
 * @author Justis
 */
public interface Move
{
	boolean isValidAttack(Entity attacker, Entity defender);
	void attack(Entity attacker, Entity defender);
	String getName();
	int getType();
}
