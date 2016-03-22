/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;
import mysterydungeon.entity.Entity;

/**
 *
 * @author Justis
 */
public class DirectionAction extends AbstractAction
{
    private final int direction;
    private final DungeonComp comp;
    private final Dungeon dungeon;

    public DirectionAction(DungeonComp dungeon, int direction)
    {
            this.direction = direction;
            comp = dungeon;
            this.dungeon = dungeon.getDungeon();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
            Entity player = dungeon.getEntities().get(0);
            Node current = player.getCurrentNode();
            Node intended = current.getPath(direction);
            if(intended != null)
            {
                    player.setDestinationNode(intended);
                    player.facing = direction;
                    dungeon.updateAll();
            }
            comp.repaint();
    }
}
