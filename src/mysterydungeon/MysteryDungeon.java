/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import mysterydungeon.entity.Entity;

/**
 *
 * @author Justis
 */
public class MysteryDungeon extends JFrame{

    public static final int INITIAL_WIDTH = 1000;
    public static final int INITIAL_HEIGHT = 800;
    public static final String TILES = "Sprites/tiles.png";
    public static final String TILEMAP = "Maps/map1.txt";
    
    public static final JProgressBar HPBAR = new JProgressBar(0, 100);
    public static final JTextArea LOG = new JTextArea("Entered the Dungeon.\n", 50, 10);
    

    private DungeonComp dungeon;

    public static void main(String[] args)
    {
            MysteryDungeon frame = new MysteryDungeon(TILES, TILEMAP);
            frame.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
    }

    public MysteryDungeon(String tileFilename, String tilemapFilename)
    {
            JPanel frame = new JPanel(new GridBagLayout());
            GridBagConstraints c;

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
            
            JPanel hud = createHud();
            c = setGridBagConstraints(5, 0, 1, 3, 1, 1);
            frame.add(hud, c);
            
            addKeyListener(Controls.getInstance());
            this.setFocusable(true);
            baseMapButton.addKeyListener(Controls.getInstance());
            regMapButton.addKeyListener(Controls.getInstance());
            refreshButton.addKeyListener(Controls.getInstance());
            kachunkButton.addKeyListener(Controls.getInstance());
            retryButton.addKeyListener(Controls.getInstance());
            pathButton.addKeyListener(Controls.getInstance());
            roomButton.addKeyListener(Controls.getInstance());
            showEntityButton.addKeyListener(Controls.getInstance());
            spawnEnemy.addKeyListener(Controls.getInstance());
            clearEnemy.addKeyListener(Controls.getInstance());
            
            add(frame);
    }

    private GridBagConstraints setGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty)
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
    
    private JPanel createHud()
    {
        JPanel hud = new JPanel(new GridBagLayout());
        hud.add(new JLabel("Player Stats"), setGridBagConstraints(0, 0, 2, 1, 1, 0.1));
        hud.add(new JLabel("HP:"), setGridBagConstraints(0, 1, 1, 1, 0.1, 0.1));
        hud.add(HPBAR, setGridBagConstraints(1, 1, 1, 1, 0.9, 0.1));
        Entity player = dungeon.getDungeon().getEntities().get(0);
        HPBAR.setMaximum(player.getMaximumHP());
        HPBAR.setValue(player.getCurrentHP());
        HPBAR.setString(String.format("%d/%d", player.getCurrentHP(), player.getMaximumHP()));
        HPBAR.setStringPainted(true);
        hud.add(new JLabel("Log:"), setGridBagConstraints(0, 2, 2, 1, 1, 0.1));
        hud.add(new JScrollPane(LOG), setGridBagConstraints(0, 3, 2, 2, 1, 1));
        LOG.setForeground(Color.WHITE);
        LOG.setBackground(Color.BLACK);
        return hud;
    }
}
