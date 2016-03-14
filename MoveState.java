import java.util.*;

public class MoveState extends EntityState
{
	private int cDirection = 0;
	private Node targetNode = null;

	public void doState(Entity e, Dungeon d)
	{
		Node current = e.getCurrentNode();
		if(d.getRoom(current) != null)
		{
			if(targetNode == null)
			{
				//Pick a target exit, not the same as what we just left.
				RoomNode currentRoom = d.getRoom(current);
				Node randomExteriorNode;
				Node randomInteriorNode;
				while(true)
				{
					int randomPosition = (int)(Math.random() * currentRoom.getExteriorNodes().size());
					randomExteriorNode = currentRoom.getExteriorNodes().get(randomPosition);
					randomInteriorNode = currentRoom.getJustInteriorNodes().get(randomPosition);
					if(!randomInteriorNode.equals(current)){break;}
				}
				targetNode = randomExteriorNode;
			}
			Node nextNode = nextNode(e, d, current, targetNode);
			cDirection = getDirection(current, nextNode);
			e.setCurrentNode(nextNode);
		}
		else
		{
			//You're on a pathway. Keep going the same way. If you're at an intersection, pick one at random.
			targetNode = null;
			ArrayList<Node> candidateNodes = new ArrayList<>();
			for(int dir = 0; dir < 8; dir++)
			{
				if(current.getPath(dir) != null)
				{
					candidateNodes.add(current.getPath(dir));
				}
			}
			if(candidateNodes.size() <= 2) //Straight, corner, or dead end.
			{
				for(int dir = 0; dir < 4; dir++)
				{
					int leftDirection = Math.floorMod(cDirection + dir, 8);
					int rightDirection = Math.floorMod(cDirection - dir, 8);
					if(current.getPath(leftDirection) != null)
					{
						cDirection = leftDirection;
						e.setCurrentNode(current.getPath(leftDirection));
						break;
					}
					else if(current.getPath(rightDirection) != null)
					{
						cDirection = rightDirection;
						e.setCurrentNode(current.getPath(rightDirection));
						break;
					}

				}
			}
			else
			{
				Node exclude = current.getPath((cDirection + 4) % 8);
				Node randomNode;
				while(true)
				{
					int randomPosition = (int)(Math.random() * candidateNodes.size());
					randomNode = candidateNodes.get(randomPosition);
					if(!randomNode.equals(exclude)){break;}
				}
				cDirection = getDirection(current, randomNode);
				e.setCurrentNode(randomNode);
			}

		}
	}

	public int getDirection(Node start, Node end)
	{
		for(int dir = 0; dir < 8; dir++)
		{
			if(start.getPath(dir) != null && start.getPath(dir).equals(end)){return dir;}
		}
		return -1;
	}
}
