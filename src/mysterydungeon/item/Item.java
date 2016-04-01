/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.item;

import mysterydungeon.entity.Entity;

/**
 *
 * @author Justis
 */
public interface Item
{
    public boolean useItem(Entity user);
}
