import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Dungeon extends JComponent
{
	private String tileFilename;
	private String tilemapFilename;

	private BufferedImage tilebase;
	private BufferedImage[] tiles;
	private int[][] tilemap;

	public Dungeon(String tileFilename, String tilemapFilename)
	{
		this.tileFilename = tileFilename;
		this.tilemapFilename = tilemapFilename;
		try
		{
			tilebase = ImageIO.read(new File(tileFilename));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		//tiles = generateTiles(tileFilename);
	}

	public void paint(Graphics g)
	{
		g.drawImage(tilebase, 0, 0, null);
	}

	/*private BufferedImage[] generateTiles(String filename)
	{
		BufferedImage tilebase = null;
		try
		{
			tilebase = ImageIO.read(new File(filename));
			return divideTiles(tilebase, 16, 16);
		}
		catch(IOException e)
		{
			System.out.println("Error in generateTiles");
			e.printStackTrace();
			System.exit(0);
		}
	}

	private BufferedImage[] divideTiles(Image image, int width, int height);
	{
		int tileWidth = image.getWidth() / width;
		int tileHeight = image.getHeight() / height;
		int totalTiles = tileWidth * tileHeight;
		Image[] returnArray = new Image[totalTiles];
		for(int tile = 0; tile < totalTiles; tile++)
		{
			returnArray[tile] = image.getSubimage()
		}
	}*/
}