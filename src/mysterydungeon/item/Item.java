/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.item;

import mysterydungeon.entity.Entity;

/**
 * The interface for a usable item in this game.
 * @author Justis
 */
public interface Item
{
    /**
     * Dictate what the item will do when used.
     * @param user The user of the item.
     * @return True if the item should be consumed, false if not.
     */
    boolean useItem(Entity user);
}
