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
import mysterydungeon.animation.Animation;
import mysterydungeon.animation.FadeToClearAnimation;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.entity.SpeciesEntity;

/**
 * Represents a dungeon-revealing item.
 * This type of item removes the fog of war entirely.
 * @author jlamanna
 */
public class RevealItem implements Item
{
    
    /**
     * The item "Seeing Light".
     */
    public static final RevealItem SEEING_LIGHT = new RevealItem("Seeing Light", "Reveals the layout of the dungeon.");
    
    private final String name;
    private final String desc;
    
    /**
     * Creates a dungeon-revealing item.
     * @param name The name of this item.
     * @param description The description of this item.
     */
    public RevealItem(String name, String description)
    {
        this.name = name;
        desc = description;
    }
    
    @Override
    public boolean useItem(SpeciesEntity user)
    {
        Dungeon dungeon = user.getDungeon();
        Animation.animate(new FadeToClearAnimation(0, 0, dungeon.getMask(), 16), 50);
        //Because of pass-by-reference, no need to set the dungeon's mask.
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
        catch(IOException ex)
        {
            return new BufferedImage(24, 24, BufferedImage.TYPE_4BYTE_ABGR);
        }
    }

    @Override
    public String getDescription()
    {
        return desc;
    }
    
    @Override
    public int getType()
    {
        return Item.LIGHT;
    }
}
