//
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Dungeon extends JComponent
{
	public static final int TILE_SIZE = 24;
	public static final boolean BASEMAP = false;
	public static final boolean TILEMAP = true;

	private String tileFilename;
	private String tilemapFilename;
	private BufferedImage tilebase;
	private BufferedImage[] tiles;
	private int[][] tilemap;
	private int[][] basemap;

	private boolean mapToDraw = BASEMAP;

	public Dungeon(String tileFilename, String tilemapFilename)
	{
		this.tileFilename = tileFilename;
		this.tilemapFilename = tilemapFilename;
		tiles = generateTiles(tileFilename);
		basemap = generateBasemap(tilemapFilename);
		tilemap = generateTilemap(basemap);
	}

	public void paint(Graphics g)
	{
		if(mapToDraw == TILEMAP){paintMap(g, 10);}
		else{paintBasemap(g, 10, 10, 14, 50);}
	}

	private void paintMap(Graphics g, int backgroundTile)
	{
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

	private BufferedImage[] generateTiles(String filename)
	{
		BufferedImage tilebase = null;
		try
		{
			tilebase = ImageIO.read(new File(filename));
		}
		catch(IOException e)
		{
			System.out.println("Error in generateTiles");
			e.printStackTrace();
			System.exit(0);
		}
		return divideTiles(tilebase, TILE_SIZE, TILE_SIZE);
	}

	private BufferedImage[] divideTiles(BufferedImage image, int width, int height)
	{
		int tileWidth = image.getWidth(null) / width;
		int tileHeight = image.getHeight(null) / height;
		int totalTiles = tileWidth * tileHeight;
		BufferedImage[] returnArray = new BufferedImage[totalTiles];
		for(int col = 0; col < tileHeight; col++)
		{
			for(int row = 0; row < tileWidth; row++)
			{
				returnArray[col*tileWidth+row] = image.getSubimage(row*height, col*width, height, width);
			}
		}
		return returnArray;
	}

	private int[][] generateBasemap(String filename)
	{
		int[][] returnValue = null;
		try
		{	
			Scanner inScanner = new Scanner(new File(filename));
			int mapWidth = inScanner.nextInt();
			int mapHeight = inScanner.nextInt();
			inScanner.nextLine(); //There's a newline left after nextInt. This eats the newLine.
			returnValue = new int[mapHeight][mapWidth];
			for(int row = 0; row < returnValue.length; row++)
			{
				String[] line = inScanner.nextLine().split(" +");
				for(int col = 0; col < line.length; col++)
				{
					returnValue[row][col] = Integer.parseInt(line[col]);
				}
			}
		}
		catch(FileNotFoundException ex)
		{
			ex.printStackTrace();
		}
		return returnValue;
	}

	private int[][] generateTilemap(int[][] basemap)
	{
		return TileOp.convertTilemap(basemap);
	}

	private int[][] generateTilemap(String filename)
	{
		return generateBasemap(filename);
	}

	private BufferedImage getTile(BufferedImage[] tiles, int[][] tilemap, int row, int col)
	{
		int tile = tilemap[row][col];
		return tiles[tile];
	}

	public void setDrawMap(boolean maptype)
	{
		mapToDraw = maptype;
		repaint();
	}

	public void refresh()
	{
		tilemap = generateTilemap(basemap);
		repaint();
	}

	public void reopen()
	{
		basemap = generateBasemap(tilemapFilename);
		tilemap = generateTilemap(basemap);
		repaint();
	}
}