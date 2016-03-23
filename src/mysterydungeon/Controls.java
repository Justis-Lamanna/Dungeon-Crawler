/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Justis
 */
public class Controls implements KeyListener
{
    private static Controls singleton = null;
    private static final int[] conversion = {5, 4, 3, 6, -1, 2, 7, 0, 1}; //Converts Num1-Num9 to direction
    
    private boolean directionPressed = false;
    private int direction = -1;
    
    private boolean facePressed = false;
    
    private Controls(){}
    
    public static Controls getInstance()
    {
        if(singleton == null)
        {
            singleton = new Controls();
        }
        return singleton;
    }
    
    public boolean isDirectionPressed(){return directionPressed;}
    public void setDirectionPressed(boolean set){directionPressed = set;}
    
    public int getDirection(){return direction;}
    public void setDirection(int newdirection){direction = newdirection;}
    
    public boolean isFacePressed(){return facePressed;}
    public void setFacePressed(boolean set){facePressed = set;}
    
    public void clearDirection()
    {
        direction = -1;
        directionPressed = false;
    }
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() >= KeyEvent.VK_NUMPAD1 && e.getKeyCode() <= KeyEvent.VK_NUMPAD9)
        {
            directionPressed = true;
            int index = e.getKeyCode() - KeyEvent.VK_NUMPAD1;
            direction = conversion[index];
        }
        else if(e.getKeyCode() == KeyEvent.VK_CONTROL)
        {
            facePressed = true;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        if(e.getKeyCode() >= KeyEvent.VK_NUMPAD1 && e.getKeyCode() <= KeyEvent.VK_NUMPAD9)
        {
            directionPressed = false;
            direction = -1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_CONTROL)
        {
            facePressed = false;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e)
    {
        //Nothing I guess?
    }
}
