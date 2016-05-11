/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.entity;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import mysterydungeon.DungeonComp;
import mysterydungeon.MysteryDungeon;
import mysterydungeon.animation.Animation;
import mysterydungeon.animation.FadeLetters;
import mysterydungeon.animation.FadeScreenAnimation;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;

/**
 * Represents the stairs of the dungeon.
 * @author jlamanna
 */
public class StairEntity implements ActionEntity
{

    /**
     * A constant representing stairs moving up.
     */
    public static final boolean UP = true;

    /**
     * A constant representing the stairs moving down.
     */
    public static final boolean DOWN = false;
    
    private final Node location;
    private final Dungeon dungeon;
    private final boolean type;
    
    /**
     * Creates the stairs.
     * @param dungeon The dungeon these stairs reside in.
     * @param type The type of stairs, up or down.
     */
    public StairEntity(Dungeon dungeon, boolean type)
    {
        location = Entity.generateRandomLocation(dungeon);
        this.dungeon = dungeon;
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
    public void onTurn()
    {
        SpeciesEntity player = dungeon.getEntities().get(0);
        if(player.getDestinationNode().getX() == this.getX() && player.getDestinationNode().getY() == this.getY())
        {
            
            DungeonComp component = DungeonComp.getInstance();
            //Fade screen to black. Hold as black.
            Animation fadeAnimation = new FadeScreenAnimation(
                    0, 0, component.getWidth(), component.getHeight(), 0, 8);
            Animation.animate(fadeAnimation, 20, false);
            //Next floor. Make it!
            dungeon.getLayout().nextFloor();
            component.getDungeon().startNextFloor();
            //Pop up the dungeon level. Fades out level.
            String floor = String.format("FLOOR %s%s", dungeon.getFloor() < 0 ? "B" : "", Math.abs(dungeon.getFloor()));
            int stringX = (DungeonComp.getInstance().getWidth() / 2) - (floor.length() * 8);
            int stringY = (DungeonComp.getInstance().getHeight() / 3) - 12; // 12 is equal to the font height divided by two.
            Animation letterAnimation = new FadeLetters(stringX, stringY, floor);
            MysteryDungeon.playNote(0, 500, 150, 100);
            Animation.animate(letterAnimation, 20);
            //Pulls out the black that was held from before.
            component.removeAnimation(fadeAnimation);
            //Fades screen to the new dungeon.
            Animation fadeBackAnimation = new FadeScreenAnimation(
                    0, 0, component.getWidth(), component.getHeight(), 255, -8);
            Animation.animate(fadeBackAnimation, 20);
        }
    }
}
