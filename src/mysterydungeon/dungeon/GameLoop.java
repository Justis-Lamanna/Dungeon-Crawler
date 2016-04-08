/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.dungeon;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import mysterydungeon.Controls;
import mysterydungeon.DungeonComp;
import mysterydungeon.MysteryDungeon;
import mysterydungeon.animation.Animation;
import mysterydungeon.animation.FadeScreenAnimation;
import mysterydungeon.entity.SpeciesEntity;
import mysterydungeon.entity.ItemEntity;
import mysterydungeon.entity.StairEntity;
import mysterydungeon.move.Move;
import static mysterydungeon.move.Move.delay;

/**
 * The main loop of the game.
 * This class essentially operates on a never-ending loop,
 * which constantly updates the game and repaints.
 * @author Justis
 */
public class GameLoop implements Runnable
{

    /**
     * The number of frames that should be used to walk one tile.
     */
    public static final int FRAMES_WALK = 12;
    
    /**
     * The number of frames that should be used to run one tile.
     */
    public static final int FRAMES_RUN = 6;
    
    private boolean useMove = true;
    private final DungeonComp comp;
    private final Controls controls = Controls.getInstance();
    
    /**
     * Creates an instance of this GameLoop.
     */
    public GameLoop()
    {
        this.comp = DungeonComp.getInstance();
    }
    
