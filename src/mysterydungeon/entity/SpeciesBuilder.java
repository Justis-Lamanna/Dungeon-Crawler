/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.entity;

import mysterydungeon.move.Move;

/**
 *
 * @author Justis
 */
public class SpeciesBuilder
{
    private final Species species;
    
    /**
     *
     * @param name
     * @param filename
     * @param hp
     */
    public SpeciesBuilder(String name, String filename, int hp)
    {
        species = new Species(name, filename, hp);
    }
    
    /**
     *
     * @param newMove
     * @return
     */
    public SpeciesBuilder addMove(Move newMove)
    {
        species.addMove(newMove);
        return this;
    }
    
    /**
     *
     * @param set
     * @return
     */
    public SpeciesBuilder setWater(boolean set)
    {
        species.setWater(set);
        return this;
    }
    
    /**
     *
     * @return
     */
    public Species make()
    {
        return species;
    }
    
}
