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
}