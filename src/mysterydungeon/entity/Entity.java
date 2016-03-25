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
 *
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
     *
     */
    public int facing;

    /**
     *
     * @param dungeon
     * @param species
     * @param startState
     * @param player
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
     *
     * @param dungeon
     * @param species
     * @param player
     */
    public Entity(Dungeon dungeon, Species species, boolean player)
    {
        this(dungeon, species, new MoveState(), player);
    }

    /**
     *
     * @param dungeon
     * @param species
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
     *
     */
    public void randomizeLocation()
    {
        currentNode = generateLocation();
        currentX = currentNode.getX() * DungeonComp.TILE_SIZE;
        currentY = currentNode.getY() * DungeonComp.TILE_SIZE;
        gotoNode = currentNode;
    }

    /**
     *
     * @return
     */
    public Species getSpecies()
    {
        return species;
    }

    /**
     *
     * @return
     */
    public Node getCurrentNode()
    {
        return currentNode;
    }

    /**
     *
     * @param n
     */
    public void setCurrentNode(Node n)
    {
        currentNode = n;
        currentX = currentNode.getX() * DungeonComp.TILE_SIZE;
        currentY = currentNode.getY() * DungeonComp.TILE_SIZE;
    }

    /**
     *
     * @return
     */
    public Node getDestinationNode()
    {
        return gotoNode;
    }

    /**
     *
     * @param n
     */
    public void setDestinationNode(Node n)
    {
        gotoNode = n;
    }

    /**
     *
     * @return
     */
    public int getX()
    {
        return currentNode.getX();
    }

    /**
     *
     * @return
     */
    public int getY()
    {
        return currentNode.getY();
    }

    /**
     *
     * @return
     */
    public int getPixelX()
    {
        return currentX;
    }

    /**
     *
     * @return
     */
    public int getPixelY()
    {
        return currentY;
    }

    /**
     *
     * @param newX
     * @param newY
     */
    public void setPixel(int newX, int newY)
    {
        currentX = newX;
        currentY = newY;
    }

    /**
     *
     * @param dx
     * @param dy
     */
    public void addPixel(int dx, int dy)
    {
        currentX += dx;
        currentY += dy;
    }

    /**
     *
     * @param state
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
     *
     */
    public void doState()
    {
        if(currentState != null)
        {
            currentState.doState(this, dungeon);
        }
    }

    /**
     *
     * @return
     */
    public EntityState getState()
    {
        return currentState;
    }

    /**
     *
     * @return
     */
    public boolean isWater()
    {
        return species.isWater();
    }

    /**
     *
     * @return
     */
    public boolean isMoving()
    {
        return moving;
    }

    /**
     *
     * @param set
     */
    public void setMoving(boolean set)
    {
        moving = set;
    }

    /**
     *
     * @return
     */
    public int getCurrentHP(){return currentHP;}

    /**
     *
     * @return
     */
    public int getMaximumHP(){return maxHP;}

    /**
     *
     * @param newval
     */
    public void setCurrentHP(int newval)
    {
        if(newval > maxHP){newval = maxHP;}
        else if(newval < 0){newval = 0;}
        currentHP = newval;
        updateHPBar();
    }

    /**
     *
     * @param newval
     */
    public void setMaxHP(int newval)
    {
        maxHP = newval;
        updateHPBar();
    }

    /**
     *
     * @param dhp
     * @return
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
     *
     * @param newname
     */
    public void setName(String newname)
    {
        name = newname;
    }

    /**
     *
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     *
     * @return
     */
    public ArrayList<Move> getMoves()
    {
        return knownMoves;
    }

    /**
     *
     * @param newMove
     */
    public void addMove(Move newMove)
    {
        knownMoves.add(newMove);
    }

    @Override
    public String toString()
    {
        return String.format("[Entity: %s @ %d, %d]", species.getName(), getX(), getY());
    }
}
