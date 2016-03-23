/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.entity;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Justis
 */
public class Species
{
    private String name;
    private String imageFilename;
    private BufferedImage image;
    private boolean water;
    private int maxhp;

    public static final Species PLAYER = new Species("Player", "Sprites/player.png", 40);
    public static final Species ROBOT1 = new Species("Robot1", "Sprites/robot1.png", 20);
    public static final Species ROBOT2 = new Species("Robot2", "Sprites/robot2.png", 20);
    public static final Species ROBOT3 = new Species("X0L0TL", "Sprites/robot3.png", true, 20);
    public static final Species ROBOT4 = new Species("Robot4", "Sprites/robot4.png", true, 20);
    public static final Species ROBOT5 = new Species("Robot5", "Sprites/robot5.png", true, 20);
    public static final Species ROBOT6 = new Species("Robot6", "Sprites/robot6.png", true, 20);
    public static final Species ROBOT7 = new Species("Robot7", "Sprites/robot7.png", 20);
    public static final Species ROBOT8 = new Species("Robot8", "Sprites/robot8.png", 20);
    public static final Species ROBOT9 = new Species("Robot9", "Sprites/robot9.png", 20);
    public static final Species ROBOT10 = new Species("Robot10", "Sprites/robot10.png", 20);
    public static final Species ROBOT11 = new Species("Robot11", "Sprites/robot11.png", 20);
    public static final Species ROBOT12 = new Species("Robot12", "Sprites/robot12.png", 20);
    public static final Species ROBOT13 = new Species("Robot13", "Sprites/robot13.png", 20);
    public static final Species ROBOT14 = new Species("Robot14", "Sprites/robot14.png", 20);

    public Species(String name, String imageFilename, int hp)
    {
            this(name, imageFilename, false, hp);
    }

    public Species(String name, String imageFilename, boolean water, int hp)
    {
            this.name = name;
            this.imageFilename = imageFilename;
            this.image = null;
            this.water = water;
            this.maxhp = hp;
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

    public boolean isWater()
    {
            return water;
    }
    
    public int getHP()
    {
        return maxhp;
    }
}
