package bui.dungeon;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

public class DirectionAction extends AbstractAction
{
	private int direction;
	private DungeonComp comp;
	private Dungeon dungeon;

	public DirectionAction(DungeonComp dungeon, int direction)
	{
		this.direction = direction;
		comp = dungeon;
		this.dungeon = dungeon.getDungeon();
	}

	public void actionPerformed(ActionEvent e)
	{
		Entity player = dungeon.getEntities().get(0);
		Node current = player.getCurrentNode();
		Node intended = current.getPath(direction);
		if(intended == null)
		{
			System.out.println("Oof");
		}
		else
		{
			player.setCurrentNode(intended);
			player.facing = direction;
			dungeon.updateAll();
		}
		comp.repaint();
	}
}