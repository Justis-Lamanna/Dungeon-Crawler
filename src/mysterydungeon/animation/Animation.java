/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.animation;

import java.awt.image.BufferedImage;

/**
 *
 * @author jlamanna
 */
public interface Animation
{
    boolean animate();
    BufferedImage getImage();
    int getX();
    int getY();
    
    public static BufferedImage scale(BufferedImage original, double dx, double dy)
    {
        BufferedImage newImage = new BufferedImage((int)(original.getWidth()*dx), (int)(original.getHeight()*dy), BufferedImage.TYPE_4BYTE_ABGR);
        for(int xx = 0; xx < newImage.getWidth(); xx++)
        {
            for(int yy = 0; yy < newImage.getHeight(); yy++)
            {
                int pixel = original.getRGB((int)(xx/dx), (int)(yy/dy));
                newImage.setRGB(xx, yy, pixel);
            }
        }
        return newImage;
    }
}
