/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.entity;

import mysterydungeon.move.BrawlMove;
import mysterydungeon.move.Move;

/**
 * A builder pattern to assist with development of species.
 * As species grow more complex, a builder pattern may be more useful. Species
 * in the future should have many different aspects, and creating a long constructor
 * is impractical. 
 * @author Justis
 */
public class SpeciesBuilder
{
    private final Species species;
    
    /**
     * Begin building a species.
     * @param name The name of this species.
     * @param filename The filename of this species image.
     * @param hp The maximum HP of this species.
     */
    public SpeciesBuilder(String name, String filename, int hp)
    {
        species = new Species(name, filename, hp);
    }
    
    /**
     * Add a usable move to this species.
     * @param newMove The move to allow this species to use.
     * @return This builder, for chaining.
     */
    public SpeciesBuilder addMove(Move newMove)
    {
        species.addMove(newMove);
        return this;
    }
    
    /**
     * Set if this species can walk over water.
     * @param set True if it should walk over water, false if not.
     * @return This builder, for chaining.
     */
    public SpeciesBuilder setWater(boolean set)
    {
        species.setWater(set);
        return this;
    }
    
    /**
     * Create the final species.
     * @return The built species.
     */
    public Species make()
    {
        if(species.getMoves().isEmpty())
        {
            species.addMove(new BrawlMove(5)); //Wimpy attack if there is no other.
        }
        return species;
    }
    
}
