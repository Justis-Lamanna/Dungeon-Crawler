/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Class that keeps track of keys pressed and released. This class is used as a medium
 * between key-based interrupts and key polling, used in other games.
 * @author Justis
 */
public class Controls implements KeyListener
{
    private static Controls singleton = null;
    private static final int[] CONVERSION = {5, 4, 3, 6, -1, 2, 7, 0, 1}; //Converts Num1-Num9 to direction
    
    /**
     * The number of attacks the player can learn.
     */
    public static final int NUM_ATTACKS = 3; //NUM_ATTACKS should be somewhere between 1 and 9, for logic's sake.
    
    private boolean directionPressed = false;
    private int direction = -1;
    
    private final boolean[] attackPressed = new boolean[NUM_ATTACKS];
    
    private boolean facePressed = false;
    
    private Controls(){}
    
    /**
     * Get the current instance of the Control class.
     * @return An instance of the this class.
     */
    public static Controls getInstance()
    {
        if(singleton == null)
        {
            singleton = new Controls();
        }
        return singleton;
    }
    
    /**
     * Returns a boolean on whether a direction key is pressed.
     * @return True if a direction key is pressed, and false if not.
     */
    public boolean isDirectionPressed(){return directionPressed;}

    /**
     * Overrides whether a direction was pressed or not.
     * @param set True to force a direction pressed, false to force it released.
     */
    public void setDirectionPressed(boolean set){directionPressed = set;}
    
    /**
     * Get the current direction pressed.
     * @return Direction that was pressed, with 0 being north, 1 being northeast, and so on.
     */
    public int getDirection(){return direction;}

    /**
     * Sets the direction pressed to something else.
     * @param newdirection The new direction.
     */
    public void setDirection(int newdirection){direction = newdirection;}
    
    /**
     * Returns whether Ctrl was pressed, signifying to stand still in-game.
     * @return True if Ctrl is pressed, and false if it is not pressed.
     */
    public boolean isFacePressed(){return facePressed;}

    /**
     * Override whether Ctrl was pressed or not.
     * @param set True if Ctrl should read as pressed, false if it should read as released.
     */
    public void setFacePressed(boolean set){facePressed = set;}
    
    /**
     * Check whether an attack was pressed or not.
     * @return True if an attack was pressed, false if one was not.
     */
    public boolean isAttackPressed()
    {
        boolean returnValue = false;
        for(boolean slot : attackPressed)
        {
            returnValue |= slot;
        }
        return returnValue;
    }

    /**
     * Return which attack was pressed.
     * @return The attack pressed. 0 if none were pressed, or attack slot + 1 otherwise.
     */
    public int attackPressed()
    {
        for(int attack = 0; attack < attackPressed.length; attack++)
        {
            if(attackPressed[attack]){return attack + 1;}
        }
        return 0;
    }

    /**
     * Disable an attack slot from being pressed.
     * @param slot The attack slot to disable.
     */
    public void clearAttackPressed(int slot)
    {
        attackPressed[slot] = false;
    }
    
    /**
     * Resets the direction keys. Call after reading direction to prevent sliding.
     */
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
