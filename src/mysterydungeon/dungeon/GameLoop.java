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

/**
 *
 * @author Justis
 */
public class GameLoop extends Thread
{
    public static final int FRAMES_WALK = 6;
    
    private DungeonComp comp;
    private boolean moving = false;
    int moveFrame = 0;
    
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
            Controls controls = Controls.getInstance();
            ArrayList<Entity> entities = dungeon.getEntities();
            Entity player = entities.get(0);
            Node pNode = player.getCurrentNode();
            for(Entity entity : entities)
            {
                if(entity.isMoving())
                {
                    int[] iValues = interpolate(entity, delta);
                    entity.addPixel(iValues[0], iValues[1]);
                    if(entity.getDestinationNode().equals(entity.getPixelX(), entity.getPixelY()))
                    {
                        entity.setCurrentNode(entity.getDestinationNode());
                        entity.setMoving(false);
                    }
                }
                else if(entity.equals(player) && controls.isDirectionPressed())
                {
                    if(!controls.isFacePressed() && pNode.getPath(controls.getDirection()) != null)
                    {    
                        player.setDestinationNode(pNode.getPath(controls.getDirection()));
                        player.facing = controls.getDirection();
                        controls.clearDirection();
                        for(Entity others : entities)
                        {
                            others.doState();
                            others.setMoving(true);
                        }
                        player.addHP(1);
                    }
                    else if(controls.isFacePressed())
                    {
                        player.facing = controls.getDirection();
                        controls.clearDirection();
                    }
                }
            }
        }
        
        private int[] interpolate(Entity entity, double delta)
        {
            int[] returnPoints = new int[2];
            returnPoints[0] = (entity.getDestinationNode().getX() - entity.getX()) * (24 / FRAMES_WALK);
            returnPoints[1] = (entity.getDestinationNode().getY() - entity.getY()) * (24 / FRAMES_WALK);
            return returnPoints;
        }
}
