/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.dungeon;

import java.util.ArrayList;
import mysterydungeon.Controls;
import mysterydungeon.DungeonComp;
import mysterydungeon.entity.Entity;
import mysterydungeon.move.BrawlMove;
import mysterydungeon.move.Move;

/**
 *
 * @author Justis
 */
public class GameLoop extends Thread
{
    public static final int FRAMES_WALK = 6;
    
    private final DungeonComp comp;
    int moveFrame = 0;
    Move move = new BrawlMove(10);
    
    public GameLoop(DungeonComp dungeon)
    {
        this.comp = dungeon;
    }
    
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
                if(comp.getDungeon() != null)
                {
                    updateGame(delta);
                }
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
        
        public void updateGame(double delta)
        {
            Dungeon dungeon = comp.getDungeon();
            ArrayList<Entity> entities = dungeon.getEntities();
            Entity player = entities.get(0);
            for(Entity entity : entities)
            {
                if(entity.isMoving())
                {
                    int[] iValues = interpolate(entity, 6);
                    entity.addPixel(iValues[0], iValues[1]);
                    if(entity.getDestinationNode().equals(entity.getPixelX(), entity.getPixelY()))
                    {
                        entity.setCurrentNode(entity.getDestinationNode());
                        entity.setMoving(false);
                    }
                }
                else if(entity.equals(player))
                {
                    if(handleControls(entity))
                    {
                        for(Entity others : entities)
                        {
                            others.doState();
                            others.setMoving(true);
                        }
                        player.addHP(1);
                    }
                }
            }
        }
        
        private int[] interpolate(Entity entity, int delta)
        {
            int[] returnPoints = new int[2];
            returnPoints[0] = (entity.getDestinationNode().getX() - entity.getX()) * (24 / delta);
            returnPoints[1] = (entity.getDestinationNode().getY() - entity.getY()) * (24 / delta);
            return returnPoints;
        }
        
        private boolean handleControls(Entity player)
        {
            Node playerNode = player.getCurrentNode();
            Controls controls = Controls.getInstance();
            if(controls.isFacePressed() && controls.isDirectionPressed())
            {
                if(controls.getDirection() != -1)
                {
                    player.facing = controls.getDirection();
                    controls.clearDirection();
                    return false;
                }
                else
                {
                    controls.clearDirection();
                    return true;
                }
            }
            if(controls.isDirectionPressed())
            {
                if(controls.getDirection() != -1 && playerNode.getPath(controls.getDirection()) != null)
                {
                    player.setDestinationNode(playerNode.getPath(controls.getDirection()));
                    player.facing = controls.getDirection();
                    player.setMoving(true);
                    controls.clearDirection();
                    return true;
                }
                else if(controls.getDirection() == -1)
                {
                    controls.clearDirection();
                    return true;
                }
            }
            return false;
        }
}
