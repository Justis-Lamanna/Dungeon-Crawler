/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.animation;

import java.awt.image.BufferedImage;

/**
 *
 * @author Justis
 */
public class FadeScreenAnimation implements Animation
{
    private int x;
    private int y;
    private int currentAlpha;
    private int steps;
    private BufferedImage image;
    
    public FadeScreenAnimation(int x, int y, int width, int height, int startAlpha, int steps)
    {
        this.x = x;
        this.y = y;
        this.currentAlpha = startAlpha;
        this.steps = steps;
        image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        for(int xx = 0; xx < image.getWidth(); xx++)
        {
            for(int yy = 0; yy < image.getHeight(); yy++)
            {
                image.setRGB(xx, yy, (currentAlpha << 24));
            }
        }
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
        return image;
    }
    
    @Override
    public boolean animate()
    {
        for(int xx = 0; xx < image.getWidth(); xx++)
        {
            for(int yy = 0; yy < image.getHeight(); yy++)
            {
                image.setRGB(xx, yy, (currentAlpha << 24));
            }
        }
        currentAlpha += steps;
        if(currentAlpha < 0)
        {
            currentAlpha = 0;
        }
        else if(currentAlpha > 255)
        {
            currentAlpha = 255;
        }
        return currentAlpha == 0 || currentAlpha == 255;
    }
}
