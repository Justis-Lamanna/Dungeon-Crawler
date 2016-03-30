/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.dungeon;

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
 * Class that's responsible for handling stuff related to the Dungeon, such as
 * finding nodes, paths, and rooms, as well as entity spawning and removing.
 * @author Justis
 */
public class Dungeon
{

    /**
     * A sample list containing all enemy Species.
     */
    public static final Species[] TEST_LIST = {
            Species.ROBOT1, Species.ROBOT2, Species.ROBOT3, 
            Species.ROBOT4, Species.ROBOT5, Species.ROBOT6, 
            Species.ROBOT7, Species.ROBOT8, Species.ROBOT9,
            Species.ROBOT10, Species.ROBOT11, Species.ROBOT12,
            Species.ROBOT13, Species.ROBOT14};

    private final String basemapFilename;
    private int[][] tilemap;
    private int[][] basemap;
    private Node[][] nodes;
    private final ArrayList<RoomNode> rooms = new ArrayList<>();
    private boolean[][] mask;

    private Entity player;
    private final Species[] possibleSpecies;
    private final ArrayList<Entity> enemies = new ArrayList<>();
    private final DungeonComp comp;

    /**
     * The random number generator, used in all instances of randomness in-game.
     */
    public static final Random PRNG = new Random();

    /**
     * Creates an instance of a Dungeon.
     * @param comp The DungeonComp that this dungeon is linked to.
     * @param basemapFilename The filename of the base map this dungeon should display.
     * @param speciesList A list of possible species that may appear in the dungeon.
     */
    public Dungeon(DungeonComp comp, String basemapFilename, Species[] speciesList)
    {
        this.comp = comp;
        this.basemapFilename = basemapFilename;
        possibleSpecies = speciesList;
    }
    
    /**
     * Calculates the relevant data for the dungeon, loads the player and enemies, and starts the game loop.
     */
    public void startDungeon()
    {
        loadDungeon();
        player = new Entity(this, Species.PLAYER, null, true);
        enemies.clear();
        spawnEnemies(1);
    }

    /**
     * Calculates the relevant data for the dungeon.
     */
    public void loadDungeon()
    {
        generateBasemap(basemapFilename);
        generateTilemap(basemap);
        findNodes();
        findPaths();
        findRooms();
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
    
    private void addUnvisitedToQueue(Node current, int direction, LinkedList<Node> nodesToVisit, boolean[][] visited)
    {
        Node nw = current.getPath(direction);
        if(nw != null && !visited[nw.getY()][nw.getX()] && isRoom(nw.getY(), nw.getX()))
        {
            nodesToVisit.add(nw);
            visited[nw.getY()][nw.getX()] = true;
        }
    }

    /**
     * Returns the base map of this dungeon.
     * @return A 2-dimensional array of the current map, with:<br>0 meaning obstacle<br>1 meaning passable<br>2 meaning water
     */
    public int[][] getBasemap()
    {
        return basemap;
    }

    /**
     * Returns the tile map of this dungeon.
     * @return A 2-dimensional array of the current map, with each value representing the tile number.
     */
    public int[][] getTilemap()
    {
        return tilemap;
    }

    /**
     * Returns all the nodes of this dungeon, in array form.
     * @return A 2-dimensional array of the current map, with a node on every passable space, and null on an obstacle.
     */
    public Node[][] getNodes()
    {
        return nodes;
    }

    /**
     * Returns all the rooms of this dungeon.
     * @return List of RoomNodes contained in this dungeon.
     */
    public ArrayList<RoomNode> getRooms()
    {
            return rooms;
    }

    /**
     * Returns all the entities in this dungeon.
     * @return List of all entities, with the player occupying slot 0.
     */
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

    /**
     * Spawns some number of enemies in this dungeon.
     * @param number The number of enemies to spawn.
     */
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
    
    /**
     * Spawns a specific enemy in this dungeon.
     * @param entity The entity to spawn.
     */
    public void spawnEnemy(Entity entity)
    {
        enemies.add(entity);
    }

    /**
     * Destroy all enemies in this dungeon.
     */
    public void clearEnemies()
    {
        enemies.clear();
    }

    /**
     * Destroy a specific enemy in this dungeon.
     * @param enemy The enemy to destroy.
     */
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

    /**
     * Causes each entity to act for its current turn, according to their state.
     */
    public void updateAll()
    {
        for(Entity enemy : enemies)
        {
            enemy.doState();
        }
    }

    /**
     * Generates a random node contained in the map.
     * @param type The type of node desired.
     * @return A random node of the specified type.
     */
    public Node randomNode(int type)
    {
        ArrayList<Node> list = getNodesList();
        int randomPosition;
        do
        {
            randomPosition = (int)(PRNG.nextInt(list.size()));
        } while (list.get(randomPosition).getType() != type);
        return list.get(randomPosition);
    }

    /**
     * Generates a random node in a room.
     * @param previous The previous node used, which is disqualified from randomness.
     * @param current The current node used, which is disqualified from randomness.
     * @return A random Node, guaranteed to be in a room.
     */
    public Node randomRoomNode(Node previous, Node current)
    {
        while(true)
        {
            int randomPosition = (int)(PRNG.nextInt(rooms.size()));
            Node randomNode = rooms.get(randomPosition).getCenter();
            if(!randomNode.equals(previous) && !randomNode.equals(current)){return randomNode;}
        }
    }

    /**
     * Returns all the nodes in a map, this time in a list.
     * @return A list of all nodes in this Dungeon.
     */
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

    /**
     * Determines the room a specific node is in.
     * @param node The node, which is used to find its room.
     * @return The RoomNode that the specified Node is contained in, or null if it's not in a room.
     */
    public RoomNode getRoom(Node node)
    {
        for(RoomNode room : rooms)
        {
            if(room.getNodes().contains(node)){return room;}
        }
        return null;
    }

    /**
     * Checks if a certain tile has been seen by the player or not.
     * @param row The row of the tile to check.
     * @param col The column of the tile to check.
     * @return True if the tile has been seen, false if it hasn't
     */
    public boolean isDiscovered(int row, int col)
    {
        return mask[row][col];
    }

    /**
     * Sets a certain tile as having been seen by the player.
     * @param row The row of the tile to set as discovered.
     * @param col The column of the tile to set as discovered.
     */
    public void setDiscovered(int row, int col)
    {
        //mask[row][col] = true;
        //Calculate where the new circle should be drawn
        int circleX = col * DungeonComp.TILE_SIZE;
        int circleY = row * DungeonComp.TILE_SIZE;
        comp.appendMask(circleX, circleY);
    }
    
    public boolean[][] getDiscovered()
    {
        return mask;
    }
}
