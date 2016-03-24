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
    private static final int[] CONVERSION = {5, 4, 3, 6, -1, 2, 7, 0, 1}; //Converts Num1-Num9 to direction
    private static final int NUM_ATTACKS = 3; //NUM_ATTACKS should be somewhere between 1 and 9, for logic's sake.
    
    private boolean directionPressed = false;
    private int direction = -1;
    
    private boolean[] attackPressed = new boolean[NUM_ATTACKS];
    
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
    
    public boolean isAttackPressed()
    {
        boolean returnValue = false;
        for(boolean slot : attackPressed)
        {
            returnValue |= slot;
        }
        return returnValue;
    }
    public int attackPressed()
    {
        for(int attack = 0; attack < attackPressed.length; attack++)
        {
            if(attackPressed[attack]){return attack + 1;}
        }
        return 0;
    }
    public void clearAttackPressed(int slot)
    {
        attackPressed[slot] = false;
    }
    
    public void clearDirection()
    {
        direction = -1;
        directionPressed = false;
    }
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() >= KeyEvent.VK_NUMPAD1 && e.getKeyCode() < KeyEvent.VK_NUMPAD9)
        {
            directionPressed = true;
            int index = e.getKeyCode() - KeyEvent.VK_NUMPAD1;
            direction = CONVERSION[index];
        }
        else if(e.getKeyCode() == KeyEvent.VK_CONTROL)
        {
            facePressed = true;
        }
        else if(e.getKeyCode() >= KeyEvent.VK_1 && e.getKeyCode() < (KeyEvent.VK_1 + NUM_ATTACKS))
        {
            int index = e.getKeyCode() - KeyEvent.VK_1;
            attackPressed[index] = true;
            System.out.printf("Attack %d pressed.\n", index);
        }
        
    }
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        if(e.getKeyCode() >= KeyEvent.VK_NUMPAD1 && e.getKeyCode() < KeyEvent.VK_NUMPAD9)
        {
            directionPressed = false;
            direction = -1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_CONTROL)
        {
            facePressed = false;
        }
        else if(e.getKeyCode() >= KeyEvent.VK_1 && e.getKeyCode() < (KeyEvent.VK_1 + NUM_ATTACKS))
        {
            int index = e.getKeyCode() - KeyEvent.VK_1;
            attackPressed[index] = false;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e)
    {
        //Nothing I guess?
    }
}
