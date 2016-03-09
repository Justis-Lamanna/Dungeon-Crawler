import java.util.*;

public class Entity
{
	private Node currentNode;
	private Node gotoNode;
	private Dungeon dungeon;
	private Species species;
	private Random prng;

	public Entity(Dungeon dungeon, Species species, long seed)
	{
		this.dungeon = dungeon;
		this.species = species;
		prng = new Random(seed);
		randomizeLocation();
	}

	public Entity(Dungeon dungeon, Species species)
	{
		this(dungeon, species, System.currentTimeMillis());
	}

	private Node generateLocation()
	{
		ArrayList<RoomNode> rooms = dungeon.getRooms();
		int randomRoom = prng.nextInt(rooms.size());
		RoomNode room = rooms.get(randomRoom);
		ArrayList<Node> nodes = room.getNodes();
		int randomNode = prng.nextInt(nodes.size());
		//System.out.println(nodes.get(randomNode));
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

	public int getX()
	{
		return currentNode.getX();
	}

	public int getY()
	{
		return currentNode.getY();
	}

	@Override
	public String toString()
	{
		return String.format("[Entity: %s @ %d, %d]", species.getName(), getX(), getY());
	}
}