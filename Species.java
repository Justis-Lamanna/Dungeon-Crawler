import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.*;

public class Species
{
	private String name;
	private String imageFilename;
	private BufferedImage image;

	public static final Species PLAYER = new Species("Player", "Sprites/player.png");
	public static final Species ROBOT1 = new Species("Robot1", "Sprites/robot1.png");
	public static final Species ROBOT2 = new Species("Robot2", "Sprites/robot2.png");
	public static final Species ROBOT3 = new Species("X0L0TL", "Sprites/robot3.png");
	public static final Species ROBOT4 = new Species("Robot4", "Sprites/robot4.png");
	public static final Species ROBOT5 = new Species("Robot5", "Sprites/robot5.png");
	public static final Species ROBOT6 = new Species("Robot6", "Sprites/robot6.png");
	public static final Species ROBOT7 = new Species("Robot7", "Sprites/robot7.png");
	public static final Species ROBOT8 = new Species("Robot8", "Sprites/robot8.png");
	public static final Species ROBOT9 = new Species("Robot9", "Sprites/robot9.png");
	public static final Species ROBOT10 = new Species("Robot10", "Sprites/robot10.png");

	public Species(String name, String imageFilename)
	{
		this.name = name;
		this.imageFilename = imageFilename;
		this.image = null;
	}

	public String getName()
	{
		return name;
	}

	public BufferedImage getImage()
	{
		if(image == null)
		{	
			try
			{
				image = ImageIO.read(new File(imageFilename));
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
		return image;
	}
}