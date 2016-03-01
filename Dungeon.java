import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Dungeon extends JComponent
{
	public static final int TILE_SIZE = 24;

	private String tileFilename;
	private String tilemapFilename;

	private BufferedImage tilebase;
	private BufferedImage[] tiles;
	private int[][] tilemap;

	public Dungeon(String tileFilename, String tilemapFilename)
	{
		this.tileFilename = tileFilename;
		this.tilemapFilename = tilemapFilename;
		tiles = generateTiles(tileFilename);
		tilemap = parseTilemap(tilemapFilename);
	}

	public void paint(Graphics g)
	{
		paintMap(g, 10);
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
					g.drawImage(getTile(row, col), col*TILE_SIZE, row*TILE_SIZE, null);
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

	private int[][] parseTilemap(String filename)
	{
		int[][] returnArray;
		try(Scanner inScanner = new Scanner(new File(filename)))
		{
			int width = inScanner.nextInt();
			int height = inScanner.nextInt();
			inScanner.nextLine(); //There's a newline left after parsing width and height; This consumes it.
			return parseTilemap(inScanner, width, height);
		}
		catch(FileNotFoundException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	private int[][] parseTilemap(Scanner inScanner, int width, int height)
	{
		int[][] returnArray = new int[height][width];
		for(int row = 0; row < height; row++)
		{
			String[] line = inScanner.nextLine().split(" +");
			for(int col = 0; col < width; col++)
			{
				returnArray[row][col] = Integer.parseInt(line[col]);
			}
		}
		return returnArray;
	}

	private BufferedImage getTile(int row, int col)
	{
		int tileNumber = tilemap[row][col];
		return tiles[tileNumber];
	}

	public void setTiles(String newTileFilename)
	{
		tileFilename = newTileFilename;
		tiles = generateTiles(tileFilename);
		repaint();
	}

	public void setTilemap(String newTilemapFilename)
	{
		tilemapFilename = newTilemapFilename;
		tilemap = parseTilemap(tilemapFilename);
		repaint();
	}
}