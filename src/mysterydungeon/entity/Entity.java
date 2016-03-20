/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.entity;

import java.util.ArrayList;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;
import mysterydungeon.dungeon.RoomNode;

/**
 *
 * @author Justis
 */
public class Entity
{
	private Node currentNode;
	private Node gotoNode;
	private Dungeon dungeon;
	private Species species;
	private EntityState currentState;
	
	public int facing;

	public Entity(Dungeon dungeon, Species species, EntityState startState)
	{
		this.dungeon = dungeon;
		this.species = species;
		randomizeLocation();
		currentState = startState;
	}

	public Entity(Dungeon dungeon, Species species)
	{
		this(dungeon, species, new MoveState());
	}

	private Node generateLocation()
	{
		ArrayList<RoomNode> rooms = dungeon.getRooms();
		int randomRoom = dungeon.prng.nextInt(rooms.size());
		RoomNode room = rooms.get(randomRoom);
		ArrayList<Node> nodes = room.getNodes();
		int randomNode = dungeon.prng.nextInt(nodes.size());
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

	public EntityState getState()
	{
		return currentState;
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
