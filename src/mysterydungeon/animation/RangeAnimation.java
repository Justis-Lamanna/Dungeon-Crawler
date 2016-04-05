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
 * An animation for a ranged attack.
 * Currently, a red beam is shot in some direction for some amount of tiles.
 * @author jlamanna
 */
public class RangeAnimation implements Animation
{
    private int xx;
    private int yy;
    private int counter = 0;
    private final int direction;
    private final int range;
    private BufferedImage image;
    
    private static final int[] dx = {0, 1, 1, 1, 0, -1, -1, -1};
    private static final int[] dy = {-1, -1, 0, 1, 1, 1, 0, -1};
    
    /**
     * Create a range animation.
     * @param startX The X coordinate of the top-left corner of where the animation begins.
     * @param startY The Y coordinate of the top-left corner of where the animation begins.
     * @param direction The direction of the firing.
     * @param range The number of tiles the animation should extend.
     */
    public RangeAnimation(int startX, int startY, int direction, int range)
    {
        xx = startX;
        yy = startY;
        this.direction = direction;
        this.range = range;
        try
        {
            BufferedImage fullImage = ImageIO.read(new File("Sprites/range.png"));
            image = fullImage.getSubimage(0, direction*24, 24, 24);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    @Override
    public boolean animate()
    {
        if(counter == 8 * (range + 1))
        {
            return true;
        }
        else
        {
            xx += dx[direction] * 3;
            yy += dy[direction] * 3;
            counter++;
            return false;
        }
    }
    
    @Override
    public int getAnimationX()
    {
        return xx;
    }
    
    @Override
    public int getAnimationY()
    {
        return yy;
    }
    
    @Override
    public BufferedImage getImage()
    {
        return image;
    }
}
