//
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DungeonView extends JFrame
{
	public static final int INITIAL_WIDTH = 800;
	public static final int INITIAL_HEIGHT = 800;
	public static final String TILES = "tiles.png";
	public static final String TILEMAP = "tilemap.txt";

	private DungeonComp dungeon;

	public static void main(String[] args)
	{
		DungeonView frame = new DungeonView(TILES, TILEMAP);
		frame.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public DungeonView(String tileFilename, String tilemapFilename)
	{
		JPanel frame = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		dungeon = new DungeonComp(tileFilename, tilemapFilename);
		JScrollPane scrollpane = new JScrollPane(dungeon);
		c = setGridBagConstraints(0, 0, 5, 1, 0.9, 1.0);
		frame.add(scrollpane, c);

		JButton baseMapButton = new JButton("Base Map");
		c = setGridBagConstraints(0, 1, 1, 1, 0.1, 0.1);
		baseMapButton.addActionListener((ActionEvent e) -> dungeon.setDrawMap(DungeonComp.BASEMAP, true));
		frame.add(baseMapButton, c);

		JButton regMapButton = new JButton("Tile Map");
		c = setGridBagConstraints(0, 2, 1, 1, 0.1, 0.1);
		regMapButton.addActionListener((ActionEvent e) -> dungeon.setDrawMap(DungeonComp.TILEMAP, true));
		frame.add(regMapButton, c);

		JButton refreshButton = new JButton("Reopen Tile Map");
		c = setGridBagConstraints(1, 1, 1, 1, 0.1, 0.1);
		refreshButton.addActionListener((ActionEvent e) -> dungeon.reopen(true));
		frame.add(refreshButton, c);

		JButton kachunkButton = new JButton("Ka-Chunk!");
		c = setGridBagConstraints(1, 2, 1, 1, 0.1, 0.1);
		kachunkButton.addActionListener((ActionEvent e) -> dungeon.kachunk(true));
		frame.add(kachunkButton, c);

		JButton retryButton = new JButton("Toggle Nodes");
		c = setGridBagConstraints(2, 1, 1, 1, 0.1, 0.1);
		retryButton.addActionListener((ActionEvent e) -> dungeon.toggleNodes(true));
		frame.add(retryButton, c);

		JButton pathButton = new JButton("Toggle Path");
		c = setGridBagConstraints(2, 2, 1, 1, 0.1, 0.1);
		pathButton.addActionListener((ActionEvent e) -> dungeon.togglePaths(true));
		frame.add(pathButton, c);

		JButton roomButton = new JButton("Toggle Rooms");
		c = setGridBagConstraints(3, 1, 1, 1, 0.1, 0.1);
		roomButton.addActionListener((ActionEvent e) -> dungeon.toggleRooms(true));
		frame.add(roomButton, c);

		JButton showEntityButton = new JButton("Toggle Entities");
		c = setGridBagConstraints(3, 2, 1, 1, 0.1, 0.1);
		showEntityButton.addActionListener((ActionEvent e) -> dungeon.toggleEntity(true));
		frame.add(showEntityButton, c);

		JButton spawnEnemy = new JButton("Spawn Enemy");
		c = setGridBagConstraints(4, 1, 1, 1, 0.1, 0.1);
		spawnEnemy.addActionListener((ActionEvent e) -> dungeon.spawnEnemies(1, true));
		frame.add(spawnEnemy, c);

		JButton clearEnemy = new JButton("Clear Enemies");
		c = setGridBagConstraints(4, 2, 1, 1, 0.1, 0.1);
		clearEnemy.addActionListener((ActionEvent e) -> dungeon.clearEnemies(true));
		frame.add(clearEnemy, c);

		addInput("NUMPAD8", "goNorth", new DirectionAction(dungeon, 0));
		addInput("NUMPAD9", "goNorthWest", new DirectionAction(dungeon, 1));
		addInput("NUMPAD6", "goWest", new DirectionAction(dungeon, 2));
		addInput("NUMPAD3", "goSouthWest", new DirectionAction(dungeon, 3));
		addInput("NUMPAD2", "goSouth", new DirectionAction(dungeon, 4));
		addInput("NUMPAD1", "goSouthEast", new DirectionAction(dungeon, 5));
		addInput("NUMPAD4", "goEast", new DirectionAction(dungeon, 6));
		addInput("NUMPAD7", "goNorthEast", new DirectionAction(dungeon, 7));

		add(frame);
	}

	public GridBagConstraints setGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty)
	{
		return new GridBagConstraints(
			gridx, 
			gridy, 
			gridwidth, 
			gridheight, 
			weightx, 
			weighty, 
			GridBagConstraints.CENTER, 
			GridBagConstraints.BOTH, 
			new Insets(5, 5, 5, 5), 
			0, 
			0);
	}

	public void addInput(String stroke, String name, Action action)
	{
		dungeon.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(stroke), name);
		dungeon.getActionMap().put(name, action);
	}
}