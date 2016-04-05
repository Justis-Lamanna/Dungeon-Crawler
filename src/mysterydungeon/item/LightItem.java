/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.item;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import javax.imageio.ImageIO;
import mysterydungeon.MysteryDungeon;
import mysterydungeon.animation.Animation;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.entity.Entity;

/**
 *
 * @author jlamanna
 */
public class LightItem implements Item
{
    public static final LightItem TORCH = new LightItem("Torch", 1.5);
    public static final LightItem FLASHLIGHT = new LightItem("Flashlight", 2);
    
    private final double size;
    private final String name;
    
    public LightItem(String name, double size)
    {
        this.size = size;
        this.name = name;
    }
    
    @Override
    public boolean useItem(Entity user)
    {
        BufferedImage shadow;
        try
        {
            shadow = ImageIO.read(new File("Sprites/shadow.png"));
        }
        catch(IOException ex)
        {
            shadow = new BufferedImage((int)(100 * size), (int)(100 * size), BufferedImage.TYPE_4BYTE_ABGR);
        }
        MysteryDungeon.updateLog(String.format("%s was able to see farther.", user.getName()));
        Dungeon thisDungeon = user.getDungeon();
        thisDungeon.setShadow(Animation.scale(shadow, size, size));
        thisDungeon.setDiscovered(user.getX(), user.getY());
        return REMOVE;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public BufferedImage getImage()
    {
        try
        {
            return ImageIO.read(new File("Sprites/item_" + name.replaceAll(" ", "_") + ".png"));
        }
        catch (IOException ex)
        {
            return new BufferedImage(24, 24, BufferedImage.TYPE_4BYTE_ABGR);
        }
    }

    @Override
    public String getDescription()
    {
        return String.format("Increases view size by %.1fx", size);
    }
}
