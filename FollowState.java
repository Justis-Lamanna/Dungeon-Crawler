public class FollowState extends EntityState
{
	public void doState(Entity e, Dungeon d)
	{
		Entity player = d.getEntities().get(0);
		Node target = player.getCurrentNode();
		Node start = e.getCurrentNode();
		e.setCurrentNode(nextNode(e, d, start, target));
	}
}