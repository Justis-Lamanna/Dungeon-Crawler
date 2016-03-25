/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.dungeon;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import mysterydungeon.DungeonComp;
import mysterydungeon.entity.Entity;
import mysterydungeon.entity.Species;

/**
 *
 * @author Justis
 */
public class Dungeon
{
	public static final Species[] TEST_LIST = {
		Species.ROBOT1, Species.ROBOT2, Species.ROBOT3, 
		Species.ROBOT4, Species.ROBOT5, Species.ROBOT6, 
		Species.ROBOT7, Species.ROBOT8, Species.ROBOT9,
		Species.ROBOT10, Species.ROBOT11, Species.ROBOT12,
                Species.ROBOT13, Species.ROBOT14};

	private final String tilemapFilename;
	private int[][] tilemap;
	private int[][] basemap;
	private Node[][] nodes;
	private final ArrayList<RoomNode> rooms = new ArrayList<>();
        private boolean[][] mask;

	private Entity player;
	private final Species[] possibleSpecies;
	private final ArrayList<Entity> enemies = new ArrayList<>();
        private final DungeonComp comp;
        
        public static final Random PRNG = new Random();
        public boolean gameRunning = true;

	public Dungeon(DungeonComp comp, String tilemapFilename, Species[] speciesList)
	{
		this.comp = comp;
                this.tilemapFilename = tilemapFilename;
		possibleSpecies = speciesList;
		loadDungeon();
                GameLoop loop = new GameLoop(comp);
                loop.start();
	}

	public void loadDungeon()
	{
		generateBasemap(tilemapFilename);
		generateTilemap(basemap);
		findNodes();
		findPaths();
		findRooms();
		player = new Entity(this, Species.PLAYER, null, true);
		enemies.clear();
		spawnEnemies(1);
	}

