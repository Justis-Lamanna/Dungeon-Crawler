/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon;

import mysterydungeon.dungeon.Dungeon;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import mysterydungeon.animation.Animation;
import mysterydungeon.animation.AnimatedEntity;
import mysterydungeon.dungeon.Node;
import mysterydungeon.dungeon.RoomNode;
import mysterydungeon.entity.SpeciesEntity;
import mysterydungeon.entity.FollowState;
import mysterydungeon.entity.ItemEntity;
import mysterydungeon.move.Move;

/**
 * The class that is used to draw an instance of Dungeon.
 * <br><br>
 * The base map is specified in the following format:
 * <br>1. The width and height of the map, in number of tiles
 * <br>2. A series of numbers, with:
 * <br>a. 0, which means an obstacle.
 * <br>b. 1, which means a standard floor.
 * <br>c. 2, which means a water tile. (Not entirely working yet)
 * <br><br>
 * Tile maps are similar, except the tile number is used. Tile 0 is the top left
 * corner, Tile 1 is the one immediately to the right, and so on, wrapping to
 * the next line when the end of the row is reached.
 * @author Justis
 */
public class DungeonComp extends JComponent
{

    /**
     * The width and height of one tile, in pixels.
     */
    public static final int TILE_SIZE = 24;

    /**
     * A constant used to specify the base map should be drawn.
     */
    public static final boolean BASEMAP = false;

    /**
     * A constant used to specify the tile map should be drawn.
     */
    public static final boolean TILEMAP = true;
    
    private static DungeonComp singleton = null;

    private Dungeon dungeon;
    private BufferedImage[] tiles;
    private boolean mapToDraw = TILEMAP;
    private boolean drawNodes = false;
    private boolean drawPaths = false;
    private boolean drawRooms = false;
    private boolean drawEntity = true;
    private boolean drawMask = true;
    
    private ArrayList<Animation> animations = new ArrayList<>();

    private static BufferedImage attackImage;
    private static BufferedImage arrowImage;

    private DungeonComp(String tiles, Dungeon dungeon)
    {
        super();
        this.dungeon = dungeon;
        dungeon.startDungeon();
        generateTiles(tiles);
        try{attackImage = ImageIO.read(new File("Sprites/attack.png"));}
        catch(IOException ex){attackImage = new BufferedImage(32, 32, BufferedImage.TYPE_4BYTE_ABGR);}
        try{arrowImage = ImageIO.read(new File("Sprites/arrows.png"));}
        catch(IOException ex){arrowImage = new BufferedImage(32, 32, BufferedImage.TYPE_4BYTE_ABGR);}
    }
    
    /**
     * Creates and returns a singleton of this component.
     * As opposed to passing this component around, in order to make things repaint,
     * I've moved to the use of a singleton. This one needs to be called only once,
     * in order to initialize; When that's done, you can use the no argument form.
     * @param tiles Filename of the tiles image.
     * @param dungeon Dungeon to paint.
     * @return An instance of the DungeonComp.
     */
    public static DungeonComp getInstance(String tiles, Dungeon dungeon)
    {
        if(singleton == null)
        {
            singleton = new DungeonComp(tiles, dungeon);
        }
        return singleton;
    }
    
    /**
     * Returns a singleton of this component.
     * Once the version with parameters is used, there is no reason to provide
     * more parameters.
     * @return An instance of the DungeonComp.
     */
    public static DungeonComp getInstance()
    {
        return singleton;
    }

    @Override
    public void paint(Graphics g)
    {
        if(mapToDraw == TILEMAP){paintMap(g, 10);}
        else{paintBasemap(g, 10, 10, 14, 49);}
        if(drawNodes){paintNodes(g);}
        if(drawPaths){paintPaths(g);}
        if(drawRooms){paintRooms(g);}
        paintItems(g);
        g.drawImage(dungeon.getStairs().getImage(), dungeon.getStairs().getX() * TILE_SIZE, dungeon.getStairs().getY() * TILE_SIZE, null);
        if(drawEntity){paintEntities(g);}
        if(drawMask){paintMask(g);}
        for(Animation anim : animations)
        {
            g.drawImage(anim.getImage(), anim.getX(), anim.getY(), null);
        }
        paintMoves(g);
    }

