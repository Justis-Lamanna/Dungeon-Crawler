/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.animation;

import java.awt.image.BufferedImage;
import mysterydungeon.entity.Entity;

/**
 *
 * @author jlamanna
 */
public class FadeToClearAnimation implements Animation
{
    private final int x;
    private final int y;
    private final BufferedImage image;
    private final int step;
    private int counter;
    
    /**
     *
     * @param x
     * @param y
     * @param image
     * @param step
     */
    public FadeToClearAnimation(int x, int y, BufferedImage image, int step)
    {
        this.x = x;
        this.y = y;
        this.step = step;
        this.image = image;
        counter = 0;
    }
    
    @Override
    public boolean animate()
    {
        for(int xx = 0; xx < image.getWidth(); xx++)
        {
            for(int yy = 0; yy < image.getHeight(); yy++)
            {
                int argb = image.getRGB(xx, yy);
                int alpha = argb >>> 24;
                int rgb = argb & 0xFFFFFF;
                if(alpha >= step)
                {
                    alpha -= step;
                }
                else
                {
                    alpha = 0;
                }
                argb = (alpha << 24) + rgb;
                image.setRGB(xx, yy, argb);
            }
        }
        return counter++ > (256 / step);
    }

    @Override
    public BufferedImage getImage()
    {
        return image;
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
    
    /**
     *
     * @return
     */
    @Override
    public Entity getContained()
    {
        return this;
    }
    
}
