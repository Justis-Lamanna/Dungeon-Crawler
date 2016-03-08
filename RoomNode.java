import java.util.*;

public class RoomNode
{
	private ArrayList<Node> containedNodes;
	private ArrayList<RoomNode> connections;

	private Node centerNode;

	public RoomNode()
	{
		containedNodes = new ArrayList<Node>();
		connections = new ArrayList<RoomNode>();
	}

	public void addNode(Node node)
	{
		containedNodes.add(node);
	}

	public void addConnection(RoomNode node)
	{
		connections.add(node);
	}

	public void addDoubleConnection(RoomNode node)
	{
		connections.add(node);
		node.addConnection(this);
	}

	public ArrayList<Node> getNodes()
	{
		return containedNodes;
	}

	public boolean calculateCenter()
	{
		for(Node node : containedNodes)
		{
			if(
				containedNodes.contains(node.getPath(Node.NORTH)) &&
				containedNodes.contains(node.getPath(Node.EAST)) &&
				containedNodes.contains(node.getPath(Node.SOUTH)) &&
				containedNodes.contains(node.getPath(Node.WEST)))
			{
				centerNode = node;
				return true;
			}
		}
		//Nothing matched the criteria.
		return false;
	}

	public int getX()
	{
		return centerNode.getX();
	}

	public int getY()
	{
		return centerNode.getY();
	}

	@Override
	public String toString()
	{
		String ret = "";
		for(Node node : containedNodes)
		{
			ret += String.format("(%d, %d)\t", node.getX(), node.getY());
		}
		return ret;
	}
}