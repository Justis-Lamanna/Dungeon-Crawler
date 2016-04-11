/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.GameLoop;
import mysterydungeon.entity.SpeciesEntity;
import mysterydungeon.item.Item;
import mysterydungeon.item.ItemComboBoxRenderer;
import mysterydungeon.item.NullItem;

/**
 * Entry point for the game. Initializes the main frame, such as the buttons,
 * HUD, and controls.
 * @author Justis
 */
public class MysteryDungeon extends JFrame{

    /**
     * The initial width of the frame.
     */
    public static final int INITIAL_WIDTH = 1000;

    /**
     * THe initial height of the frame.
     */
    public static final int INITIAL_HEIGHT = 800;

    /**
     * The filename of the tiles used for this demo.
     */
    public static final String TILES = "Sprites/tiles.png";

    /**
     * The filename of the base map used for this demo. In future editions,
     * dungeons will be randomly generated.
     */
    public static final String TILEMAP = "Maps/map1.txt";
    
    /**
     * A constant representing the numpad control scheme.
     */
    public static final int NUMPAD = 0;
    
    /**
     * A constant representing the arrow keys control scheme.
     */
    public static final int ARROW_KEYS = 1;
    
    private static int controlScheme = NUMPAD;
    
    private static JProgressBar hpBar = new JProgressBar(0, 100);
    private static JProgressBar staminaBar = new JProgressBar(0, 100);
    private static JComboBox<Item> inventory = new JComboBox<>();
    private static JTextArea log = new JTextArea("Entered the Dungeon.\n", 50, 10);
    
    private static MidiChannel[] midi;
    private static Synthesizer synth;

    private DungeonComp dungeon;

    /**
     * Start the game!
     * @param args Nothing is really done through args right now.
     */
    public static void main(String[] args)
    {
        MysteryDungeon frame = new MysteryDungeon(TILES, TILEMAP);
        frame.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        initializeMidi();
        new GameLoop().run();
    }
    
    private static void initializeMidi()
    {
        try
        {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            midi = synth.getChannels();
        }
        catch(MidiUnavailableException ex)
        {
            ex.printStackTrace();
        }   
    }
    
    public static void playNote(int instrument, int note, int volume, int delay)
    {
        Instrument[] instr = synth.getDefaultSoundbank().getInstruments();
        synth.loadInstrument(instr[instrument]);
        midi[0].noteOn(note, volume);
        delay(delay);
        midi[0].noteOff(note);
    }
    
    public static void delay(long millis)
    {
        long stop = System.currentTimeMillis() + millis;
        while(System.currentTimeMillis() < stop);
    }
    
    public static int getControlScheme()
    {
        return controlScheme;
    }

