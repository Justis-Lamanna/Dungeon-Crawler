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
    private File[] roomMaps;
    private final int mapWidth;
    private final int mapHeight;
    
    /**
     * Creates a DungeonLayout.
     * @param basemap The base map of the dungeon.
     */
    public DungeonLayout(int[][] basemap)
    {
        this.basemap = basemap;
        roomMaps = null;
        mapWidth = basemap[0].length;
        mapHeight = basemap.length;
    }
    
    /**
     * Creates a DungeonLayout.
     * This one creates the dungeon dynamically, using the random number generator.
     * The parameter roomsDirectory points to a specific directory, containing all
     * room layouts that the dungeon can contain. The dungeon will place these rooms
     * at random, and connect them with paths. The layouts are built in the same formatting
     * as the standard hard-coded base maps.
     * @param roomsDirectory The directory containing all the room maps.
     * @param width The width of the dungeon.
     * @param height The height of the dungeon.
     */
    public DungeonLayout(String roomsDirectory, int width, int height)
    {
        File rooms = new File(roomsDirectory);
        mapWidth = width;
        mapHeight = height;
        basemap = new int[width][height];
        if(rooms.isDirectory())
        {
            roomMaps = rooms.listFiles((d, n) -> n.endsWith(".txt"));
        }
        generateFloor();
    }
    
    /**
     * Gets the base map of this dungeon.
     * @return The base map of the dungeon.
     */
    public int[][] getBaseMap()
    {
        return basemap;
    }
    
    /**
     *
     */
    public void nextFloor()
    {
        if(roomMaps == null)
        {
            int random = Dungeon.PRNG.nextInt(4);
            if(random == 1){swapHorizontal();}
            else if(random == 2){swapVertical();}
            else if(random == 3){swapHorizontal(); swapVertical();}
        }
        else
        {
            generateFloor();
        }
    }
    
    /**
     *
     */
    public void swapHorizontal()
    {
        int width = basemap[0].length;
        int height = basemap.length;
        int[][] newMap = new int[height][width];
        for(int xx = 0; xx < height; xx++)
        {
            for(int yy = 0; yy < width; yy++)
            {
                newMap[xx][width - 1 - yy] = basemap[xx][yy];
            }
        }
        basemap = newMap;
    }
    
    /**
     *
     */
    public void swapVertical()
    {
        int width = basemap[0].length;
        int height = basemap.length;
        int[][] newMap = new int[height][width];
        for(int xx = 0; xx < height; xx++)
        {
            for(int yy = 0; yy < width; yy++)
            {
                newMap[height - 1 - xx][yy] = basemap[xx][yy];
            }
        }
        basemap = newMap;
    }
     
    /**
     *
     * @param filename
     * @return
     */
    public static int[][] readLayout(String filename)
    {
        int[][] returnMap = null;
        try
        {	
            Scanner inScanner = new Scanner(new File(filename));
            int mapWidth = inScanner.nextInt();
            int mapHeight = inScanner.nextInt();
            inScanner.nextLine(); //There's a newline left after nextInt. This eats the newLine.
            returnMap = new int[mapHeight][mapWidth];
            for(int row = 0; row < returnMap.length; row++)
            {
                String[] line = inScanner.nextLine().split(" +");
                for(int col = 0; col < line.length; col++)
                {
                    returnMap[row][col] = Integer.parseInt(line[col]);
                }
            }
        }
        catch(IOException ex)
        {
            System.out.println("Error in DungeonLayout.readLayout()");
            ex.printStackTrace();
        }
        return returnMap;
    }
    
    private void generateFloor()
    {
        replace(basemap, 0, 1);
    }
    
    private void replace(int[][] array, int original, int replace)
    {
        for(int row = 0; row < array.length; row++)
        {
            for(int col = 0; col < array[0].length; col++)
            {
                if(array[row][col] == original){array[row][col] = replace;}
            }
        }
    }
}
