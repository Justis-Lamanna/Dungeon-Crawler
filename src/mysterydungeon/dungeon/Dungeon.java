/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.dungeon;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import mysterydungeon.DungeonComp;
import mysterydungeon.entity.SpeciesEntity;
import mysterydungeon.entity.Species;
import mysterydungeon.MysteryDungeon;
import mysterydungeon.animation.AnimatedEntity;
import mysterydungeon.animation.BounceAnimation;
import mysterydungeon.entity.Entity;
import mysterydungeon.entity.ItemEntity;
import mysterydungeon.entity.StairEntity;
import mysterydungeon.item.*;

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
    
    /**
     * A sample list containing all items.
     */
    public static final Item[] TEST_ITEMS = {
        HPItem.REPAIR_V1, HPItem.REPAIR_V2, HPItem.REPAIR_V10,
        LightItem.TORCH, LightItem.FLASHLIGHT,
        RevealItem.SEEING_LIGHT,
        StaminaItem.AAA_BATTERY, StaminaItem.C_BATTERY, StaminaItem.NV_BATTERY
    };

    private DungeonLayout layout;
    private int[][] tilemap;
    private int[][] basemap;
    private Node[][] nodes;
    private final ArrayList<RoomNode> rooms = new ArrayList<>();

    private AnimatedEntity player;
    private final Species[] possibleSpecies;
    private final ArrayList<AnimatedEntity> enemies = new ArrayList<>();
    
    private BufferedImage mask = null;
    private BufferedImage shadow = null;
    
    private final Item[] possibleItems;
    private final ArrayList<Entity> items = new ArrayList<>();
    
    private StairEntity stairs;
    private int floor;
    private boolean up;

    /**
     * The random number generator, used in all instances of randomness in-game.
     */
    public static final Random PRNG = new Random();

    /**
     * Creates an instance of a Dungeon.
     * @param basemapFilename The filename of the base map this dungeon should display.
     * @param speciesList A list of possible species that may appear in the dungeon.
     * @param itemList A list of possible items that may appear in the dungeon.
     * @param up True if stairs go up, false if they go down.
     */
    public Dungeon(String basemapFilename, Species[] speciesList, Item[] itemList, boolean up)
    {
        this(new DungeonLayout(DungeonLayout.readLayout(basemapFilename)), speciesList, itemList, up);
    }
    
    /**
     * Creates an instance of a Dungeon.
     * @param layout The layout of this dungeon.
     * @param speciesList An array of the species that may appear in the dungeon.
     * @param itemList An array of the items that may appear in the dungeon.
     * @param up True if stairs go up, false if they go down.
     */
    public Dungeon(DungeonLayout layout, Species[] speciesList, Item[] itemList, boolean up)
    {
        possibleSpecies = speciesList;
        possibleItems = itemList;
        floor = 0;
        this.layout = layout;
        this.up = up;
    }
    
    /**
     * Initialize the next floor of the dungeon.
     * More specifically, this clears the log, initializes player, spawns
     * enemies and items, initializes the mask, generates the stairs, and
     * increments the floor by one. This method also allows you to conveniently
     * swap to a new layout.
     * @param nextFloorLayout The layout of the next floor.
     */
    public void startNextFloor(DungeonLayout nextFloorLayout)
    {
        layout = nextFloorLayout;
        startNextFloor();
    }
    
    /**
     * Initialize the next floor of the dungeon.
     * More specifically, this clears the log, initializes player, spawns
     * enemies and items, initializes the mask, generates the stairs, and
     * increments the floor by one. The layout of the dungeon will not change; this
     * essentially just acts as a retry for this floor.
     */
    public void startNextFloor()
    {
        loadDungeon();
        MysteryDungeon.clearLog();
        if(player == null)
        {
            //Players are a pseudo-singleton...There's only one player!
            player = new AnimatedEntity(this, Species.PLAYER, null, true);
            player.setAnimation(new BounceAnimation(player, 24));
            player.addItems(LightItem.TORCH, LightItem.FLASHLIGHT, RevealItem.SEEING_LIGHT);
        }
        else
        {
            player.randomizeLocation();
        }
        enemies.clear();
        spawnEnemies(1);
        items.clear();
        spawnItems(1);
        initializeMask();
        stairs = new StairEntity(this, up);
        floor++;
    }

    /**
     * Calculates the relevant data for the dungeon.
     */
    public void loadDungeon()
    {
        basemap = layout.getBaseMap();
        generateTilemap(basemap);
        findNodes();
        findPaths();
        findRooms();
    }

    private void generateTilemap(int[][] basemap)
    {
        tilemap = TileOp.convertTilemap(basemap);
            //TODO: Handle this better.
    }

    private void findNodes()
    {
        nodes = new Node[basemap.length][basemap[0].length];
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
     * Returns a list of all entities in this dungeon, as animated entities.
     * @return A list of AnimatedEntities, with the player occupying slot 0.
     */
    public ArrayList<AnimatedEntity> getEntities()
    {
        ArrayList<AnimatedEntity> entities = new ArrayList<>();
        entities.add(player);
        for(AnimatedEntity enemy : enemies)
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
            AnimatedEntity enemy = new AnimatedEntity(this, randomSpecies);
            enemy.setAnimation(new BounceAnimation(enemy, 24));
            spawnEnemy(enemy);
        }
    }
    
    public void spawnEnemy(AnimatedEntity enemy)
    {
        while(!isValidPosition(enemy))
        {
            enemy.randomizeLocation();
        }
        enemies.add(enemy);
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
    public void clearEnemy(SpeciesEntity enemy)
    {
        for(int index = 0; index < enemies.size(); index++)
        {
            if(enemies.get(index).getContained().equals(enemy))
            {
                enemies.remove(enemies.get(index));
                return;
            }
        }
    }

    private boolean isValidPosition(SpeciesEntity newEnemy)
    {
        int newEnemyX = newEnemy.getTileX();
        int newEnemyY = newEnemy.getTileY();
        if(newEnemyX == player.getX() && newEnemyY == player.getY())
        {
            return false;
        }
        for(AnimatedEntity enemy : enemies)
        {
            if(newEnemyX == enemy.getTileX() && newEnemyY == enemy.getTileY())
            {
                return false;
            }
        }
        return true;
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
     * Sets a certain tile as having been seen by the player.
     * More specifically, this calls appendMask() in the DungeonComp
     * class, after calculating the coordinates of the circle.
     * @param x The row of the tile to set as discovered.
     * @param y The column of the tile to set as discovered.
     */
    public void setDiscovered(int x, int y)
    {
        int circleX = x * DungeonComp.TILE_SIZE - ((shadow.getWidth() - DungeonComp.TILE_SIZE) / 2);
        int circleY = y * DungeonComp.TILE_SIZE - ((shadow.getHeight() - DungeonComp.TILE_SIZE) / 2);
        for(int xx = 0; xx < shadow.getWidth(); xx++)
        {
            for(int yy = 0; yy < shadow.getHeight(); yy++)
            {
                try
                {
                    int shadowRGB = shadow.getRGB(xx, yy);
                    int maskRGB = mask.getRGB(circleX+xx, circleY+yy);
                    int betterRGB = (shadowRGB >>> 24) < (maskRGB >>> 24) ? shadowRGB : maskRGB;
                    mask.setRGB(circleX+xx, circleY+yy, betterRGB);
                }
                catch(ArrayIndexOutOfBoundsException ex)
                {
                    //Nothing
                }
            }
        }
    }
    
    private void initializeMask()
    {
        //try{shadow = ImageIO.read(new File("Sprites/shadow.png"));}
        //catch(IOException ex){shadow = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);}
        shadow = generateShadow(100, 25);
        mask = new BufferedImage(tilemap[0].length * DungeonComp.TILE_SIZE, tilemap.length * DungeonComp.TILE_SIZE, BufferedImage.TYPE_4BYTE_ABGR);
        for(int xx = 0; xx < mask.getWidth(); xx++)
        {
            for(int yy = 0; yy < mask.getHeight(); yy++)
            {
                mask.setRGB(xx, yy, 0xFF000000);
            }
        }
        setDiscovered(
                ((SpeciesEntity)player.getContained()).getTileX(), 
                ((SpeciesEntity)player.getContained()).getTileY());
    }
    
    /**
     * Returns the current mask of the dungeon.
     * The mask contains 100-pixel diameter circles around each discovered node.
     * @return The mask, with unfamiliar areas blacked out, and visited areas visible.
     */
    public BufferedImage getMask()
    {
        return mask;
    }
    
    /**
     * Sets a new window to be used by the fog of war.
     * @param newShadow The new shadow. The image should be transparent where the mask
     * should clear, and solid black where it shouldn't.
     */
    public void setShadow(BufferedImage newShadow)
    {
        shadow = newShadow;
    }
    
    /**
     * Get the current shadow being used.
     * @return The shadow used currently.
     */
    public BufferedImage getShadow()
    {
        return shadow;
    }
    
    /**
     * Generate a circular fog of war type thing.
     * Dynamically creates a circle of the specified outerRadius. A concentric
     * circle of innerRadius is created as well, and this inner circle is set to full
     * transparency. Everything outside of this circle is faded to black, according
     * to the formula:
     * <br>Alpha = (256 / (outerRadius ^ 2)) * (distance from center ^ 2)
     * @param outerRadius The full radius of the circle, in pixels.
     * @param innerRadius The radius of full transparency inside the circle.
     * @return The circle generated.
     */
    public static BufferedImage generateShadow(int outerRadius, int innerRadius)
    {
        BufferedImage returnShadow = new BufferedImage(outerRadius * 2, outerRadius * 2, BufferedImage.TYPE_4BYTE_ABGR);
        int mid = returnShadow.getWidth() / 2;
        for(int xx = 0; xx < returnShadow.getWidth(); xx++)
        {
            for(int yy = 0; yy < returnShadow.getHeight(); yy++)
            {
                double distance = Point.distance(xx, yy, mid, mid);
                int rgb;
                if(distance < innerRadius / 2)
                {
                    rgb = 0;
                }
                else
                {
                    //This fancy formula is responsible for the fade from clear to black.
                    rgb = (int)((256.0 / (outerRadius * outerRadius)) * distance * distance);
                }
                if(rgb > 255){rgb = 255;}
                returnShadow.setRGB(xx, yy, rgb << 24);
            }
        }
        return returnShadow;
    }
    
    /**
     * Get a list of all items on the dungeon floor.
     * @return A list of all items in the dungeon.
     */
    public ArrayList<ItemEntity> getItems()
    {
        ArrayList<ItemEntity> newItems = new ArrayList<>();
        for(Entity item : items)
        {
            newItems.add((ItemEntity)item);
        }
        return newItems;
    }
    
    /**
     * Create a number of items.
     * This chooses a random item, from the list of items provided when the dungeon
     * was created. It is then placed in a random room.
     * @param number The number of items to spawn.
     */
    public void spawnItems(int number)
    {
        for(int count = 0; count < number; count++)
        {
            Item randomItem = possibleItems[PRNG.nextInt(possibleItems.length)];
            spawnItem(randomItem);
        }
    }
    
    /**
     * Creates an item.
     * This spawns a specific item, and places it in a random room.
     * @param item The item to spawn.
     */
    public void spawnItem(Item item)
    {
        ItemEntity itemEntity = new ItemEntity(item, this);
        items.add(itemEntity);
    }
    
    /**
     * Creates an item.
     * This spawns a specific item, and places it in a specific place.
     * @param item The item to spawn.
     * @param node The node to spawn it on.
     */
    public void spawnItem(Item item, Node node)
    {
        items.add(new ItemEntity(item, this, node));
    }
    
    /**
     * Removes all items on the dungeon floor.
     */
    public void clearItems()
    {
        items.clear();
    }
    
    /**
     * Removes a specific item entity on the dungeon floor.
     * @param item The item to remove.
     */
    public void clearItem(ItemEntity item)
    {
        items.remove(item);
    }
    
    /**
     * Get the stairs!
     * @return The StairEntity representing the stairs.
     */
    public StairEntity getStairs()
    {
        return stairs;
    }
    
    /**
     * Get this dungeon's layout.
     * @return The layout of this dungeon.
     */
    public DungeonLayout getLayout()
    {
        return layout;
    }
    
    /**
     * Get the current floor you're on.
     * @return The floor number. If negative, you're going down.
     */
    public int getFloor()
    {
        return up ? floor : -floor;
    }
    
    /**
     * Set the floor you're currently on.
     * @param floor The new floor number.
     */
    public void setFloor(int floor)
    {
        this.floor = floor;
    }
}
