/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;
import mysterydungeon.move.Move;

/**
 * A state which actively pursues the player.
 * @author Justis
 */
public class FollowState extends EntityState
{   

    /**
     * Generates the behavior of this entity.
     * More specifically, it finds the shortest path to the player, and
     * follows it.
     * @param e The entity this state is attached to.
     * @param d The dungeon this entity resides in.
     */
    @Override
    public void doState(SpeciesEntity e, Dungeon d)
    {
        if(e.getCurrentHP() > 0)
        {
            SpeciesEntity player = d.getEntities().get(0);
            ArrayList<Move> moves = (ArrayList<Move>)e.getMoves().clone();
            Collections.sort((List)moves);
            for(Move move : moves)
            {
                if(canUseMove(move, e, d))
                {
                    ArrayList<SpeciesEntity> playerList = new ArrayList<>();
                    playerList.add(player);
                    move.attack(d, e, playerList);
                    return;
                }
            }
            Node target = player.getDestinationNode();
            Node start = e.getCurrentNode();
            Node next = nextNode(e, d, start, target);
            if(!target.equals(next))
            {
                e.setDestinationNode(next);
            }
            else
            {
                for(int dir = 0; dir < 8; dir++)
                {
                    if(start.getPath(dir) != null)
                    {
                        e.setDestinationNode(start.getPath(dir));
                        break;
                    }
                }
            }
            pickupItem(d, e);
        }
    }

    /**
     * Returns an integer representing this state.
     * @return The constant number 1.
     */
    @Override
    public int isState()
    {
        return 1;
    }
    
    private boolean canUseMove(Move move, SpeciesEntity entity, Dungeon dungeon)
    {
        int oldFacing = entity.facing;
        SpeciesEntity player = dungeon.getEntities().get(0);
        for(int dir = 0; dir < 8; dir++)
        {
            ArrayList<SpeciesEntity> defenders = move.getDefender(dungeon, entity);
            if(defenders.contains(player))
            {
                return true;
            }
        }
        entity.facing = oldFacing;
        return false;
    }
}
