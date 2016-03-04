//
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Dungeon extends JComponent
{
	public static final int TILE_SIZE = 24;
	public static final boolean BASEMAP = false;
	public static final boolean TILEMAP = true;

	private String tileFilename;
	private String tilemapFilename;
	private BufferedImage tilebase;
	private BufferedImage[] tiles;
	private int[][] tilemap;
	private int[][] basemap;
	private Node[][] nodes;
	private ArrayList<RoomNode> rooms = new ArrayList<>();

	private boolean mapToDraw = BASEMAP;
	private boolean drawNodes = false;
	private boolean drawPaths = false;
	private boolean drawRooms = false;

	public Dungeon(String tileFilename, String tilemapFilename)
	{
		this.tileFilename = tileFilename;
		this.tilemapFilename = tilemapFilename;
		reopen(false);
	}

	public void paint(Graphics g)
	{
		if(mapToDraw == TILEMAP){paintMap(g, 10);}
		else{paintBasemap(g, 10, 10, 14, 49);}
		if(drawNodes){paintNodes(g);}
		if(drawPaths){paintPaths(g);}
		if(drawRooms){paintRooms(g);}
	}

	private void paintMap(Graphics g, int backgroundTile)
	{
		BufferedImage bgTile = tiles[backgroundTile];
		for(int row = 0; row < getHeight()/TILE_SIZE; row++)
		{
			for(int col = 0; col < getWidth()/TILE_SIZE; col++)
			{
				try
				{
					BufferedImage tile = getTile(tiles, tilemap, row, col);
					g.drawImage(tile, col*TILE_SIZE, row*TILE_SIZE, null);
				}
				catch(IndexOutOfBoundsException ex)
				{
					g.drawImage(bgTile, col*TILE_SIZE, row*TILE_SIZE, null);
				}
			}
		}
	}

	private void paintBasemap(Graphics g, int backgroundTile, int obstacleTile, int groundTile, int waterTile)
	{
		BufferedImage bgTile = tiles[backgroundTile];
		BufferedImage obTile = tiles[obstacleTile];
		BufferedImage grTile = tiles[groundTile];
		BufferedImage waTile = tiles[waterTile];
		BufferedImage[] baseTiles = {obTile, grTile, waTile};
		for(int row = 0; row < getHeight()/TILE_SIZE; row++)
		{
			for(int col = 0; col < getWidth()/TILE_SIZE; col++)
			{
				try
				{
					BufferedImage tile = getTile(baseTiles, basemap, row, col);
					g.drawImage(tile, col*TILE_SIZE, row*TILE_SIZE, null);
				}
				catch(IndexOutOfBoundsException ex)
				{
					g.drawImage(bgTile, col*TILE_SIZE, row*TILE_SIZE, null);
				}
			}
		}
	}

	private void paintNodes(Graphics g)
	{
		for(Node[] noderow : nodes)
		{
			for(Node node : noderow)
			{	
				if(node != null)
				{
					int centerX = node.getX() * TILE_SIZE + (TILE_SIZE/4);
					int centerY = node.getY() * TILE_SIZE + (TILE_SIZE/4);
					if(node.getType() == 1){g.setColor(Color.BLACK);}
					else if(node.getType() == 2){g.setColor(Color.BLUE);}
					else{g.setColor(Color.WHITE);}
					g.fillOval(centerX, centerY, TILE_SIZE/2, TILE_SIZE/2);
				}
			}
		}
	}

	private void paintPaths(Graphics g)
	{
		g.setColor(Color.BLACK);
		for(Node[] noderow : nodes)
		{
			for(Node node : noderow)
			{	
				if(node != null)
				{
					int centerX = node.getX() * TILE_SIZE + (TILE_SIZE/2);
					int centerY = node.getY() * TILE_SIZE + (TILE_SIZE/2);
					Node east = node.getPath(Node.EAST);
					if(east != null)
					{
						int westX = east.getX() * TILE_SIZE + (TILE_SIZE/2);
						int westY = east.getY() * TILE_SIZE + (TILE_SIZE/2);
						g.drawLine(centerX, centerY, westX, westY);
					}
					Node south = node.getPath(Node.SOUTH);
					if(south != null)
					{
						int southX = south.getX() * TILE_SIZE + (TILE_SIZE/2);
						int southY = south.getY() * TILE_SIZE + (TILE_SIZE/2);
						g.drawLine(centerX, centerY, southX, southY);
					}
					Node southeast = node.getPath(Node.SOUTHEAST);
					if(southeast != null)
					{
						int southeastX = southeast.getX() * TILE_SIZE + (TILE_SIZE/2);
						int southeastY = southeast.getY() * TILE_SIZE + (TILE_SIZE/2);
						g.drawLine(centerX, centerY, southeastX, southeastY);
					}
					Node southwest = node.getPath(Node.SOUTHWEST);
					if(southwest != null)
					{
						int southwestX = southwest.getX() * TILE_SIZE + (TILE_SIZE/2);
						int southwestY = southwest.getY() * TILE_SIZE + (TILE_SIZE/2);
						g.drawLine(centerX, centerY, southwestX, southwestY);
					}
				}
			}
		}
	}

	private void paintRooms(Graphics g)
	{
		g.setColor(Color.WHITE);
		for(RoomNode room : rooms)
		{
			ArrayList<Node> nodes = room.getNodes();
			for(Node node : nodes)
			{
				int centerX = node.getX() * TILE_SIZE + (TILE_SIZE/4);
				int centerY = node.getY() * TILE_SIZE + (TILE_SIZE/4);
				g.fillRect(centerX, centerY, TILE_SIZE/2, TILE_SIZE/2); 
			}
		}
	}

	private void generateTiles(String filename)
	{
		BufferedImage tilebase = null;
		try
		{
			tilebase = ImageIO.read(new File(filename));
		}
		catch(IOException e)
		{
			System.out.println("Error in generateTiles");
			e.printStackTrace();
			System.exit(0);
		}
		divideTiles(tilebase, TILE_SIZE, TILE_SIZE);
	}

	private void divideTiles(BufferedImage image, int width, int height)
	{
		int tileWidth = image.getWidth(null) / width;
		int tileHeight = image.getHeight(null) / height;
		int totalTiles = tileWidth * tileHeight;
		tiles = new BufferedImage[totalTiles];
		for(int col = 0; col < tileHeight; col++)
		{
			for(int row = 0; row < tileWidth; row++)
			{
				tiles[col*tileWidth+row] = image.getSubimage(row*height, col*width, height, width);
			}
		}
	}

	private void generateBasemap(String filename)
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
		catch(FileNotFoundException ex)
		{
			ex.printStackTrace();
		}
	}

	private void generateTilemap(int[][] basemap)
	{
		tilemap = TileOp.convertTilemap(basemap);
	}

	private BufferedImage getTile(BufferedImage[] tiles, int[][] tilemap, int row, int col)
	{
		int tile = tilemap[row][col];
		return tiles[tile];
	}

	private void findNodes()
	{
		nodes = new Node[basemap.length][basemap[0].length];
		for(int row = 0; row < basemap.length; row++)
		{
			for(int col = 0; col < basemap[0].length; col++)
			{
				if(basemap[row][col] != 0)
				{
					nodes[row][col] = new Node(basemap[row][col], col, row);
					//System.out.println(basemap[row][col]);
				}
			}
		}
	}

	private void findPaths()
	{
		for(int row = 0; row < nodes.length; row++)
		{
			for(int col = 0; col < nodes[0].length; col++)
			{
				Node current = nodes[row][col];
				if(current == null){continue;}
				Node east;
				try{east = nodes[row][col+1];}
				catch(ArrayIndexOutOfBoundsException ex){east = null;}
				current.setDoublePath(east, Node.EAST);
				Node south;
				try{south = nodes[row+1][col];}
				catch(ArrayIndexOutOfBoundsException ex){south = null;}
				current.setDoublePath(south, Node.SOUTH);
				if(east != null && south != null)
				{
					Node southeast;
					try{southeast = nodes[row+1][col+1];}
					catch(ArrayIndexOutOfBoundsException ex){southeast = null;}
					current.setDoublePath(southeast, Node.SOUTHEAST);
				}
				Node west;
				try{west = nodes[row][col-1];}
				catch(ArrayIndexOutOfBoundsException ex){west = null;}
				if(west != null && south != null)
				{
					Node southwest;
					try{southwest = nodes[row+1][col-1];}
					catch(ArrayIndexOutOfBoundsException ex){southwest = null;}
					current.setDoublePath(southwest, Node.SOUTHWEST);
				}
			}
		}
	}

	private void findRooms()
	{
		boolean[][] visited = new boolean[nodes.length][nodes[0].length];
		for(int row = 0; row < nodes.length; row++)
		{
			for(int col = 0; col < nodes[0].length; col++)
			{
				if(nodes[row][col] != null && !visited[row][col] && isRoom(row, col))
				{
					createRoom(row, col, visited);
				}
			}
		}
	}

	private boolean isRoom(int row, int col)
	{
		Node current = nodes[row][col];
		return
			(current.getPath(Node.NORTHWEST) != null) ||
			(current.getPath(Node.NORTHEAST) != null) ||
			(current.getPath(Node.SOUTHWEST) != null) ||
			(current.getPath(Node.SOUTHEAST) != null);
	}

	private void createRoom(int row, int col, boolean[][] visited)
	{
		LinkedList<Node> nodesToVisit = new LinkedList<>();
		RoomNode room = new RoomNode();
		nodesToVisit.add(nodes[row][col]);
		while(nodesToVisit.size() > 0)
		{
			Node current = nodesToVisit.remove();
			visited[current.getY()][current.getX()] = true;
			room.addNode(current);
			addUnvisitedToQueue(current, Node.NORTHWEST, nodesToVisit, visited);
			addUnvisitedToQueue(current, Node.NORTHEAST, nodesToVisit, visited);
			addUnvisitedToQueue(current, Node.SOUTHWEST, nodesToVisit, visited);
			addUnvisitedToQueue(current, Node.SOUTHEAST, nodesToVisit, visited);
			addUnvisitedToQueue(current, Node.NORTH, nodesToVisit, visited);
			addUnvisitedToQueue(current, Node.EAST, nodesToVisit, visited);
			addUnvisitedToQueue(current, Node.WEST, nodesToVisit, visited);
			addUnvisitedToQueue(current, Node.SOUTH, nodesToVisit, visited);
		}
		rooms.add(room);
	}

	public void addUnvisitedToQueue(Node current, int direction, LinkedList nodesToVisit, boolean[][] visited)
	{
		Node nw = current.getPath(direction);
		if(nw != null && !visited[nw.getY()][nw.getX()] && isRoom(nw.getY(), nw.getX()))
		{
			nodesToVisit.add(nw);
			visited[nw.getY()][nw.getX()] = true;
		}
	}

	public void setDrawMap(boolean maptype, boolean repaint)
	{
		mapToDraw = maptype;
		if(repaint){repaint();}
	}

	public void reopen(boolean repaint)
	{
		generateTiles(tileFilename);
		generateBasemap(tilemapFilename);
		generateTilemap(basemap);
		findNodes();
		findPaths();
		findRooms();
		if(repaint){repaint();}
	}

	public void toggleNodes(boolean repaint)
	{
		drawNodes = !drawNodes;
		if(repaint){repaint();}
	}

	public void togglePaths(boolean repaint)
	{
		drawPaths = !drawPaths;
		if(repaint){repaint();}
	}

	public void toggleRooms(boolean repaint)
	{
		drawRooms = !drawRooms;
		if(repaint){repaint();}
	}
}