/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.item;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;
import mysterydungeon.entity.SpeciesEntity;

/**
 *
 * @author jlamanna
 */
public class RevealItem implements Item
{
    
    public static final RevealItem SEEING_LIGHT = new RevealItem("Seeing Light", "Reveals the layout of the dungeon.");
    
    private final String name;
    private final String desc;
    
    public RevealItem(String name, String description)
    {
        this.name = name;
        desc = description;
    }
    
    @Override
    public boolean useItem(SpeciesEntity user)
    {
        Dungeon dungeon = user.getDungeon();
        ArrayList<Node> nodes = dungeon.getNodesList();
        for(Node node : nodes)
        {
            dungeon.setDiscovered(node.getX(), node.getY());
        }
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
    
}
