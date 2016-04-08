/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.entity;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import mysterydungeon.DungeonComp;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;

/**
 *
 * @author jlamanna
 */
public class StairEntity implements Entity
{
    public static final boolean UP = true;
    public static final boolean DOWN = false;
    
    private final Node location;
    private final boolean type;
    
    public StairEntity(Dungeon dungeon, boolean type)
    {
        location = Entity.generateRandomLocation(dungeon);
        this.type = type;
    }
    
    @Override
    public int getX()
    {
        return location.getX();
    }

    @Override
    public int getY()
    {
        return location.getY();
    }

    @Override
    public BufferedImage getImage()
    {
        try
        {
            BufferedImage stairImage = ImageIO.read(new File("Sprites/stairs.png"));
            if(type == UP)
            {
                return stairImage.getSubimage(0, 0, 24, 24);
            }
            else
            {
                return stairImage.getSubimage(0, 24, 24, 24);
            }
        }
        catch (IOException ex)
        {
            return new BufferedImage(24, 24, BufferedImage.TYPE_4BYTE_ABGR);
        }
    }

    @Override
    public Object getContained()
    {
        return null;
    }
    
}
