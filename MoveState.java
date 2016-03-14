import java.util.*;

public class MoveState extends EntityState
{
	private Node previousRoom = null;

	public void doState(Entity e, Dungeon d)
	{
		//ArrayList<Node> graph = d.getNodesList();
		Node start = e.getCurrentNode();
		Node end = e.getDestinationNode();
		if(start.equals(end))
		{
			end = d.randomRoomNode(previousRoom, e.getDestinationNode());
			previousRoom = e.getDestinationNode();
			e.setDestinationNode(end);
		}
		Node next = nextNode(e, d, start, end);
		System.out.println(next);
		e.setCurrentNode(nextNode(e, d, start, end));
	}
}