	private void generateBasemap(String filename)
	{
		try
		{	
			Scanner inScanner = new Scanner(new File(filename));
			int mapWidth = inScanner.nextInt();
			int mapHeight = inScanner.nextInt();
			inScanner.nextLine(); //There's a newline left after nextInt. This eats the newLine.
			basemap = new int[mapHeight][mapWidth];
			for(int row = 0; row < basemap.length; row++)
			{
				String[] line = inScanner.nextLine().split(" +");
				for(int col = 0; col < line.length; col++)
				{
					basemap[row][col] = Integer.parseInt(line[col]);
				}
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}

	private void generateTilemap(int[][] basemap)
	{
		tilemap = TileOp.convertTilemap(basemap);
		//TODO: Handle this better.
	}

	private void findNodes()
	{
		nodes = new Node[basemap.length][basemap[0].length];
                mask = new boolean[basemap.length][basemap[0].length];
		for(int row = 0; row < basemap.length; row++)
		{
			for(int col = 0; col < basemap[0].length; col++)
			{
				if(basemap[row][col] != 0)
				{
					nodes[row][col] = new Node(basemap[row][col], col, row);
                                        mask[row][col] = false;
				}
			}
		}
	}

	private void findPaths()
	{
		for(int row = 0; row < nodes.length; row++)
		{
			for(int col = 0; col < nodes[0].length; col++)
			{
				Node current = nodes[row][col];
				if(current != null)
				{
					Node east;
					try{east = nodes[row][col+1];}
					catch(ArrayIndexOutOfBoundsException ex){east = null;}
					current.setDoublePath(east, Node.EAST);
					Node south;
					try{south = nodes[row+1][col];}
					catch(ArrayIndexOutOfBoundsException ex){south = null;}
					current.setDoublePath(south, Node.SOUTH);
					if(east != null && south != null)
					{
						Node southeast;
						try{southeast = nodes[row+1][col+1];}
						catch(ArrayIndexOutOfBoundsException ex){southeast = null;}
						current.setDoublePath(southeast, Node.SOUTHEAST);
					}
					Node west;
					try{west = nodes[row][col-1];}
					catch(ArrayIndexOutOfBoundsException ex){west = null;}
					if(west != null && south != null)
					{
						Node southwest;
						try{southwest = nodes[row+1][col-1];}
						catch(ArrayIndexOutOfBoundsException ex){southwest = null;}
						current.setDoublePath(southwest, Node.SOUTHWEST);
					}
				}
			}
		}
	}

	private void findRooms()
	{
		rooms.clear();
		boolean[][] visited = new boolean[nodes.length][nodes[0].length];
		for(int row = 0; row < nodes.length; row++)
		{
			for(int col = 0; col < nodes[0].length; col++)
			{
				if(nodes[row][col] != null && !visited[row][col] && isRoom(row, col))
				{
					createRoom(row, col, visited);
				}
			}
		}
	}

	private boolean isRoom(int row, int col)
	{
		Node current = nodes[row][col];
		Node northwest = current.getPath(Node.NORTHWEST);
		Node northeast = current.getPath(Node.NORTHEAST);
		Node southwest = current.getPath(Node.SOUTHWEST);
		Node southeast = current.getPath(Node.SOUTHEAST);
		if(
			(northwest != null && northwest.getType() == current.getType()) ||
			(northeast != null && northeast.getType() == current.getType()) ||
			(southwest != null && southwest.getType() == current.getType()) ||
			(southeast != null && southeast.getType() == current.getType()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private void createRoom(int row, int col, boolean[][] visited)
	{
		LinkedList<Node> nodesToVisit = new LinkedList<>();
		RoomNode room = new RoomNode();
		nodesToVisit.add(nodes[row][col]);
		while(nodesToVisit.size() > 0)
		{
			Node current = nodesToVisit.remove();
			visited[current.getY()][current.getX()] = true;
			room.addNode(current);
			addUnvisitedToQueue(current, Node.NORTHWEST, nodesToVisit, visited);
			addUnvisitedToQueue(current, Node.NORTH, nodesToVisit, visited);
			addUnvisitedToQueue(current, Node.NORTHEAST, nodesToVisit, visited);
			addUnvisitedToQueue(current, Node.EAST, nodesToVisit, visited);
			addUnvisitedToQueue(current, Node.SOUTHEAST, nodesToVisit, visited);
			addUnvisitedToQueue(current, Node.SOUTH, nodesToVisit, visited);
			addUnvisitedToQueue(current, Node.SOUTHWEST, nodesToVisit, visited);
			addUnvisitedToQueue(current, Node.WEST, nodesToVisit, visited);
		}
		if(room.calculateCenter()) //If a center can't be found, we consider this to mean it wasn't a room after all.
		{
			room.calculateExteriorNodes();
			rooms.add(room);
		}
	}

	public void addUnvisitedToQueue(Node current, int direction, LinkedList<Node> nodesToVisit, boolean[][] visited)
	{
		Node nw = current.getPath(direction);
		if(nw != null && !visited[nw.getY()][nw.getX()] && isRoom(nw.getY(), nw.getX()))
		{
			nodesToVisit.add(nw);
			visited[nw.getY()][nw.getX()] = true;
		}
	}

	public int[][] getBasemap()
	{
		return basemap;
	}

	public int[][] getTilemap()
	{
		return tilemap;
	}

	public Node[][] getNodes()
	{
		return nodes;
	}

	public ArrayList<RoomNode> getRooms()
	{
		return rooms;
	}

	public ArrayList<Entity> getEntities()
	{
		ArrayList<Entity> entities = new ArrayList<>();
		entities.add(player);
		for(Entity enemy : enemies)
		{
			entities.add(enemy);
		}
		return entities;
	}

	public void spawnEnemies(int number)
	{
		for(int count = 0; count < number; count++)
		{
			Species randomSpecies = possibleSpecies[PRNG.nextInt(possibleSpecies.length)];
			Entity enemy = new Entity(this, randomSpecies);
			while(!isValidPosition(enemy))
			{
				enemy.randomizeLocation();
			}
			enemies.add(enemy);
		}
	}

	public void clearEnemies()
	{
		enemies.clear();
	}
        
        public void clearEnemy(Entity enemy)
        {
            enemies.remove(enemy);
        }

	private boolean isValidPosition(Entity newEnemy)
	{
		int newEnemyX = newEnemy.getX();
		int newEnemyY = newEnemy.getY();
		if(newEnemyX == player.getX() && newEnemyY == player.getY())
		{
			return false;
		}
		for(Entity enemy : enemies)
		{
			if(newEnemyX == enemy.getX() && newEnemyY == enemy.getY())
			{
				return false;
			}
		}
		return true;
	}

	public void updateAll()
	{
		for(Entity enemy : enemies)
		{
			enemy.doState();
		}
	}

	public Node randomNode(int type)
	{
		ArrayList<Node> list = getNodesList();
		int randomPosition;
		do
		{
			randomPosition = (int)(Math.random() * list.size());
		} while (list.get(randomPosition).getType() != type);
		return list.get(randomPosition);
	}

	public Node randomRoomNode(Node previous, Node current)
	{
		while(true)
		{
			int randomPosition = (int)(Math.random() * rooms.size());
			Node randomNode = rooms.get(randomPosition).getCenter();
			if(!randomNode.equals(previous) && !randomNode.equals(current)){return randomNode;}
		}
	}

	public ArrayList<Node> getNodesList()
	{
		ArrayList<Node> retList = new ArrayList<>();
		for(int row = 0; row < nodes.length; row++)
		{
			for(int col = 0; col < nodes[0].length; col++)
			{
				if(nodes[row][col] != null)
				{
					retList.add(nodes[row][col]);
				}
			}
		}
		return retList;
	}

	public RoomNode getRoom(Node node)
	{
		for(RoomNode room : rooms)
		{
			if(room.getNodes().contains(node)){return room;}
		}
		return null;
	}
        
        public boolean isDiscovered(int row, int col)
        {
            return mask[row][col];
        }
        
        public void setDiscovered(int row, int col)
        {
            mask[row][col] = true;
        }
        
        public void setDiscovered(int row, int col, int range)
        {
            /*for(int drow = row-range; drow < row+range; drow++)
            {
                for(int dcol = col-range; dcol < col+range; dcol++)
                {
                    try
                    {
                        mask[drow][dcol] = true;
                    }
                    catch(ArrayIndexOutOfBoundsException ex)
                    {
                        //Do nothing.
                    }
                }
            }*/
            setDiscovered(row, col, range, range, range, range);
        }
        
        public void setDiscovered(int row, int col, int plusrow, int minusrow, int pluscol, int minuscol)
        {
            for(int drow = row-minusrow; drow <= row+plusrow; drow++)
            {
                for(int dcol = col-minuscol; dcol <= col+pluscol; dcol++)
                {
                    try
                    {
                        mask[drow][dcol] = true;
                    }
                    catch(ArrayIndexOutOfBoundsException ex)
                    {
                        //Do nothing.
                    }
                }
            }
        }
}
