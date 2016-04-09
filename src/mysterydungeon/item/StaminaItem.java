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
import mysterydungeon.entity.SpeciesEntity;

/**
 * Represents a stamina-healing item.
 * @author jlamanna
 */
public class StaminaItem implements Item
{
    public static final Item AAA_BATTERY = new StaminaItem("AAA Battery", 50);
    public static final Item C_BATTERY = new StaminaItem("C Battery", 100);
    public static final Item NV_BATTERY = new StaminaItem("9V Battery", 200);
    
    private final String name;
    private final int staminaToHeal;
    
    /**
     * Creates a stamina-healing item.
     * @param name The name of the item.
     * @param stamina The amount of stamina to heal.
     */
    public StaminaItem(String name, int stamina)
    {
        this.name = name;
        staminaToHeal = stamina;
    }
    
    @Override
    public boolean useItem(SpeciesEntity user)
    {
        int added = user.addStamina(staminaToHeal);
        if(added == 0)
        {
            MysteryDungeon.updateLog("Stamina was already maxed out!");
            return KEEP;
        }
        else
        {
            MysteryDungeon.updateLog(String.format("Stamina was increased by %d.", -added));
            return REMOVE;
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
        return String.format("Increases your stamina by up to %d.", staminaToHeal);
    }
    
    @Override
    public int getType()
    {
        return Item.STAMINA_HEALING;
    }
}
