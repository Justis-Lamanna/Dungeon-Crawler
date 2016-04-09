/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.dungeon;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Represents the layout of a dungeon.
 * In future work, this will serve as the dungeon generator class. It also includes
 * constructors for hard-coded layouts.
 * @author Justis
 */
public class DungeonLayout
{
    private int[][] basemap;
    
    /**
     * Creates a DungeonLayout
     * @param basemap The base map of the dungeon.
     */
    public DungeonLayout(int[][] basemap)
    {
        this.basemap = basemap;
    }
    
    /**
     * Creates a DungeonLayout
     * @param filename The filename, at which contains the base map of the dungeon.
     */
    public DungeonLayout(String filename)
    {
        recalculate(filename);
    }
    
    /**
     * Gets the base map of this dungeon.
     * @return The base map of the dungeon.
     */
    public int[][] getBaseMap()
    {
        return basemap;
    }
    
    public void recalculate(String filename)
    {
        try
        {	
            Scanner inScanner = new Scanner(new File(filename));
            int mapWidth = inScanner.nextInt();
            int mapHeight = inScanner.nextInt();
            inScanner.nextLine(); //There's a newline left after nextInt. This eats the newLine.
            basemap = new int[mapHeight][mapWidth];
            for(int row = 0; row < basemap.length; row++)
            {
                String[] line = inScanner.nextLine().split(" +");
                for(int col = 0; col < line.length; col++)
                {
                    basemap[row][col] = Integer.parseInt(line[col]);
                }
            }
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
        
}