    /**
     * Begins running this GameLoop.
     * This class was originally intended to be a separate thread, but
     * it caused slight issues in regards to reading and writing. This class
     * now runs normally, once the dungeon and 
     */
    @Override
    public void run()
    {
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        boolean gameRunning = true;
        while(gameRunning)
        {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double)OPTIMAL_TIME); //1 if updateLength == optimal time
            updateGame(delta);
            controls.update();
            comp.repaint();
            long sleepTime = (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000;
            if(sleepTime < 10){sleepTime = 10;}
            try
            {
                Thread.sleep(sleepTime);
            }
            catch(InterruptedException ex)
            {
                gameRunning = false;
            }
        }
        System.exit(0);
    }
        
    /**
     * Updates the game using whatever game logic is programmed in.
     * @param delta A fraction representing the actual loop time, divided by the optimal time (One second).
     */
    public void updateGame(double delta)
    {
        Dungeon dungeon = comp.getDungeon();
        ArrayList<SpeciesEntity> entities = dungeon.getEntities();
        SpeciesEntity player = entities.get(0);
        boolean doneMoving = true;
        boolean isRunning = controls.isKeyDown(KeyEvent.VK_NUMPAD5);
        for(SpeciesEntity entity : entities)
        {
            if(entity.isMoving())
            {
                doneMoving = false;
                int[] iValues = interpolate(entity, isRunning ? FRAMES_RUN : FRAMES_WALK);
                entity.addPixel(iValues[0], iValues[1]);
                if(entity.getDestinationNode().equals(entity.getX(), entity.getY()))
                {
                    entity.setCurrentNode(entity.getDestinationNode());
                    entity.setMoving(false);
                }
            }
        }
        if(doneMoving && handleControls(dungeon, player))
        {
            for(SpeciesEntity others : entities)
            {
                others.doState();
                others.setMoving(true);
            }
            onPlayerStep(dungeon, player);
        }
        if(getAttackPressed() == -1)
        {
            useMove = true;
        }
    }
        
    private int[] interpolate(SpeciesEntity entity, int delta)
    {
        int[] returnPoints = new int[2];
        returnPoints[0] = (entity.getDestinationNode().getX() - entity.getTileX()) * (24 / delta);
        returnPoints[1] = (entity.getDestinationNode().getY() - entity.getTileY()) * (24 / delta);
        return returnPoints;
    }

    private boolean handleControls(Dungeon dungeon, SpeciesEntity player)
    {
        Node playerNode = player.getCurrentNode();
        ArrayList<SpeciesEntity> entities = dungeon.getEntities();
        int directionPressed = getDirectionPressed();
        int attackPressed = getAttackPressed();
        if(controls.isKeyDown(KeyEvent.VK_CONTROL))
        {
            if(directionPressed != -1)
            {
                player.facing = directionPressed;
                return false;
            }
            else
            {
                for(int dir = 0; dir < 8; dir++)
                {
                    Node next = playerNode;
                    for(int range = 0; range < 4; range++)
                    {
                        if(next == null){break;}
                        next = next.getPath(dir);
                        for(SpeciesEntity entity : entities)
                        {
                            if(entity.getCurrentNode().equals(next))
                            {
                                player.facing = dir;
                                return false;
                            }
                        }
                    }
                }
                return false;
            }
        }
        else if(directionPressed != -1)
        {
            if(playerNode.getPath(directionPressed) != null)
            {
                player.setDestinationNode(playerNode.getPath(directionPressed));
                player.facing = directionPressed;
                player.setMoving(true);
                return true;
            }
            else
            {
                //Collision. Make a bump noise in the future, I guess?
            }
        }
        else if(attackPressed != -1 && useMove)
        {
            ArrayList<Move> knownMoves = player.getMoves();
            if(attackPressed < knownMoves.size())
            {
                Move attackUsed = knownMoves.get(attackPressed);
                useMove = false;
                //Now, we go for the kill!
                ArrayList<SpeciesEntity> defender = attackUsed.getDefender(comp.getDungeon(), player);
                attackUsed.attack(comp.getDungeon(), player, defender);
                return true;
            }
        }
        return false;
    }

    private int getDirectionPressed()
    {
        if(controls.isKeyDown(KeyEvent.VK_NUMPAD4)){return 6;}
        else if(controls.isKeyDown(KeyEvent.VK_NUMPAD6)){return 2;}
        else if(controls.isKeyDown(KeyEvent.VK_NUMPAD8)){return 0;}
        else if(controls.isKeyDown(KeyEvent.VK_NUMPAD2)){return 4;}
        else if(controls.isKeyDown(KeyEvent.VK_NUMPAD7)){return 7;}
        else if(controls.isKeyDown(KeyEvent.VK_NUMPAD9)){return 1;}
        else if(controls.isKeyDown(KeyEvent.VK_NUMPAD3)){return 3;}
        else if(controls.isKeyDown(KeyEvent.VK_NUMPAD1)){return 5;}
        else{return -1;}
    }

    private int getAttackPressed()
    {
        for(int key = 0; key < 9; key++)
        {
            if(controls.isKeyDown(KeyEvent.VK_1 + key))
            {
                return key;
            }
        }
        return -1;
    }

    private void onPlayerStep(Dungeon dungeon, SpeciesEntity player)
    {
        int playerCenterX = player.getDestinationNode().getX();
        int playerCenterY = player.getDestinationNode().getY();
        dungeon.setDiscovered(playerCenterX, playerCenterY);
        player.addHP(1);
        ArrayList<ItemEntity> items = dungeon.getItems();
        for(ItemEntity item : items)
        {
            if(playerCenterX == item.getX() && playerCenterY == item.getY())
            {
                dungeon.clearItem(item);
                MysteryDungeon.updateLog(String.format("Picked up a %s.", item.getContained().getName()));
                player.addItem(item.getContained());
                break;
            }
        }
        StairEntity stairs = dungeon.getStairs();
        if(playerCenterX == stairs.getX() && playerCenterY == stairs.getY())
        {
            doStairs();
        }
    }
    
    public void doStairs()
    {
        DungeonComp component = DungeonComp.getInstance();
        Animation fadeAnimation = new FadeScreenAnimation(
                0, 0, component.getWidth(), component.getHeight(), 0, -8);
        Animation.animate(fadeAnimation, 20);
        component.getDungeon().startDungeon();
        fadeAnimation = new FadeScreenAnimation(
                0, 0, component.getWidth(), component.getHeight(), 255, 8);
        Animation.animate(fadeAnimation, 20);
    }
}
