/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.dungeon;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
    
    
    public DungeonLayout(String roomsDirectory, int width, int height)
    {
        File rooms = new File(roomsDirectory);
        basemap = new int[width][height];
        for(int row = 0; row < height; row++)
        {
            for(int col = 0; col < width; col++)
            {
                basemap[row][col] = 1;
            }
        }
        if(rooms.isDirectory())
        {
            File[] roomMaps = rooms.listFiles((d, n) -> n.endsWith(".txt"));
            for(File map : roomMaps)
            {
                System.out.println(map);
            }
        }
    }
    
    /**
     * Gets the base map of this dungeon.
     * @return The base map of the dungeon.
     */
    public int[][] getBaseMap()
    {
        return basemap;
    }
    
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
}
