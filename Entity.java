import java.util.*;

public class Entity
{
	private Node currentNode;
	private Node gotoNode;
	private Dungeon dungeon;
	private Species species;
	private Random prng;
	private EntityState currentState;
	
	public int facing;

	public Entity(Dungeon dungeon, Species species, long seed, EntityState startState)
	{
		this.dungeon = dungeon;
		this.species = species;
		prng = new Random(seed);
		randomizeLocation();
		currentState = startState;
	}

	public Entity(Dungeon dungeon, Species species, long seed)
	{
		this(dungeon, species, seed, new MoveState());
	}

	public Entity(Dungeon dungeon, Species species, EntityState startState)
	{
		this(dungeon, species, System.currentTimeMillis(), startState);
	}

	public Entity(Dungeon dungeon, Species species)
	{
		this(dungeon, species, System.currentTimeMillis(), new MoveState());
	}

	private Node generateLocation()
	{
		ArrayList<RoomNode> rooms = dungeon.getRooms();
		int randomRoom = prng.nextInt(rooms.size());
		RoomNode room = rooms.get(randomRoom);
		ArrayList<Node> nodes = room.getNodes();
		int randomNode = prng.nextInt(nodes.size());
		return nodes.get(randomNode);
	}

	public void randomizeLocation()
	{
		currentNode = generateLocation();
		gotoNode = currentNode;
	}

	public Species getSpecies()
	{
		return species;
	}

	public Node getCurrentNode()
	{
		return currentNode;
	}

	public void setCurrentNode(Node n)
	{
		currentNode = n;
	}

	public Node getDestinationNode()
	{
		return gotoNode;
	}

	public void setDestinationNode(Node n)
	{
		gotoNode = n;
	}

	public int getX()
	{
		return currentNode.getX();
	}

	public int getY()
	{
		return currentNode.getY();
	}

	public void setState(EntityState state)
	{	
		currentState = state;
	}

	public void doState()
	{
		currentState.doState(this, dungeon);
	}

	public boolean isWater()
	{
		return species.isWater();
	}

	@Override
	public String toString()
	{
		return String.format("[Entity: %s @ %d, %d]", species.getName(), getX(), getY());
	}
}