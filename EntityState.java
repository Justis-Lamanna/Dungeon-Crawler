import java.util.*;

public abstract class EntityState
{
	abstract void doState(Entity e, Dungeon d);

	public Node nextNode(Entity entity, Dungeon dungeon, Node start, Node end)
	{
		LinkedList<Node> path = findShortestPath(entity, dungeon, start, end);
		//System.out.println(path);
		if(path.size() > 1){return path.get(1);} //0 is the current node, so 1 is the next node to go to.
		else{return start;}
	}

	public LinkedList<Node> findShortestPath(HashMap<Node, Node> map, Node start, Node end)
	{
		return readPath(map, start, end);
	}

	public LinkedList<Node> findShortestPath(Entity entity, Dungeon dungeon, Node start, Node end)
	{
		LinkedList<Node> path = readPath(findShortestPath(entity, dungeon, start), start, end);
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

	public LinkedList<Node> readPath(HashMap<Node, Node> map, Node start, Node target)
	{
		LinkedList<Node> ss = new LinkedList<>();
		Node uu = target;
		while(map.get(uu) != null)
		{
			ss.push(uu);
			uu = map.get(uu);
		}
		ss.push(uu);
		if(ss.peek().equals(start))
		{
			return ss;
		}
		else
		{
			return new LinkedList<Node>();
		}
	}

	protected boolean isValidNode(Entity entity, Dungeon dungeon, Node current, Node next)
	{
		if(next.getType() == Node.LAND && !isOccupied(entity, dungeon, next))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	protected boolean isOccupied(Entity entity, Dungeon dungeon, Node node)
	{
		ArrayList<Entity> enemies = dungeon.getEntities();
		Entity player = enemies.get(0);
		for(Entity enemy : enemies)
		{
			if(enemy.equals(player)){continue;}
			if(enemy.equals(entity)){break;}
			Node enemyNode = enemy.getCurrentNode();
			if(node.equals(enemyNode)){return true;}
		}
		return false;
	}
}