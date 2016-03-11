import java.util.*;

public class MoveState implements EntityState
{
	private Node previousRoom = null;

	public void doState(Entity e, Dungeon d)
	{
		ArrayList<Node> graph = d.getNodesList();
		Node start = e.getCurrentNode();
		Node end = e.getDestinationNode();
		if(start.equals(end))
		{
			end = d.randomRoomNode(previousRoom, e.getDestinationNode());
			previousRoom = e.getDestinationNode();
			e.setDestinationNode(end);
		}
		LinkedList<Node> path = readPath(findNextNode2(graph, start, end), end);
		e.setCurrentNode(path.get(1));
	}

	public HashMap<Node, Node> findNextNode2(ArrayList<Node> graph, Node start, Node end)
	{
		HashMap<Node, Integer> dist = new HashMap<>();
		HashMap<Node, Node> prev = new HashMap<>();
		LinkedList<Node> queue = new LinkedList<>();

		for(Node node : graph)
		{
			dist.put(node, Integer.MAX_VALUE);
			prev.put(node, null);
			queue.add(node);
		}

		dist.put(start, 0);

		while(queue.size() > 0)
		{
			Node uu = minimumDistance(queue, dist);
			queue.remove(uu);
			for(int dir = 0; dir < 8; dir++)
			{
				Node vv = uu.getPath(dir);
				if(vv == null || vv.getType() != uu.getType()){continue;}
				int alt = dist.get(uu) + 1;
				if(alt < dist.get(vv))
				{
					dist.put(vv, alt);
					prev.put(vv, uu);
				}
			}
		}

		return prev;
	}

	public Node minimumDistance(LinkedList<Node> queue, HashMap<Node, Integer> dist)
	{
		int min = Integer.MAX_VALUE;
		Node minNode = queue.get(0);
		for(Node node : queue)
		{
			int nodeDist = dist.get(node);
			if(nodeDist < min)
			{
				min = nodeDist;
				minNode = node;
			}
		}
		return minNode;
	}

	public LinkedList<Node> readPath(HashMap<Node, Node> map, Node target)
	{
		LinkedList<Node> ss = new LinkedList<>();
		Node uu = target;
		while(map.get(uu) != null)
		{
			ss.push(uu);
			uu = map.get(uu);
		}
		ss.push(uu);
		return ss;
	}
}
