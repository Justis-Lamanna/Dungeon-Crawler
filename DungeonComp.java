//
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DungeonComp extends JComponent
{
	public static final int TILE_SIZE = 24;
	public static final boolean BASEMAP = false;
	public static final boolean TILEMAP = true;

	private Dungeon dungeon;
	private BufferedImage[] tiles;
	private boolean mapToDraw = TILEMAP;
	private boolean drawNodes = false;
	private boolean drawPaths = false;
	private boolean drawRooms = false;
	private boolean drawEntity = true;

	public DungeonComp(String tileFilename, String tilemapFilename)
	{
		dungeon = new Dungeon(tilemapFilename, Dungeon.TEST_LIST);
		generateTiles(tileFilename);
	}

	public void paint(Graphics g)
	{
		if(mapToDraw == TILEMAP){paintMap(g, 10);}
		else{paintBasemap(g, 10, 10, 14, 49);}
		if(drawNodes){paintNodes(g);}
		if(drawPaths){paintPaths(g);}
		if(drawRooms){paintRooms(g);}
		if(drawEntity){paintEntities(g);}
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
					g.drawImage(bgTile, col*TILE_SIZE, row*TILE_SIZE, null);
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

	private void paintEntities(Graphics g)
	{
		ArrayList<Entity> allEntities = dungeon.getEntities();
		for(Entity entity : allEntities)
		{
			BufferedImage entityImage = entity.getSpecies().getImage();
			int drawX = (entity.getX() * TILE_SIZE) - ((entityImage.getWidth() - TILE_SIZE) / 2);
			int drawY = (entity.getY() * TILE_SIZE) - (entityImage.getHeight() - TILE_SIZE);
			g.drawImage(entityImage, drawX, drawY, null);
		}
	}

	private int calculateDrawPoint(int tileNumber, int imageWidth)
	{
		return (tileNumber * TILE_SIZE) - ((imageWidth - TILE_SIZE)/2);
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
				tiles[col*tileWidth+row] = image.getSubimage(row*height, col*width, height, width);
			}
		}
	}

	private static BufferedImage getTile(BufferedImage[] tiles, int[][] tilemap, int row, int col)
	{
		int tile = tilemap[row][col];
		return tiles[tile];
	}

	public void setDrawMap(boolean maptype, boolean repaint)
	{
		mapToDraw = maptype;
		if(repaint){repaint();}
	}

	public void reopen(boolean repaint)
	{
		dungeon.loadDungeon();
		if(repaint){repaint();}
	}

	public void toggleNodes(boolean repaint)
	{
		drawNodes = !drawNodes;
		if(repaint){repaint();}
	}

	public void togglePaths(boolean repaint)
	{
		drawPaths = !drawPaths;
		if(repaint){repaint();}
	}

	public void toggleRooms(boolean repaint)
	{
		drawRooms = !drawRooms;
		if(repaint){repaint();}
	}

	public void toggleEntity(boolean repaint)
	{
		drawEntity = !drawEntity;
		if(repaint){repaint();}
		
	}

	public void kachunk(boolean repaint)
	{
		dungeon.updateAll();
		if(repaint){repaint();}
	}

	public Dungeon getDungeon()
	{
		return dungeon;
	}

	public void spawnEnemies(int number, boolean repaint)
	{
		dungeon.spawnEnemies(number);
		if(repaint){repaint();}
	}

	public void clearEnemies(boolean repaint)
	{
		dungeon.clearEnemies();
		if(repaint){repaint();}
	}
}