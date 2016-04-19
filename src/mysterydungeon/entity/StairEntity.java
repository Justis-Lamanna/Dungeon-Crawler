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
import mysterydungeon.dungeon.DungeonLayout;
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
    private final Dungeon dungeon;
    private final boolean type;
    
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
    public Object getContained()
    {
        return null;
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
            //Flip layout some way.
            DungeonLayout layout = dungeon.getLayout();
            int random = Dungeon.PRNG.nextInt(4);
            if(random == 1){layout.swapHorizontal();}
            else if(random == 2){layout.swapVertical();}
            else if(random == 3){layout.swapHorizontal(); layout.swapVertical();}
            component.getDungeon().startNextFloor(layout);
            //Pop up the dungeon level. Fades out level.
            String floor = String.format("FLOOR %s%s", dungeon.getFloor() < 0 ? "B" : "", Math.abs(dungeon.getFloor()));
            int stringX = (DungeonComp.getInstance().getWidth() / 2) - (floor.length() * 8);
            int stringY = (DungeonComp.getInstance().getHeight() / 3) - 12;
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
