/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.animation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author jlamanna
 */
public class RoomAnimation implements Animation
{
    private int x;
    private int y;
    private BufferedImage image;
    private int counter;
    
    public RoomAnimation(int startX, int startY)
    {
        x = startX;
        y = startY;
        counter = 0;
        try{image = ImageIO.read(new File("Sprites/room.png"));}
        catch(IOException ex){ex.printStackTrace();}
    }
    
    @Override
    public int getX()
    {
        return x;
    }
    
    @Override
    public int getY()
    {
        return y;
    }
    
    @Override
    public BufferedImage getImage()
    {
        if(counter < 8)
        {
            return image.getSubimage(0, 0, 64, 64);
        }
        else if(counter < 16)
        {
            return image.getSubimage(0, 64, 64, 64);
        }
        else
        {
            return image.getSubimage(0, 128, 64, 64);
        }
    }
    
    @Override
    public boolean animate()
    {
        if(counter == 24)
        {
            return true;
        }
        else
        {
            counter++;
            return false;
        }
    }
}
