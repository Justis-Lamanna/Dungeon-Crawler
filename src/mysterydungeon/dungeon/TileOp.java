/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.dungeon;

/**
 * A class representing tile operations; More specifically, this translates
 * the base map into the prettier tile map. I hate this class dearly.
 * @author Justis
 */
public class TileOp
{

    /**
     * A constant representing an obstacle.
     */
    public static final int OBSTACLE = 0;

    /**
     * A constant representing a non-obstacle.
     */
    public static final int LAND = 1;

    /**
     * A constant representing water.
     */
    public static final int WATER = 2;

    /**
     * A constant representing the tile number of the northwest cliff corner.
     */
    public static final int O_NW = 0;

    /**
     * A constant representing the tile number of the north cliff edge.
     */
    public static final int O_N = 2;

    /**
     * A constant representing the tile number of the northeast cliff corner.
     */
    public static final int O_NE = 4;

    /**
     * A constant representing the tile number of the west cliff edge.
     */
    public static final int O_W = 8;

    /**
     * A constant representing the tile number of the center of a cliff.
     */
    public static final int O_C = 10;

    /**
     * A constant representing the tile number of the east cliff edge.
     */
    public static final int O_E = 12;

    /**
     * A constant representing the tile number of the southwest cliff corner.
     */
    public static final int O_SW = 16;

    /**
     * A constant representing the tile number of the south cliff edge.
     */
    public static final int O_S = 18;

    /**
     * A constant representing the tile number of the southeast cliff corner.
     */
    public static final int O_SE = 20;

    /**
     * A constant representing the tile number of the northwest cliff reverse corner.
     */
    public static final int O_NW_CORNER = 24;

    /**
     * A constant representing the tile number of the northeast cliff reverse corner.
     */
    public static final int O_NE_CORNER = 25;

    /**
     * A constant representing the tile number of the southwest cliff reverse corner.
     */
    public static final int O_SW_CORNER = 32;

    /**
     * A constant representing the tile number of the southeast cliff reverse corner.
     */
    public static final int O_SE_CORNER = 33;

    /**
     *
     */
    public static final int O_W_HORIZONTAL = 26;

    /**
     *
     */
    public static final int O_C_HORIZONTAL = 27;

    /**
     *
     */
    public static final int O_E_HORIZONTAL = 28;

    /**
     *
     */
    public static final int O_N_VERTICAL = 34;

    /**
     *
     */
    public static final int O_C_VERTICAL = 35;

    /**
     *
     */
    public static final int O_S_VERTICAL = 36;

    /**
     *
     */
    public static final int O_ROCK = 29;

    /**
     *
     * @param basemap
     * @return
     */
    public static int[][] convertTilemap(int[][] basemap)
    {
        int[][] newmap = new int[basemap.length][basemap[0].length];
        for(int row = 0; row < basemap.length; row++)
        {
                for(int col = 0; col < basemap[0].length; col++)
                {
                        convertTile(basemap, newmap, row, col);
                }
                //System.out.println();
        }
        return newmap;
    }

    private static void convertTile(int[][] basemap, int[][] newmap, int row, int col)
    {
        if(basemap[row][col] == OBSTACLE)
        {
            int count = 0;
            int dcount = 1;
            for(int drow = -1; drow <= 1; drow++)
            {
                for(int dcol = -1; dcol <= 1; dcol++)
                {
                        //if(drow * dcol == 0){continue;}
                    try
                    {
                        int type = basemap[row+drow][col+dcol];
                        if(type == LAND || type == WATER){count += dcount;}
                    }
                    catch(IndexOutOfBoundsException ex){}
                    dcount *= 2;
                }
            }
            //System.out.print(count + "\t");
            newmap[row][col] = getObstacleTile(count);
        }
        else if(basemap[row][col] == LAND)
        {
            int random = (int)(Math.random() * 3);
            newmap[row][col] = 14 + (random * 8);
            //System.out.print("||\t");
        }
        else
        {
            newmap[row][col] = 49;
        }
    }

    private static int getObstacleTile(int count)
    {
        switch(count)
        {
            case 0: return O_C;
            case 1: return O_NW_CORNER;
            case 2:
            case 3:
            case 6:
            case 7: return O_N + (int)(Math.random() * 3) - 1;
            case 4: return O_NE_CORNER;
            case 8:
            case 9:
            case 65:
            case 72:
            case 73: return O_W;
            case 11: 
            case 15:
            //case 67: 
            case 75: 
            case 79: return O_NW;
            case 32:
            case 36:
            case 288:
            case 292: return O_E;
            case 38: 
            case 39: 
            case 294:
            case 295: return O_NE;
            case 64: return O_SW_CORNER;
            case 128:
            case 192:
            case 384:
            case 448: return O_S + (int)(Math.random() * 3) - 1;
            case 193:
            case 200: 
            case 201: 
            case 449:
            case 456: 
            case 457: return O_SW;
            case 256: return O_SE_CORNER;
            case 416: 
            case 420:
            case 452: 
            case 480:
            case 484: return O_SE;
            case 455: 
            case 325: return O_C_HORIZONTAL;
            case 463: 
            case 454:
            case 203: return O_W_HORIZONTAL;
            case 487: 
            case 422: return O_E_HORIZONTAL;
            case 365: return O_C_VERTICAL;
            case 367: return O_N_VERTICAL;
            case 493: return O_S_VERTICAL;
            default: return O_ROCK;
        }
    }
}
