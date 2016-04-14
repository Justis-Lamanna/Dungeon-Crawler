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
public class FadeLetters implements Animation
{
    private int x;
    private int y;
    private int counter = 0;
    private BufferedImage string;
    
    public FadeLetters(int x, int y, String word)
    {
        this.x = x;
        this.y = y;
        counter = 0;
        string = new BufferedImage(word.length() * 16, 24, BufferedImage.TYPE_4BYTE_ABGR);
        BufferedImage font;
        try
        {
            font = ImageIO.read(new File("Sprites/font.png"));
        }
        catch(IOException ex)
        {
            font = new BufferedImage(80, 72, BufferedImage.TYPE_4BYTE_ABGR);
        }
        for(int ii = 0; ii < word.length(); ii++)
        {
            char letter = word.charAt(ii);
            BufferedImage letterImage = getLetter(font, letter);
            placeImage(string, letterImage, ii*16, 0);
        }
    }
    
    @Override
    public boolean animate()
    {
        return counter++ >= 60;
    }

    @Override
    public BufferedImage getImage()
    {
        return string;
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
    
    private BufferedImage getLetter(BufferedImage font, char letter)
    {
        int subimage;
        if(letter >= '0' && letter <= '9')
        {
            subimage = letter - '0' + 26;
        }
        else if(letter == ' ')
        {
            subimage = 36;
        }
        else
        {
            subimage = letter - 'A';
        }
        return font.getSubimage(subimage * 16, 0, 16, 24);
    }
    
    private void placeImage(BufferedImage string, BufferedImage letter, int x, int y)
    {
        for(int xx = 0; xx < letter.getWidth(); xx++)
        {
            for(int yy = 0; yy < letter.getHeight(); yy++)
            {
                int rgb = letter.getRGB(xx, yy);
                string.setRGB(x+xx, y+yy, rgb);
            }
        }
    }
}