    private void paintMap(Graphics g, int backgroundTile)
    {
        int[][] tilemap = dungeon.getTilemap();
        BufferedImage bgTile = tiles[backgroundTile];
        for(int row = 0; row < getHeight()/TILE_SIZE; row++)
        {
            for(int col = 0; col < getWidth()/TILE_SIZE; col++)
            {
                try
                {
                    BufferedImage tile = getTile(tiles, tilemap, row, col);
                    g.drawImage(tile, col*TILE_SIZE, row*TILE_SIZE, null);
                }
                catch(IndexOutOfBoundsException ex)
                {
                    g.setColor(Color.BLACK);
                    g.fillRect(col*TILE_SIZE, row*TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    private void paintBasemap(Graphics g, int backgroundTile, int obstacleTile, int groundTile, int waterTile)
    {
        BufferedImage bgTile = tiles[backgroundTile];
        BufferedImage obTile = tiles[obstacleTile];
        BufferedImage grTile = tiles[groundTile];
        BufferedImage waTile = tiles[waterTile];
        BufferedImage[] baseTiles = {obTile, grTile, waTile};
        int[][] basemap = dungeon.getBasemap();
        for(int row = 0; row < getHeight()/TILE_SIZE; row++)
        {
            for(int col = 0; col < getWidth()/TILE_SIZE; col++)
            {
                try
                {
                    BufferedImage tile = getTile(baseTiles, basemap, row, col);
                    g.drawImage(tile, col*TILE_SIZE, row*TILE_SIZE, null);
                }
                catch(IndexOutOfBoundsException ex)
                {
                    g.drawImage(bgTile, col*TILE_SIZE, row*TILE_SIZE, null);
                }
            }
        }
    }

    private void paintNodes(Graphics g)
    {
        Node[][] nodes = dungeon.getNodes();
        for(Node[] noderow : nodes)
        {
            for(Node node : noderow)
            {	
                if(node != null)
                {
                    int centerX = node.getX() * TILE_SIZE + (TILE_SIZE/4);
                    int centerY = node.getY() * TILE_SIZE + (TILE_SIZE/4);
                    if(node.getType() == 1){g.setColor(Color.BLACK);}
                    else if(node.getType() == 2){g.setColor(Color.BLUE);}
                    else{g.setColor(Color.WHITE);}
                    g.fillOval(centerX, centerY, TILE_SIZE/2, TILE_SIZE/2);
                }
            }
        }
    }
    
    private void paintMask(Graphics g)
    {
        g.drawImage(dungeon.getMask(), 0, 0, null);
    }

    private void paintPaths(Graphics g)
    {
        g.setColor(Color.BLACK);
        Node[][] nodes = dungeon.getNodes();
        for(Node[] noderow : nodes)
        {
            for(Node node : noderow)
            {	
                if(node != null)
                {
                    int centerX = node.getX() * TILE_SIZE + (TILE_SIZE/2);
                    int centerY = node.getY() * TILE_SIZE + (TILE_SIZE/2);
                    Node east = node.getPath(Node.EAST);
                    if(east != null)
                    {
                        int westX = east.getX() * TILE_SIZE + (TILE_SIZE/2);
                        int westY = east.getY() * TILE_SIZE + (TILE_SIZE/2);
                        g.drawLine(centerX, centerY, westX, westY);
                    }
                    Node south = node.getPath(Node.SOUTH);
                    if(south != null)
                    {
                        int southX = south.getX() * TILE_SIZE + (TILE_SIZE/2);
                        int southY = south.getY() * TILE_SIZE + (TILE_SIZE/2);
                        g.drawLine(centerX, centerY, southX, southY);
                    }
                    Node southeast = node.getPath(Node.SOUTHEAST);
                    if(southeast != null)
                    {
                        int southeastX = southeast.getX() * TILE_SIZE + (TILE_SIZE/2);
                        int southeastY = southeast.getY() * TILE_SIZE + (TILE_SIZE/2);
                        g.drawLine(centerX, centerY, southeastX, southeastY);
                    }
                    Node southwest = node.getPath(Node.SOUTHWEST);
                    if(southwest != null)
                    {
                        int southwestX = southwest.getX() * TILE_SIZE + (TILE_SIZE/2);
                        int southwestY = southwest.getY() * TILE_SIZE + (TILE_SIZE/2);
                        g.drawLine(centerX, centerY, southwestX, southwestY);
                    }
                }
            }
        }
    }

    private void paintRooms(Graphics g)
    {
        ArrayList<RoomNode> rooms = dungeon.getRooms();
        for(RoomNode room : rooms)
        {
            ArrayList<Node> nodes = room.getNodes();
            g.setColor(Color.RED);
            for(Node node : nodes)
            {
                int centerX = node.getX() * TILE_SIZE + (TILE_SIZE/4);
                int centerY = node.getY() * TILE_SIZE + (TILE_SIZE/4);
                g.fillRect(centerX, centerY, TILE_SIZE/2, TILE_SIZE/2);
            }

            ArrayList<Node> extNodes = room.getExteriorNodes();
            g.setColor(Color.ORANGE);
            for(Node node : extNodes)
            {
                int centerX = node.getX() * TILE_SIZE + (TILE_SIZE/4);
                int centerY = node.getY() * TILE_SIZE + (TILE_SIZE/4);
                g.fillRect(centerX, centerY, TILE_SIZE/2, TILE_SIZE/2);
            }

            g.setColor(Color.PINK);
            int roomCenterX = room.getX() * TILE_SIZE + (TILE_SIZE/4);
            int roomCenterY = room.getY() * TILE_SIZE + (TILE_SIZE/4);
            g.fillRect(roomCenterX, roomCenterY, TILE_SIZE/2, TILE_SIZE/2);
        }
    }
    
    private void paintItems(Graphics g)
    {
        ArrayList<ItemEntity> allItems = dungeon.getItems();
        for(ItemEntity item : allItems)
        {
            BufferedImage itemImage = item.getContained().getImage();
            int drawX = item.getX() * TILE_SIZE;
            int drawY = item.getY() * TILE_SIZE;
            g.drawImage(itemImage, drawX, drawY, null);
        }
    }

    private void paintEntities(Graphics g)
    {
        ArrayList<AnimatedEntity> allEntities = dungeon.getAnimatedEntities();
        for(AnimatedEntity anim : allEntities)
        {
            SpeciesEntity entity = anim.getContained();
            BufferedImage entityImage = anim.getImage();
            int drawX = calculateDrawPointX(anim.getX(), entityImage.getWidth());
            int drawY = calculateDrawPointY(anim.getY(), entityImage.getHeight());
            g.drawImage(entityImage, drawX, drawY, null);
            if(entity.getState() != null && entity.getState().getClass().equals(FollowState.class))
            {
                drawX += (entityImage.getWidth() - attackImage.getWidth()) / 2;
                drawY -= attackImage.getHeight();
                g.drawImage(attackImage, drawX, drawY, null);
            }
            else
            {
                int facing = entity.facing;
                drawX = calculateDrawPointX(entity.getX(), 32);
                drawY = calculateDrawPointY(entity.getY(), 32);
                BufferedImage arrow = arrowImage.getSubimage(0, facing*32, 32, 32);
                g.drawImage(arrow, drawX, drawY, null);
            }
            anim.animate();
        }
    }
    
    private void paintMoves(Graphics g)
    {
        ArrayList<Move> moves = dungeon.getEntities().get(0).getMoves();
        int keyWidth = getWidth() / 4;
        for(int index = 0; index < moves.size(); index++)
        {
            int startX = index * keyWidth;
            g.setColor(Color.WHITE);
            g.fillRect(startX, getHeight() - 40, keyWidth, 40);
            g.setColor(Color.RED);
            g.drawRect(startX, getHeight() - 40, keyWidth, 40);
            g.setColor(Color.BLACK);
            g.drawString(String.format("%d. %s (Stamina: %d)", index+1, moves.get(index).getName(), moves.get(index).getStamina()), startX + 10, getHeight() - 22);
            g.drawString(moves.get(index).getDescription(), startX + 10, getHeight() - 7);
        }
    }

    private int calculateDrawPointX(int pixel, int imageWidth)
    {
        return (pixel) - ((imageWidth - TILE_SIZE)/2);
    }

    private int calculateDrawPointY(int pixel, int imageHeight)
    {
        return (pixel) - (imageHeight - TILE_SIZE);
    }

    private void generateTiles(String filename)
    {
        BufferedImage tilebase = null;
        try
        {
            tilebase = ImageIO.read(new File(filename));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        divideTiles(tilebase, TILE_SIZE, TILE_SIZE);
    }

    private void divideTiles(BufferedImage image, int width, int height)
    {
        int tileWidth = image.getWidth(null) / width;
        int tileHeight = image.getHeight(null) / height;
        int totalTiles = tileWidth * tileHeight;
        tiles = new BufferedImage[totalTiles];
        for(int col = 0; col < tileHeight; col++)
        {
            for(int row = 0; row < tileWidth; row++)
            {
                    //
                    tiles[col*tileWidth+row] = image.getSubimage(row*height, col*width, height, width);
            }
        }
    }

    private static BufferedImage getTile(BufferedImage[] tiles, int[][] tilemap, int row, int col)
    {
        int tile = tilemap[row][col];
        return tiles[tile];
    }
    
    /**
     * Add an animation to be painted.
     * @param anim The animation to schedule for painting.
     */
    public void addAnimation(Animation anim)
    {
        animations.add(anim);
    }
    
    /**
     * Remove an animation, so it's no longer painted.
     * @param anim The animation to remove from the painting queue.
     */
    public void removeAnimation(Animation anim)
    {
        animations.remove(anim);
    }

    /**
     * Sets whether the base map or tile map should be drawn.
     * @param maptype The type of map to use.
     * @param repaint True of repaint should be called, false if not.
     */
    public void setDrawMap(boolean maptype, boolean repaint)
    {
        mapToDraw = maptype;
        if(repaint){repaint();}
    }

    /**
     * Completely reloads the dungeon.
     * @param repaint True if repaint should be called, false if not.
     */
    public void reopen(boolean repaint)
    {
        dungeon.startDungeon();
        if(repaint){repaint();}
    }

    /**
     * Toggles between drawing nodes and not drawing nodes.
     * @param repaint True if repaint should be called, false if not.
     */
    public void toggleNodes(boolean repaint)
    {
        drawNodes = !drawNodes;
        if(repaint){repaint();}
    }

    /**
     * Toggles between drawing paths and not drawing paths.
     * @param repaint True if repaint should be called, false if not.
     */
    public void togglePaths(boolean repaint)
    {
        drawPaths = !drawPaths;
        if(repaint){repaint();}
    }

    /**
     * Toggles between drawing room indicators and not drawing room indicators.
     * @param repaint True if repaint should be called, false if not.
     */
    public void toggleRooms(boolean repaint)
    {
        drawRooms = !drawRooms;
        if(repaint){repaint();}
    }

    /**
     * Toggles between drawing entities and not drawing entities.
     * @param repaint True if repaint should be called, false if not.
     */
    public void toggleEntity(boolean repaint)
    {
        drawEntity = !drawEntity;
        if(repaint){repaint();}

    }

    /**
     * Gets the Dungeon that this class is responsible for painting.
     * @return This dungeon this class is responsible for.
     */
    public Dungeon getDungeon()
    {
        return dungeon;
    }

    /**
     * Create some number of enemies.
     * @param number Number of enemies to spawn
     * @param repaint True if repaint should be called, false if not.
     */
    public void spawnEnemies(int number, boolean repaint)
    {
        dungeon.spawnEnemies(number);
        if(repaint){repaint();}
    }

    /**
     * Deletes all enemies.
     * @param repaint True if repaint should be called, false if not.
     */
    public void clearEnemies(boolean repaint)
    {
        dungeon.clearEnemies();
        if(repaint){repaint();}
    }
    
    /**
     * Toggles between drawing the mask or not. The mask is the darkness that
     * covers unexplored areas of the dungeon.
     * @param repaint True if repaint should be called, false if not.
     */
    public void toggleMask(boolean repaint)
    {
        drawMask = !drawMask;
        if(repaint){repaint();}
    }
}
