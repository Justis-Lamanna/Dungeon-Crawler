/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.entity;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import mysterydungeon.move.BrawlMove;
import mysterydungeon.move.Move;
import mysterydungeon.move.RangeMove;
import mysterydungeon.move.RoomMove;

/**
 * The class representing the common attributes of a species.
 * A species is distinct from an entity in that entities may vary between instances
 * of the same species. This variance includes held items, different HP levels, etc.
 * There is also commonalities, such as the initial max HP, initial name, and
 * image. These commonalities are contained in one class and linked to their respective
 * entities.
 * @author Justis
 */
public class Species
{
    private String name;
    private String imageFilename;
    private BufferedImage image;
    private boolean water;
    private int maxhp;
    private ArrayList<Move> moves = new ArrayList<>();

    /**
     * A constant representing the player species.
     */
    public static final Species PLAYER = 
            new SpeciesBuilder("Player", "Sprites/player.png", 40)
            .addMove(new BrawlMove(15))
            .addMove(new RangeMove(20, 4))
            .addMove(new RoomMove(20))
            .make();

    /**
     * A constant representing the Robot1 species.
     */
    public static final Species ROBOT1 = new Species("Robot1", "Sprites/robot1.png", 20);

    /**
     * A constant representing the Robot2 species.
     */
    public static final Species ROBOT2 = new Species("Robot2", "Sprites/robot2.png", 20);

    /**
     * A constant representing the X0L0TL species.
     */
    public static final Species ROBOT3 = new Species("X0L0TL", "Sprites/robot3.png", true, 20);

    /**
     * A constant representing the Robot4 species.
     */
    public static final Species ROBOT4 = new Species("Robot4", "Sprites/robot4.png", true, 20);

    /**
     * A constant representing the Robot5 species.
     */
    public static final Species ROBOT5 = new Species("Robot5", "Sprites/robot5.png", true, 20);

    /**
     * A constant representing the Robot6 species.
     */
    public static final Species ROBOT6 = new Species("Robot6", "Sprites/robot6.png", true, 20);

    /**
     * A constant representing the Robot7 species.
     */
    public static final Species ROBOT7 = new Species("Robot7", "Sprites/robot7.png", 20);

    /**
     * A constant representing the Robot8 species.
     */
    public static final Species ROBOT8 = new Species("Robot8", "Sprites/robot8.png", 20);

    /**
     * A constant representing the Robot9 species.
     */
    public static final Species ROBOT9 = new Species("Robot9", "Sprites/robot9.png", 20);

    /**
     * A constant representing the Robot10 species.
     */
    public static final Species ROBOT10 = new Species("Robot10", "Sprites/robot10.png", 20);

    /**
     * A constant representing the Robot11 species.
     */
    public static final Species ROBOT11 = new Species("Robot11", "Sprites/robot11.png", 20);

    /**
     * A constant representing the Robot12 species.
     */
    public static final Species ROBOT12 = new Species("Robot12", "Sprites/robot12.png", 20);

    /**
     * A constant representing the Robot13 species.
     */
    public static final Species ROBOT13 = new Species("Robot13", "Sprites/robot13.png", 20);

    /**
     * A constant representing the Robot14 species.
     */
    public static final Species ROBOT14 = new Species("Robot14", "Sprites/robot14.png", 20);

    /**
     * Generate a custom species.
     * This species, by default, cannot walk on water.
     * @param name The name of the species.
     * @param imageFilename The filename of this species image.
     * @param hp The maximum HP of this species.
     */
    public Species(String name, String imageFilename, int hp)
    {
        this(name, imageFilename, false, hp);
    }

    /**
     * Generate a custom species.
     * @param name The name of this species.
     * @param imageFilename The filename of this species image.
     * @param water True if this entity can walk on water, false if not.
     * @param hp The maximum HP of this species.
     */
    public Species(String name, String imageFilename, boolean water, int hp)
    {
        this.name = name;
        this.imageFilename = imageFilename;
        this.image = null;
        this.water = water;
        this.maxhp = hp;
    }

    /**
     * Get this species name.
     * @return The name of the species.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get this species image.
     * The species image won't be stored until this method is called.
     * @return This species image.
     */
    public BufferedImage getImage()
    {
        if(image == null)
        {	
            try
            {
                image = ImageIO.read(new File(imageFilename));
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }
        return image;
    }

    /**
     * Return whether or not this species can move on water.
     * @return True if this species can walk on water, false if not.
     */
    public boolean isWater()
    {
        return water;
    }
    
    /**
     * Set whether or not this species can move on water.
     * @param set True if this species should walk on water, false if not.
     */
    public void setWater(boolean set)
    {
        water = set;
    }
    
    /**
     * Get the maximum HP of this species.
     * @return
     */
    public int getHP()
    {
        return maxhp;
    }
    
    /**
     * Add a move that this species can use.
     * @param newMove The move this species should have added to its arsenal.
     */
    public void addMove(Move newMove)
    {
        moves.add(newMove);
    }
    
    /**
     * Get a list of moves this species can use.
     * @return A list of moves, containing all the moves this entity knows.
     */
    public ArrayList<Move> getMoves()
    {
        return moves;
    }
}
