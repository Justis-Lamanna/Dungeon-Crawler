/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.item;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import mysterydungeon.MysteryDungeon;
import mysterydungeon.entity.Entity;

/**
 * Represents a HP healing item.
 * @author jlamanna
 */
public class HPItem implements Item
{
    public static final HPItem REPAIR_V1 = new HPItem("Repair V1", 25);
    public static final HPItem REPAIR_V2 = new HPItem("Repair V2", 50);
    public static final HPItem REPAIR_V10 = new HPItem("Repair V10", 75);
    
    private final String name;
    private final int hpToHeal;
    
    public HPItem(String name, int HP)
    {
        this.name = name;
        hpToHeal = HP;
    }
    
    @Override
    public boolean useItem(Entity user)
    {
        int added = user.addHP(hpToHeal);
        if(added == 0)
        {
            MysteryDungeon.updateLog("HP was already maxed out!");
            return false;
        }
        else
        {
            MysteryDungeon.updateLog(String.format("HP was healed by %d.", added));
            return true;
        }
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
        return String.format("Increases your HP by up to %d.", hpToHeal);
    }
    
}
