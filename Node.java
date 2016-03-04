public class Node
{
	private int x;
	private int y;
	private int type;
	private Node[] connections = new Node[8];

	public static final int NORTH = 0;
	public static final int NORTHEAST = 1;
	public static final int EAST = 2;
	public static final int SOUTHEAST = 3;
	public static final int SOUTH = 4;
	public static final int SOUTHWEST = 5;
	public static final int WEST = 6;
	public static final int NORTHWEST = 7;

	public Node(int type, int x, int y)
	{
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getType()
	{
		return type;
	}

	public void setPath(Node connect, int direction)
	{
		connections[direction] = connect;
	}

	public void setDoublePath(Node connect, int direction)
	{
		connections[direction] = connect;
		int oppositeDirection = (direction + 4) % 8;
		if(connect != null)
		{
			connect.setPath(this, oppositeDirection);
		}
	}

	public Node getPath(int direction)
	{
		return connections[direction];
	}
}