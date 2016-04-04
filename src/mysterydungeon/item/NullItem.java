/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.item;

import java.awt.image.BufferedImage;
import mysterydungeon.entity.Entity;

/**
 * Represents the empty item.
 * Used when the inventory is empty, so it doesn't glitch out.
 * @author jlamanna
 */
public class NullItem implements Item
{

    public static final NullItem NULL_ITEM = new NullItem();
    
    @Override
    public boolean useItem(Entity user)
    {
        return false;
    }

    @Override
    public String getName()
    {
        return "No items";
    }

    @Override
    public BufferedImage getImage()
    {
        return new BufferedImage(24, 24, BufferedImage.TYPE_4BYTE_ABGR);
    }

    @Override
    public String getDescription()
    {
        return "Items can be found on the ground.";
    }
    
}
