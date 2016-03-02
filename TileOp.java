public class TileOp
{
	public static final int OBSTACLE = 0;
	public static final int LAND = 1;
	public static final int WATER = 2;

	public static int[][] convertTilemap(int[][] basemap)
	{
		int[][] newmap = new int[basemap.length][basemap[0].length];
		for(int row = 0; row < basemap.length; row++)
		{
			for(int col = 0; col < basemap[0].length; col++)
			{
				convertTile(basemap, newmap, row, col);
			}
			System.out.println();
		}
		return newmap;
	}

	public static void convertTile(int[][] basemap, int[][] newmap, int row, int col)
	{
		if(basemap[row][col] == OBSTACLE)
		{
			int count = 0;
			int dcount = 1;
			for(int drow = -1; drow <= 1; drow++)
			{
				for(int dcol = -1; dcol <= 1; dcol++)
				{
					try
					{
						if(basemap[row+drow][col+dcol] == LAND){count += dcount;}
					}
					catch(IndexOutOfBoundsException ex){}
					dcount *= 2;
				}
			}
			//newmap[row][col] = getCorrespondingTile(count);
		}
		else
		{
			newmap[row][col] = basemap[row][col];
		}
	}
}