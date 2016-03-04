import java.util.*;

public class RoomNode
{
	private ArrayList<Node> containedNodes;
	private ArrayList<RoomNode> connections;

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