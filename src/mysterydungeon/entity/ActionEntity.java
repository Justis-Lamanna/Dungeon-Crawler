/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.entity;

/**
 * Entity with an action.
 * These are entities that do something every turn. This would apply to the
 * player, allies, and enemies, to dictate their motion. This is also used for
 * items and stairs, which check if they've been stepped on and perform some action.
 * @author jlamanna
 */
public interface ActionEntity extends Entity
{
    /**
     * A function that will be automatically called on each turn.
     * For entities, this will generally hand off to the state. For
     * items and stairs, this will check for someone standing on it.
     */
    void onTurn();
}
