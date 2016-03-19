package bui.dungeon;

public class FollowState extends EntityState
{
	public void doState(Entity e, Dungeon d)
	{
		Entity player = d.getEntities().get(0);
		Node target = player.getCurrentNode();
		Node start = e.getCurrentNode();
		Node next = nextNode(e, d, start, target);
		if(!target.equals(next))
		{
			e.setCurrentNode(next);
		}
	}

	public int isState()
	{
		return 1;
	}
}