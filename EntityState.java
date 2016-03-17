import java.util.*;

public abstract class EntityState
{
	abstract void doState(Entity e, Dungeon d);

	public Node nextNode(Entity entity, Dungeon dungeon, Node start, Node end)
	{
		LinkedList<Node> path = findShortestPath(entity, dungeon, start, end);
		return path.get(1); //0 is the current node, so 1 is the next node to go to.
	}

	public LinkedList<Node> findShortestPath(HashMap<Node, Node> map, Node end)
	{
		return readPath(map, end);
	}

	public LinkedList<Node> findShortestPath(Entity entity, Dungeon dungeon, Node start, Node end)
	{
		LinkedList<Node> path = readPath(findShortestPath(entity, dungeon, start), end);
		return path;
	}

	public HashMap<Node, Node> findShortestPath(Entity entity, Dungeon dungeon, Node start)
	{
		HashMap<Node, Integer> dist = new HashMap<>();
		HashMap<Node, Node> prev = new HashMap<>();
		LinkedList<Node> queue = new LinkedList<>();
		ArrayList<Node> graph = dungeon.getNodesList();

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
				if(vv == null || !isValidNode(entity, dungeon, uu, vv)){continue;}
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

	private Node minimumDistance(LinkedList<Node> queue, HashMap<Node, Integer> dist)
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

	private boolean isValidNode(Entity entity, Dungeon dungeon, Node current, Node next)
	{
		if(next.getType() == Node.LAND)
		{
			return true;
		}
		else if(next.getType() == Node.WATER && entity.isWater())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}