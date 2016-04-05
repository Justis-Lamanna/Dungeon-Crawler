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
 * Creates an animation for a room attack.
 * Currently, this causes a small shockwave to emit from the player.
 * @author jlamanna
 */
public class RoomAnimation implements Animation
{
    private int x;
    private int y;
    private BufferedImage image;
    private int counter;
    
    /**
     * Create a room animation.
     * @param startX The X coordinate of the upper-left corner of the animation frame.
     * @param startY The y coordinate of the upper-left corner of the animation frame.
     */
    public RoomAnimation(int startX, int startY)
    {
        x = startX;
        y = startY;
        counter = 0;
        try{image = ImageIO.read(new File("Sprites/room.png"));}
        catch(IOException ex){ex.printStackTrace();}
    }
    
    @Override
    public int getAnimationX()
    {
        return x;
    }
    
    @Override
    public int getAnimationY()
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
