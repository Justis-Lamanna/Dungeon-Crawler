/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.entity;

import java.util.ArrayList;
import mysterydungeon.MysteryDungeon;
import mysterydungeon.DungeonComp;
import mysterydungeon.dungeon.Dungeon;
import mysterydungeon.dungeon.Node;
import mysterydungeon.dungeon.RoomNode;
import mysterydungeon.move.Move;

/**
 * A class representing an entity. An entity is any moving object in a dungeon.
 * @author Justis
 */
public class Entity
{
    private Node currentNode;
    private Node gotoNode;
    private Dungeon dungeon;
    private Species species;
    private EntityState currentState;
    private int currentX;
    private int currentY;
    private boolean moving;
    private String name;
    private boolean isPlayer;

    private int currentHP;
    private int maxHP;

    private ArrayList<Move> knownMoves;

    /**
     * The direction this entity is facing.
     */
    public int facing;

    /**
     * Creates an entity.
     * Remember, the entity will only be drawn when it's added to the dungeon!
     * @param dungeon The dungeon this entity resides in.
     * @param species The species this entity is.
     * @param startState The initial state of this entity.
     * @param player True if this entity is the player, false if not.
     */
    public Entity(Dungeon dungeon, Species species, EntityState startState, boolean player)
    {
        this.dungeon = dungeon;
        this.species = species;
        this.name = species.getName();
        this.isPlayer = player;
        maxHP = species.getHP();
        currentHP = maxHP;
        knownMoves = (ArrayList<Move>)species.getMoves().clone(); //Clone so we don't modify the actual species data.
        randomizeLocation();
        currentState = startState;
    }

    /**
     * Creates an entity.
     * This entity will be initialized with a default state of MoveState, causing
     * it to wander the dungeons until it sees the player. Remember, the entity will
     * only be drawn when it's added to the dungeon!
     * @param dungeon The dungeon this entity resides in.
     * @param species The species this entity is.
     * @param player True if this entity is the player, false if not.
     */
    public Entity(Dungeon dungeon, Species species, boolean player)
    {
        this(dungeon, species, new MoveState(), player);
    }

    /**
     * Creates an entity.
     * This entity will be initialized with a default state of MoveState, causing
     * it to wander the dungeons until it sees the player. It is also, by default,
     * not the player. Remember, the entity will only be drawn when it's added to
     * the dungeon!
     * @param dungeon The dungeon this entity resides in.
     * @param species The species this entity is.
     */
    public Entity(Dungeon dungeon, Species species)
    {
        this(dungeon, species, new MoveState(), false);
    }

    private Node generateLocation()
    {
        ArrayList<RoomNode> rooms = dungeon.getRooms();
        int randomRoom = Dungeon.PRNG.nextInt(rooms.size());
        RoomNode room = rooms.get(randomRoom);
        ArrayList<Node> nodes = room.getNodes();
        int randomNode = Dungeon.PRNG.nextInt(nodes.size());
        return nodes.get(randomNode);
    }

    /**
     * Places this entity on a random node in a random room.
     */
    public void randomizeLocation()
    {
        currentNode = generateLocation();
        currentX = currentNode.getX() * DungeonComp.TILE_SIZE;
        currentY = currentNode.getY() * DungeonComp.TILE_SIZE;
        gotoNode = currentNode;
    }

    /**
     * Get the species of this entity.
     * @return This entity's species.
     */
    public Species getSpecies()
    {
        return species;
    }

    /**
     * Get the current node this entity is on.
     * @return The entity's current node.
     */
    public Node getCurrentNode()
    {
        return currentNode;
    }

    /**
     * Set the current node this entity is on.
     * This method results in an immediate warp. If you want to slide to
     * a new node, you must use setDestinationNode.
     * @param n The node this entity should move to.
     */
    public void setCurrentNode(Node n)
    {
        currentNode = n;
        currentX = currentNode.getX() * DungeonComp.TILE_SIZE;
        currentY = currentNode.getY() * DungeonComp.TILE_SIZE;
    }

    /**
     * Get the node this entity is traveling to.
     * @return The destination node of this entity.
     */
    public Node getDestinationNode()
    {
        return gotoNode;
    }

    /**
     * Set the node this entity should travel to.
     * @param n The node this entity should be traveling to.
     */
    public void setDestinationNode(Node n)
    {
        gotoNode = n;
    }

    /**
     * Get the X position, in tiles, of this Entity.
     * @return
     */
    public int getX()
    {
        return currentNode.getX();
    }

    /**
     * Get the Y position, in tiles, of this Entity.
     * @return
     */
    public int getY()
    {
        return currentNode.getY();
    }

