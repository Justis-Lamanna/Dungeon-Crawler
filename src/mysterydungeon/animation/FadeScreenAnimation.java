/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.animation;

import java.awt.image.BufferedImage;

/**
 * Creates and fades a rectangle or solid black.
 * @author Justis
 */
public class FadeScreenAnimation implements Animation
{
    private final int x;
    private final int y;
    private int currentAlpha;
    private final int steps;
    private final BufferedImage image;
    
    /**
     * Builds this animation.
     * @param x The x position of this rectangle.
     * @param y The y position of this rectangle.
     * @param width The width of this rectangle.
     * @param height The height of this rectangle.
     * @param startAlpha The starting alpha of this rectangle.
     * @param steps The number to subtract from the alpha each frame.
     */
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