    /**
     * Initializes the frame, as well as the controls.
     * @param tileFilename Filename of the tiles to use.
     * @param tilemapFilename Filename of the base map to use.
     */
    public MysteryDungeon(String tileFilename, String tilemapFilename)
    {
        JPanel frame = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        
        Dungeon dg = new Dungeon(tilemapFilename, Dungeon.TEST_LIST, Dungeon.TEST_ITEMS);
        dungeon = DungeonComp.getInstance(tileFilename, dg);
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

        JButton kachunkButton = new JButton("Toggle Mask");
        c = setGridBagConstraints(1, 2, 1, 1, 0.1, 0.1);
        kachunkButton.addActionListener((ActionEvent e) -> dungeon.toggleMask(true));
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
        
        JMenuBar menu = createMenu();
        setJMenuBar(menu);

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
        SpeciesEntity player = dungeon.getDungeon().getEntities().get(0);
        JPanel hud = new JPanel(new GridBagLayout());
        
        hud.add(new JLabel("Player Stats"), setGridBagConstraints(0, 0, 3, 1, 1, 0.1));
        
        hud.add(new JLabel("HP:"), setGridBagConstraints(0, 1, 1, 1, 0.1, 0.1));
        hud.add(hpBar, setGridBagConstraints(1, 1, 2, 1, 0.9, 0.1));
        updateHP(player.getCurrentHP(), player.getMaximumHP());
        
        hud.add(new JLabel("Stamina:"), setGridBagConstraints(0, 2, 1, 1, 0.1, 0.1));
        hud.add(staminaBar, setGridBagConstraints(1, 2, 2, 1, 0.9, 0.1));
        updateStamina(player.getCurrentStamina(), player.getMaximumStamina());
        
        hud.add(new JLabel("Inventory:"), setGridBagConstraints(0, 3, 3, 1, 1, 0.1));
        
        hud.add(inventory, setGridBagConstraints(1, 4, 2, 1, 1, 0.1));
        inventory.addKeyListener(Controls.getInstance());
        inventory.setRenderer(new ItemComboBoxRenderer());
        updateInventory(player.getItems());
        JButton use = new JButton("Use");
        use.addKeyListener(Controls.getInstance());
        use.addActionListener(e -> player.useItem((Item)inventory.getSelectedItem()));
        hud.add(use, setGridBagConstraints(0, 4, 1, 1, 1, 0.1));
        
        hud.add(new JLabel("Log:"), setGridBagConstraints(0, 5, 3, 1, 1, 0.1));
        
        hud.add(new JScrollPane(log), setGridBagConstraints(0, 6, 3, 2, 1, 1));
        log.setForeground(Color.WHITE);
        log.setBackground(Color.BLACK);
        log.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        DefaultCaret caret = (DefaultCaret)log.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        JButton clearLog = new JButton("Clear Log");
        clearLog.addActionListener(e -> clearLog());
        hud.add(clearLog, setGridBagConstraints(0, 9, 3, 1, 1, 0.1));
        
        return hud;
    }
    
    private JMenuBar createMenu()
    {
        JMenuBar menu = new JMenuBar();
        JMenu controls = new JMenu("Controls");
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem numpad = new JRadioButtonMenuItem("Numpad", true);
        numpad.addItemListener(i -> controlScheme = NUMPAD);
        JRadioButtonMenuItem dpad = new JRadioButtonMenuItem("Arrow Keys");
        dpad.addItemListener(i -> controlScheme = ARROW_KEYS);
        group.add(numpad);
        group.add(dpad);
        controls.add(numpad);
        controls.add(dpad);
        menu.add(controls);
        return menu;
    }
    
    /**
     * Updates the HP bar to the player's current HP values.
     * @param current The player's current HP.
     * @param max The player's max HP.
     */
    public static void updateHP(int current, int max)
    {
        hpBar.setMaximum(max);
        hpBar.setValue(current);
        hpBar.setString(String.format("%d/%d", current, max));
        hpBar.setStringPainted(true);
    }
    
    /**
     * Updates the stamina bar to the player's current stamina values.
     * @param current The player's current stamina.
     * @param max The player's maximum stamina.
     */
    public static void updateStamina(int current, int max)
    {
        staminaBar.setMaximum(max);
        staminaBar.setValue(current);
        staminaBar.setString(String.format("%d/%d", current, max));
        staminaBar.setStringPainted(true);
    }
    
    /**
     * Updates the log.
     * This method automatically inserts a newline.
     * @param line The line to append to the end of the log.
     */
    public static void updateLog(String line)
    {
        log.append(line + "\n");
    }
    
    /**
     * Clears the log.
     */
    public static void clearLog()
    {
        log.setText("");
    }
    
    /**
     * Updates the inventory drop-down.
     * @param items The list of items.
     */
    public static void updateInventory(ArrayList<Item> items)
    {
        inventory.removeAllItems();
        if(items.isEmpty())
        {
            inventory.addItem(NullItem.NULL_ITEM);
        }
        else
        {
            for(Item item : items)
            {
                inventory.addItem(item);
            }
        }
    }
}