    /**
     * Get the X position, in pixels, of this Entity.
     * @return
     */
    public int getPixelX()
    {
        return currentX;
    }

    /**
     * Get the Y position, in pixels, of this entity.
     * @return
     */
    public int getPixelY()
    {
        return currentY;
    }

    /**
     * Set the current X-Y position of this sprite, in pixels.
     * @param newX
     * @param newY
     */
    public void setPixel(int newX, int newY)
    {
        currentX = newX;
        currentY = newY;
    }

    /**
     * Add a certain value to the X and Y position of this sprite.
     * IN PIXELS!
     * @param dx
     * @param dy
     */
    public void addPixel(int dx, int dy)
    {
        currentX += dx;
        currentY += dy;
    }

    /**
     * Sets the state of this Entity.
     * @param state THe new state this Entity should have.
     */
    public void setState(EntityState state)
    {	
        currentState = state;
        if(state.getClass().equals(FollowState.class))
        {
            MysteryDungeon.LOG.append(String.format("%s gave chase!\n", species.getName()));
        }
    }

    /**
     * Causes this entity to act in its current state.
     */
    public void doState()
    {
        if(currentState != null)
        {
            currentState.doState(this, dungeon);
        }
    }

    /**
     * Get this entity's state.
     * @return The entity's current state.
     */
    public EntityState getState()
    {
        return currentState;
    }

    /**
     * Check if this entity can walk over water.
     * @return True if it can move over water, false if not.
     */
    public boolean isWater()
    {
        return species.isWater();
    }

    /**
     * Check if this sprite is in the process of moving.
     * @return True if the sprite is in motion, false if not.
     */
    public boolean isMoving()
    {
        return moving;
    }

    /**
     * Set this sprite as moving.
     * @param set True if this sprite should be in motion, false if not.
     */
    public void setMoving(boolean set)
    {
        moving = set;
    }

    /**
     * Get the current HP of this entity.
     * @return This entity's current HP.
     */
    public int getCurrentHP(){return currentHP;}

    /**
     * Get the maximum HP of this entity.
     * @return The entity's maximum HP.
     */
    public int getMaximumHP(){return maxHP;}

    /**
     * Set the current HP of this entity.
     * If the new value is below zero, it is set to zero. If the new value is
     * higher than the maximum HP, it's set to the maximum HP.
     * Updates the HP bar if this is the player.
     * @param newval This entity's new current HP.
     */
    public void setCurrentHP(int newval)
    {
        if(newval > maxHP){newval = maxHP;}
        else if(newval < 0){newval = 0;}
        currentHP = newval;
        updateHPBar();
    }

    /**
     * Set the maximum HP of this entity.
     * Updates the HP bar if this is the player.
     * @param newval This entity's new maximum HP.
     */
    public void setMaxHP(int newval)
    {
        maxHP = newval;
        updateHPBar();
    }

    /**
     * Adds a certain amount to the current HP.
     * If the resulting HP is below zero, it's set to zero. If the resulting
     * HP is greater than the maximum HP, it's set to the maximum HP.
     * @param dhp The amount of HP to add to the current HP.
     * @return The amount of HP that was actually added.
     */
    public int addHP(int dhp)
    {
        int oldHP = currentHP;
        currentHP += dhp;
        if(currentHP > maxHP){currentHP = maxHP;}
        else if(currentHP < 0){currentHP = 0;}
        updateHPBar();
        return oldHP - currentHP;
    }

    private void updateHPBar()
    {
        if(isPlayer)
        {
            MysteryDungeon.HPBAR.setValue(currentHP);
            MysteryDungeon.HPBAR.setMaximum(maxHP);
            MysteryDungeon.HPBAR.setString(String.format("%d/%d", currentHP, maxHP));
        }
    }

    /**
     * Set the name of this entity.
     * @param newname The new name of this entity.
     */
    public void setName(String newname)
    {
        name = newname;
    }

    /**
     * Get the name of this entity.
     * @return The name of this entity.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get a list of moves this Entity has.
     * @return A list of moves this entity can use.
     */
    public ArrayList<Move> getMoves()
    {
        return knownMoves;
    }

    /**
     * Adds a new move to this entity's arsenal.
     * @param newMove The new move this entity can use.
     */
    public void addMove(Move newMove)
    {
        knownMoves.add(newMove);
    }
    
    public boolean isPlayer()
    {
        return isPlayer;
    }

    @Override
    public String toString()
    {
        return String.format("[Entity: %s @ %d, %d]", species.getName(), getX(), getY());
    }
}
