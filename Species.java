import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.*;

public class Species
{
	private String name;
	private BufferedImage image;

	public static final Species PLAYER = new Species("Player", "Sprites/player.png");
	public static final Species ROBOT1 = new Species("Robot1", "Sprites/robot1.png");
	public static final Species ROBOT2 = new Species("Robot2", "Sprites/robot2.png");

	public Species(String name, String imageFilename)
	{
		this.name = name;
		this.image = getImage(imageFilename);
	}

	private BufferedImage getImage(String filename)
	{
		BufferedImage sprite = null;
		try
		{
			sprite = ImageIO.read(new File(filename));
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		return sprite;
	}

	public String getName()
	{
		return name;
	}

	public BufferedImage getImage()
	{
		return image;
	}
}